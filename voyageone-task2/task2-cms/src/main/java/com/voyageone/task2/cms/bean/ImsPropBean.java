package com.voyageone.task2.cms.bean;

public class ImsPropBean {
    int propId;
    int propType;
    int parentPropId;
    String propName;

    public int getPropId() {
        return propId;
    }

    public void setPropId(int propId) {
        this.propId = propId;
    }

    public int getPropType() {
        return propType;
    }

    public void setPropType(int propType) {
        this.propType = propType;
    }

    public int getParentPropId() {
        return parentPropId;
    }

    public void setParentPropId(int parentPropId) {
        this.parentPropId = parentPropId;
    }

    public String getPropName() {
        return propName;
    }

    public void setPropName(String propName) {
        this.propName = propName;
    }
}
