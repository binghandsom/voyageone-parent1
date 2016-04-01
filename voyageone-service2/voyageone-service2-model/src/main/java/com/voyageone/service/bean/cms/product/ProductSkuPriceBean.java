package com.voyageone.service.bean.cms.product;

/**
 * SKU Price Model
 *
 * @author chuanyu.liang 2015/12/10
 * @version 2.0.0
 * @since 2.0.0
 */
public class ProductSkuPriceBean {

    //skuCode
    private String skuCode;
    //msrp价格
    private Double priceMsrp;
    //建议市场价格
    private Double priceRetail;
    //当前销售价格
    private Double priceSale;

    //供应商成本价
    private Double clientNetPrice;
    //供应商MSRP价
    private Double clientMsrpPrice;
    //供应商销售价
    private Double clientRetailPrice;

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

    public Double getClientNetPrice() {
        return clientNetPrice;
    }

    public void setClientNetPrice(Double clientNetPrice) {
        this.clientNetPrice = clientNetPrice;
    }

    public Double getClientMsrpPrice() {
        return clientMsrpPrice;
    }

    public void setClientMsrpPrice(Double clientMsrpPrice) {
        this.clientMsrpPrice = clientMsrpPrice;
    }

    public Double getClientRetailPrice() {
        return clientRetailPrice;
    }

    public void setClientRetailPrice(Double clientRetailPrice) {
        this.clientRetailPrice = clientRetailPrice;
    }
}
