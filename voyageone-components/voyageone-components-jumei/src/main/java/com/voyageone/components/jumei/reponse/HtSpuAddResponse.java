package com.voyageone.components.jumei.reponse;

import com.voyageone.common.util.JacksonUtil;

import java.io.IOException;
import java.util.Map;

/**
 * HtSpuAddResponse 添加Spu信息
 * @author peitao.sun, 2016/3/29
 * @version 2.0.0
 * @since 2.0.0
 */
public class HtSpuAddResponse extends BaseJMResponse {
//    {
//        "error_code": 0,
//            "reason": "success",
//            "response": {"spu_no":"jumei_spu_no"}
//    }
    private String error_code;
    private String reason;
    private String jumei_spu_no;
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

    public String getJumei_spu_no() {
        return jumei_spu_no;
    }

    public void setJumei_spu_no(String jumei_spu_no) {
        this.jumei_spu_no = jumei_spu_no;
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
                if (mapSesponse.containsKey("spu_no")) {
                    this.setJumei_spu_no(mapSesponse.get("spu_no").toString());
                }
            }
            if ("0".equals(this.error_code)) {
                this.setIs_Success(true);
            } else {
                this.setIs_Success(false);
                StringBuffer sbMsg = new StringBuffer(" 添加Spu信息(/v1/htSpu/add)时,发生错误[" + this.error_code + ":");
                switch (this.error_code) {
                    case "10002":
                        sbMsg.append("client_id,client_key,sign 认证失败");
                        break;
                    case "100001":
                        sbMsg.append("jumei_product_id, 聚美产品ID参数错误");
                        break;
                    case "100013":
                        sbMsg.append("property，规格错误");
                        break;
                    case "100014":
                        sbMsg.append("size，容量/尺码错误");
                        break;
                    case "100016":
                        sbMsg.append("abroad_price， 海外官网价错误");
                        break;
                    case "100017":
                        sbMsg.append("area_code， 货币符号Id错误");
                        break;
                    default:
                        sbMsg.append(map.get("reason").toString());
                }
                sbMsg.append("] ");
                this.setErrorMsg(sbMsg.toString() + this.body);
            }
        } catch (Exception ex) {
            logger.error("setBody ",ex);
            this.setIs_Success(false);
            this.setErrorMsg("HtSpuAddResponse 返回参数解析错误" + this.body);
        }
    }
}
