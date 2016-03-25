package com.voyageone.service.model.cms.mongo.feed;

/**
 * Created by james.li on 2015/11/27.
 */
public class CmsBtFeedInfoModel_Sku {
    private Double price_current;
    private Double price_msrp;
    private Double price_net;
    private Double price_client_retail;
    private Double price_client_msrp;
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

    public Double getPrice_net() {
        return price_net;
    }

    public void setPrice_net(Double price_net) {
        this.price_net = price_net;
    }

    public Double getPrice_client_retail() {
        return price_client_retail;
    }

    public void setPrice_client_retail(Double price_client_retail) {
        this.price_client_retail = price_client_retail;
    }

    public Double getPrice_client_msrp() {
        return price_client_msrp;
    }

    public void setPrice_client_msrp(Double price_client_msrp) {
        this.price_client_msrp = price_client_msrp;
    }

    @Override
    public boolean equals(Object obj) {
         return  this.sku.equalsIgnoreCase(((CmsBtFeedInfoModel_Sku)obj).getSku());
    }
}
