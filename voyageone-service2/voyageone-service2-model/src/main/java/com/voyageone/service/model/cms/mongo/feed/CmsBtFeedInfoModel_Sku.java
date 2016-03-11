package com.voyageone.service.model.cms.mongo.feed;

/**
 * Created by james.li on 2015/11/27.
 */
public class CmsBtFeedInfoModel_Sku {
    private Double price_current;
    private Double price_msrp;
    private String sku;
    private String size;
    private String barcode;
    private String clientSku;

    public Double getPrice_current() {
        return price_current;
    }

    public void setPrice_current(Double price_current) {
        this.price_current = price_current;
    }

    public Double getPrice_msrp() {
        return price_msrp;
    }

    public void setPrice_msrp(Double price_msrp) {
        this.price_msrp = price_msrp;
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

    @Override
    public boolean equals(Object obj) {
         return  this.sku.equalsIgnoreCase(((CmsBtFeedInfoModel_Sku)obj).getSku());
    }
}
