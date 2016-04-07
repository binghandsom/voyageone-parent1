package com.voyageone.components.sears.bean;

/**
 * Created by james.li on 2015/11/23.
 */
public class OrderResponse {

    private String orderId;
    private String message;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
