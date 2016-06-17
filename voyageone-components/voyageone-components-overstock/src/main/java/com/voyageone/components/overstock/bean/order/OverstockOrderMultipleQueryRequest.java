package com.voyageone.components.overstock.bean.order;

import com.overstock.mp.mpc.externalclient.model.OrderStatusType;
import com.voyageone.components.overstock.bean.OverstockMultipleRequest;

/**
 * @author aooer 2016/6/14.
 * @version 2.0.0
 * @since 2.0.0
 */
public class OverstockOrderMultipleQueryRequest extends OverstockMultipleRequest {

    private String orderNumber;

    private OrderStatusType status= OrderStatusType.ACCEPTED;

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public OrderStatusType getStatus() {
        return status;
    }

    public void setStatus(OrderStatusType status) {
        this.status = status;
    }
}
