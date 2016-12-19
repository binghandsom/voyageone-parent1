package com.voyageone.service.bean.cms.mt.channel.config;

import java.util.Date;

/**
 * Created by dell on 2016/12/13.
 */
public class CmsMtChannelConfigInfo {
     Boolean isChecked;//是否使用

    public Boolean getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(Boolean checked) {
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

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
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

    public Boolean getIsPlatform() {
        return isPlatform;
    }

    public void setIsPlatform(Boolean platform) {
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
    public Boolean getIsConfigValue1() {
        return isConfigValue1;
    }

    public void setIsConfigValue1(Boolean isConfigValue1) {
        this.isConfigValue1 = isConfigValue1;
    }
    public Boolean getIsConfigValue2() {
        return isConfigValue2;
    }

    public void setIsConfigValue2(Boolean isConfigValue2) {
        this.isConfigValue2 = isConfigValue2;
    }
    public Boolean getIsConfigValue3() {
        return isConfigValue3;
    }

    public void setIsConfigValue3(Boolean isConfigValue3) {
        this.isConfigValue3 = isConfigValue3;
    }
     Integer id;
     String configKey;
     String configCode;
     String configValue1;
     String configValue2;
     String configValue3;
     Date created;
     Date modified;
     String modifier;
     Boolean isConfigValue1;//是否启用config_value1
     Boolean isConfigValue2;//是否启用config_value2
     Boolean isConfigValue3;//是否启用config_value3
     Boolean isPlatform;//是否平台级别参数
     String sample;
     String comment;
}
