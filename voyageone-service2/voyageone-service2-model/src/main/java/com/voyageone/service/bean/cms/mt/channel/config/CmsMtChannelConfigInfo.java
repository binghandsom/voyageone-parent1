package com.voyageone.service.bean.cms.mt.channel.config;

import java.util.Date;

/**
 * Created by dell on 2016/12/13.
 */
public class CmsMtChannelConfigInfo {
    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public void setConfigValue1(Boolean configValue1) {
        isConfigValue1 = configValue1;
    }

    public void setConfigValue1(String configValue1) {
        this.configValue1 = configValue1;
    }

    public String getConfigValue2() {
        return configValue2;
    }

    public void setConfigValue2(Boolean configValue2) {
        isConfigValue2 = configValue2;
    }

    public void setConfigValue2(String configValue2) {
        this.configValue2 = configValue2;
    }

    public String getConfigValue3() {
        return configValue3;
    }

    public void setConfigValue3(Boolean configValue3) {
        isConfigValue3 = configValue3;
    }

    public Boolean getPlatform() {
        return isPlatform;
    }

    public void setPlatform(Boolean platform) {
        isPlatform = platform;
    }

    public String getSample() {
        return sample;
    }

    public void setSample(String sample) {
        this.sample = sample;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setConfigValue3(String configValue3) {
        this.configValue3 = configValue3;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public Boolean isChecked;//是否使用
    public Integer id;
    public String configKey;
    public String configCode;
    public String configValue1;
    public String configValue2;
    public String configValue3;
    public String creater;
    public Date modified;
    public String modifier;
    public Boolean isConfigValue1;//是否启用config_value1
    public Boolean isConfigValue2;//是否启用config_value2
    public Boolean isConfigValue3;//是否启用config_value3
    public Boolean isPlatform;//是否平台级别参数
    public String sample;
    public String comment;
}
