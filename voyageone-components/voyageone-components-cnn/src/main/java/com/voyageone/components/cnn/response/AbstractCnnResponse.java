package com.voyageone.components.cnn.response;

import com.voyageone.common.util.JacksonUtil;

/**
 * Created by morse on 2017/7/31.
 */
public abstract class AbstractCnnResponse {

    private String code;

    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return JacksonUtil.bean2Json(this);
    }
}
