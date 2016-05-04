package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;

public class CmsMtPlatformConfigModel extends BaseModel {

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


}