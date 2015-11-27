package com.voyageone.batch.cms.model;

/**
 * Created by zhujiaye on 15/6/28.
 */
public class PropOptionModel {

    // 属性可选项id（自增长）
    private int propOptionId;
    // 属性id
    private int propId;
    // 属性可选项显示名称
    private String propOptionName;
    // 属性可选项值
    private String propOptionValue;

    public int getPropOptionId() {
        return propOptionId;
    }

    public void setPropOptionId(int propOptionId) {
        this.propOptionId = propOptionId;
    }

    public int getPropId() {
        return propId;
    }

    public void setPropId(int propId) {
        this.propId = propId;
    }

    public String getPropOptionName() {
        return propOptionName;
    }

    public void setPropOptionName(String propOptionName) {
        this.propOptionName = propOptionName;
    }

    public String getPropOptionValue() {
        return propOptionValue;
    }

    public void setPropOptionValue(String propOptionValue) {
        this.propOptionValue = propOptionValue;
    }
}
