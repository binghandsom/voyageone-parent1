package com.voyageone.components.gilt.bean;

/**
 * @author aooer 2016/2/1.
 * @version 2.0.0
 * @since 2.0.0
 */
public enum GiltErrorType {

    user_order_expired	("user_order_expired","The specified Order has expired. A new Order must be placed."),

    internal_no_inventory	("internal_no_inventory","The requested Sku ID has no Inventory remaining"),

    invalid_order_status("invalid_order_status","The Order cannot be placed into this state. For example a \"confirmed\" Order cannot be changed to \"placed\""),

    sku_not_found("sku_not_found","The provided Sku Id did not match up to any Sku that Gilt provides"),

    quantity_not_satisfied("quantity_not_satisfied","There is not enough remaining inventory for the provide Sku Id to fulfil the Order."),

    unable_to_cancel ("unable_to_cancel","The order has been confirmed and processed and therefore cannot be cancelled through the API")
        ;

    private String type;

    private String description;

    GiltErrorType(String type, String description) {
        this.type = type;
        this.description = description;
    }
}
