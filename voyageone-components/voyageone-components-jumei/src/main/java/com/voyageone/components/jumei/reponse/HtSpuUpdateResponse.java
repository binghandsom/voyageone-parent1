package com.voyageone.components.jumei.reponse;

import com.voyageone.common.util.JacksonUtil;

import java.io.IOException;
import java.util.Map;

/**
 * HtSpuUpdateResponse 修改Spu信息
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
                this.setIs_Success(false);
                StringBuffer sbMsg = new StringBuffer(" 修改Spu信息(/v1/htSpu/update)时,发生错误[" + this.error_code + ":");
                switch (this.error_code) {
                    case "10002":
                        sbMsg.append("client_id,client_key,sign 认证失败");
                        break;
                    case "100001":
                        sbMsg.append("jumei_spu_no 参数格式错误");
                        break;
                    case "100002":
                        sbMsg.append("update_data必须不为空的JSON,有效字段至少一个");
                        break;
                    case "100013":
                        sbMsg.append("property，规格格式错误");
                        break;
                    case "100014":
                        sbMsg.append("size，容量/尺码错误");
                        break;
                    case "100015":
                        sbMsg.append("abroad_url,海外地址错误");
                        break;
                    case "100016":
                        sbMsg.append("abroad_price， 海外官网价错误");
                        break;
                    case "100017":
                        sbMsg.append("area_code，货币单位错误");
                        break;
                    default:
                        sbMsg.append(map.containsKey("reason") ? map.get("reason").toString() : "");
                }
                sbMsg.append("] ");
                this.setErrorMsg(sbMsg.toString() + this.body);
            }
        } catch (Exception ex) {
            logger.error("setBody ",ex);
            this.setIs_Success(false);
            this.setErrorMsg("HtSpuUpdateResponse 返回参数解析错误" + this.body);
        }
    }
}
