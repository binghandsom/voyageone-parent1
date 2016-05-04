package com.voyageone.components.intltarget.bean.inventory;

import java.util.List;

/**
 * @author aooer 2016/4/19.
 * @version 2.0.0
 * @since 2.0.0
 */
public class TargetInventory {

    private List<TargetInventoryOrderProduct> products;

    public List<TargetInventoryOrderProduct> getProducts() {
        return products;
    }

    public void setProducts(List<TargetInventoryOrderProduct> products) {
        this.products = products;
    }
}
