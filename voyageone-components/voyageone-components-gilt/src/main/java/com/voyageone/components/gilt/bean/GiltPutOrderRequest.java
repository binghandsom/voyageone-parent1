package com.voyageone.components.gilt.bean;

import org.springframework.util.Assert;

import java.util.List;
import java.util.UUID;

/**
 * @author aooer 2016/2/2.
 * @version 2.0.0
 * @since 2.0.0
 */
public class GiltPutOrderRequest {

    /* ? 待确认参数 */
    private UUID id;

    private List<GiltOrderItem> order_items;

    public List<GiltOrderItem> getOrder_items() {
        return order_items;
    }

    public void setOrder_items(List<GiltOrderItem> order_items) {
        this.order_items = order_items;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void check(){
        Assert.notNull(id,"id 不能为空");
        Assert.notEmpty(order_items,"order items不能为空");
    }
}
