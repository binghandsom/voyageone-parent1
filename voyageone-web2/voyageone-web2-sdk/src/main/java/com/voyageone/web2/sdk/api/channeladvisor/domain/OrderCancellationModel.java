package com.voyageone.web2.sdk.api.channeladvisor.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author aooer 2016/9/6.
 * @version 2.0.0
 * @since 2.0.0
 */
public class OrderCancellationModel {

    @JsonProperty("OrderId")
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
