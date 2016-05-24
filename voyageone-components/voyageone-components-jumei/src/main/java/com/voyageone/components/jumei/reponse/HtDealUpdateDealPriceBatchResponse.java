package com.voyageone.components.jumei.reponse;

import com.voyageone.common.util.JacksonUtil;

import java.io.IOException;
import java.util.Map;

public class HtDealUpdateDealPriceBatchResponse extends BaseJMResponse {
    /*
   {
    "error_code": "0",
    "reason": "success",
    "response": {
         "successCount": "成功的条数"
    }
}
系统类异常示例
业务类异常示例
错误编码
{
    "error_code": 500,
    "reason": "error",
    "response": ""
}
    */
    private String error_code;
    private String reason;
    private boolean is_Success;
    private String errorMsg;
    private String body;
    private int successCount;//成功条数

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
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
                Map<String, Object> mapSesponse = (Map<String, Object>) map.get("response");
                if (mapSesponse.containsKey("successCount")) {
                    //返回成功条数
                    this.setSuccessCount(Integer.getInteger(mapSesponse.get("successCount").toString()));
                }
            }
            if ("0".equals(this.error_code)) {
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