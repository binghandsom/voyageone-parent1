package com.voyageone.web2.sdk.api.response;

import com.voyageone.web2.sdk.api.VoApiResponse;

/**
 * SimpleResponse
 * Created on 2016-08-15
 *
 * @author chuanyu.liang
 * @version 2.0.0
 * @since 2.0.0
 */
public class SimpleResponse extends VoApiResponse {

    /**
     * 数据体信息
     */
    private Object data;

    public SimpleResponse(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
