package com.voyageone.wms.modelbean;

public class StocktakeBean {
    private long take_stock_id;
    private String order_channel_id;
    private String take_stock_name;
    private int store_id;
    private String take_stock_type;
    private String take_stock_status;
    private String syn_flg;
    private boolean active;
    private String created;
    private String creater;
    private String modified;
    private String modifier;


    public long getTake_stock_id() {
        return take_stock_id;
    }

    public void setTake_stock_id(long take_stock_id) {
        this.take_stock_id = take_stock_id;
    }

    public String getOrder_channel_id() {
        return order_channel_id;
    }

    public void setOrder_channel_id(String order_channel_id) {
        this.order_channel_id = order_channel_id;
    }

    public String getTake_stock_name() {
        return take_stock_name;
    }

    public void setTake_stock_name(String take_stock_name) {
        this.take_stock_name = take_stock_name;
    }

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public String getTake_stock_type() {
        return take_stock_type;
    }

    public void setTake_stock_type(String take_stock_type) {
        this.take_stock_type = take_stock_type;
    }

    public String getTake_stock_status() {
        return take_stock_status;
    }

    public void setTake_stock_status(String take_stock_status) {
        this.take_stock_status = take_stock_status;
    }

    public String getSyn_flg() {
        return syn_flg;
    }

    public void setSyn_flg(String syn_flg) {
        this.syn_flg = syn_flg;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
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
