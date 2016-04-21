package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;


public class CmsMtChannelConfigModel extends BaseModel {

    /**

     */
    private String channelId;
    /**

     */
    private String configKey;
    /**

     */
    private String configCode;
    /**

     */
    private String configValue1;
    /**

     */
    private String configValue2;
    /**

     */
    private String configValue3;
    /**

     */
    private String comment;

    /**

     */
    private int status;
    /**
     * 0:基本配置 1:禁用关键词
     */
    private int configType;


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
    public String getConfigKey() {

        return this.configKey;
    }

    public void setConfigKey(String configKey) {
        if (configKey != null) {
            this.configKey = configKey;
        } else {
            this.configKey = "";
        }

    }


    /**

     */
    public String getConfigCode() {

        return this.configCode;
    }

    public void setConfigCode(String configCode) {
        if (configCode != null) {
            this.configCode = configCode;
        } else {
            this.configCode = "";
        }

    }


    /**

     */
    public String getConfigValue1() {

        return this.configValue1;
    }

    public void setConfigValue1(String configValue1) {
        if (configValue1 != null) {
            this.configValue1 = configValue1;
        } else {
            this.configValue1 = "";
        }

    }


    /**

     */
    public String getConfigValue2() {

        return this.configValue2;
    }

    public void setConfigValue2(String configValue2) {
        if (configValue2 != null) {
            this.configValue2 = configValue2;
        } else {
            this.configValue2 = "";
        }

    }


    /**

     */
    public String getConfigValue3() {

        return this.configValue3;
    }

    public void setConfigValue3(String configValue3) {
        if (configValue3 != null) {
            this.configValue3 = configValue3;
        } else {
            this.configValue3 = "";
        }

    }


    /**

     */
    public String getComment() {

        return this.comment;
    }

    public void setComment(String comment) {
        if (comment != null) {
            this.comment = comment;
        } else {
            this.comment = "";
        }

    }


    /**

     */
    public int getStatus() {

        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    /**
     * 0:基本配置 1:禁用关键词
     */
    public int getConfigType() {

        return this.configType;
    }

    public void setConfigType(int configType) {
        this.configType = configType;
    }

}