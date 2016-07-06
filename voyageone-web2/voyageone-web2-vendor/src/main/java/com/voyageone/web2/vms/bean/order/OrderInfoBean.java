package com.voyageone.web2.vms.bean.order;

/**
 * 单条的订单信息
 * Created by vantis on 16-7-6.
 */
public class OrderInfoBean {
    private String orderId;
    private long orderDateTimestamp;
    private String sku;
    private String desc;
    private int status;
    private double price;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public long getOrderDateTimestamp() {
        return orderDateTimestamp;
    }

    public void setOrderDateTimestamp(long orderDateTimestamp) {
        this.orderDateTimestamp = orderDateTimestamp;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
