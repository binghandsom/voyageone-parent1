package com.voyageone.components.intltarget.error;

/**
 * @author aooer 2016/4/12.
 * @version 2.0.0
 * @since 2.0.0
 */
public class TargetException {

    /* 此处两个属性之所以首字母大写，是为了和Target返回值key匹配，ignore 驼峰写发 */
    private String Message;

    private String Detail;

    /*******   getter setter   ******/

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getDetail() {
        return Detail;
    }

    public void setDetail(String detail) {
        Detail = detail;
    }
}
