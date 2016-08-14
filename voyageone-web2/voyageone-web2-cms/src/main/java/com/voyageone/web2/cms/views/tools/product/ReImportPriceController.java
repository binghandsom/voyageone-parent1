package com.voyageone.web2.cms.views.tools.product;

import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.common.asserts.Assert;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.PlatformBean;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.MapUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.impl.cms.PlatformCategoryService;
import com.voyageone.service.impl.cms.PlatformService;
import com.voyageone.service.impl.cms.feed.FeedInfoService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategoryTreeModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants.CMS_TOOLS_REPRICE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * 重新对指定范围的商品进行价格计算
 * <p>
 * Created by jonas on 8/9/16.
 *
 * @author jonas
 * @version 2.4.0
 * @since 2.4.0
 */
@RestController
@RequestMapping(value = CMS_TOOLS_REPRICE.ROOT, method = RequestMethod.POST)
public class ReImportPriceController extends CmsController {

    private final PlatformService platformService;

    private final PlatformCategoryService platformCategoryService;

    private final ProductService productService;

    private final FeedInfoService feedInfoService;

    @Autowired
    public ReImportPriceController(PlatformCategoryService platformCategoryService, PlatformService platformService, ProductService productService, FeedInfoService feedInfoService) {
        this.platformCategoryService = platformCategoryService;
        this.platformService = platformService;
        this.productService = productService;
        this.feedInfoService = feedInfoService;
    }

    @RequestMapping(CMS_TOOLS_REPRICE.GET_CHANNEL_LIST)
    public AjaxResponse getChannelList() {

        List<Map<String, Object>> channelList = Stream.of(ChannelConfigEnums.Channel.values())
                .filter(channel -> !channel.equals(ChannelConfigEnums.Channel.NONE))
                .map(channel -> MapUtil.toMap("value", channel.getId(), "label", channel.getFullName()))
                .collect(toList());

        return success(channelList);
    }

    @RequestMapping(CMS_TOOLS_REPRICE.GET_PLATFORM_LIST)
    public AjaxResponse getPlatformList(@RequestBody Map<String, Object> channel) {

        String channelId = (String) channel.get("value");

        List<ShopBean> channelShopList = Shops.getChannelShopList(channelId);

        if (channelShopList == null || channelShopList.isEmpty())
            return success(new Object[0]);

        List<PlatformBean> platformBeanList = platformService.getAll();

        Map<Double, String> platformMap = platformBeanList.stream().collect(toMap(p -> (double) p.getPlatform_id(), PlatformBean::getComment));

        List<Map<String, Object>> jsonList = channelShopList.stream()
                .map(ShopBean::getPlatform_id)
                .distinct()
                .filter(StringUtils::isNumeric)
                .map(Double::valueOf)
                .map(platformId -> MapUtil.toMap("value", platformId, "label", platformMap.get(platformId)))
                .collect(toList());

        return success(jsonList);
    }

    @RequestMapping(CMS_TOOLS_REPRICE.GET_CART_LIST)
    public AjaxResponse getCartList(@RequestBody Map<String, Map<String, Object>> params) {

        Map<String, Object> channel = params.get("channel");

        Map<String, Object> platform = params.get("platform");

        String channelId = (String) channel.get("value");

        Integer platformId = (Integer) platform.get("value");

        List<ShopBean> channelShopList = Shops.getChannelShopList(channelId);

        if (channelShopList == null || channelShopList.isEmpty())
            return success(new Object[0]);

        List<Map<String, Object>> jsonList = channelShopList.stream()
                .filter(shopBean -> Integer.valueOf(shopBean.getPlatform_id()).equals(platformId))
                .map(shopBean -> MapUtil.toMap("value", shopBean.getCart_id(), "label", shopBean.getShop_name()))
                .collect(toList());

        return success(jsonList);
    }

    @RequestMapping(CMS_TOOLS_REPRICE.GET_PLATFORM_CATEGORY_LIST)
    public AjaxResponse getPlatformCategoryList(@RequestBody Map<String, Object> cart) {

        String cartId = (String) cart.get("value");

        List<CmsMtPlatformCategoryTreeModel> categoryTreeModelList = platformCategoryService.getCmsMtPlatformCategoryTreeModelLeafList(Integer.valueOf(cartId));

        return success(categoryTreeModelList);
    }

    @RequestMapping(CMS_TOOLS_REPRICE.SET_UPDATE_FLG)
    public AjaxResponse setUpdateFlg(@RequestBody Map<String, Map<String, Object>> params) throws ExecutionException, InterruptedException {

        Map<String, Object> cart = params.get("cart");
        Map<String, Object> channel = params.get("channel");
        Map<String, Object> category = params.get("category");

        Assert.notNull(cart).elseThrowDefaultWithTitle("cart");
        Assert.notNull(channel).elseThrowDefaultWithTitle("channel");

        String cartId = (String) cart.get("value");
        String channelId = (String) channel.get("value");
        String categoryId;

        String qurey = String.format("\"platforms.P%s\":{$exists:true}", cartId);

        if (category != null) {
            categoryId = (String) category.get("catId");
            qurey += String.format(",\"platforms.P%s.pCatId\":%s", cartId, categoryId);
        }

        List<CmsBtProductModel> productModelList = productService.getList(channelId, new JomgoQuery()
                .setQuery("{" + qurey + "}")
                .setProjectionExt("common.fields.code"));

        int codeSize = productModelList.size();

        Stream<String> codeStream = productModelList.stream()
                .map(product -> product.getCommon().getFields().getCode());

        Consumer<String> switchFeedUpdating = (code) -> {

            CmsBtFeedInfoModel feedInfoModel = feedInfoService.getProductByCode(channelId, code);

            if (feedInfoModel == null)
                return;

            feedInfoModel.setUpdFlg(0);

            feedInfoService.updateFeedInfo(feedInfoModel);
        };

        if (codeSize > 1000) {
            new ForkJoinPool(5).submit(() -> codeStream.parallel().forEach(switchFeedUpdating)).get();
        } else {
            codeStream.forEach(switchFeedUpdating);
        }

        return success(codeSize);
    }
}
