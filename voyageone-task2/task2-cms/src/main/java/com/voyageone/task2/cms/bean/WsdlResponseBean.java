package com.voyageone.task2.cms.bean;

/**
 * 用于返回 webservice请求的结果数据
 * Created by zero on 7/28/2015.
 *
 * @author zero
 */
public class WsdlResponseBean<T> {

    /**
     * 结果 OK/NG
     */
    private String result = "NG";

    /**
     * 消息code
     */
    private String messageCode = "";

    /**
     * 消息
     */
    private String message = "";

    /**
     * 类型
     */
    private int messageType;

    /**
     * 数据体信息
     */
    private T resultInfo;

    /**
     * @return the result
     */
    public String getResult() {
        return result;
    }

    /**
     * @param result the result to set
     */
    public void setResult(String result) {
        this.result = result;
    }

    /**
     * @return the messageCode
     */
    public String getMessageCode() {
        return messageCode;
    }

    /**
     * @param messageCode the messageCode to set
     */
    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the messageType
     */
    public int getMessageType() {
        return messageType;
    }

    /**
     * @param messageType the messageType to set
     */
    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    /**
     * @return the resultInfo
     */
    public T getResultInfo() {
        return resultInfo;
    }

    /**
     * @param resultInfo the resultInfo to set
     */
    public void setResultInfo(T resultInfo) {
        this.resultInfo = resultInfo;
    }
}
