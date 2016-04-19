package com.voyageone.components.jumei.reponse;

import com.voyageone.common.util.JacksonUtil;

import java.io.IOException;
import java.util.Map;

/**
 * Created by dell on 2016/3/29.
 */
public class HtDealUpdateResponse extends JMResponse {
    //    {
//        "error_code": "0",
//            "reason": "success",
//            "response": ""
//    }
//    错误示例
//            错误编码
//    {
//        "error_code": "500",
//            "reason": "error",
//            "response": ""
//    }
    String errorMsg;

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public String getBody() {
        return body;
    }

    public boolean is_Success() {
        return is_Success;
    }

    @Override
    public void setBody(String body) throws IOException {
        Map<String, Object> map = JacksonUtil.jsonToMap(body);
        if (map.containsKey("error_code") && map.get("error_code") == "0") {
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

}
