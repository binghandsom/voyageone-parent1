package com.voyageone.task2.cms.bean;

public class PlatformPropBean {
    // 第三方平台的属性id
    private String platformPropId;
    private String platformPropName;

    public PlatformPropBean(String platformPropId, String platformPropName) {
        this.platformPropId = platformPropId;
        this.platformPropName = platformPropName;
    }

    public String getPlatformPropId() {
        return platformPropId;
    }

    public void setPlatformPropId(String platformPropId) {
        this.platformPropId = platformPropId;
    }

    public String getPlatformPropName() {
        return platformPropName;
    }

    public void setPlatformPropName(String platformPropName) {
        this.platformPropName = platformPropName;
    }
}
