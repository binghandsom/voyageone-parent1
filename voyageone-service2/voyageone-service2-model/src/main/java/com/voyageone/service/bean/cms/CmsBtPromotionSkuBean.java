package com.voyageone.service.bean.cms;

import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;

import java.math.BigDecimal;

/**
 * @author james.li on 2015/12/15.
 * @version 2.0.0
 */
public class CmsBtPromotionSkuBean extends CmsBtPromotionGroupsBean {

    private Long productId;

    private String productCode;

    private String productSku;

    private String size;

    private Integer qty;
    /**
     * 中国官网价格
     */
    protected BigDecimal msrpRmb;

    /**
     * 中国指导价格
     */
    protected BigDecimal retailPrice;

    /**
     * 中国最终售价
     */
    protected BigDecimal salePrice;

    /**
     * 活动价
     */
    protected BigDecimal promotionPrice;

    /**
     * 海外官网价格
     */
    protected BigDecimal msrpUsd;

    public BigDecimal getMsrpRmb() {
        return msrpRmb;
    }

    public void setMsrpRmb(BigDecimal msrpRmb) {
        this.msrpRmb = msrpRmb;
    }

    public BigDecimal getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(BigDecimal retailPrice) {
        this.retailPrice = retailPrice;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public BigDecimal getPromotionPrice() {
        return promotionPrice;
    }

    public void setPromotionPrice(BigDecimal promotionPrice) {
        this.promotionPrice = promotionPrice;
    }

    public BigDecimal getMsrpUsd() {
        return msrpUsd;
    }

    public void setMsrpUsd(BigDecimal msrpUsd) {
        this.msrpUsd = msrpUsd;
    }

    public CmsBtPromotionSkuBean(CmsBtProductModel productInfo, CmsBtProductGroupModel groupModel, int promotionId, String operator, String productSku, Integer qty) {

        super(productInfo, groupModel, promotionId, operator);
        this.setProductId(productInfo.getProdId());
        this.setProductCode(productInfo.getCommon().getFields().getCode());
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

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
