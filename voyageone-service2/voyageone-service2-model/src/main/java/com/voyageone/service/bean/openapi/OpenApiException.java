package com.voyageone.service.bean.openapi;

/**
 * Created by dell on 2016/4/25.
 */
public class OpenApiException extends Exception {
    int errorCode;//错误码
    String msg;//消息

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    ErrorEnumInterface enumInterface;

    public ErrorEnumInterface getEnumInterface() {
        return enumInterface;
    }

    public void setEnumInterface(ErrorEnumInterface enumInterface) {
        this.enumInterface = enumInterface;
    }

    public OpenApiException(ErrorEnumInterface enumInterface) {
        this(enumInterface, "");
    }

    public OpenApiException(ErrorEnumInterface enumInterface, String msg) {
        this(enumInterface, msg, null);
    }

    public OpenApiException(ErrorEnumInterface enumInterface, Throwable ex) {
        this(enumInterface, "", ex);
    }
    public OpenApiException(ErrorEnumInterface enumInterface, String msg, Throwable ex) {
        super(msg, ex);
        this.setEnumInterface(enumInterface);
        this.setErrorCode(enumInterface.getCode());
        this.setMsg(msg + "," + enumInterface.getMsg());
    }
}
