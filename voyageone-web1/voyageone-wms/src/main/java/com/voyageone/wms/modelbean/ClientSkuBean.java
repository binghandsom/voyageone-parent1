package com.voyageone.wms.modelbean;

/**
 * Created by Tester on 4/30/2015.
 *
 * @author Jonas
 */
public class ClientSkuBean {
    private String order_channel_id;
    private String barcode;
    private String item_code;
    private String size;
    private int active;
    private String created;
    private String creater;
    private String modified;
    private String modifier;

    public String getOrder_channel_id() {
        return order_channel_id;
    }

    public void setOrder_channel_id(String order_channel_id) {
        this.order_channel_id = order_channel_id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getItem_code() {
        return item_code;
    }

    public void setItem_code(String item_code) {
        this.item_code = item_code;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
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
