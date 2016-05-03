package com.voyageone.components.intltarget.bean.cart;

import java.util.List;

/**
 * @author aooer 2016/4/13.
 * @version 2.0.0
 * @since 2.0.0
 */
public class TargetCartCheckoutResponse {

    public String orderId;
    public List<OrderItem> orderItem;
    public String responseTime;
    public String customerOrderNumber;
    public String placedDate;

    /** OrderItem is the inner class of TargetCartCheckout */
    public class OrderItem {
        public String orderItemId;

        public String getOrderItemId() {
            return orderItemId;
        }

        public void setOrderItemId(String orderItemId) {
            this.orderItemId = orderItemId;
        }
    }

}
