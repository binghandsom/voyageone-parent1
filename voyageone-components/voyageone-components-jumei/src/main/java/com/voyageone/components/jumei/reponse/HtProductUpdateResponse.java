package com.voyageone.components.jumei.reponse;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.UnicodeUtil;

import java.io.IOException;
import java.util.Map;

/**
 * HtProductUpdateResponse
 * @author peitao.sun, 2016/3/29
 * @version 2.0.0
 * @since 2.0.0
 */
public class HtProductUpdateResponse extends BaseJMResponse {

    private boolean is_Success;
    private String errorMsg;
    private String body;

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

    public String getBody() {
        return body;
    }

    public void setBody(String body) throws IOException {
        try {
        Map<String, Object> map = JacksonUtil.jsonToMap(body);
        if (map.containsKey("error_code") && "0".equals(map.get("error_code"))) {
            this.setIs_Success(true);
        } else {
            this.setErrorMsg(body);
        }
        this.body = body;
        } catch (Exception ex) {
            logger.error("setBody ",ex);
            this.setIs_Success(false);
            this.setErrorMsg("返回参数解析错误" + UnicodeUtil.decodeUnicode(this.body));
        }
    }

}
