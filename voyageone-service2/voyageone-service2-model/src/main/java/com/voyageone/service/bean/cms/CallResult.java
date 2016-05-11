package com.voyageone.service.bean.cms;

/**
 * Created by dell on 2016/4/7.
 */
public class CallResult {
     boolean result;
    String msg;
    String errorCode;
    Object resultData;

    public Object getResultData() {
        return resultData;
    }

    public void setResultData(Object resultData) {
        this.resultData = resultData;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public  CallResult()
    {
        this.result=true;
    }
    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
