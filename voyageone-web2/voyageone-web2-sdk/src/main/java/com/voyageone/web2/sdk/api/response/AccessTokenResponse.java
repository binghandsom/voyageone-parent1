package com.voyageone.web2.sdk.api.response;

import com.voyageone.web2.sdk.api.VoApiResponse;

/**
 * AccessTokenResponse
 * Created on 2016-08-15
 *
 * @author chuanyu.liang
 * @version 2.0.0
 * @since 2.0.0
 */
public class AccessTokenResponse extends VoApiResponse {
    /**
     * access_token
     */
    private String access_token;

    /**
     * expires_in
     */
    private int expires_in;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }
}
