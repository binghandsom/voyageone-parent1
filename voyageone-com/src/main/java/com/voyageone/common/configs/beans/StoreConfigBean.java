package com.voyageone.common.configs.beans;

/**
 * Created by Jack on 7/6/2015.
 */
public class StoreConfigBean {
    private long store_id;

    private String cfg_name;

    private String cfg_val1;

    private String cfg_val2;

    private String comment;

    public long getStore_id() {
        return store_id;
    }

    public void setStore_id(long store_id) {
        this.store_id = store_id;
    }

    public String getCfg_name() {
        return cfg_name;
    }

    public void setCfg_name(String cfg_name) {
        this.cfg_name = cfg_name;
    }

    public String getCfg_val1() {
        return cfg_val1;
    }

    public void setCfg_val1(String cfg_val1) {
        this.cfg_val1 = cfg_val1;
    }

    public String getCfg_val2() {
        return cfg_val2;
    }

    public void setCfg_val2(String cfg_val2) {
        this.cfg_val2 = cfg_val2;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
