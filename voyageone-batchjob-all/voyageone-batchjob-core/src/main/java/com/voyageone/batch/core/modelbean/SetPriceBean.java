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
    private String shipping_price;
    private String discount;
    private String sum_unit;

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

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getSum_unit() {
        return sum_unit;
    }

    public void setSum_unit(String sum_unit) {
        this.sum_unit = sum_unit;
    }
}


