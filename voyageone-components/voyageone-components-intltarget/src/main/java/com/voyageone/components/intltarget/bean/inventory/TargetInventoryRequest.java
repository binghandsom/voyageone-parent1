package com.voyageone.components.intltarget.bean.inventory;

import java.util.List;

/**
 * @author aooer 2016/4/19.
 * @version 2.0.0
 * @since 2.0.0
 */
public class TargetInventoryRequest {

    private List<TargetInventoryOrderRequestProduct> products;

    public List<TargetInventoryOrderRequestProduct> getProducts() {
        return products;
    }

    public void setProducts(List<TargetInventoryOrderRequestProduct> products) {
        this.products = products;
    }
}
