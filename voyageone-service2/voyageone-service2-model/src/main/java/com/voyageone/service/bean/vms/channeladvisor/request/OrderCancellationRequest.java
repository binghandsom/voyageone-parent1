package com.voyageone.service.bean.vms.channeladvisor.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.voyageone.service.bean.vms.channeladvisor.order.OrderItemCancellationModel;

import java.util.List;

/**
 * @author aooer 2016/9/6.
 * @version 2.0.0
 * @since 2.0.0
 */
public class OrderCancellationRequest {

    @JsonProperty("OrderID")
    private String orderId;

    @JsonProperty("Items")
    private List<OrderItemCancellationModel> items;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<OrderItemCancellationModel> getItems() {
        return items;
    }

    public void setItems(List<OrderItemCancellationModel> items) {
        this.items = items;
    }
}
