package com.voyageone.components.jumei.Reponse;

import com.voyageone.common.util.JacksonUtil;

import java.io.IOException;
import java.util.Map;

/**
 * Created by dell on 2016/3/29.
 */
public class HtDealCopyDealResponse extends JMResponse {
    //    {
//        "error_code": 0,
//            "reason": "success",
//            "response": {"jumei_hash_id" : "ht12345677"}
//    }
    String error_code;
    String reason;
    String jumei_hash_id;
    boolean is_Success;
    String errorMsg;

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

    public String getJumei_hash_id() {
        return jumei_hash_id;
    }

    public void setJumei_hash_id(String jumei_hash_id) {
        this.jumei_hash_id = jumei_hash_id;
    }

    @Override
    public String getBody() {
        return body;
    }

    String body;

    @Override
    public void setBody(String body) throws IOException {
        this.body = body;
        try {
            Map<String, Object> map = JacksonUtil.jsonToMap(body);
            if (map.containsKey("error_code")) {
                this.setError_code(map.get("error_code").toString());
            }
            if (map.containsKey("reason")) {
                this.setReason(map.get("reason").toString());
            }
            if (map.containsKey("response")) {
                Map<String, Object> mapSesponse = (Map<String, Object>) map.get("response");
                if (mapSesponse.containsKey("jumei_hash_id")) {
                    this.setJumei_hash_id(mapSesponse.get("jumei_hash_id").toString());
                }
            }
            if (this.getError_code() == "0") {
                this.setIs_Success(true);
            } else {
                this.setErrorMsg(this.getBody());
            }
        } catch (Exception ex) {
            this.setIs_Success(false);
            this.setErrorMsg("返回参数解析错误" + this.getBody());
        }
    }
}
