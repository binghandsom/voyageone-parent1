package com.voyageone.components.jumei.reponse;

import com.voyageone.common.util.JacksonUtil;

import java.io.IOException;
import java.util.Map;

/**
 * Created by dell on 2016/3/29.
 */
public class HtProductUpdateResponse extends JMResponse {

    @Override
    public void setBody(String body) throws IOException {
        Map<String, Object> map = JacksonUtil.jsonToMap(body);
        if (map.containsKey("is_Success") && (map.get("is_Success").toString().equals("1"))) {
            this.setIs_Success(true);
        } else {
            this.setErrorMsg(body);
        }
        this.body = body;
    }
    String body;
    boolean is_Success;
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

    String errorMsg;


}
