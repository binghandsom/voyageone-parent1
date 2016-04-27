package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;

public class CmsBtTaskTejiabaoModel extends BaseModel {

    /**

     */
    private int promotionId;
    /**
     * 0:特价宝 1:价格披露
     */
    private int taskType;
    /**

     */
    private String key;
    /**

     */
    private String numIid;
    /**
     * 0:初始化 1:等待刷新 2:运行成功 9:运行失败
     */
    private int synFlg;
    /**

     */
    private String errMsg;


    /**

     */
    public int getPromotionId() {

        return this.promotionId;
    }

    public void setPromotionId(int promotionId) {
        this.promotionId = promotionId;
    }


    /**
     * 0:特价宝 1:价格披露
     */
    public int getTaskType() {

        return this.taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }


    /**

     */
    public String getKey() {

        return this.key;
    }

    public void setKey(String key) {
        if (key != null) {
            this.key = key;
        } else {
            this.key = "";
        }

    }


    /**

     */
    public String getNumIid() {

        return this.numIid;
    }

    public void setNumIid(String numIid) {
        if (numIid != null) {
            this.numIid = numIid;
        } else {
            this.numIid = "";
        }

    }


    /**
     * 0:初始化 1:等待刷新 2:运行成功 9:运行失败
     */
    public int getSynFlg() {

        return this.synFlg;
    }

    public void setSynFlg(int synFlg) {
        this.synFlg = synFlg;
    }


    /**

     */
    public String getErrMsg() {

        return this.errMsg;
    }

    public void setErrMsg(String errMsg) {
        if (errMsg != null) {
            this.errMsg = errMsg;
        } else {
            this.errMsg = "";
        }

    }

}