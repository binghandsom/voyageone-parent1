package com.voyageone.batch.cms.model;

/**
 * Created by Leo on 15-12-14.
 */
public class SkuInventoryModel {
    private String sku;
    private int inventory;

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }
}
