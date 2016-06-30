package com.voyageone.components.jumei.reponse;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.UnicodeUtil;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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

错误编码
{
    "error_code": 500,
    "reason": "error",
    "response": ""
}
业务类异常示例
    {
        "error_code": "302",
            "reason": "error",
            "response": {
        "successCount": 0,
                "errorList": [
        {
            "jumei_sku_no": 1,
                "jumei_deal_id": 1111,
                "error_code": 505,
                "error_message":"hash_id为: 1111不属于商家: 40!"
        },
        {
            "jumei_sku_no": 1,
                "jumei_deal_id": "2222",
                "error_code": 100011,
                "error_message": ""
        }
        ]
    }
    }
    */
    private String error_code;
    private String reason;
    private boolean is_Success;
    private String errorMsg;
    private String body;
    private int successCount;//成功条数

    private List<JuMeiSkuError> errorList;

    public List<JuMeiSkuError> getErrorList() {
        return errorList;
    }

    public void setErrorList(List<JuMeiSkuError> errorList) {
        this.errorList = errorList;
    }

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
                if (map.get("response") instanceof Map) {
                    Map<String, Object> mapSuccess = (Map<String, Object>) map.get("response");
                    if (mapSuccess.containsKey("successCount")) {
                        //返回成功条数
                        this.setSuccessCount(Integer.parseInt(mapSuccess.get("successCount").toString()));
                    }
                    if (mapSuccess.containsKey("errorList")) {
                        loadErrorList(mapSuccess);
                    }
                }
            }
            if ("0".equals(this.error_code)) {
                this.setIs_Success(true);
            } else {
                this.setErrorMsg(UnicodeUtil.decodeUnicode(this.body));
            }
        } catch (Exception ex) {
            this.setIs_Success(false);
            this.setErrorMsg("返回参数解析错误" + UnicodeUtil.decodeUnicode(this.body));
        }
    }

    private void loadErrorList(Map<String, Object> mapSuccess) {
        List<Map<String, Object>> listError = (List<Map<String, Object>>) mapSuccess.get("errorList");
        this.setErrorList(new ArrayList<>());
        JuMeiSkuError error = null;
        for (Map<String, Object> mapError : listError) {
            error = new JuMeiSkuError();
            if (mapError.containsKey("jumei_deal_id")) {
                error.setJumei_deal_id(mapError.get("jumei_deal_id").toString());
            }
            if (mapError.containsKey("jumei_sku_no")) {
                error.setJumei_sku_no(mapError.get("jumei_sku_no").toString());
            }
            if (mapError.containsKey("error_code")) {
                error.setError_code(mapError.get("error_code").toString());
            }
            if (mapError.containsKey("error_message")) {
                error.setError_message(mapError.get("error_message").toString());
            }
            this.getErrorList().add(error);
        }
    }

    public class JuMeiSkuError implements Serializable {

        private String jumei_sku_no;
        private String jumei_deal_id;
        private String error_code;
        private String error_message;

        public String getJumei_sku_no() {
            return jumei_sku_no;
        }

        public void setJumei_sku_no(String jumei_sku_no) {
            this.jumei_sku_no = jumei_sku_no;
        }

        public String getJumei_deal_id() {
            return jumei_deal_id;
        }

        public void setJumei_deal_id(String jumei_deal_id) {
            this.jumei_deal_id = jumei_deal_id;
        }

        public String getError_code() {
            return error_code;
        }

        public void setError_code(String error_code) {
            this.error_code = error_code;
        }

        public String getError_message() {
            return error_message;
        }

        public void setError_message(String error_message) {
            this.error_message = error_message;
        }

    }
}
