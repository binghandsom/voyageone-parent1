package com.voyageone.web2.cms.model;

import com.voyageone.cms.service.model.CmsBtProductModel;

import java.util.List;

/**
 * @author james.li on 2015/12/15.
 * @version 2.0.0
 */
public class CmsBtPromotionSkuModel extends CmsBtPromotionGroupModel {
    private String productCode;

    private List<String> productSku;

    private List<Integer> qty;

    public CmsBtPromotionSkuModel(CmsBtProductModel productInfo, int cartId, int promotionId, String operator) {
        super(productInfo, cartId, promotionId, operator);
    }

    public CmsBtPromotionSkuModel() {
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public List<String> getProductSku() {
        return productSku;
    }

    public void setProductSku(List<String> productSku) {
        this.productSku = productSku;
    }

    public List<Integer> getQty() {
        return qty;
    }

    public void setQty(List<Integer> qty) {
        this.qty = qty;
    }
}
