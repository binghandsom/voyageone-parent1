package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;

public class CmsMtCommonPropModel extends BaseModel {

    private String propId;
    /**

     */
    private String propParentId;
    /**

     */
    private String propName;
    /**

     */
    private String propType;
    /**
     * 0:add 1:del 2:自动改名顺序+1
     */
    private int actionType;
    /**

     */
    private String platformPropRefId;
    /**
     * 1:必须 0:非必须
     */
    private String rules;
    /**

     */
    private String defult;
    /**

     */
    private int isComm;
    /**

     */
    private int isCode;
    /**
     * 提示信息
     */
    private String tips;
    /**

     */
    private String valueType;


    /**

     */
    public String getPropId() {

        return this.propId;
    }

    public void setPropId(String propId) {
        if (propId != null) {
            this.propId = propId;
        } else {
            this.propId = "";
        }

    }


    /**

     */
    public String getPropParentId() {

        return this.propParentId;
    }

    public void setPropParentId(String propParentId) {
        if (propParentId != null) {
            this.propParentId = propParentId;
        } else {
            this.propParentId = "";
        }

    }


    /**

     */
    public String getPropName() {

        return this.propName;
    }

    public void setPropName(String propName) {
        if (propName != null) {
            this.propName = propName;
        } else {
            this.propName = "";
        }

    }


    /**

     */
    public String getPropType() {

        return this.propType;
    }

    public void setPropType(String propType) {
        if (propType != null) {
            this.propType = propType;
        } else {
            this.propType = "";
        }

    }


    /**
     * 0:add 1:del 2:自动改名顺序+1
     */
    public int getActionType() {

        return this.actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }


    /**

     */
    public String getPlatformPropRefId() {

        return this.platformPropRefId;
    }

    public void setPlatformPropRefId(String platformPropRefId) {
        if (platformPropRefId != null) {
            this.platformPropRefId = platformPropRefId;
        } else {
            this.platformPropRefId = "";
        }

    }


    /**
     * 1:必须 0:非必须
     */
    public String getRules() {

        return this.rules;
    }

    public void setRules(String rules) {
        if (rules != null) {
            this.rules = rules;
        } else {
            this.rules = "";
        }

    }


    /**

     */
    public String getDefult() {

        return this.defult;
    }

    public void setDefult(String defult) {
        if (defult != null) {
            this.defult = defult;
        } else {
            this.defult = "";
        }

    }


    /**

     */
    public int getIsComm() {

        return this.isComm;
    }

    public void setIsComm(int isComm) {
        this.isComm = isComm;
    }


    /**

     */
    public int getIsCode() {

        return this.isCode;
    }

    public void setIsCode(int isCode) {
        this.isCode = isCode;
    }


    /**
     * 提示信息
     */
    public String getTips() {

        return this.tips;
    }

    public void setTips(String tips) {
        if (tips != null) {
            this.tips = tips;
        } else {
            this.tips = "";
        }

    }


    /**

     */
    public String getValueType() {

        return this.valueType;
    }

    public void setValueType(String valueType) {
        if (valueType != null) {
            this.valueType = valueType;
        } else {
            this.valueType = "";
        }

    }

}