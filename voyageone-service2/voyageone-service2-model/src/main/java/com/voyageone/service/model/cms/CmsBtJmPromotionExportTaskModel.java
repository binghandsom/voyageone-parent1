/*
 * CmsBtJmPromotionExportTaskModel.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;
import java.util.Date;

/**
 * JMBTProductExportTask||商品导入任务
 */
public class CmsBtJmPromotionExportTaskModel extends BaseModel {
    /**
     * 推广活动id
     */
    protected Integer cmsBtJmPromotionId;

    /**
     * 导出的文件名
     */
    protected String fileName;

    /**
     * 文件路径
     */
    protected String filePath;

    /**
     * 异常信息
     */
    protected String errorMsg;

    /**
     * 成功导出的行数
     */
    protected Integer successRows;

    /**
     * 1:导出失败
     */
    protected Integer errorCode;

    /**
     * 导入开始时间
     */
    protected Date beginTime;

    /**
     * 导入结束时间
     */
    protected Date endTime;

    /**
     * 是否导出
     */
    protected Boolean isExport;

    /**
     * 导出模板类型:0:导入模板1：价格修正模板 2:专场模板
     */
    protected Integer templateType;

    protected String parameter;

    public Integer getCmsBtJmPromotionId() {
        return cmsBtJmPromotionId;
    }

    public void setCmsBtJmPromotionId(Integer cmsBtJmPromotionId) {
        this.cmsBtJmPromotionId = cmsBtJmPromotionId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName == null ? null : fileName.trim();
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath == null ? null : filePath.trim();
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

    public Boolean getIsExport() {
        return isExport;
    }

    public void setIsExport(Boolean isExport) {
        this.isExport = isExport;
    }

    public Integer getTemplateType() {
        return templateType;
    }

    public void setTemplateType(Integer templateType) {
        this.templateType = templateType;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter == null ? null : parameter.trim();
    }
}