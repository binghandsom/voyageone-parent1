package com.voyageone.service.bean.cms;

import com.voyageone.common.idsnowflake.FactoryIdWorker;

/**
 * Created by dell on 2017/1/10.
 */
public class OperationResult {
    public OperationResult() {
        this.id = FactoryIdWorker.nextId();
    }

    long id;

    public long getId() {
        return id;
    }

    String code;
    String msg;
    int errorCode;
    boolean result = true;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return sbError.toString();
    }

    public void setMsg(String msg) {
        sbError.append(msg);
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    StringBuilder sbError = new StringBuilder();

    public void addError(String format, Object... args) {
        this.result = false;
        sbError.append(String.format(format, args));
    }
    public void addErrorln(String format, Object... args) {
        this.addError(format,args);
        sbError.append("\\r\\n");
    }
}
