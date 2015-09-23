package com.voyageone.batch.ims.modelbean;

/**
 * Created by zhujiaye on 15/6/28.
 */
public class PropBean {

    // 属性id（自增长）
    private int propId;
    // 类目id
    private int categoryId;
    // 属性名称
    private String propName;
    // 属性类型
    private int propType;
    // 属性默认值
    private String propValueDefault;
    // 是否是一级属性
    private int isTopProp;
    // 是否是父属性（是否包含子属性：1：包含； 0：不包含）
    private int isParent;
    // 是否必填
    private int isRequired;
    // 父属性id
    private int parentPropId;
    // 内容（备用）
    private String content;

    public int getPropId() {
        return propId;
    }

    public void setPropId(int propId) {
        this.propId = propId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getPropName() {
        return propName;
    }

    public void setPropName(String propName) {
        this.propName = propName;
    }

    public int getPropType() {
        return propType;
    }

    public void setPropType(int propType) {
        this.propType = propType;
    }

    public String getPropValueDefault() {
        return propValueDefault;
    }

    public void setPropValueDefault(String propValueDefault) {
        this.propValueDefault = propValueDefault;
    }

    public int getIsTopProp() {
        return isTopProp;
    }

    public void setIsTopProp(int isTopProp) {
        this.isTopProp = isTopProp;
    }

    public int getIsParent() {
        return isParent;
    }

    public void setIsParent(int isParent) {
        this.isParent = isParent;
    }

    public int getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(int isRequired) {
        this.isRequired = isRequired;
    }

    public int getParentPropId() {
        return parentPropId;
    }

    public void setParentPropId(int parentPropId) {
        this.parentPropId = parentPropId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
