package com.voyageone.batch.wms.modelbean;

/**
 * Created by fred on 2015/8/5.
 * 第三方日报用
 */
public class ThirdReportBean {
    private String transfer_id;
    private String order_number;
    private String transfer_sku;
    private String sales_unit;
    private String order_channel_id;
    private String created;
    private String source_order_id;
    private String cart_id;
    private String itemcode;
    private String size;

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getTransfer_sku() {
        return transfer_sku;
    }

    public void setTransfer_sku(String transfer_sku) {
        this.transfer_sku = transfer_sku;
    }

    public String getSales_unit() {
        return sales_unit;
    }

    public void setSales_unit(String sales_unit) {
        this.sales_unit = sales_unit;
    }

    public String getOrder_channel_id() {
        return order_channel_id;
    }

    public void setOrder_channel_id(String order_channel_id) {
        this.order_channel_id = order_channel_id;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getSource_order_id() {
        return source_order_id;
    }

    public void setSource_order_id(String source_order_id) {
        this.source_order_id = source_order_id;
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public String getTransfer_id() {
        return transfer_id;
    }

    public void setTransfer_id(String transfer_id) {
        this.transfer_id = transfer_id;
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
}
