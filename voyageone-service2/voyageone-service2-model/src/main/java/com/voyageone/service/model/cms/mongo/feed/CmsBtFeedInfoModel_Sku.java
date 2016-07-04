package com.voyageone.service.model.cms.mongo.feed;

import java.util.List;

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
    private List<String> image;
    private Integer qty;
    private String relationshipType;
    private String variationTheme;


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
        if (qty == null) return 0;
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public List<String> getImage() { return image; }

    public void setImage(List<String> image) { this.image = image; }


    public String getRelationshipType() {
        return relationshipType;
    }

    public void setRelationshipType(String relationshipType) {
        this.relationshipType = relationshipType;
    }

    public String getVariationTheme() {
        return variationTheme;
    }

    public void setVariationTheme(String variationTheme) {
        this.variationTheme = variationTheme;
    }

    @Override
    public boolean equals(Object obj) {
         return  this.sku.equalsIgnoreCase(((CmsBtFeedInfoModel_Sku)obj).getSku());
    }
}
