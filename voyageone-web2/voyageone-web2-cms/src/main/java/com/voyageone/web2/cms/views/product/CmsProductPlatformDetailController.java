package com.voyageone.web2.cms.views.product;

import com.voyageone.service.bean.cms.CmsProductPlatformDetail.SaveCartSkuPriceParameter;
import com.voyageone.service.bean.cms.CmsProductPlatformDetail.SetCartSkuIsSaleParameter;
import com.voyageone.service.impl.cms.PlatformCategoryService;
import com.voyageone.service.impl.cms.prices.IllegalPriceConfigException;
import com.voyageone.service.impl.cms.prices.PriceCalculateException;
import com.voyageone.service.impl.cms.prices.PriceService;
import com.voyageone.service.impl.cms.product.CmsBtPriceConfirmLogService;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategoryTreeModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import com.voyageone.web2.cms.views.search.CmsAdvanceSearchService;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lewis on 15-12-16.
 *
 * @author lewis
 */
@RestController
@RequestMapping(method = RequestMethod.POST, value = CmsUrlConstants.PRODUCT.DETAIL.ROOT)
public class CmsProductPlatformDetailController extends CmsController {

    @Autowired
    private CmsProductPlatformDetailService cmsProductPlatformDetailService;

    @Autowired
    private PlatformCategoryService platformCategoryService;

    @Autowired
    private CmsBtPriceConfirmLogService cmsBtPriceConfirmLogService;
    
    @Autowired
    private CmsAdvanceSearchService cmsAdvanceSearchService;

    @Autowired
    private PriceService priceService;

    @RequestMapping(CmsUrlConstants.PRODUCT.DETAIL.SaveCartSkuPrice)
    public  AjaxResponse  saveCartSkuPrice(@RequestBody SaveCartSkuPriceParameter parameter) throws Exception {
        UserSessionBean userSessionBean = getUser();
        cmsProductPlatformDetailService.saveCartSkuPrice(parameter, userSessionBean.getSelChannelId(), userSessionBean.getUserName());
        return success(null);
    }

        @RequestMapping(CmsUrlConstants.PRODUCT.DETAIL.SetCartSkuIsSale)
    public  AjaxResponse setCartSkuIsSale(@RequestBody SetCartSkuIsSaleParameter parameter) {
        UserSessionBean userSessionBean = getUser();
        cmsProductPlatformDetailService.setCartSkuIsSale(parameter, userSessionBean.getSelChannelId(), userSessionBean.getUserName());
        return success(null);
    }

    @RequestMapping(CmsUrlConstants.PRODUCT.DETAIL.GetCalculateCartMsrp)
    public  AjaxResponse getCalculateCartMsrp(@RequestBody Long prodId) throws IllegalPriceConfigException, PriceCalculateException {
        return success(cmsProductPlatformDetailService.getCalculateCartMsrp(getUser().getSelChannelId(),prodId));

    }
    @RequestMapping(CmsUrlConstants.PRODUCT.DETAIL.GetProductPriceSales)
    public AjaxResponse getProductPriceSales(@RequestBody Long prodId) {
        return success(cmsProductPlatformDetailService.getProductPriceSales(getUser().getSelChannelId(),prodId));
    }
    @RequestMapping(CmsUrlConstants.PRODUCT.DETAIL.GET_PRODUCT_PLATFORM)
    public AjaxResponse doGetProductPlatform(@RequestBody Map params) {
        Long prodId = Long.parseLong(String.valueOf(params.get("prodId")));

        String channelId = getUser().getSelChannelId();
        Integer cartId = Integer.parseInt(String.valueOf(params.get("cartId")));
        Map<String, Object> result = new HashMap<>();

        result.put("mastData", cmsProductPlatformDetailService.getProductMastData(channelId, prodId, cartId));
        result.put("platform", cmsProductPlatformDetailService.getProductPlatform(channelId, prodId, cartId, getLang()));
        result.put("channelConfig", cmsAdvanceSearchService.getChannelConfig(channelId, cartId, getLang()));
        result.put("autoSyncPriceMsrp", priceService.getAutoSyncPriceMsrpOption(channelId, cartId).getConfigValue1());
        result.put("autoSyncPriceSale", cmsProductPlatformDetailService.getAutoSyncPriceSaleOption(channelId, cartId));

        return success(result);
    }

