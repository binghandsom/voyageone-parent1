package com.voyageone.components.cn.beans;

/**
 * Created by jonas on 15/6/8.
 */
public class InventoryUpdateBean {

    private String sku;
    private int qty;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
