package com.voyageone.web2.base.message;

/**
 * 预定义的提示信息
 * @author Jonas
 * @version 2.0.0, 12/4/15
 */
public class MessageModel {

    private String code;

    private DisplayType displayType;

    private String message;

    private String lang;

    /**
     * 不对外开放的信息模型构造函数
     */
    protected MessageModel() {
    }

    public String getCode() {
        return code;
    }

    protected void setCode(String code) {
        this.code = code;
    }

    public DisplayType getDisplayType() {
        return displayType;
    }

    protected void setDisplayType(DisplayType displayType) {
        this.displayType = displayType;
    }

    public String getMessage() {
        return message;
    }

    protected void setMessage(String message) {
        this.message = message;
    }

    public String getLang() {
        return lang;
    }

    protected void setLang(String lang) {
        this.lang = lang;
    }
}
