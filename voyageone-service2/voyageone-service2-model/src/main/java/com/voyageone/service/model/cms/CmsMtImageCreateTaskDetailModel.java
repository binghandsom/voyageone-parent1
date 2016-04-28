package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;

import java.util.Date;

public class CmsMtImageCreateTaskDetailModel extends BaseModel {

    /**

     */
    private int cmsMtImageCreateTaskId;
    /**

     */
    private int cmsMtImageCreateFileId;
    /**
     * 执行开始时间
     */
    private Date beginTime;
    /**
     * 执行结束时间
     */
    private Date endTime;
    /**
     * 1:完成
     */
    private int status;


    /**

     */
    public int getCmsMtImageCreateTaskId() {

        return this.cmsMtImageCreateTaskId;
    }

    public void setCmsMtImageCreateTaskId(int cmsMtImageCreateTaskId) {
        this.cmsMtImageCreateTaskId = cmsMtImageCreateTaskId;
    }


    /**

     */
    public int getCmsMtImageCreateFileId() {

        return this.cmsMtImageCreateFileId;
    }

    public void setCmsMtImageCreateFileId(int cmsMtImageCreateFileId) {
        this.cmsMtImageCreateFileId = cmsMtImageCreateFileId;
    }


    /**
     * 执行开始时间
     */
    public Date getBeginTime() {

        return this.beginTime;
    }

    public void setBeginTime(Date beginTime) {
        if (beginTime != null) {
            this.beginTime = beginTime;
        } else {
            this.beginTime = new Date();
        }

    }


    /**
     * 执行结束时间
     */
    public Date getEndTime() {

        return this.endTime;
    }

    public void setEndTime(Date endTime) {
        if (endTime != null) {
            this.endTime = endTime;
        } else {
            this.endTime = new Date();
        }

    }


    /**
     * 1:完成
     */
    public int getStatus() {

        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


}