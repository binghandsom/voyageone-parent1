package com.voyageone.common.masterdate.schema.exception;


import com.voyageone.common.masterdate.schema.enums.TopSchemaErrorCodeEnum;

public class TopSchemaException extends RuntimeException {
    private static final long serialVersionUID = -6952144909179839874L;
    private String errorCode;
    private String errorMsg;
    private String fieldId;

    public TopSchemaException(String massage) {
        super(massage);
        this.errorCode = TopSchemaErrorCodeEnum.ERROR_CODE_40000.getErrorCode();
        this.errorMsg = massage;
    }

    public TopSchemaException(String massage, Throwable cause) {
        super(massage, cause);
        this.errorCode = TopSchemaErrorCodeEnum.ERROR_CODE_40000.getErrorCode();
        this.errorMsg = massage;
    }

    public TopSchemaException(String errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public TopSchemaException(TopSchemaErrorCodeEnum codeEnum, String fieldId) {
        super(codeEnum.getErrorMsg());
        String msg = codeEnum.getErrorMsg();
        if(fieldId != null && fieldId.length() != 0) {
            msg = msg + "At the filed which id is " + fieldId;
        }

        this.errorCode = codeEnum.getErrorCode();
        this.errorMsg = msg;
        this.fieldId = fieldId;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return this.errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getFieldId() {
        return this.fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }
}

