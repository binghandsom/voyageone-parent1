package com.voyageone.common.configs.beans;

/**
 * 映射 ct_exception_log 表
 * Created on 11/26/15.
 *
 * @author Jonas
 * @version 2.0.0
 */
public class ExceptionLogBean {
    /**
     * 日志时间
     */
    private String dateTime;

    /**
     * 当前用户名
     */
    private String userName;

    /**
     * 发生异常时的请求URL
     */
    private String url;

    /**
     * 异常描述
     */
    private String description;

    /**
     * 异常类型
     */
    private String exceptionType;

    /**
     * 创建时间
     */
    private String created;

    /**
     * 堆栈信息
     */
    private String stackInfo;

    /**
     * @return the dateTime
     */
    public String getDateTime() {
        return dateTime;
    }

    /**
     * @param dateTime the dateTime to set
     */
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the exceptionType
     */
    public String getExceptionType() {
        return exceptionType;
    }

    /**
     * @param exceptionType the exceptionType to set
     */
    public void setExceptionType(String exceptionType) {
        this.exceptionType = exceptionType;
    }

    /**
     * @return the created
     */
    public String getCreated() {
        return created;
    }

    /**
     * @param created the created to set
     */
    public void setCreated(String created) {
        this.created = created;
    }

    /**
     * @return the stackInfo
     */
    public String getStackInfo() {
        return stackInfo;
    }

    /**
     * @param stackInfo the stackInfo to set
     */
    public void setStackInfo(String stackInfo) {
        this.stackInfo = stackInfo;
    }

}
