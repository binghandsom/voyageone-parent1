package com.voyageone.cms.service.model;


import com.voyageone.base.dao.mongodb.model.BaseMongoMap;

import java.util.List;
import java.util.Map;

public class CmsBtProductModel_Sku extends BaseMongoMap {

    public CmsBtProductModel_Sku() {

    }
    public CmsBtProductModel_Sku(Map m) {
        this.putAll(m);
    }


    public String getSku() {
        return (String) getAttribute("sku");
    }

    public void setSku(String sku) {
        setAttribute("sku", sku);
    }

    public String getUpc() {
        return (String) getAttribute("upc");
    }

    public void setUpc(String upc) {
        setAttribute("upc", upc);
    }

    public double getPriceMsrp() {
        return (double) getAttribute("priceMsrp");
    }

    public void setPriceMsrp(double priceMsrp) {
        setAttribute("priceMsrp", priceMsrp);
    }

    public double getPriceRetail() {
        return (double) getAttribute("priceRetail");
    }

    public void setPriceRetail(double priceRetail) {
        setAttribute("priceRetail", priceRetail);
    }

    public double getPriceSale() {
        return (double) getAttribute("priceSale");
    }

    public void setPriceSale(double priceSale) {
        setAttribute("priceSale", priceSale);
    }

    public String getPlatformArrCode() {
        return (String) getAttribute("platformArrCode");
    }

    public void setPlatformArrCode(String platformArrCode) {
        setAttribute("platformArrCode", platformArrCode);
    }

}