package com.voyageone.service.bean.com;


import com.voyageone.base.dao.mysql.BaseModel;

/**
 * 渠道下应用系统的权限
 * Created on 11/30/15.
 *
 * @author Jonas
 * @version 2.0.0
 */
public class ctUserRolePropertyBean extends BaseModel {

    private Integer user_id;

    private Integer role_id;

    private String property_id;

    private String description;

    private int active;

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getRole_id() {
        return role_id;
    }

    public void setRole_id(Integer role_id) {
        this.role_id = role_id;
    }

    public String getProperty_id() {
        return property_id;
    }

    public void setProperty_id(String property_id) {
        this.property_id = property_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }
}
