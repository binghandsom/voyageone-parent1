package com.voyageone.components.gilt.bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author aooer 2016/2/1.
 * @version 2.0.0
 * @since 2.0.0
 */
public class GiltOrder {

    private UUID id;//Universally Unique Identifier (Your customer's order)

    private GiltOrderStatus status;//Current Order status

    private Date expires_on;//Date and time of when this order will expire if not confirmed

    private List<GiltOrderItem> order_items;//List of order items contained in this order

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public GiltOrderStatus getStatus() {
        return status;
    }

    public void setStatus(GiltOrderStatus status) {
        this.status = status;
    }

    public Date getExpires_on() {
        return expires_on;
    }

    public void setExpires_on(Date expires_on) {
        this.expires_on = expires_on;
    }

    public List<GiltOrderItem> getOrder_items() {
        return order_items;
    }

    public void setOrder_items(List<GiltOrderItem> order_items) {
        this.order_items = order_items;
    }
}
