package com.voyageone.batch.wms.modelbean;

import java.util.List;

/**
 * Created by Leo on 15-7-29.
 */
public class CmsWsdlResponseBean{

    private String result;
    private String messageCode;
    private String message;
    private String messageType;
    private List<ProductBean> resultInfo;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public List<ProductBean> getResultInfo() {
        return resultInfo;
    }

    public void setResultInfo(List<ProductBean> resultInfo) {
        this.resultInfo = resultInfo;
    }
}
