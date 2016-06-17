package com.voyageone.components.overstock.bean.order;

import com.overstock.mp.mpc.externalclient.model.OrderStatusType;

/**
 * @author aooer 2016/6/8.
 * @version 2.0.0
 * @since 2.0.0
 */
public class OverstockOrderWholeCancelRequest {

    private String orderId; //查询orderid

    private OrderStatusType orderStatusType= OrderStatusType.CANCELED;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public OrderStatusType getOrderStatusType() {
        return orderStatusType;
    }

    public void setOrderStatusType(OrderStatusType orderStatusType) {
        this.orderStatusType = orderStatusType;
    }
}
