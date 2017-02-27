package com.voyageone.common.configs.beans;


import com.voyageone.base.dao.mysql.BaseModel;


/**
 * CmsChannelConfigBean 配置专用类
 * @author chuanyu.liang, 12/11/15
 * @version 2.0.0
 * @since 2.0.0
 */
public class CmsChannelConfigBean extends BaseModel {

    private String channelId;

    private String configKey;

    private String configCode;

    private String configValue1;

    private String configValue2;

    private String configValue3;

    public CmsChannelConfigBean(){
    }

    public CmsChannelConfigBean(String configValue1, String configValue2, String configValue3){
        this.configValue1 = configValue1;
        this.configValue2 = configValue2;
        this.configValue3 = configValue3;
    }

    public CmsChannelConfigBean(String channelId,
                                String configKey,
                                String configCode,
                                String configValue1,
                                String configValue2,
                                String configValue3,
                                String modifier
    ){

        this.channelId = channelId;
        this.configKey = configKey;
        this.configCode = configCode;
        this.configValue1 = configValue1;
        this.configValue2 = configValue2;
        this.configValue3 = configValue3;

        this.setModifier(modifier);
        this.setCreater(modifier);
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
}
