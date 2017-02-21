package com.voyageone.task2.cms.bean;

/**
 * Created by gjl on 2017/2/16.
 */
public class SuperFeedKitBagStockBean extends SuperFeedBean {
    private String variationid;

    private String productid;

    private String quantity;

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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity == null ? null : quantity.trim();
    }
}
