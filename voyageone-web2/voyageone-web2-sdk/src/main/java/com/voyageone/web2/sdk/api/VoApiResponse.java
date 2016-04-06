package com.voyageone.web2.sdk.api;


/**
 * Respose Entity
 * Created on 2015-12-14
 *
 * @author chuanyu.liang
 * @version 2.0.0
 * @since 2.0.0
 */
public class VoApiResponse {

    /**
     * 消息code
     */
    protected String code = "0";

    /**
     * 消息
     */
    protected String message;

    public VoApiResponse() {

    }


    public VoApiResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
