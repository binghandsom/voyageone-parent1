package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;

public class CmsMtPlatformSkusModel extends BaseModel {

    /**

     */
    private String channelId;
    /**

     */
    private int cartId;
    /**

     */
    private String platformCategoryId;
    /**
     * 属性类型：(c: color), (s: size), (c_e: color_extra), (s_e: size_extra)
     */
    private String attrType;
    /**
     * 连续的数字（channel， cart， category， type）
     */
    private int idx;
    /**

     */
    private String attrName;
    /**

     */
    private String attrValue;
    /**

     */
    private boolean active;


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

     */
    public String getPlatformCategoryId() {

        return this.platformCategoryId;
    }

    public void setPlatformCategoryId(String platformCategoryId) {
        if (platformCategoryId != null) {
            this.platformCategoryId = platformCategoryId;
        } else {
            this.platformCategoryId = "";
        }

    }


    /**
     * 属性类型：(c: color), (s: size), (c_e: color_extra), (s_e: size_extra)
     */
    public String getAttrType() {

        return this.attrType;
    }

    public void setAttrType(String attrType) {
        if (attrType != null) {
            this.attrType = attrType;
        } else {
            this.attrType = "";
        }

    }


    /**
     * 连续的数字（channel， cart， category， type）
     */
    public int getIdx() {

        return this.idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }


    /**

     */
    public String getAttrName() {

        return this.attrName;
    }

    public void setAttrName(String attrName) {
        if (attrName != null) {
            this.attrName = attrName;
        } else {
            this.attrName = "";
        }

    }


    /**

     */
    public String getAttrValue() {

        return this.attrValue;
    }

    public void setAttrValue(String attrValue) {
        if (attrValue != null) {
            this.attrValue = attrValue;
        } else {
            this.attrValue = "";
        }

    }


    /**

     */
    public boolean getActive() {

        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }


}