package com.voyageone.components.jumei.reponse;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.UnicodeUtil;

import java.io.IOException;
import java.util.Map;

/**
 * HtMallStatusUpdateBatchResponse 批量上下架商城商品[MALL]
 *
 * @author morse on 2016/8/29
 * @version 2.5.0
 */
public class HtMallStatusUpdateBatchResponse extends BaseJMResponse {
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
                this.setSuccess(false);
                StringBuffer sbMsg = new StringBuffer("批量上下架商城商品[MALL](/v1/htMall/updateMallStatusBatch)时,");
                switch (this.error_code) {
                    case "10002":
                        sbMsg.append("client_id,client_key,sign 认证失败");
                    case "302":
                        sbMsg.append("未全部成功,包括全部失败(successCount对应成功的条数, msg对应失败原因, errorList对应失败的信息)");
                    case "100001":
                        sbMsg.append("goods_json参数错误，必须是json数据,一次最多处理20个");
                    case "100002":
                        sbMsg.append("jumei_mall_id参数错误，必须是正整数");
                    case "100003":
                        sbMsg.append("status参数错误，只能是dispaly或hidden或forbidden");
                    case "100004":
                        sbMsg.append("该mall不属于商家");
                    default:
                        sbMsg.append(map.get("reason").toString());
                }
                this.setErrorMsg(this.body + " " + sbMsg);
            }
            if (map.containsKey("reason")) {
                this.setReason(map.get("reason").toString());
            }
        } catch (Exception ex) {
            logger.error("setBody ",ex);
            this.setSuccess(false);
            this.setErrorMsg("HtMallStatusUpdateBatchResponse 返回参数解析错误" + UnicodeUtil.decodeUnicode(this.body));
        }
    }

}
