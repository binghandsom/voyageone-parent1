package com.voyageone.common.configs.beans;

/**
 * Created by Jack on 6/3/2015.
 */
public class DiscountRateBean {
    private String order_channel_id;

    private String ship_channel;

    private String discount_rate;

    public String getOrder_channel_id() {
        return order_channel_id;
    }

    public void setOrder_channel_id(String order_channel_id) {
        this.order_channel_id = order_channel_id;
    }

    public String getShip_channel() {
        return ship_channel;
    }

    public void setShip_channel(String ship_channel) {
        this.ship_channel = ship_channel;
    }

    public String getDiscount_rate() {
        return discount_rate;
    }

    public void setDiscount_rate(String discount_rate) {
        this.discount_rate = discount_rate;
    }
}
