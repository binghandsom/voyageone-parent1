/*
 * TmTaskControlModel.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.model.admin;

/**
 * 
 */
public class TmTaskControlModel extends AdminBaseModel {
    protected String taskId;

    protected String cfgName;

    protected String cfgVal1;

    protected String cfgVal2;

    protected String endTime;

    protected String comment;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId == null ? null : taskId.trim();
    }

    public String getCfgName() {
        return cfgName;
    }

    public void setCfgName(String cfgName) {
        this.cfgName = cfgName == null ? null : cfgName.trim();
    }

    public String getCfgVal1() {
        return cfgVal1;
    }

    public void setCfgVal1(String cfgVal1) {
        this.cfgVal1 = cfgVal1 == null ? null : cfgVal1.trim();
    }

    public String getCfgVal2() {
        return cfgVal2;
    }

    public void setCfgVal2(String cfgVal2) {
        this.cfgVal2 = cfgVal2 == null ? null : cfgVal2.trim();
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime == null ? null : endTime.trim();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
    }
}