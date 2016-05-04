package com.voyageone.components.intltarget.bean.cart;

import java.util.List;

/**
 * @author aooer 2016/5/3.
 * @version 2.0.0
 * @since 2.0.0
 */
public class TargetCartCheckoutResponse {
    private String orderId;

    public String getOrderId() {
        return this.orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    private List<OrderItem> orderItem;

    public List<OrderItem> getOrderItem() {
        return this.orderItem;
    }

    public void setOrderItem(List<OrderItem> orderItem) {
        this.orderItem = orderItem;
    }


    /**
     * OrderItem is the inner class of TargetCartCheckoutResponse
     */
    public static class OrderItem {
        private String orderItemId;

        public String getOrderItemId() {
            return this.orderItemId;
        }

        public void setOrderItemId(String orderItemId) {
            this.orderItemId = orderItemId;
        }

    }

    private String responseTime;

    public String getResponseTime() {
        return this.responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    private String customerOrderNumber;

    public String getCustomerOrderNumber() {
        return this.customerOrderNumber;
    }

    public void setCustomerOrderNumber(String customerOrderNumber) {
        this.customerOrderNumber = customerOrderNumber;
    }

    private String placedDate;

    public String getPlacedDate() {
        return this.placedDate;
    }

    public void setPlacedDate(String placedDate) {
        this.placedDate = placedDate;
    }

}
