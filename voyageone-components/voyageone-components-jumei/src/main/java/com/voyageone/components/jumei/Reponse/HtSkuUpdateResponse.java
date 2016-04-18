package com.voyageone.components.jumei.Reponse;
import com.voyageone.common.util.JacksonUtil;
import java.io.IOException;
import java.util.Map;
/**
 * Created by dell on 2016/3/29.
 */
public class HtSkuUpdateResponse extends JMResponse {

    String error_code;
    String reason;
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


    @Override
    public String getBody() {
        return body;
    }

    String body;

    @Override
    public void setBody(String body) throws IOException {
        this.body = body;
        try {
            //{"is_Success" : 1}
//            {
//                "error": {
//                "code": "500"
//            }
//            }
            Map<String, Object> map = JacksonUtil.jsonToMap(body);
            if (map.containsKey("error")) {
                this.setError_code(map.get("code").toString());
            }
            if (map.containsKey("reason")) {
                this.setReason(map.get("reason").toString());
            }
            if (map.containsKey("is_Success") && map.get("is_Success").equals("1")) {
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
