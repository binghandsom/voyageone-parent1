package com.voyageone.service.bean.openapi;

/**
 * Created by dell on 2016/4/29.
 */
public class OpenApiSubError {
    int errorCode;
    String errorMsg;

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public OpenApiSubError(int errorCode, String errorMsg) {
        this.setErrorCode(errorCode);
        this.setErrorMsg(errorMsg);
    }
    public OpenApiSubError(ErrorEnumInterface errorEnumInterface) {
        this.setErrorCode(errorEnumInterface.getCode());
        this.setErrorMsg(errorEnumInterface.getMsg());
    }
}
