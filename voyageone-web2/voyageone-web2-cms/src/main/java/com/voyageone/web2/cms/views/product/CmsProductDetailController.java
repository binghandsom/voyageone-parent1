package com.voyageone.web2.cms.views.product;

import com.google.common.base.Preconditions;
import com.voyageone.common.asserts.Assert;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.bean.cms.CustomPropBean;
import com.voyageone.service.bean.cms.product.DelistingParameter;
import com.voyageone.service.bean.cms.product.GetChangeMastProductInfoParameter;
import com.voyageone.service.bean.cms.product.SetMastProductParameter;
import com.voyageone.service.impl.cms.feed.FeedCustomPropService;
import com.voyageone.service.impl.cms.prices.PriceService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lewis on 15-12-16.
 *
 * @author lewis
 */
@RestController
@RequestMapping(method = RequestMethod.POST, value = CmsUrlConstants.PRODUCT.DETAIL.ROOT)
public class CmsProductDetailController extends CmsController {

    @Autowired
    CmsProductDetailService productPropsEditService;

    @Autowired
    FeedCustomPropService feedCustomPropService;

    @Autowired
    ProductService productService;

    @Autowired
    PriceService priceService;

    @Autowired
    CmsProductPlatformDetailService cmsProductPlatformDetailService;

    @RequestMapping(CmsUrlConstants.PRODUCT.DETAIL.UPDATE_PRODUCT_MASTER_INFO)
    public AjaxResponse doUpdateProductMasterInfo(@RequestBody Map requestMap) {
        String channelId = getUser().getSelChannelId();
        String user = getUser().getUserName();
        String updateTime = productPropsEditService.updateProductMasterInfo(channelId, user, requestMap);
        Map<String, Object> updateInfo = new HashMap<>();
        updateInfo.put("modified", updateTime);
        return success(updateInfo);
    }

    @RequestMapping(CmsUrlConstants.PRODUCT.DETAIL.UPDATE_PRODUCT_SKU_INFO)
    public AjaxResponse doUpdateProductSkuInfo(@RequestBody Map requestMap) {
        String channelId = getUser().getSelChannelId();
        String user = getUser().getUserName();
        String categoryId = requestMap.get("categoryId").toString();
        Long productId = Long.valueOf(requestMap.get("productId").toString());
        String categoryFullPath = requestMap.get("categoryFullPath").toString();
        Map skuMap = (Map) requestMap.get("skuFields");
        String modified = requestMap.get("modified").toString();
        String updateTime = productPropsEditService.updateProductSkuInfo(channelId, user, categoryId, productId, modified, categoryFullPath, skuMap);
        Map<String, Object> updateInfo = new HashMap<>();
        updateInfo.put("modified", updateTime);
        return success(updateInfo);
    }

    @RequestMapping(CmsUrlConstants.PRODUCT.DETAIL.UPDATE_PRODUCT_ALL_INFO)
    public AjaxResponse doUpdateProductAllInfo(@RequestBody Map requestMap) {

        String channelId = getUser().getSelChannelId();
        String userName = getUser().getUserName();

//        String updateTime = productPropsEditService.updateProductAllInfo(channelId, userName, requestMap);
//
//        Map<String, Object> updateInfo = new HashMap<>();
//
//        updateInfo.put("modified", updateTime);

        return success(productPropsEditService.updateProductAllInfo(channelId, userName, requestMap));

    }

    @RequestMapping(CmsUrlConstants.PRODUCT.DETAIL.UPDATE_PRODUCT_FEED)
    public AjaxResponse doUpdateProductFeedInfo(@RequestBody Map requestMap) {

        String channelId = getUser().getSelChannelId();
        String userName = getUser().getUserName();

        return success(productPropsEditService.updateProductFeedInfo(channelId, userName, requestMap));

    }

    @RequestMapping(CmsUrlConstants.PRODUCT.DETAIL.CHANGE_CATEGORY)
    public AjaxResponse doChangeCategory(@RequestBody Map requestMap) {

        Map<String, Object> resultMap = productPropsEditService.changeProductCategory(requestMap, getUser(), getCmsSession());

        return success(resultMap);
    }

