package com.voyageone.web2.sdk.api.domain;

/**
 * SKU Price Model
 *
 * @author chuanyu.liang 2015/12/10
 * @version 2.0.0
 * @since 2.0.0
 */
public class ProductSkuPriceModel {

    //skuCode
    private String skuCode;
    //msrp价格区间
    private Double priceMsrp;
    //建议市场价格区间
    private Double priceRetail;
    //当前销售价格区间
    private Double priceSale;

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public Double getPriceMsrp() {
        return priceMsrp;
    }

    public void setPriceMsrp(Double priceMsrp) {
        this.priceMsrp = priceMsrp;
    }

    public Double getPriceRetail() {
        return priceRetail;
    }

    public void setPriceRetail(Double priceRetail) {
        this.priceRetail = priceRetail;
    }

    public Double getPriceSale() {
        return priceSale;
    }

    public void setPriceSale(Double priceSale) {
        this.priceSale = priceSale;
    }
}
