package com.voyageone.batch.ims.modelbean;

/**
 * Created by zhujiaye on 15/6/25.
 */
public class PlatformPropOptionBean {
    // 第三方平台类目的属性的选项的hash
    private String platformPropOptionHash;
    // 第三方平台类目的属性的hash
    private String platformPropHash;
    // 第三方平台的选项名称
    private String platformPropOptionName;
    // 第三方平台的选项值
    private String platformPropOptionValue;

    public String getPlatformPropOptionHash() {
        return platformPropOptionHash;
    }

    public void setPlatformPropOptionHash(String platformPropOptionHash) {
        this.platformPropOptionHash = platformPropOptionHash;
    }

    public String getPlatformPropHash() {
        return platformPropHash;
    }

    public void setPlatformPropHash(String platformPropHash) {
        this.platformPropHash = platformPropHash;
    }

    public String getPlatformPropOptionName() {
        return platformPropOptionName;
    }

    public void setPlatformPropOptionName(String platformPropOptionName) {
        this.platformPropOptionName = platformPropOptionName;
    }

    public String getPlatformPropOptionValue() {
        return platformPropOptionValue;
    }

    public void setPlatformPropOptionValue(String platformPropOptionValue) {
        this.platformPropOptionValue = platformPropOptionValue;
    }
}
