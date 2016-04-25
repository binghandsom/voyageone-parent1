package com.voyageone.service.model.openapi;

/**
 * Created by dell on 2016/4/25.
 */
public class ProductGetImageResultData {
    String oSSFilePath;
    String usCDNFilePath;

    public String getOSSFilePath() {
        return oSSFilePath;
    }

    public void setOSSFilePath(String oSSFilePath) {
        this.oSSFilePath = oSSFilePath;
    }

    public String getUsCDNFilePath() {
        return usCDNFilePath;
    }

    public void setUsCDNFilePath(String usCDNFilePath) {
        this.usCDNFilePath = usCDNFilePath;
    }
}
