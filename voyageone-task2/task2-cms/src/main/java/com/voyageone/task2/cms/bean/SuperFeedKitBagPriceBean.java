package com.voyageone.task2.cms.bean;

/**
 * Created by gjl on 2017/2/16.
 */
public class SuperFeedKitBagPriceBean extends SuperFeedBean {

    private String variationid;

    private String productid;

    private String sitename;

    private String siteid;

    private String territory;

    private String price;

    private String pricewas;

    private String currency;

    public String getVariationid() {
        return variationid;
    }

    public void setVariationid(String variationid) {
        this.variationid = variationid == null ? null : variationid.trim();
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid == null ? null : productid.trim();
    }

    public String getSitename() {
        return sitename;
    }

    public void setSitename(String sitename) {
        this.sitename = sitename == null ? null : sitename.trim();
    }

    public String getSiteid() {
        return siteid;
    }

    public void setSiteid(String siteid) {
        this.siteid = siteid == null ? null : siteid.trim();
    }

    public String getTerritory() {
        return territory;
    }

    public void setTerritory(String territory) {
        this.territory = territory == null ? null : territory.trim();
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price == null ? null : price.trim();
    }

    public String getPricewas() {
        return pricewas;
    }

    public void setPricewas(String pricewas) {
        this.pricewas = pricewas == null ? null : pricewas.trim();
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency == null ? null : currency.trim();
    }

}
