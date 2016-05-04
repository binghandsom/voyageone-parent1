package com.voyageone.web2.base.ajax;

import com.voyageone.common.message.enums.DisplayType;
import com.voyageone.service.bean.com.MessageBean;

/**
 * 用于返回 Ajax 请求的结果数据
 *
 * @author jonas
 * @version 2.0.0 2015-12-05 15:07:45
 */
public class AjaxResponse {

    /**
     * 消息code
     */
    private String code;

    /**
     * 消息的提示方式
     */
    private DisplayType displayType;

    /**
     * 消息
     */
    private String message;

    /**
     * 数据体信息
     */
    private Object data;

    /**
     * 跳转地址
     */
    private String redirectTo;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public DisplayType getDisplayType() {
        return displayType;
    }

    public void setDisplayType(DisplayType displayType) {
        this.displayType = displayType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 扩展的 set 方法, 直接同步 set 其他属性
     *
     * @param message 信息模型
     * @param args    格式化参数
     */
    public void setMessage(MessageBean message, Object[] args) {
        setMessage(String.format(message.getMessage(), args));
        setCode(message.getCode());
        setDisplayType(message.getDisplayType());
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getRedirectTo() {
        return redirectTo;
    }

    public void setRedirectTo(String redirectTo) {
        this.redirectTo = redirectTo;
    }
}
