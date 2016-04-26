package com.voyageone.service.model.openapi;

/**
 * Created by dell on 2016/4/26.
 */
public abstract class OpenApiRespone {
    long requestId;
    int errorCode;
    String errorMsg;

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public OpenApiRespone() {
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
