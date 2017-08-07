package com.voyageone.web2.cms.views.usa;

import com.mongodb.WriteResult;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.util.ListUtils;
import com.voyageone.service.impl.cms.TypeChannelsService;
import com.voyageone.service.impl.cms.feed.FeedInfoService;
import com.voyageone.service.impl.cms.usa.UsaFeedInfoService;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel_Platform_Cart;
import com.voyageone.web2.base.BaseController;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.bean.usa.FeedRequest;

import com.voyageone.web2.core.bean.UserSessionBean;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 美国CMS Feed相关Controller
 *
 * @Author rex.wu
 * @Create 2017-07-05 17:09
 */
@RestController
@RequestMapping(value = UsaCmsUrlConstants.FEED.ROOT)
public class UsaCmsFeedController extends BaseController {

    @Autowired
    private FeedInfoService feedInfoService;
    @Autowired
    private UsaFeedInfoService usaFeedInfoService;
    @Autowired
    private TypeChannelsService typeChannelsService;

    /**
     * 统一的当前语言环境提供
     */
    @Override
    public String getLang() {
        return "en";
    }

    /**
     * 获取Feed Detail
     *
     * @param reqParams Feed请求参数
     */
    @RequestMapping(value = UsaCmsUrlConstants.FEED.DETAIL)
    @ResponseBody
    public AjaxResponse getFeedDetail(@RequestBody FeedRequest reqParams) {

        String channelId = getUser().getSelChannelId();
        // 返回数据
        Map<String, Object> resultMap = new HashMap<>();
        CmsBtFeedInfoModel feed = usaFeedInfoService.getFeedById(channelId, reqParams.getId());
        resultMap.put("feed", feed);
        if (feed != null) {
            resultMap.put("brandList", TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.BRAND_41, channelId, getLang()));
            resultMap.put("productTypeList", TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.PROUDCT_TYPE_57, channelId, getLang()));
            resultMap.put("sizeTypeList", TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.PROUDCT_TYPE_58, channelId, getLang()));
            resultMap.put("materialList", TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.material_TYPE_103, channelId, getLang()));
            resultMap.put("originList", TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.origin_TYPE_104, channelId, getLang()));
            resultMap.put("colorMap", TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.colorMap_TYPE_105, channelId, getLang()));

            // 平台
            List<TypeChannelBean> platforms = typeChannelsService.getUsPlatformTypeList(channelId, getLang());
            resultMap.put("platforms", platforms);
            initPlatform(feed, platforms);
        }
        return success(resultMap);
    }

    private void initPlatform(CmsBtFeedInfoModel feed, List<TypeChannelBean> platforms) {
        if (feed == null || CollectionUtils.isEmpty(platforms)) {
            return;
        }

        for (TypeChannelBean cart : platforms) {
            Integer cartId = NumberUtils.toInt(cart.getValue(), -1);
            CmsBtFeedInfoModel_Platform_Cart platformCart = null;
            if (cartId > 0 && cartId < 20) {
                // platforms
                platformCart = feed.getUsPlatform(cartId);
                if (platformCart == null) {
                    platformCart = new CmsBtFeedInfoModel_Platform_Cart();
                    platformCart.setCartId(cartId);
                    platformCart.setIsSale(1);
                    feed.getUsPlatforms().put("P" + cartId, platformCart);
                }
            } else if (cartId > 20) {
                // usPlatforms
                platformCart = feed.getPlatform(cartId);
                if (platformCart == null) {
                    platformCart = new CmsBtFeedInfoModel_Platform_Cart();
                    platformCart.setCartId(cartId);
                    platformCart.setIsSale(1);
                    feed.getPlatforms().put("P" + cartId, platformCart);
                }
            }
        }
    }

    /**
     * 获取同Model的Feed或Product信息，用以Copy部分Code级属性
     *
     * @param reqParams Feed请求参数
     */
    @RequestMapping(value = UsaCmsUrlConstants.FEED.GET_TOP_MODEL)
    public AjaxResponse getTopModel(@RequestBody FeedRequest reqParams) {
        return success(usaFeedInfoService.getTopFeedByModel(getUser().getSelChannelId(), reqParams.getCode(), reqParams.getModel(), reqParams.getTop()));
    }

    /**
     * 重新计算Feed相关价格
     *
     * @param reqParams Feed请求参数
     */
    @RequestMapping(value = UsaCmsUrlConstants.FEED.SET_PRICE)
    public AjaxResponse setPrice(@RequestBody FeedRequest reqParams) {
        return success(usaFeedInfoService.setPrice(reqParams.getFeed()));
    }

    /**
     * 批量Approve Feed
     *
     * @param reqParams Feed请求参数
     */
    @RequestMapping(value = UsaCmsUrlConstants.FEED.APPROVE)
    public AjaxResponse approve(@RequestBody FeedRequest reqParams) {
        List<String> codeList = reqParams.getCodeList();
        Boolean selAll = reqParams.getSelAll();
        if (selAll != null && selAll) {
            codeList = null;
        }else{
            reqParams.setSearchMap(new HashMap<>());
        }
        List<String> status = new ArrayList<>();
        status.add(CmsConstants.ProductStatus.Ready.toString());
        reqParams.getSearchMap().put("status",status );
        reqParams.getSearchMap().put("approvePricing", Collections.singletonList("1"));
        reqParams.getSearchMap().put("codeList",codeList);
        codeList = usaFeedInfoService.getFeedCodeList(reqParams.getSearchMap(), getUser().getSelChannelId());

        if (!CollectionUtils.isEmpty(codeList)) {
            UserSessionBean user = getUser();
            usaFeedInfoService.approve(user.getSelChannelId(), codeList, reqParams.getApproveInfo(), user.getUserName());
        }
        return success("");
    }

    /**
     * 更新Feed信息: Save 或 Submit至下一步 或 Approve
     *
     * @param reqParams 请求参数
     */
    @RequestMapping(value = UsaCmsUrlConstants.FEED.UPDATE)
    public AjaxResponse updateFeed(@RequestBody FeedRequest reqParams) {
        UserSessionBean user = getUser();
        return success(usaFeedInfoService.saveOrSubmitFeed(user.getSelChannelId(), reqParams.getFeed(), Objects.equals(Integer.valueOf(1), reqParams.getFlag()), user.getUserName()));
    }

    /**
     * 条件查询feed列表信息
     */
    @RequestMapping(value = UsaCmsUrlConstants.FEED.LIST)
    public AjaxResponse getFeedList(@RequestBody Map params) {
        Map<String, Object> resultBean = new HashMap<String, Object>();
        String selChannelId = getUser().getSelChannelId();
        //获取数据列表
        List<CmsBtFeedInfoModel> feedList = usaFeedInfoService.getFeedList(params, selChannelId);

        resultBean.put("feedList", feedList);
        //获取数据总量
        Long feedListTotal = usaFeedInfoService.getFeedCount(params, selChannelId);
        resultBean.put("feedListTotal", feedListTotal);
        // 返回feed信息
        return success(resultBean);
    }

    /**
     * 修改Approve price,Msrp($),price($)
     * @param params
     * @return
     */
    @RequestMapping(value = UsaCmsUrlConstants.FEED.UPDATEONE)
    public AjaxResponse upDateOne(@RequestBody Map params) {
        WriteResult writeResult = null;
        Double msrpPrice = null;
        Double price = null;
        if (params != null) {
            HashMap<String, Object> queryMap = new HashMap<>();
            String code = (String) params.get("code");
            if (code != null) {
                queryMap.put("code", code);
            }
            //这里有类型转换异常
            String priceClientMsrp = (String) params.get("priceClientMsrp");
            if (StringUtils.isNotEmpty(priceClientMsrp)) {
                msrpPrice = Double.parseDouble(priceClientMsrp);
            }
            String price1 = (String) params.get("price");
            if (StringUtils.isNotEmpty(price1)) {
                price = Double.parseDouble(price1);
            }


            CmsBtFeedInfoModel cmsBtFeedInfoModel = feedInfoService.getProductByCode(getUser().getSelChannelId(), code);
            if (cmsBtFeedInfoModel != null) {
                cmsBtFeedInfoModel.setModifier(getUser().getUserName());
                final Double finalMsrpPrice = msrpPrice;
                final Double finalPrice = price;
                cmsBtFeedInfoModel.getSkus().forEach(sku -> {
                    if (finalMsrpPrice != null) {
                        sku.setPriceClientMsrp(finalMsrpPrice);
                    }
                    if (finalPrice != null) {
                        sku.setPriceClientRetail(finalPrice);
                        sku.setPriceNet(finalPrice);
                    }
                });
            }
            String approvePricing = (String) params.get("approvePricing");

            if (approvePricing != null) {
                cmsBtFeedInfoModel.setApprovePricing(approvePricing);
            }
            cmsBtFeedInfoModel = usaFeedInfoService.setPrice(cmsBtFeedInfoModel);
            writeResult = feedInfoService.updateFeedInfo(cmsBtFeedInfoModel);
        }


        return success(writeResult);
    }


}
