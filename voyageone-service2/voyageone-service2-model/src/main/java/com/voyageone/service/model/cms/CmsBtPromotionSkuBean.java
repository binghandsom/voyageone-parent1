package com.voyageone.service.model.cms;

import com.voyageone.service.bean.cms.CmsBtPromotionGroupsBean;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;

/**
 * @author james.li on 2015/12/15.
 * @version 2.0.0
 */
public class CmsBtPromotionSkuBean extends CmsBtPromotionGroupsBean {

    private Long productId;

    private String productCode;

    private String productSku;

    private Integer qty;

    public CmsBtPromotionSkuBean(CmsBtProductModel productInfo, int cartId, int promotionId, String operator, String productSku, Integer qty) {

        super(productInfo, cartId, promotionId, operator);
        this.setProductId(productInfo.getProdId());
        this.setProductCode(productInfo.getFields().getCode());
        this.setProductSku(productSku);
        this.setQty(qty == null ? 0 : qty);
    }

    public CmsBtPromotionSkuBean() {
    }

    public String getProductSku() {
        return productSku;
    }

    public void setProductSku(String productSku) {
        this.productSku = productSku;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

}
