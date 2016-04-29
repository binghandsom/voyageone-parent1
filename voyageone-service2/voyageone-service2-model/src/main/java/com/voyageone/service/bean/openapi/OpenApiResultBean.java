package com.voyageone.service.bean.openapi;

import com.voyageone.service.bean.openapi.image.ImageErrorEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2016/4/26.
 */
public abstract class OpenApiResultBean {
    long requestId;
    int errorCode;
    String errorMsg;

    public List<OpenApiSubError> getSubErrorList() {
        return subErrorList;
    }

    public void setSubErrorList(List<OpenApiSubError> subErrorList) {
        this.subErrorList = subErrorList;
    }

    List<OpenApiSubError> subErrorList;

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public OpenApiResultBean() {
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

    public void setEnumError(ErrorEnumInterface errorEnumInterface) {
        this.setErrorCode(errorEnumInterface.getCode());
        this.setErrorMsg(errorEnumInterface.getMsg());
    }
    public void setSubEnumError(ErrorEnumInterface errorEnumInterface) {
        if (this.getSubErrorList() == null) {
            this.setSubErrorList(new ArrayList<>());
        }
        this.getSubErrorList().add(new OpenApiSubError(errorEnumInterface));
    }
}
