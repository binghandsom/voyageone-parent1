package com.voyageone.batch.core.modelbean;

/**
 * Created by fred on 2015/8/10.
 */
public class SetPriceBean {
    private String source_order_id;
    private String order_date_time;
    private String cart_id;
    private String sku;
    private String price;
    // 订单折扣
    private String shipping_price;
    // 运费
    private String shipping_fee;

    // 订单数量
    private String quantity_ordered;
    // 第三方SKU
    private String client_sku;
    // 关税率
    private String duty_rate;

    public String getSource_order_id() {
        return source_order_id;
    }

    public void setSource_order_id(String source_order_id) {
        this.source_order_id = source_order_id;
    }

    public String getOrder_date_time() {
        return order_date_time;
    }

    public void setOrder_date_time(String order_date_time) {
        this.order_date_time = order_date_time;
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getShipping_price() {
        return shipping_price;
    }

    public void setShipping_price(String shipping_price) {
        this.shipping_price = shipping_price;
    }

    public String getShipping_fee() {
        return shipping_fee;
    }

    public void setShipping_fee(String shipping_fee) {
        this.shipping_fee = shipping_fee;
    }

    public String getQuantity_ordered() {
        return quantity_ordered;
    }

    public void setQuantity_ordered(String quantity_ordered) {
        this.quantity_ordered = quantity_ordered;
    }

    public String getClient_sku() {
        return client_sku;
    }

    public void setClient_sku(String client_sku) {
        this.client_sku = client_sku;
    }

    public String getDuty_rate() {
        return duty_rate;
    }

    public void setDuty_rate(String duty_rate) {
        this.duty_rate = duty_rate;
    }
}


