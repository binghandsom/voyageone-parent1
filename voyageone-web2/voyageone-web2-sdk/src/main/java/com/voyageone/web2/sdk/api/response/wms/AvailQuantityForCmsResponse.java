package com.voyageone.web2.sdk.api.response.wms;

import com.voyageone.web2.sdk.api.VoApiResponse;

import java.util.List;

/**
 * Created by Charis on 2017/5/25.
 */
public class AvailQuantityForCmsResponse extends VoApiResponse {

    private String code;

    private String message;

    private Object data;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
