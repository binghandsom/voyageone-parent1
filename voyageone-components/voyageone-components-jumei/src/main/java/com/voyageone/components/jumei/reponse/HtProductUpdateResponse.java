package com.voyageone.components.jumei.reponse;

import com.voyageone.common.util.JacksonUtil;

import java.io.IOException;
import java.util.Map;

/**
 * HtProductUpdateResponse
 * @author peitao.sun, 2016/3/29
 * @version 2.0.0
 * @since 2.0.0
 */
public class HtProductUpdateResponse extends BaseJMResponse {

    private boolean is_Success;
    private String errorMsg;
    private String body;

    public boolean getIs_Success() {
        return is_Success;
    }

    public void setIs_Success(boolean is_Success) {
        this.is_Success = is_Success;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) throws IOException {
        Map<String, Object> map = JacksonUtil.jsonToMap(body);
        if (map.containsKey("is_Success") && "1".equals(map.get("is_Success"))) {
            this.setIs_Success(true);
        } else {
            this.setErrorMsg(body);
        }
        this.body = body;
    }

}
