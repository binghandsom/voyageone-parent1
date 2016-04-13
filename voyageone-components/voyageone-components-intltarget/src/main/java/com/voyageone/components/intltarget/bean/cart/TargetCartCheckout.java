package com.voyageone.components.intltarget.bean.cart;

/**
 * @author aooer 2016/4/13.
 * @version 2.0.0
 * @since 2.0.0
 */
public class TargetCartCheckout {

    private String orderId;

    private String orderItemId;

    private String physicalStoreId;

    /********* getter setter ********/

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId;
    }

    public String getPhysicalStoreId() {
        return physicalStoreId;
    }

    public void setPhysicalStoreId(String physicalStoreId) {
        this.physicalStoreId = physicalStoreId;
    }
}
