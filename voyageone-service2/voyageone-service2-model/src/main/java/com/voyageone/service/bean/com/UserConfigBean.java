package com.voyageone.service.bean.com;

import java.io.Serializable;

/**
 * 自定义的用户配置
 * Created on 11/26/15.
 *
 * @author Jonas
 * @version 2.0.0
 */
public class UserConfigBean implements Serializable {
    private int user_id;

    private String cfg_name;

    private String cfg_val1;

    private String cfg_val2;

    private String comment;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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
