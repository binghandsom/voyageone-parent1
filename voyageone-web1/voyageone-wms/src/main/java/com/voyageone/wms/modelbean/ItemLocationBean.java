package com.voyageone.wms.modelbean;

/**
 * wms_bt_item_location
 * Created by Tester on 5/18/2015.
 *
 * @author Jonas
 */
public class ItemLocationBean {
    private int item_location_id;
    private String order_channel_id;
    private int store_id;
    private String code;
    private String sku;
    private int location_id;
    private boolean active;
    private String created;
    private String creater;
    private String modified;
    private String modifier;

    public int getItem_location_id() {
        return item_location_id;
    }

    public void setItem_location_id(int item_location_id) {
        this.item_location_id = item_location_id;
    }

    public String getOrder_channel_id() {
        return order_channel_id;
    }

    public void setOrder_channel_id(String order_channel_id) {
        this.order_channel_id = order_channel_id;
    }

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public int getLocation_id() {
        return location_id;
    }

    public void setLocation_id(int location_id) {
        this.location_id = location_id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }
}
