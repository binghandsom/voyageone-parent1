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
        if(map.containsKey("is_Success")) {
            this.setIs_Success(Integer.getInteger(map.get("is_Success").toString()));
        }
        if(map.containsKey("error"))
        {
            this.setError((Map<String, Object>) map.get("error"));
        }
        this.body = body;
    }
    String body;
    int is_Success;
    public int getIs_Success() {
        return is_Success;
    }
    public void setIs_Success(int is_Success) {
        this.is_Success = is_Success;
    }
    public Map<String, Object> getError() {
        return error;
    }
    public void setError(Map<String, Object> error) {
        this.error = error;
    }
    Map<String, Object> error;


}
