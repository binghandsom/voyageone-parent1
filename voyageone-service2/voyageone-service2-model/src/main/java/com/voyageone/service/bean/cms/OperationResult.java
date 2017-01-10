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
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
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
}
