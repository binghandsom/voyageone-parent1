package com.voyageone.service.model.cms.mongo;

import com.voyageone.base.dao.mongodb.model.BaseMongoModel;

import java.util.List;

/**
 * Created by dell on 2016/12/27.
 */
public class CmsBtOperationLogModel extends BaseMongoModel {

    /**
     * 店铺Id
     */
    String channelId;
    /**
     * job名称 或 api(请求url)
     */
    String name;
    /**
     * 队列名称 或 api(请求url) ->中文显示
     */
    String title;
    /**
     * 参数
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
     * 错误数据的消息, 一个商品/sku一条错误记录
     */
    List<CmsBtOperationLogModel_Msg> msg;
    /**
     * 错误异常堆栈
     */
    String stackTrace;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<CmsBtOperationLogModel_Msg> getMsg() {
        return msg;
    }

    public void setMsg(List<CmsBtOperationLogModel_Msg> msg) {
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

}

