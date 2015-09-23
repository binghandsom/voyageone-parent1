package com.voyageone.batch.ims.modelbean;

public class PropRuleBean {

    // 属性的规则的id
    private int propRuleId;
    // 属性的id
    private int propId;
    // 属性的规则的名称
    private String propRuleName;
    // 属性的规则的值
    private String propRuleValue;
    // 属性的规则的单位
    private String propRuleUnit;
    // 属性的规则的url
    private String propRuleUrl;
    // 属性的规则的是否包含
    private String propRuleExProperty;
    // 属性的规则之关联关系
    private String propRuleRelationship;
    // 属性的规则之关联关系的连接方式（And / Or）
    private String propRuleRelationshipOperator;

    public int getPropRuleId() {
        return propRuleId;
    }

    public void setPropRuleId(int propRuleId) {
        this.propRuleId = propRuleId;
    }

    public int getPropId() {
        return propId;
    }

    public void setPropId(int propId) {
        this.propId = propId;
    }

    public String getPropRuleName() {
        return propRuleName;
    }

    public void setPropRuleName(String propRuleName) {
        this.propRuleName = propRuleName;
    }

    public String getPropRuleValue() {
        return propRuleValue;
    }

    public void setPropRuleValue(String propRuleValue) {
        this.propRuleValue = propRuleValue;
    }

    public String getPropRuleUnit() {
        return propRuleUnit;
    }

    public void setPropRuleUnit(String propRuleUnit) {
        this.propRuleUnit = propRuleUnit;
    }

    public String getPropRuleUrl() {
        return propRuleUrl;
    }

    public void setPropRuleUrl(String propRuleUrl) {
        this.propRuleUrl = propRuleUrl;
    }

    public String getPropRuleExProperty() {
        return propRuleExProperty;
    }

    public void setPropRuleExProperty(String propRuleExProperty) {
        this.propRuleExProperty = propRuleExProperty;
    }

    public String getPropRuleRelationship() {
        return propRuleRelationship;
    }

    public void setPropRuleRelationship(String propRuleRelationship) {
        this.propRuleRelationship = propRuleRelationship;
    }

    public String getPropRuleRelationshipOperator() {
        return propRuleRelationshipOperator;
    }

    public void setPropRuleRelationshipOperator(String propRuleRelationshipOperator) {
        this.propRuleRelationshipOperator = propRuleRelationshipOperator;
    }
}
