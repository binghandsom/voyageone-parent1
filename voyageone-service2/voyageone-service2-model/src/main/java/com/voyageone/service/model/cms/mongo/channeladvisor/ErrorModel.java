package com.voyageone.service.model.cms.mongo.channeladvisor;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.voyageone.service.enums.channeladvisor.ErrorIDEnum;

/**
 * @author aooer 2016/9/6.
 * @version 2.0.0
 * @since 2.0.0
 */
public class ErrorModel {

    @JsonProperty("ID")
    private ErrorIDEnum id;

    @JsonProperty("ErrorCode")
    private String errorCode;

    @JsonProperty("Message")
    private String message;

    public ErrorModel(ErrorIDEnum errorIDEnum) {
        this.id = errorIDEnum;
        this.errorCode = String.valueOf(errorIDEnum.getCode());
        this.message = errorIDEnum.getDefaultMessage();
    }

    public ErrorModel(ErrorIDEnum errorIDEnum, String message) {
        this.id = errorIDEnum;
        this.errorCode = String.valueOf(errorIDEnum.getCode());
        this.message = message;
    }

    public ErrorModel(ErrorIDEnum errorIDEnum, String errorCode, String message) {
        this.id = errorIDEnum;
        this.errorCode = errorCode;
        this.message = message;
    }

    public ErrorIDEnum getId() {
        return id;
    }

    public void setId(ErrorIDEnum id) {
        this.id = id;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