    @RequestMapping(CmsUrlConstants.PRODUCT.DETAIL.CHANGE_PLATFORM_CATEGORY)
    public AjaxResponse doChangePlatformCategory(@RequestBody Map params) {
        Long prodId = Long.parseLong(String.valueOf(params.get("prodId")));

        String channelId = getUser().getSelChannelId();
        int cartId = Integer.parseInt(String.valueOf(params.get("cartId")));
        String catId = String.valueOf(params.get("catId"));
        Map<String, Object> result = new HashMap<>();

        String catPath = (String) params.get("catPath");

        result.put("platform", cmsProductPlatformDetailService.changePlatformCategory(channelId, prodId, cartId, catId, catPath, getLang()));

        return success(result);
    }

    @RequestMapping(CmsUrlConstants.PRODUCT.DETAIL.UPDATE_PRODUCT_PLATFORM)
    public AjaxResponse doUpdateProductPlatform(@RequestBody Map params) {
        Long prodId = Long.parseLong(String.valueOf(params.get("prodId")));

        String channelId = getUser().getSelChannelId();

        Map<String, Object> result = new HashMap<>();

        Map<String, Object> platform = (Map<String, Object>) params.get("platform");

        //插入workload表时，blnSmartSx标识是否为智能上新
        Boolean blnSmartSx = params.get("type") != null ? String.valueOf(params.get("type")).equals("intel") : false;

        result.put("modified", cmsProductPlatformDetailService.updateProductPlatform(channelId, prodId, platform, getUser().getUserName(),blnSmartSx));

        return success(result);
    }

    /**
     * type:记录是否为ready状态,temporary:暂存
     */
    @RequestMapping(CmsUrlConstants.PRODUCT.DETAIL.UPDATE_PRODUCT_PLATFORM_CHK)
    public AjaxResponse doUpdateProductPlatformChk(@RequestBody Map params) {

        String channelId = getUser().getSelChannelId();
        Long prodId = Long.parseLong(String.valueOf(params.get("prodId")));
        String type = String.valueOf(params.get("type"));
        Map<String, Object> platform = (Map<String, Object>) params.get("platform");
        String errcode = null;
        Integer cartId = Integer.valueOf(platform.get("cartId").toString());

        if(!type.equals("temporary")){
            cmsProductPlatformDetailService.priceChk(channelId, prodId, platform, cartId);
        }

        return doUpdateProductPlatform(params);

    }

    @RequestMapping(CmsUrlConstants.PRODUCT.DETAIL.CHECK_CATEGORY)
    public AjaxResponse checkCategory(@RequestBody Map params) {

        String catPath = params.get("pCatPath").toString();
        Integer cartId = (Integer) params.get("cartId");
        CmsMtPlatformCategoryTreeModel cmsMtPlatformCategoryTreeModel = platformCategoryService.getCategoryByCatPath(getUser().getSelChannelId(), catPath, cartId);
        return success(cmsMtPlatformCategoryTreeModel != null);
    }

    @RequestMapping(CmsUrlConstants.PRODUCT.DETAIL.CopyProperty)
    public AjaxResponse copyProperty(@RequestBody Map params) {

        Map<String, Object> result = new HashMap<>();
        Long prodId = Long.parseLong(String.valueOf(params.get("prodId")));
        Integer cartId = (Integer) params.get("cartId");
        result.put("platform", cmsProductPlatformDetailService.copyPropertyFromMainProduct(getUser().getSelChannelId(), prodId, cartId, getLang()));
        return success(result);
    }
    @RequestMapping(CmsUrlConstants.PRODUCT.DETAIL.GET_PLATFORM_CATEGORIES)
    public AjaxResponse getPlatformCategories(@RequestBody Map<String, Integer> params) {

        Integer cartId = params.get("cartId");

        return success(cmsProductPlatformDetailService.getPlatformCategories(getUser(), cartId));
    }

    @RequestMapping(CmsUrlConstants.PRODUCT.DETAIL.PriceConfirm)
    public AjaxResponse priceConfirm(@RequestBody Map<String, Object> params) {

        String productCode = String.valueOf(params.get("productCode"));

        String channelId = getUser().getSelChannelId();

        Map<String, Object> result = new HashMap<>();

        Map<String, Object> platform = (Map<String, Object>) params.get("platform");

        CmsBtProductModel_Platform_Cart platformModel = new CmsBtProductModel_Platform_Cart(platform);
        cmsBtPriceConfirmLogService.addConfirmed(getUser().getSelChannelId(),productCode,platformModel,getUser().getUserName());
        return success(true);
    }
}

