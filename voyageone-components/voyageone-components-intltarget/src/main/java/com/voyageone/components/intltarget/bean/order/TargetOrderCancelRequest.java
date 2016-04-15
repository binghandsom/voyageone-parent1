package com.voyageone.components.intltarget.bean.order;

import java.util.List;

/**
 * @author aooer 2016/4/13.
 * @version 2.0.0
 * @since 2.0.0
 */
public class TargetOrderCancelRequest {

    private String orderNumber;

    private List<String> orderItemId;

    /********  getter setter  ********/

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public List<String> getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(List<String> orderItemId) {
        this.orderItemId = orderItemId;
    }
}
