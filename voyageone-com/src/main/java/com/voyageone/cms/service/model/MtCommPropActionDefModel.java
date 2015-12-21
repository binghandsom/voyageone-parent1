package com.voyageone.cms.service.model;


import java.util.List;

/**
 * Created by lewis on 15-12-7.
 */
public class MtCommPropActionDefModel {

    private String propId;
    private String propName;
    private String parentPropId;
    private String propType;
    private String propLevel;
    private int actionType;
    private String platformPropRefId;
    private String actionRules;
    private String tips;
    private String defaultValue;
    private MtCommPropActionDefRuleModel ruleMode;

    private List<MtCommPropActionDefModel> defModels;

    public String getParentPropId() {
        return parentPropId;
    }

    public void setParentPropId(String parentPropId) {
        this.parentPropId = parentPropId;
    }

    public List<MtCommPropActionDefModel> getDefModels() {
        return defModels;
    }

    public void setDefModels(List<MtCommPropActionDefModel> defModels) {
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

    public MtCommPropActionDefRuleModel getRuleMode() {
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
        this.ruleMode = new MtCommPropActionDefRuleModel(this.actionRules);
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

}
