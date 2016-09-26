package com.voyageone.components.jumei.reponse;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.UnicodeUtil;

import java.io.IOException;
import java.util.Map;

/**
 * HtMallSkuPriceUpdateResponse 批量修改商城价格[MALL]
 *
 * @author morse on 2016/8/29
 * @version 2.5.0
 */
public class HtMallSkuPriceUpdateResponse extends BaseJMResponse {
    private boolean isSuccess;
    private String error_code;
    private String reason;
    private String errorMsg;
    private String body;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
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
        this.body = body;
        try {
            Map<String, Object> map = JacksonUtil.jsonToMap(body);
            if (map.containsKey("error_code")) {
                this.setError_code(map.get("error_code").toString());
            }
            if ("0".equals(this.error_code)) {
                this.setSuccess(true);
            } else {
                this.setErrorMsg(body);
            }
            if (map.containsKey("reason")) {
                this.setReason(map.get("reason").toString());
            }
        } catch (Exception ex) {
            logger.error("setBody ",ex);
            this.setSuccess(false);
            this.setErrorMsg("HtMallSkuPriceUpdateResponse 返回参数解析错误" + UnicodeUtil.decodeUnicode(this.body));
        }
    }

}
