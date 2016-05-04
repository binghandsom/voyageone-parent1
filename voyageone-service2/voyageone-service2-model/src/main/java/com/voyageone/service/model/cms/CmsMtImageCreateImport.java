/*
 * CmsMtImageCreateImport.java
 * Copyright(C) 20xx-2015 Voyageonone Group Inc.
 * All rights reserved.
 * -----------------------------------------------
 * 2016-05-04 Created
 */
package com.voyageone.service.model.cms;


import com.voyageone.base.dao.mysql.BaseModel;

import java.util.Date;

/**
 * JMBTProductImportTask||商品导入任务
 */
public class CmsMtImageCreateImport extends BaseModel {
    /**
     * 生成图片任务id
     */
    private Integer cmsMtImageCreateTaskId;

    /**
     * 是否导入
     */
    private Boolean isImport;

    /**
     * 导入的文件名
     */
    private String fileName;

    /**
     * 异常信息
     */
    private String errorMsg;

    /**
     * 功导入成功的行数
     */
    private Integer successRows;

    /**
     * 导入失败的行数
     */
    private Integer failuresRows;

    /**
     * 导入失败的行记录  存储的文件
     */
    private String failuresFileName;

    /**
     * 0:导入成功  1:导入异常    2：(生成失败记录文件)
     */
    private Integer errorCode;

    /**
     * 导入开始时间
     */
    private Date beginTime;

    /**
     * 导入结束时间
     */
    private Date endTime;

    public Integer getCmsMtImageCreateTaskId() {
        return cmsMtImageCreateTaskId;
    }

    public void setCmsMtImageCreateTaskId(Integer cmsMtImageCreateTaskId) {
        this.cmsMtImageCreateTaskId = cmsMtImageCreateTaskId;
    }

    public Boolean getIsImport() {
        return isImport;
    }

    public void setIsImport(Boolean isImport) {
        this.isImport = isImport;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName == null ? null : fileName.trim();
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg == null ? null : errorMsg.trim();
    }

    public Integer getSuccessRows() {
        return successRows;
    }

    public void setSuccessRows(Integer successRows) {
        this.successRows = successRows;
    }

    public Integer getFailuresRows() {
        return failuresRows;
    }

    public void setFailuresRows(Integer failuresRows) {
        this.failuresRows = failuresRows;
    }

    public String getFailuresFileName() {
        return failuresFileName;
    }

    public void setFailuresFileName(String failuresFileName) {
        this.failuresFileName = failuresFileName == null ? null : failuresFileName.trim();
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}