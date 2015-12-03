package com.voyageone.wms.modelbean;

/**
 * wms_bt_item_details
 * Created by Tester on 5/18/2015.
 *
 * @author Jonas
 */
public class ItemDetailBean {
    private int item_details_id;
    private String order_channel_id;
    private String sku;
    private String itemcode;
    private String size;
    private String barcode;
    private boolean active;
    private String created;
    private String creater;
    private String modified;
    private String modifier;

    public int getItem_details_id() {
        return item_details_id;
    }

    public void setItem_details_id(int item_details_id) {
        this.item_details_id = item_details_id;
    }

    public String getOrder_channel_id() {
        return order_channel_id;
    }

    public void setOrder_channel_id(String order_channel_id) {
        this.order_channel_id = order_channel_id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getItemcode() {
        return itemcode;
    }

    public void setItemcode(String itemcode) {
        this.itemcode = itemcode;
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
