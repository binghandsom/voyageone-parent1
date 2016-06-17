package com.voyageone.components.overstock.bean.order;

/**
 * @author aooer 2016/6/8.
 * @version 2.0.0
 * @since 2.0.0
 */
public class OverstockOrderLineCancelRequest {

    private String orderId; //查询orderid

    private String orderRemoveLineNumber; //待删除的lineNumber

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderRemoveLineNumber() {
        return orderRemoveLineNumber;
    }

    public void setOrderRemoveLineNumber(String orderRemoveLineNumber) {
        this.orderRemoveLineNumber = orderRemoveLineNumber;
    }
}
