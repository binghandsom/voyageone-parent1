package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;

import java.util.Date;

public class CmsMtImageCreateTaskModel extends BaseModel {

    /**

     */
    private String name = "";
    /**
     * 1:处理完成
     */
    private int status;

Date beginTime;
    Date endTime;

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    /**

     */
    public String getName() {

        return this.name;
    }

    public void setName(String name) {
        if (name != null) {
            this.name = name;
        } else {
            this.name = "";
        }

    }


    /**
     * 1:处理完成
     */
    public int getStatus() {

        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


}