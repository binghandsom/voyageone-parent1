package com.voyageone.service.model.cms;


import com.voyageone.base.dao.mysql.BaseModel;

public class CmsBtChannelConfigModel extends BaseModel {

    private Integer seq;

    private String channelId;

    private String configKey;

    private String configCode;

    private String configValue1;

    private String configValue2;

    private String configValue3;

    private String comment;

    private Integer status;

    private Integer configType;

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigCode() {
        return configCode;
    }

    public void setConfigCode(String configCode) {
        this.configCode = configCode;
    }

    public String getConfigValue1() {
        return configValue1;
    }

    public void setConfigValue1(String configValue1) {
        this.configValue1 = configValue1;
    }

    public String getConfigValue2() {
        return configValue2;
    }

    public void setConfigValue2(String configValue2) {
        this.configValue2 = configValue2;
    }

    public String getConfigValue3() {
        return configValue3;
    }

    public void setConfigValue3(String configValue3) {
        this.configValue3 = configValue3;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getConfigType() {
        return configType;
    }

    public void setConfigType(Integer configType) {
        this.configType = configType;
    }
}
