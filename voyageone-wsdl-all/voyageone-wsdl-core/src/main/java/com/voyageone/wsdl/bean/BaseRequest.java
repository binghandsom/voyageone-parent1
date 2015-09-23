package com.voyageone.wsdl.bean;

/**
 * Created by sn3 on 2015-08-10.
 */
public class BaseRequest {

    /**
     * 验证key
     */
    private String apiKey;
    /**
     * 签名 md5(apikey+报文内容)
     */
    private String apiSign;

    /**
     * 报文内容
     */
    private String apiContents;

    /**
     * xml or json
     */
    private String format;

    /**
     * 时间轴
     */
    private String timestamp;


    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiSign() {
        return apiSign;
    }

    public void setApiSign(String apiSign) {
        this.apiSign = apiSign;
    }

    public String getApiContents() {
        return apiContents;
    }

    public void setApiContents(String apiContents) {
        this.apiContents = apiContents;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
