package com.voyageone.components.intltarget.bean.inventory;

/**
 * @author aooer 2016/4/19.
 * @version 2.0.0
 * @since 2.0.0
 */
public class TargetInventoryRequestProduct {

    private String product_id;
    private String location_ids;
    private String channel_id;
    private boolean is_parent_id;
    private String multichannel_option;
    private String inventory_type;
    private String sort;
    private String field_groups;
    private int limit;

    //~~~~~~~~~~getter setter


    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getLocation_ids() {
        return location_ids;
    }

    public void setLocation_ids(String location_ids) {
        this.location_ids = location_ids;
    }

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public boolean is_parent_id() {
        return is_parent_id;
    }

    public void setIs_parent_id(boolean is_parent_id) {
        this.is_parent_id = is_parent_id;
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

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getField_groups() {
        return field_groups;
    }

    public void setField_groups(String field_groups) {
        this.field_groups = field_groups;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
