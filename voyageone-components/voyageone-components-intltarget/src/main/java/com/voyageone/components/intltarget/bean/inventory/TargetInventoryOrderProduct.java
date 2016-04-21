package com.voyageone.components.intltarget.bean.inventory;

/**
 * @author aooer 2016/4/19.
 * @version 2.0.0
 * @since 2.0.0
 */
public class TargetInventoryOrderProduct {

    private int request_line_id;

    private TargetInventoryProduct product;

    public int getRequest_line_id() {
        return request_line_id;
    }

    public void setRequest_line_id(int request_line_id) {
        this.request_line_id = request_line_id;
    }

    public TargetInventoryProduct getProduct() {
        return product;
    }

    public void setProduct(TargetInventoryProduct product) {
        this.product = product;
    }
}
