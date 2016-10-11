package com.voyageone.components.jumei.reponse;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.common.util.UnicodeUtil;

import java.util.Map;

/**
 * HtDealUpdateResponse 修改Deal(特卖)信息
 * @author peitao.sun, 2016/3/29
 * @version 2.0.0
 * @since 2.0.0
 */
public class HtDealUpdateResponse extends BaseJMResponse {
    //    {
//        "error_code": "0",
//            "reason": "success",
//            "response": ""
//    }
//    错误示例
//            错误编码
//    {
//        "error_code": "500",
//            "reason": "error",
//            "response": ""
//    }
    private String errorMsg;
    private boolean is_Success;
    private String body;
    public String getErrorMsg() {
        return errorMsg;
    }
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
    public String getBody() {
        return body;
    }
    public boolean is_Success() {
        return is_Success;
    }
    public void setBody(String body)  {
        this.body = body;
        try {
            Map<String, Object> map = JacksonUtil.jsonToMap(body);
            if (map.containsKey("error_code") && "0".equals(map.get("error_code"))) {
                this.setIs_Success(true);
            } else {
                this.setIs_Success(false);
                if (map.get("error_code") != null) {
                    String error_code = StringUtils.toString(map.get("error_code"));
                    StringBuffer sbMsg = new StringBuffer(" 修改Deal(特卖)信息(/v1/htDeal/update)时,发生错误[" + error_code + ":");
                    switch (error_code) {
                        case "10002":
                            sbMsg.append("client_id,client_key,sign 认证失败");
                            break;
                        case "100001":
                            sbMsg.append("jumei_hash_id参数错误，不能为空");
                            break;
                        case "100002":
                            sbMsg.append("update_data参数错误，不能为空");
                            break;
                        case "100017":
                            sbMsg.append("商家无此商品");
                            break;
                        case "100018":
                            sbMsg.append("sku和deal不属于同一产品");
                            break;
                        case "100019":
                            sbMsg.append("sku_no未创建");
                            break;
                        case "100020":
                            sbMsg.append("更新失败");
                            break;
                        case "100021":
                            sbMsg.append("审核失败;(适当重试后还失败，可联系运营手动审核)");
                            break;
                        default:
                            sbMsg.append(map.containsKey("reason") ? map.get("reason").toString() : "");
                    }
                    sbMsg.append("] ");
                    this.setErrorMsg(this.getRequestUrl() + sbMsg.toString() + UnicodeUtil.decodeUnicode(body));
                }
            }
        } catch (Exception ex) {
            logger.error("setBody ",ex);
            this.setIs_Success(false);
            this.setErrorMsg("返回参数解析错误" + UnicodeUtil.decodeUnicode(this.body));
        }
    }
    public boolean getIs_Success() {
        return is_Success;
    }
    public void setIs_Success(boolean is_Success) {
        this.is_Success = is_Success;
    }
}
