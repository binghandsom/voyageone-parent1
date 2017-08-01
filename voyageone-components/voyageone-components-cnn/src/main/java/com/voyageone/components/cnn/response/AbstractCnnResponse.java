package com.voyageone.components.cnn.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.components.cnn.enums.CnnConstants;

/**
 * Created by morse on 2017/7/31.
 */
public abstract class AbstractCnnResponse {

    private int code;

    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @JsonIgnore
    public boolean isSuccess() {
        if (CnnConstants.C_CNN_RETURN_SUCCESS_0 == code) {
            return true;
        } else {
            return false;
        }
    }

    @JsonIgnore
    public String getErrMsgStr() {
        return "{" +
                "code=" + code +
                ", msg=" + msg +
                "}";
    }

    @Override
    public String toString() {
        return JacksonUtil.bean2Json(this);
    }
}
