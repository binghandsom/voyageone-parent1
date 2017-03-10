package com.voyageone.web2.cms.views.product;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
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
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

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
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, String> carts = null;
        List<TypeChannelBean> cartList = TypeChannels.getTypeListSkuCarts(user.getSelChannelId(), Constants.comMtTypeChannel.SKU_CARTS_53_A, getLang());
        if (CollectionUtils.isNotEmpty(cartList)) {
            carts = new HashMap<>();
            for (TypeChannelBean cart : cartList) {
                carts.put(cart.getValue(), cart.getName());
            }
        }
        resultMap.put("carts", carts);
        resultMap.put("statuses", CmsBtCombinedProductStatus.KV);
        resultMap.put("platformStatuses", CmsBtCombinedProductPlatformStatus.KV);

        int startSupplyChain = 0; // 店铺是否启动了供应链管理
        CmsChannelConfigBean startSupplyChainConfig = CmsChannelConfigs.getConfigBeanNoCode(user.getSelChannelId(), CmsConstants.ChannelConfig.START_SUPPLY_CHAIN);
        if (startSupplyChainConfig != null && "1".equals(startSupplyChainConfig.getConfigValue1())) {
            startSupplyChain = 1;
        }
        resultMap.put("startSupplyChain", startSupplyChain);
        return success(resultMap);
    }

    @RequestMapping(CmsUrlConstants.PRODUCT.CombinedProduct.SEARCH)
    public AjaxResponse search(@RequestBody CmsBtCombinedProductBean searchBean) {
        searchBean.setChannelId(getUser().getSelChannelId());
        Map<String, Object> resultMap = cmsBtCombinedProductService.search(searchBean.getCurr(), searchBean.getSize(), searchBean);
        return success(resultMap);
    }

    @RequestMapping(CmsUrlConstants.PRODUCT.CombinedProduct.GET_COMBINED_PRODUCT_PLATFORM_DETAIL)
    public AjaxResponse getCombinedProductPlatformDetail(@RequestBody Map<String, String> params) {
        String numId = params.get("numID");
        String cartId = params.get("cartId");
        String newFlag = params.get("new");
        if ("1".equals(newFlag)) { // 新增页面获取，判断是否已经存在
            CmsBtCombinedProductModel existOne = cmsBtCombinedProductService.getByNumId(numId, getUser().getSelChannelId(), StringUtils.isNumeric(cartId) ? Integer.valueOf(cartId) : null);
            if (existOne != null) {
                throw new BusinessException(String.format("组合商品numID=%s已存在,请直接编辑。", numId));
            }
        }
        Object productDetail = cmsBtCombinedProductService.getCombinedProductPlatformDetail(numId, getUser().getSelChannelId(), Integer.valueOf(cartId), true);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("product", productDetail);
        return success(resultMap);
    }

    /**
     * 根据单个skuCode获取SKU详情
     */
    @RequestMapping(CmsUrlConstants.PRODUCT.CombinedProduct.GET_SKU_DETAIL)
    public AjaxResponse getSkuDetail(@RequestBody Map<String, String> params) {
        String cartId = params.get("cartId");
        String skuCode = params.get("skuCode");
        String channelId = getUser().getSelChannelId();
        CmsBtCombinedProductModel_Sku_Item skuItem = cmsBtCombinedProductService.getSkuDetail(skuCode, channelId, Integer.valueOf(cartId));
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("skuItem", skuItem);
        return success(resultMap);
    }

    /**
     * 新增组合商品
     */
    @RequestMapping(CmsUrlConstants.PRODUCT.CombinedProduct.ADD)
    public AjaxResponse add(@RequestBody CmsBtCombinedProductModel modelBean) {
        cmsBtCombinedProductService.addCombinedProduct(modelBean, getUser().getSelChannelId(), getUser().getUserName());
        return success("");
    }

    /**
     * 逻辑删除组合商品
     */
    @RequestMapping(CmsUrlConstants.PRODUCT.CombinedProduct.DELETE)
    public AjaxResponse delete(@RequestBody CmsBtCombinedProductBean modelBean) {
        cmsBtCombinedProductService.deleteCombinedProduct(modelBean, getUser().getUserName(), getUser().getSelChannelId());
        return success("");
    }

    @RequestMapping(CmsUrlConstants.PRODUCT.CombinedProduct.GET_COMBINED_PRODUCT_DETAIL)
    public AjaxResponse getCombinedProductDetail(@RequestBody CmsBtCombinedProductBean modelBean) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("product", cmsBtCombinedProductService.selectById(modelBean.get_id()));
        return success(resultMap);
    }

    @RequestMapping(CmsUrlConstants.PRODUCT.CombinedProduct.EDIT)
    public AjaxResponse edit(@RequestBody CmsBtCombinedProductModel modelBean) {
        cmsBtCombinedProductService.editCombinedProduct(modelBean, getUser().getSelChannelId(), getUser().getUserName());
        return success("");
    }

    /**
     * 批量获取SKU详情
     */
    @RequestMapping(CmsUrlConstants.PRODUCT.CombinedProduct.BATCH_GET_SKU_DETAIL)
    public AjaxResponse batchGetSkuDetail(@RequestBody Map<String, Object> params) {
        String channelId = getUser().getSelChannelId();
        Integer cartId = Integer.valueOf((String) params.get("cartId"));
        List<String> skuCodes = (List<String>) params.get("skuCodes");
        Set<String> skuCodeList = new HashSet<>(skuCodes);
        return success(cmsBtCombinedProductService.batchGetSkuDetail(skuCodeList, channelId, cartId));
    }
}
