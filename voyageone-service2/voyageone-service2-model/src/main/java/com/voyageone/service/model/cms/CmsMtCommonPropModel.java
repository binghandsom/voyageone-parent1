package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;

/**
 * Created by james.li on 2015/12/7.
 */
public class CmsMtCommonPropModel extends BaseModel {
//    private Integer id;

    private String propId;

    private String propName;

    private String propType;

    private String propLevel;

    private String actionType;

    private String platformPropRefId;

    private String rules;

    private String defult;

    private String tips;

    private String mapping;

//    public Integer getId() {
//        return id;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
//    }

    public String getPropId() {
        return propId;
    }

    public void setPropId(String propId) {
        this.propId = propId;
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

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getPlatformPropRefId() {
        return platformPropRefId;
    }

    public void setPlatformPropRefId(String platformPropRefId) {
        this.platformPropRefId = platformPropRefId;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public String getDefult() {
        return defult;
    }

    public void setDefult(String defult) {
        this.defult = defult;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getMapping() {
        return mapping;
    }

    public void setMapping(String mapping) {
        this.mapping = mapping;
    }

}
