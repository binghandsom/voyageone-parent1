package com.voyageone.task2.cms.model;

/**
 * Created by Leo on 15-7-17.
 */
public class PlatformSkuInfoModel {
    private int cart_id;
    private String prop_id;
    private long sku_type;

    public int getCart_id() {
        return cart_id;
    }

    public void setCart_id(int cart_id) {
        this.cart_id = cart_id;
    }

    public String getProp_id() {
        return prop_id;
    }

    public void setProp_id(String prop_id) {
        this.prop_id = prop_id;
    }

    public long getSku_type() {
        return sku_type;
    }

    public void setSku_type(long sku_type) {
        this.sku_type = sku_type;
    }
}
