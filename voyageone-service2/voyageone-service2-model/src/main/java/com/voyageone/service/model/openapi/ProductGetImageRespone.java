package com.voyageone.service.model.openapi;

/**
 * Created by dell on 2016/4/25.
 */
public class ProductGetImageRespone {
    long requestId;
    public long getRequestId() {
        return requestId;
    }
    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }
    int errorCode;
    String errorMsg;
    ProductGetImageResultData resultData;
    public ProductGetImageRespone() {
        this.setResultData(new ProductGetImageResultData());
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

    public ProductGetImageResultData getResultData() {
        return resultData;
    }
    public void setResultData(ProductGetImageResultData resultData) {
        this.resultData = resultData;
    }
}
