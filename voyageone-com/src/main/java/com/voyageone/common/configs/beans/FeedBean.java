package com.voyageone.common.configs.beans;

/**
 * Created by Zero on 8/18/2015.
 */
public class FeedBean {
    private int id;

    private String order_channel_id;

    private String cfg_name;

    private String cfg_val1;

    private String cfg_val2;

    private String cfg_val3;

    private int is_attribute;

    private int attribute_type;

    private int display_sort;

    private String comment;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrder_channel_id() {
        return order_channel_id;
    }

    public void setOrder_channel_id(String order_channel_id) {
        this.order_channel_id = order_channel_id;
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

    public String getCfg_val3() {
        return cfg_val3;
    }

    public void setCfg_val3(String cfg_val3) {
        this.cfg_val3 = cfg_val3;
    }

    public int getIs_attribute() {
        return is_attribute;
    }

    public void setIs_attribute(int is_attribute) {
        this.is_attribute = is_attribute;
    }

    public int getAttribute_type() {
        return attribute_type;
    }

    public void setAttribute_type(int attribute_type) {
        this.attribute_type = attribute_type;
    }

    public int getDisplay_sort() {
        return display_sort;
    }

    public void setDisplay_sort(int display_sort) {
        this.display_sort = display_sort;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
