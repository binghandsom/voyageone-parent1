package com.voyageone.web2.sdk.api.domain;

/**
 * SKU Price Model
 *
 * @author chuanyu.liang 2015/12/10
 * @version 2.0.0
 * @since. 2.0.0
 */
public class ProductSkuPriceModel {

    //skuCode
    private String skuCode;
    //msrp价格区间
    private Double msrp;
    //建议市场价格区间
    private Double retailPrice;
    //当前销售价格区间
    private Double salePrice;

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public Double getMsrp() {
        return msrp;
    }

    public void setMsrp(Double msrp) {
        this.msrp = msrp;
    }

    public Double getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(Double retailPrice) {
        this.retailPrice = retailPrice;
    }

    public Double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }
}
