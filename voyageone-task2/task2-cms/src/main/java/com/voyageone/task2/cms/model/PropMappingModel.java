package com.voyageone.task2.cms.model;

/**
 * Created by zhujiaye on 15/6/28.
 */
public class PropMappingModel {

    public static final int TAG = 1;
    public static final int RANDOM = 2;
    public static final int EXPRESSION = 3;
    public static final int UNKNOWN = 4;

    // 第三方平台属性hash
    private String platformPropHash;
    // 主数据属性id
    private int propId;
    // 匹配类型
    private int mappingType;
    // 匹配值
    private String mappingValue;

    public String getPlatformPropHash() {
        return platformPropHash;
    }

    public void setPlatformPropHash(String platformPropHash) {
        this.platformPropHash = platformPropHash;
    }

    public int getPropId() {
        return propId;
    }

    public void setPropId(int propId) {
        this.propId = propId;
    }

    public int getMappingType() {
        return mappingType;
    }

    public void setMappingType(int mappingType) {
        this.mappingType = mappingType;
    }

    public String getMappingValue() {
        return mappingValue;
    }

    public void setMappingValue(String mappingValue) {
        this.mappingValue = mappingValue;
    }
}
