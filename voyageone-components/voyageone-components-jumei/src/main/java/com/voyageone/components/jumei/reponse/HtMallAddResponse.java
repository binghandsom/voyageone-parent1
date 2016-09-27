package com.voyageone.components.jumei.reponse;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.common.util.UnicodeUtil;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * HtMallAddResponse 特卖商品绑定到商城[MALL]
 *
 * @author morse on 2016/8/29
 * @version 2.5.0
 */
public class HtMallAddResponse extends BaseJMResponse {
    private String jumeiMallId;  // 聚美生成的MallId
    private boolean isSuccess;
    private String error_code;
    private String reason;
    private String errorMsg;
    private String body;

    public String getJumeiMallId() {
        return jumeiMallId;
    }

    public void setJumeiMallId(String jumeiMallId) {
        this.jumeiMallId = jumeiMallId;
    }

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
            if (map.containsKey("reason")) {
                this.setReason(map.get("reason").toString());
            }
            if (map.containsKey("response") && !StringUtils.isEmpty(StringUtils.toString(map.get("response")))) {
                LinkedHashMap<String, Object> mapSesponse = (LinkedHashMap<String, Object>)map.get("response");
                if (mapSesponse.containsKey("jumei_mall_id")) {
                    this.setJumeiMallId(mapSesponse.get("jumei_mall_id").toString());
                }
            }
            if ("0".equals(this.error_code)) {
                this.setSuccess(true);
            } else {
                this.setSuccess(false);
                StringBuffer sbMsg = new StringBuffer(" 特卖商品绑定到商城[MALL](/v1/htProduct/dealToMall)时,发生错误[" + this.error_code + ":");
                switch (this.error_code) {
                    case "10002":
                        sbMsg.append("client_id,client_key,sign 认证失败");
                        break;
                    case "120014":
                        sbMsg.append("jumei_hash_id 参数错误");
                        break;
                    case "100001":
                        sbMsg.append("skuInfo 参数错误");
                        break;
                    case "100002":
                        sbMsg.append("mallInfo 参数错误");
                        break;
                    default:
                        sbMsg.append(map.get("reason").toString());
                }
                sbMsg.append("] ");
                this.setErrorMsg(sbMsg.toString() + this.body);
            }
        } catch (Exception ex) {
            logger.error("setBody ",ex);
            this.setSuccess(false);
            this.setErrorMsg("HtMallAddResponse 返回参数解析错误" + UnicodeUtil.decodeUnicode(this.body));
        }
    }

}
