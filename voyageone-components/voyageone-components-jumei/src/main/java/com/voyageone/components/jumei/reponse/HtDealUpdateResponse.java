package com.voyageone.components.jumei.reponse;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.UnicodeUtil;

import java.io.IOException;
import java.util.Map;

/**
 * HtDealUpdateResponse
 * @author peitao.sun, 2016/3/29
 * @version 2.0.0
 * @since 2.0.0
 */
public class HtDealUpdateResponse extends BaseJMResponse {
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
    private String errorMsg;
    private boolean is_Success;
    private String body;
    public String getErrorMsg() {
        return errorMsg;
    }
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
    public String getBody() {
        return body;
    }
    public boolean is_Success() {
        return is_Success;
    }
    public void setBody(String body) throws IOException {
        Map<String, Object> map = JacksonUtil.jsonToMap(body);
        if (map.containsKey("error_code") && "0".equals(map.get("error_code"))) {
            this.setIs_Success(true);
        } else {
            this.setErrorMsg(UnicodeUtil.decodeUnicode(body));
        }
        this.body = body;
    }
    public boolean getIs_Success() {
        return is_Success;
    }
    public void setIs_Success(boolean is_Success) {
        this.is_Success = is_Success;
    }
}
