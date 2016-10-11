package com.voyageone.service.bean.vms.channeladvisor.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.voyageone.service.bean.vms.channeladvisor.CABaseModel;

import java.util.List;

/**
 * @author aooer 2016/9/6.
 * @version 2.0.0
 * @since 2.0.0
 */
public class OrderFulfillmentModel extends CABaseModel {

    @JsonProperty("ID")
    private String id;

    @JsonProperty("Items")
    private List<OrderItemFulfillmentModel> items;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<OrderItemFulfillmentModel> getItems() {
        return items;
    }

    public void setItems(List<OrderItemFulfillmentModel> items) {
        this.items = items;
    }

}
