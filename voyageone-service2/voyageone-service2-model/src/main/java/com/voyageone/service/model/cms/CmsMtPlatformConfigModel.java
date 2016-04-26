package com.voyageone.service.model.cms;

import java.io.Serializable;
import java.sql.Timestamp;

public class CmsMtPlatformConfigModel implements Serializable {
    /**

     */
    private int id;
    /**

     */
    private String channelId;
    /**

     */
    private int cartId;
    /**
     * 用于区分不同的配置类型
     */
    private int cfgType;
    /**

     */
    private String cfgName;
    /**

     */
    private String cfgVal;
    /**

     */
    private Timestamp created;
    /**

     */
    private String creater;
    /**

     */
    private Timestamp modified;
    /**

     */
    private String modifier;


    public CmsMtPlatformConfigModel() {
        setChannelId("");
        setCfgName("");
        setCfgVal("");
        setCreater("");
        setModifier("");

    }

    /**

     */
    public int getId() {

        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }


    /**

     */
    public String getChannelId() {

        return this.channelId;
    }

    public void setChannelId(String channelId) {
        if (channelId != null) {
            this.channelId = channelId;
        } else {
            this.channelId = "";
        }

    }


    /**

     */
    public int getCartId() {

        return this.cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }


    /**
     * 用于区分不同的配置类型
     */
    public int getCfgType() {

        return this.cfgType;
    }

    public void setCfgType(int cfgType) {
        this.cfgType = cfgType;
    }


    /**

     */
    public String getCfgName() {

        return this.cfgName;
    }

    public void setCfgName(String cfgName) {
        if (cfgName != null) {
            this.cfgName = cfgName;
        } else {
            this.cfgName = "";
        }

    }


    /**

     */
    public String getCfgVal() {

        return this.cfgVal;
    }

    public void setCfgVal(String cfgVal) {
        if (cfgVal != null) {
            this.cfgVal = cfgVal;
        } else {
            this.cfgVal = "";
        }

    }


    /**

     */
    public Timestamp getCreated() {

        return this.created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }


    /**

     */
    public String getCreater() {

        return this.creater;
    }

    public void setCreater(String creater) {
        if (creater != null) {
            this.creater = creater;
        } else {
            this.creater = "";
        }

    }


    /**

     */
    public Timestamp getModified() {

        return this.modified;
    }

    public void setModified(Timestamp modified) {
        this.modified = modified;
    }


    /**

     */
    public String getModifier() {

        return this.modifier;
    }

    public void setModifier(String modifier) {
        if (modifier != null) {
            this.modifier = modifier;
        } else {
            this.modifier = "";
        }

    }

}