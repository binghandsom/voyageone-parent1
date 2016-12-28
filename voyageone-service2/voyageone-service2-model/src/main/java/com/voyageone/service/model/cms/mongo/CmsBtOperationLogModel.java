package com.voyageone.service.model.cms.mongo;

import com.voyageone.base.dao.mongodb.model.BaseMongoModel;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2016/12/27.
 */
public class CmsBtOperationLogModel extends BaseMongoModel {
    /**
     *
     */
    long operationId;
    /**
     *  队列名称 或 api(请求url)
     */
    String name;

    /**
     *参数
     */
    String messageBody;
    /**
     * 1：异常 2正常
     */
    int type;
    /**
     * 备注
     */
    String comment;
    /**
     * 调用堆栈
    */
    String msg;



    String stackTrace;
    /**
     *
     */
    List<Map<String,Object>> operateInfo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public List<Map<String, Object>> getOperateInfo() {
        return operateInfo;
    }

    public void setOperateInfo(List<Map<String, Object>> operateInfo) {
        this.operateInfo = operateInfo;
    }
    public long getOperationId() {
        return operationId;
    }

    public void setOperationId(long operationId) {
        this.operationId = operationId;
    }
}
