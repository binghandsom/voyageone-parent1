package com.voyageone.service.bean.cms;


import java.util.List;

/**
 * Created by lewis on 15-12-7.
 */
public class CommonPropActionDefBean {

    private String propId;
    private String propName;
    private String parentPropId;
    private String propType;
    private String propLevel;
    private int actionType;
    private int isCom;
    private String platformPropRefId;
    private String actionRules;
    private String tips;
    private String valueType;
    private String defaultValue;
    private CommonPropActionDefRuleBean ruleMode;

    private List<CommonPropActionDefBean> defModels;

    public String getParentPropId() {
        return parentPropId;
    }

    public void setParentPropId(String parentPropId) {
        this.parentPropId = parentPropId;
    }

    public List<CommonPropActionDefBean> getDefModels() {
        return defModels;
    }

    public void setDefModels(List<CommonPropActionDefBean> defModels) {
        this.defModels = defModels;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getPropId() {
        return propId;
    }

    public void setPropId(String propId) {
        this.propId = propId;
    }

    public CommonPropActionDefRuleBean getRuleMode() {
        return ruleMode;
    }

    public String getPropName() {
        return propName;
    }

    public void setPropName(String propName) {
        this.propName = propName;
    }

    public String getPropType() {
        return propType;
    }

    public void setPropType(String propType) {
        this.propType = propType;
    }

    public String getPropLevel() {
        return propLevel;
    }

    public void setPropLevel(String propLevel) {
        this.propLevel = propLevel;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public String getPlatformPropRefId() {
        return platformPropRefId;
    }

    public void setPlatformPropRefId(String platformPropRefId) {
        this.platformPropRefId = platformPropRefId;
    }

    public String getActionRules() {
        return actionRules;
    }

    public void setActionRules(String actionRules) {
        this.actionRules = actionRules;
        this.ruleMode = new CommonPropActionDefRuleBean(this.actionRules);
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public Object getRuleValue(String ruleKey) {
        return this.ruleMode.getValue(ruleKey);
    }

    public int getIsCom() {
        return isCom;
    }

    public void setIsCom(int isCom) {
        this.isCom = isCom;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }
}