    @RequestMapping(CmsUrlConstants.PRODUCT.DETAIL.REFRESH_PRODUCT_CATEGORY)
    public AjaxResponse doRefreshProductCategory(@RequestBody Map requestMap) {

        Map<String, Object> resultMap = productPropsEditService.refreshProductCategory(requestMap, getUser());

        return success(resultMap);
    }

    @RequestMapping(CmsUrlConstants.PRODUCT.DETAIL.GET_COMMON_PRODUCTINFO)
    public AjaxResponse doGetMastProductInfo(@RequestBody Map requestMap) {

        Long prodId = Long.parseLong(String.valueOf(requestMap.get("prodId")));

        String channelId = getUser().getSelChannelId();

        return success(productPropsEditService.getMastProductInfo(channelId, prodId, getLang()));

    }

    @RequestMapping(CmsUrlConstants.PRODUCT.DETAIL.GET_COMMON_PRODUCT_SKU_INFO)
    public AjaxResponse doGetMastProductSkuInfo(@RequestBody Map requestMap) {

        Long prodId = Long.parseLong(String.valueOf(requestMap.get("prodId")));

        String channelId = getUser().getSelChannelId();

        return success(productPropsEditService.getMastProductSkuInfo(channelId, prodId, getLang()));

    }


    @RequestMapping(CmsUrlConstants.PRODUCT.DETAIL.UPDATE_COMMON_PRODUCTINFO)
    public AjaxResponse doUpdateMastProductInfo(@RequestBody Map requestMap) {

        Long prodId = Long.parseLong(String.valueOf(requestMap.get("prodId")));

        String channelId = getUser().getSelChannelId();

        Map<String, Object> productComm = (Map<String, Object>) requestMap.get("productComm");

        return success(productPropsEditService.updateCommonProductInfo(channelId, prodId, productComm, getUser().getUserName()));

    }

    @RequestMapping(CmsUrlConstants.PRODUCT.DETAIL.UPDATE_LOCK)
    public AjaxResponse doLock(@RequestBody Map requestMap) {
        Long prodId = Long.parseLong(String.valueOf(requestMap.get("prodId")));

        String lock = (String) requestMap.get("lock");
        productService.updateProductLock(getUser().getSelChannelId(), prodId, lock, getUser().getUserName());

        return success(null);
    }
    @RequestMapping(CmsUrlConstants.PRODUCT.DETAIL.DoAppSwitch)
    public AjaxResponse doAppSwitch(@RequestBody Map requestMap) {
        //{prodId: "5992", appSwitch: "0"}
        Long prodId = Long.parseLong(String.valueOf(requestMap.get("prodId")));

        String appSwitch = (String) requestMap.get("appSwitch");

        productService.updateProductAppSwitch(getUser().getSelChannelId(), prodId, appSwitch, getUser().getUserName());

        return success(null);
    }
    @RequestMapping(CmsUrlConstants.PRODUCT.DETAIL.DoTranslateStatus)
    public AjaxResponse doTranslateStatus(@RequestBody Map requestMap) {
        //{prodId: "5992", translateStatus: "0"}
        Long prodId = Long.parseLong(String.valueOf(requestMap.get("prodId")));

        String translateStatus = (String) requestMap.get("translateStatus");

        productService.updateProductTranslateStatus(getUser().getSelChannelId(), prodId, translateStatus, getUser().getUserName());

        return success(null);
    }

    @RequestMapping(CmsUrlConstants.PRODUCT.DETAIL.UPDATE_FEED_ATTS)
    public AjaxResponse updateProductAtts(@RequestBody Map requestMap) {
        Long prodId = Long.parseLong(String.valueOf(requestMap.get("prodId")));
        if (requestMap.get("feedInfo") != null) {
            List<CustomPropBean> cnProps = JacksonUtil.jsonToBeanList(JacksonUtil.bean2Json(requestMap.get("feedInfo")), CustomPropBean.class);
            productService.updateProductAtts(getUser().getSelChannelId(), prodId, cnProps, (Map<String, Boolean>) requestMap.get("productCustomIsDisp"), getUser().getUserName());
        }
        return success(null);
    }

    //获取切换主商品  的显示信息
    @RequestMapping(CmsUrlConstants.PRODUCT.DETAIL.GetChangeMastProductInfo)
    public AjaxResponse getChangeMastProductInfo(@RequestBody GetChangeMastProductInfoParameter parameter) {
        parameter.setChannelId(getUser().getSelChannelId());
        return success(productPropsEditService.getChangeMastProductInfo(parameter));
    }

