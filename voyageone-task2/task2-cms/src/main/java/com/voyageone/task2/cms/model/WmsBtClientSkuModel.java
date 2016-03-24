package com.voyageone.task2.cms.model;

import com.voyageone.base.dao.mysql.BaseModel;

/**
 * Created by jonasvlag on 16/3/11.
 *
 * @version 2.0.0
 * @since 2.0.0
 */
public class WmsBtClientSkuModel extends BaseModel {

    private int seq;

    private String order_channel_id;

    private String barcode;

    private String item_code;

    private String color;

    private String size;

    private String upc;

    private boolean active;

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
