package com.voyageone.service.model.cms.mongo.feed;

/**
 * Created by james.li on 2015/11/27.
 */
public class CmsBtFeedInfoModel_Sku {
    private Double priceCurrent;
    private Double priceMsrp;
    private Double priceNet;
    private Double priceClientRetail;
    private Double priceClientMsrp;
    private String sku;
    private String size;
    private String barcode;
    private String clientSku;
    private Integer qty;

    public Double getPriceCurrent() {
        return priceCurrent;
    }

    public void setPriceCurrent(Double priceCurrent) {
        this.priceCurrent = priceCurrent;
    }

    public Double getPriceMsrp() {
        return priceMsrp;
    }

    public void setPriceMsrp(Double priceMsrp) {
        this.priceMsrp = priceMsrp;
    }

    public Double getPriceNet() {
        return priceNet;
    }

    public void setPriceNet(Double priceNet) {
        this.priceNet = priceNet;
    }

    public Double getPriceClientRetail() {
        return priceClientRetail;
    }

    public void setPriceClientRetail(Double priceClientRetail) {
        this.priceClientRetail = priceClientRetail;
    }

    public Double getPriceClientMsrp() {
        return priceClientMsrp;
    }

    public void setPriceClientMsrp(Double priceClientMsrp) {
        this.priceClientMsrp = priceClientMsrp;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getClientSku() {
        return clientSku;
    }

    public void setClientSku(String clientSku) {
        this.clientSku = clientSku;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    @Override
    public boolean equals(Object obj) {
         return  this.sku.equalsIgnoreCase(((CmsBtFeedInfoModel_Sku)obj).getSku());
    }
}
