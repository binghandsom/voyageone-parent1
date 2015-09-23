package com.voyageone.common.configs.beans;
/**
 * Created by Jack on 6/4/2015.
 */
public class StoreBean {
    private long store_id;
    private String order_channel_id;
    private String store_name;
    private String store_type;
    private String store_location;
    private String store_kind;
    private long parent_store_id;
    private String label_type;
    private long rsv_sort;
    private String is_sale;
    private String inventory_manager;
    private String inventory_hold;
    private String inventory_syn_type;
    private String active;
    private String created;
    private String creater;
    private String modified;
    private String modifier;

    public long getStore_id() {
        return store_id;
    }

    public void setStore_id(long store_id) {
        this.store_id = store_id;
    }

    public String getOrder_channel_id() {
        return order_channel_id;
    }

    public void setOrder_channel_id(String order_channel_id) {
        this.order_channel_id = order_channel_id;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getStore_type() {
        return store_type;
    }

    public void setStore_type(String store_type) {
        this.store_type = store_type;
    }

    public String getStore_location() {
        return store_location;
    }

    public void setStore_location(String store_location) {
        this.store_location = store_location;
    }

    public String getStore_kind() {
        return store_kind;
    }

    public void setStore_kind(String store_kind) {
        this.store_kind = store_kind;
    }

    public long getParent_store_id() {
        return parent_store_id;
    }

    public void setParent_store_id(long parent_store_id) {
        this.parent_store_id = parent_store_id;
    }

    public String getLabel_type() {
        return label_type;
    }

    public void setLabel_type(String label_type) {
        this.label_type = label_type;
    }

    public long getRsv_sort() {
        return rsv_sort;
    }

    public void setRsv_sort(long rsv_sort) {
        this.rsv_sort = rsv_sort;
    }

    public String getIs_sale() {
        return is_sale;
    }

    public void setIs_sale(String is_sale) {
        this.is_sale = is_sale;
    }

    public String getInventory_manager() {
        return inventory_manager;
    }

    public void setInventory_manager(String inventory_manager) {
        this.inventory_manager = inventory_manager;
    }

    public String getInventory_hold() {
        return inventory_hold;
    }

    public void setInventory_hold(String inventory_hold) {
        this.inventory_hold = inventory_hold;
    }

    public String getInventory_syn_type() {
        return inventory_syn_type;
    }

    public void setInventory_syn_type(String inventory_syn_type) {
        this.inventory_syn_type = inventory_syn_type;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }
}
