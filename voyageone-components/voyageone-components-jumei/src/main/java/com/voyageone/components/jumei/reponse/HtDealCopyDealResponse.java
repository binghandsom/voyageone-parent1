package com.voyageone.components.jumei.reponse;

import com.voyageone.common.util.JacksonUtil;

import java.io.IOException;
import java.util.Map;

/**
 * HtDealCopyDealResponse
 * @author peitao.sun, 2016/3/29
 * @version 2.0.0
 * @since 2.0.0
 */
public class HtDealCopyDealResponse extends BaseJMResponse {
    //    {
//        "error_code": 0,
//            "reason": "success",
//            "response": {"jumei_hash_id" : "ht12345677"}
//    }
    private String error_code;
    private String reason;
    private String jumei_hash_id;
    private boolean is_Success;
    private String errorMsg;
    private String body;
    private String sell_hash_id;

    public String getSell_hash_id() {
        return sell_hash_id;
    }

    public void setSell_hash_id(String sell_hash_id) {
        this.sell_hash_id = sell_hash_id;
    }

    String response;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

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

    public String getBody() {
        return body;
    }

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
                if (map.get("response") instanceof Map) {
                    Map<String, Object> mapSesponse = (Map<String, Object>) map.get("response");
                    if (mapSesponse.containsKey("jumei_hash_id")) {
                        this.setJumei_hash_id(mapSesponse.get("jumei_hash_id").toString());
                    }
                    if (mapSesponse.containsKey("new_hash_id")) {
                        this.setJumei_hash_id(mapSesponse.get("new_hash_id").toString());
                    }
                    if (mapSesponse.containsKey("sell_hash_id")) {
                        this.setSell_hash_id(mapSesponse.get("sell_hash_id").toString());
                    }
                } else {
                    this.setResponse(map.get("response").toString());
                }
            }
            if ("0".equals(this.error_code)&&!"error".equals(this.getReason())) {
                this.setIs_Success(true);
            } else {
                this.setErrorMsg(this.getRequestUrl()+this.body);
            }
        } catch (Exception ex) {
            logger.error("setBody ", ex);
            this.setIs_Success(false);
            this.setErrorMsg("返回参数解析错误" + this.body);
        }
    }
}
