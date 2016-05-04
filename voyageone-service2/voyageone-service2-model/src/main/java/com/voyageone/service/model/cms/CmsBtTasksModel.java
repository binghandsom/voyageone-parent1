package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;

public class CmsBtTasksModel extends BaseModel {

    /**

     */
    private String taskName;
    /**
     * 0：特价宝  1：价格披露 2：库存隔离
     */
    private int taskType;
    /**

     */
    private int promotionId;
    /**

     */
    private String channelId;
    /**

     */
    private String config;
    /**

     */
    private int status;
    /**

     */
    private String activityStart;
    /**

     */
    private String activityEnd;


    /**

     */
    public String getTaskName() {

        return this.taskName;
    }

    public void setTaskName(String taskName) {
        if (taskName != null) {
            this.taskName = taskName;
        } else {
            this.taskName = "";
        }

    }


    /**
     * 0：特价宝  1：价格披露 2：库存隔离
     */
    public int getTaskType() {

        return this.taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }


    /**

     */
    public int getPromotionId() {

        return this.promotionId;
    }

    public void setPromotionId(int promotionId) {
        this.promotionId = promotionId;
    }


    /**

     */
    public String getChannelId() {

        return this.channelId;
    }

    public void setChannelId(String channelId) {
        if (channelId != null) {
            this.channelId = channelId;
        } else {
            this.channelId = "";
        }

    }


    /**

     */
    public String getConfig() {

        return this.config;
    }

    public void setConfig(String config) {
        if (config != null) {
            this.config = config;
        } else {
            this.config = "";
        }

    }


    /**

     */
    public int getStatus() {

        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    /**

     */
    public String getActivityStart() {

        return this.activityStart;
    }

    public void setActivityStart(String activityStart) {
        if (activityStart != null) {
            this.activityStart = activityStart;
        } else {
            this.activityStart = "";
        }

    }


    /**

     */
    public String getActivityEnd() {

        return this.activityEnd;
    }

    public void setActivityEnd(String activityEnd) {
        if (activityEnd != null) {
            this.activityEnd = activityEnd;
        } else {
            this.activityEnd = "";
        }

    }

}