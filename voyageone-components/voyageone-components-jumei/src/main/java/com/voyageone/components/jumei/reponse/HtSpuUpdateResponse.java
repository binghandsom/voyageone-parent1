package com.voyageone.components.jumei.reponse;

import com.voyageone.common.util.JacksonUtil;

import java.io.IOException;
import java.util.Map;

/**
 * HtSpuUpdateResponse
 * @author peitao.sun, 2016/3/29
 * @version 2.0.0
 * @since 2.0.0
 */
public class HtSpuUpdateResponse extends BaseJMResponse {

    private String error_code;
    private String reason;
    private boolean is_Success;
    private String errorMsg;
    private String body;


    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public boolean is_Success() {
        return is_Success;
    }

    public void setIs_Success(boolean is_Success) {
        this.is_Success = is_Success;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) throws IOException {
        this.body = body;
        try {
//            {
//                "error_code": "0",
//                    "reason": "success",
//                    "response": ""
//            }
            Map<String, Object> map = JacksonUtil.jsonToMap(body);
            if (map.containsKey("error_code")) {
                this.setError_code(map.get("error_code").toString());
            }
            if (map.containsKey("reason")) {
                this.setReason(map.get("reason").toString());
            }
            if (map.containsKey("reason") && "success".equals(map.get("reason"))) {
                this.setIs_Success(true);
            } else {
                this.setErrorMsg(this.body);
            }
        } catch (Exception ex) {
            this.setIs_Success(false);
            this.setErrorMsg("返回参数解析错误" + this.body);
        }
    }
}
