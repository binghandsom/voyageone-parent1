package com.voyageone.components.jumei.reponse;

import com.voyageone.common.util.JacksonUtil;

import java.io.IOException;
import java.util.Map;

/**
 * HtDealUpdateSkuIsEnableRequest 上下架Deal关联的Sku
 *
 * Created by dell on 2016/9/23.
 */
public class HtDealUpdateSkuIsEnableResponse extends BaseJMResponse {

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
            Map<String, Object> map = JacksonUtil.jsonToMap(body);
            if (map.containsKey("error_code")) {
                this.setError_code(map.get("error_code").toString());
            }
            // added by morse.lu 2016/10/08 start
            // 返回的错误格式是 "error": {"code": "500"}
            // error_code逻辑暂时保留，以防以后会变
            if (map.containsKey("error")) {
                Map<String, Object> mapError = (Map<String, Object>) map.get("error");
                if (mapError.containsKey("code")) {
                    this.setError_code(mapError.get("code").toString());
                }
            }
            // added by morse.lu 2016/10/08 end
            if (map.containsKey("is_Success") && "1".equals(map.get("is_Success"))) {
                this.setIs_Success(true);
            } else {
                this.setIs_Success(false);
                StringBuffer sbMsg = new StringBuffer(" 上下架Deal关联的Sku(/v1/htDeal/updateSkuIsEnable)时,发生错误[" + this.error_code + ":");
                switch (this.error_code) {
                    case "10002":
                        sbMsg.append("client_id,client_key,sign 认证失败");
                        break;
                    case "100002":
                        sbMsg.append("jumei_hash_id参数错误，不能为空");
                        break;
                    case "100003":
                        sbMsg.append("jumei_sku_no参数错误，不能为空");
                        break;
                    case "100004":
                        sbMsg.append("is_enable参数错误，只能是0或1");
                        break;
                    case "100005":
                        sbMsg.append("无权操作的Deal");
                        break;
                    case "100006":
                        sbMsg.append("此Deal必须有sku售卖");
                        break;
                    case "100008":
                        sbMsg.append("与此Deal相关的Sku在其他Deal下销售,不允许更新");
                        break;
                    case "100012":
                        sbMsg.append("Sku不存在");
                        break;
                    case "100013":
                        sbMsg.append("is_enable参数和数据库中参数一致，没有发生改变");
                        break;
                    default:
                        sbMsg.append(map.containsKey("reason") ? map.get("reason").toString() : "");
                }
                sbMsg.append("] ");
                this.setErrorMsg(sbMsg.toString() + this.getRequestUrl() + " " + this.body);
            }

        } catch (Exception ex) {
            logger.error("setBody ",ex);
            this.setIs_Success(false);
            this.setErrorMsg("HtDealUpdateSkuIsEnableResponse 返回参数解析错误" + this.body);
        }
    }
}
