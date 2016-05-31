package com.voyageone.service.bean.openapi.image;


import com.voyageone.service.bean.openapi.OpenApiResultBean;

public class AddListResultBean extends OpenApiResultBean {

    protected Integer taskId;

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }
}