    //设置主商品
    @RequestMapping(CmsUrlConstants.PRODUCT.DETAIL.SetMastProduct)
    public AjaxResponse setMastProduct(@RequestBody SetMastProductParameter parameter) {
        parameter.setChannelId(getUser().getSelChannelId());
        productPropsEditService.setMastProduct(parameter, getUser().getUserName());
        return success(null);
    }

    //单品下架
    @RequestMapping(CmsUrlConstants.PRODUCT.DETAIL.Delisting)
    public AjaxResponse delisting(@RequestBody DelistingParameter parameter) {
        parameter.setChannelId(getUser().getSelChannelId());
        productPropsEditService.delisting(parameter, getUser().getUserName());
        return success(null);
    }

    //group下架
    @RequestMapping(CmsUrlConstants.PRODUCT.DETAIL.DelistinGroup)
    public AjaxResponse delistinGroup(@RequestBody DelistingParameter parameter) {
        parameter.setChannelId(getUser().getSelChannelId());
        productPropsEditService.delistinGroup(parameter, getUser().getUserName());
        return success(null);
    }

    //税号变更
    @RequestMapping(CmsUrlConstants.PRODUCT.DETAIL.HsCodeChg)
    public AjaxResponse hsCodeChg(@RequestBody Map<String, Object> parameter) {

        String channelId = getUser().getSelChannelId();
        Long prodId = Long.parseLong(String.valueOf(parameter.get("prodId")));
        String hsCode = String.valueOf(parameter.get("hsCode"));
        return success(productPropsEditService.hsCodeChg(channelId, prodId, hsCode));
    }

    @RequestMapping(CmsUrlConstants.PRODUCT.DETAIL.CopyCommonProperty)
    public AjaxResponse copyProperty(@RequestBody Map params) {

        Map<String, Object> result = new HashMap<>();
        Long prodId = Long.parseLong(String.valueOf(params.get("prodId")));
        result.put("platform", productPropsEditService.copyPropertyFromMainProduct(getUser().getSelChannelId(), prodId, getLang()));
        return success(result);
    }

    /**
     * 产品详情页sku价格刷新
     *
     * @return
     * @params cartId：平台Id  prodId：产品编号   platform:平台信息
     */
    @RequestMapping(CmsUrlConstants.PRODUCT.DETAIL.UPDATE_SKUPRICE)
    public AjaxResponse updateSkuPrice(@RequestBody Map params) throws Exception {

        String channelId = getUser().getSelChannelId();
        Assert.notNull(channelId).elseThrowDefaultWithTitle("channelId");

        Integer cartId = Integer.parseInt(String.valueOf(params.get("cartId")));
        Assert.notNull(cartId).elseThrowDefaultWithTitle("cartId");

        Long prodId = Long.parseLong(String.valueOf(params.get("prodId")));
        Assert.notNull(prodId).elseThrowDefaultWithTitle("prodId");

        Map<String, Object> platform = (Map<String, Object>) params.get("platform");
        Assert.notNull(platform).elseThrowDefaultWithTitle("platform");

        Boolean priceCheck = Boolean.parseBoolean(String.valueOf(params.get("priceCheck")));
        if(priceCheck)
            priceService.priceChk(channelId, new CmsBtProductModel_Platform_Cart(platform), cartId);

        productPropsEditService.updateSkuPrice(channelId, cartId, prodId, getUser().getUserName(), new CmsBtProductModel_Platform_Cart(platform));

        return success(null);
    }
    
    /**
     * 取得SKU库存的信息（各仓库库存整体信息与详细信息）
     * @param { prodcutId }
     * @return
     */
    @RequestMapping(CmsUrlConstants.PRODUCT.DETAIL.GET_SKU_STOCK_INFO)
    public AjaxResponse getSkuStockInfo(@RequestBody String productId) {
    	Preconditions.checkArgument(StringUtils.isNotBlank(productId));
    	return success(productPropsEditService.getStockInfoBySku(getUser().getSelChannelId(), Long.valueOf(productId)));
    }

