package com.voyageone.components.onestop.bean;


import org.apache.oltu.oauth2.client.request.OAuthClientRequest;

/**
 * @description
 * @author: holysky.zhao
 * @date: 2016/11/21 16:39
 * @version:1.0.0 COPYRIGHT © 2001 - 2016 VOYAGE ONE GROUP INC. ALL RIGHTS RESERVED.
 */
public class OneStopConfig  {
    OAuthClientRequest oauth2;
    private String baseUrl; //base Url

    public OneStopConfig(String baseUrl, OAuthClientRequest oauth2) {
        this.oauth2 = oauth2;
        this.baseUrl = baseUrl;
    }
    public String getBaseUrl() {
        return baseUrl;
    }

    public OAuthClientRequest getOauth2Config() {
        return oauth2;
    }
}
