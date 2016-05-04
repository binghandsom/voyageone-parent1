package com.voyageone.components.intltarget.bean.inventory;

/**
 * @author aooer 2016/4/19.
 * @version 2.0.0
 * @since 2.0.0
 */
public class TargetInventoryRequest {

    private String product_id;

    private String multichannel_option="ship"; //default value

    private String inventory_type="all"; //default value

    private String field_groups="summary"; //default value

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getMultichannel_option() {
        return multichannel_option;
    }

    public void setMultichannel_option(String multichannel_option) {
        this.multichannel_option = multichannel_option;
    }

    public String getInventory_type() {
        return inventory_type;
    }

    public void setInventory_type(String inventory_type) {
        this.inventory_type = inventory_type;
    }

    public String getField_groups() {
        return field_groups;
    }

    public void setField_groups(String field_groups) {
        this.field_groups = field_groups;
    }

}