    /**
     * 清除product_group表里， 指定channel， cart， code的platformPid
     *
     * @return
     * @params cartId：平台Id  code：产品code
     */
    @RequestMapping(CmsUrlConstants.PRODUCT.DETAIL.RESET_TM_PRODUCT)
    public AjaxResponse resetTmProduct(@RequestBody Map params) {

        String channelId = getUser().getSelChannelId();
        Assert.notNull(channelId).elseThrowDefaultWithTitle("channelId");

        int cartId = Integer.parseInt(String.valueOf(params.get("cartId")));
        Assert.notNull(cartId).elseThrowDefaultWithTitle("cartId");

        String productCode = String.valueOf(params.get("productCode"));
        Assert.notNull(productCode).elseThrowDefaultWithTitle("productCode");

        // 只有天猫， 天猫国际有这个需要
        if (cartId == CartEnums.Cart.TM.getValue() || cartId == CartEnums.Cart.TG.getValue()) {
            productService.resetProductAndGroupPlatformPid(channelId, cartId, productCode);
        }

        return success(null);
    }

    /**
     * 修改产品共通属性中的图片属性
     * @param params
     *        imagesType 图片类型
     *        images 图片集合
     * @return
     */
    @RequestMapping(CmsUrlConstants.PRODUCT.DETAIL.RESTORE_IMG)
    public AjaxResponse restoreImg(@RequestBody Map params) {

        String prodId = String.valueOf(String.valueOf(params.get("prodId")));
        Assert.notNull(prodId).elseThrowDefaultWithTitle("prodId");

        String imagesType = String.valueOf(String.valueOf(params.get("imagesType")));
        Assert.notNull(imagesType).elseThrowDefaultWithTitle("imagesType");

        List<String> images = (List<String>) params.get("images");
        Assert.notNull(images).elseThrowDefaultWithTitle("images");

        Map<String, Object> result = new HashMap<>();
        result.put("modified",productPropsEditService.restoreImg(getUser().getSelChannelId(),
                Long.parseLong(prodId),
                imagesType,
                images,
                getUser().getUserName()
                ));

        return success(result);
    }

    /**
     * 修改产品plateForm属性中的图片属性
     * param imagesType 图片类型
     *        images 图片集合
     *        cartId 平台ID
     */
    @RequestMapping(CmsUrlConstants.PRODUCT.DETAIL.RESTORE_PLATFORM_IMG)
    public AjaxResponse restorePlateFormImg(@RequestBody Map params) {
        Integer cartId = Integer.valueOf(String.valueOf(params.get("cartId")));
        Assert.notNull(cartId).elseThrowDefaultWithTitle("cartId");

        String prodId = String.valueOf(String.valueOf(params.get("prodId")));
        Assert.notNull(prodId).elseThrowDefaultWithTitle("prodId");

        String imagesType = String.valueOf(String.valueOf(params.get("imagesType")));
        Assert.notNull(imagesType).elseThrowDefaultWithTitle("imagesType");

        List<String> images = (List<String>) params.get("images");
        Assert.notNull(images).elseThrowDefaultWithTitle("images");

        Map<String, Object> result = new HashMap<>();
        result.put("modified",productPropsEditService.restorePlatformImg(getUser().getSelChannelId(),
                Long.parseLong(prodId),
                imagesType,
                images,
                getUser().getUserName(),
                cartId
        ));

        return success(result);
    }

    @RequestMapping(CmsUrlConstants.PRODUCT.DETAIL.GetProductIdByCode)
    public AjaxResponse getProductIdByCode(@RequestBody String code) {
        Long productId = productService.getProductIdByCode(code, this.getUser().getSelChannelId());
        if (productId != null)
            return success(productId.toString());
        return success(null);
    }

    @RequestMapping(CmsUrlConstants.PRODUCT.DETAIL.UPDATE_ORIGINAL_TITLE_CN)
    public AjaxResponse updateOriginalTitleCn(@RequestBody Map params) {
        Long prodId = Long.valueOf(String.valueOf(params.get("prodId")));
        Assert.notNull(prodId).elseThrowDefaultWithTitle("prodId");
        String originalTitleCn = StringUtils.trimToNull(String.valueOf(params.get("originalTitleCn")));
        Assert.notNull(prodId).elseThrowDefaultWithTitle("originalTitleCn");

        UserSessionBean user = getUser();
        productPropsEditService.updateOriginalTitleCn(getUser().getSelChannelId(), prodId, originalTitleCn, user.getUserName());
        return success(null);
    }
}
