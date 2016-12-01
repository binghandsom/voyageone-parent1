package com.voyageone.web2.cms.views.product;

import com.voyageone.common.Constants;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.service.bean.cms.product.CmsBtCombinedProductBean;
import com.voyageone.service.bean.cms.product.CmsBtCombinedProductPlatformStatus;
import com.voyageone.service.bean.cms.product.CmsBtCombinedProductStatus;
import com.voyageone.service.impl.cms.product.CmsBtCombinedProductService;
import com.voyageone.service.model.cms.mongo.product.CmsBtCombinedProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtCombinedProductModel_Sku_Item;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import com.voyageone.web2.core.bean.UserSessionBean;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rex.wu on 2016/11/25.
 */
@RestController
@RequestMapping(method = RequestMethod.POST, value = CmsUrlConstants.PRODUCT.CombinedProduct.ROOT)
public class CombinedProductController extends CmsController {

    @Autowired
    private CmsBtCombinedProductService cmsBtCombinedProductService;

    @RequestMapping(CmsUrlConstants.PRODUCT.CombinedProduct.INIT)
    public AjaxResponse init() {
        UserSessionBean user = getUser();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, String> carts = null;
        List<TypeChannelBean> cartList = TypeChannels.getTypeListSkuCarts(user.getSelChannelId(), Constants.comMtTypeChannel.SKU_CARTS_53_A, getLang());
        if (CollectionUtils.isNotEmpty(cartList)) {
            carts = new HashMap<String, String>();
            for (TypeChannelBean cart:cartList) {
                carts.put(cart.getValue(), cart.getName());
            }
        }
        resultMap.put("carts", carts);
        resultMap.put("statuses", CmsBtCombinedProductStatus.KV);
        resultMap.put("platformStatuses", CmsBtCombinedProductPlatformStatus.KV);
        return success(resultMap);
    }

    @RequestMapping(CmsUrlConstants.PRODUCT.CombinedProduct.SEARCH)
    public AjaxResponse search(@RequestBody CmsBtCombinedProductBean searchBean) {
        searchBean.setChannelId(getUser().getSelChannelId());
        Map<String, Object> resultMap = cmsBtCombinedProductService.search(searchBean.getPage(), searchBean.getPageSize(), searchBean);
        return success(resultMap);
    }

    @RequestMapping(CmsUrlConstants.PRODUCT.CombinedProduct.GET_COMBINED_PRODUCT_PLATFORM_DETAIL)
    public AjaxResponse getCombinedProductPlatformDetail (@RequestBody Map<String, String> params) {
        String numId = params.get("numID");
        String cartId = params.get("cartId");
        Object productDetail = cmsBtCombinedProductService.getCombinedProductPlatformDetail(numId, getUser().getSelChannelId(), Integer.valueOf(cartId));
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("product", productDetail);
        return success(resultMap);
    }

    @RequestMapping(CmsUrlConstants.PRODUCT.CombinedProduct.GET_SKU_DETAIL)
    public AjaxResponse getSkuDetail(@RequestBody Map<String, String> params) {
        String cartId = params.get("cartId");
        String skuCode = params.get("skuCode");
        String channelId = getUser().getSelChannelId();
        CmsBtCombinedProductModel_Sku_Item skuItem = cmsBtCombinedProductService.getSkuDetail(skuCode, channelId, Integer.valueOf(cartId));
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("skuItem", skuItem);
        return success(resultMap);
    }

    @RequestMapping(CmsUrlConstants.PRODUCT.CombinedProduct.ADD)
    public AjaxResponse add(@RequestBody CmsBtCombinedProductModel modelBean) {
        cmsBtCombinedProductService.addCombinedProduct(modelBean, getUser().getSelChannelId(), getUser().getUserName());
        return success("");
    }

    @RequestMapping(CmsUrlConstants.PRODUCT.CombinedProduct.DELETE)
    public AjaxResponse delete(@RequestBody CmsBtCombinedProductBean modelBean) {
        cmsBtCombinedProductService.deleteCombinedProduct(modelBean, getUser().getUserName(), getUser().getSelChannelId());
        return success("");
    }

    @RequestMapping(CmsUrlConstants.PRODUCT.CombinedProduct.GET_COMBINED_PRODUCT_DETAIL)
    public AjaxResponse getCombinedProductDetail(@RequestBody CmsBtCombinedProductBean modelBean) {
        CmsBtCombinedProductModel model = cmsBtCombinedProductService.getCombinedProduct(modelBean, getUser().getSelChannelId());
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("product", model);
        return success(resultMap);
    }

    @RequestMapping(CmsUrlConstants.PRODUCT.CombinedProduct.EDIT)
    public AjaxResponse edit(@RequestBody CmsBtCombinedProductModel modelBean) {
        cmsBtCombinedProductService.editCombinedProduct(modelBean, getUser().getSelChannelId(), getUser().getUserName());
        return success("");
    }

    @RequestMapping(CmsUrlConstants.PRODUCT.CombinedProduct.ON_OFF_SHELVES)
    public AjaxResponse onOffShelves (@RequestBody CmsBtCombinedProductModel modelBean) {
        cmsBtCombinedProductService.onOffShelves(modelBean, getUser().getUserName());
        return success("");
    }
}
