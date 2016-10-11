package com.voyageone.components.jumei.reponse;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.UnicodeUtil;

import java.io.IOException;
import java.util.Map;

/**
 * HtSkuAddResponse 添加Sku信息
 * @author peitao.sun, 2016/3/29
 * @version 2.0.0
 * @since 2.0.0
 */
public class HtSkuAddResponse extends BaseJMResponse {
//    {
//        "error_code": 0,
//            "reason": "success",
//            "response": {"sku_no":"jumei_sku_no"}
//    }
    private String error_code;
    private String reason;
    private String jumei_sku_no;
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

    public String getJumei_sku_no() {
        return jumei_sku_no;
    }

    public void setJumei_sku_no(String jumei_sku_no) {
        this.jumei_sku_no = jumei_sku_no;
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
                if (mapSesponse.containsKey("sku_no")) {
                    this.setJumei_sku_no(mapSesponse.get("sku_no").toString());
                }
            }
            if ("0".equals(this.error_code)) {
                this.setIs_Success(true);
            } else {
                this.setIs_Success(false);
                StringBuffer sbMsg = new StringBuffer(" 添加Sku信息(/v1/htSku/add)时,发生错误[" + this.error_code + ":");
                switch (this.error_code) {
                    case "10002":
                        sbMsg.append("client_id,client_key,sign 认证失败");
                        break;
                    case "100010":
                        sbMsg.append("jumei_hash_id,聚美唯一Deal值错误");
                        break;
                    case "100011":
                        sbMsg.append("jumei_spu_no,聚美SPU_NO错误");
                        break;
                    case "100012":
                        sbMsg.append("stocks,库存错误");
                        break;
                    case "100013":
                        sbMsg.append("deal_price 或 market_price错误");
                        break;
                    case "100014":
                        sbMsg.append("businessman_num,商家商品唯一编码错误");
                        break;
                    case "100015":
                        sbMsg.append("sale_on_this_deal,是否在本次团购售卖SKU参数错误");
                        break;
                    case "100016":
                        sbMsg.append("发货仓库为保税区仓库时, 海关备案商品编码不能为空");
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
            this.setErrorMsg("HtSkuAddResponse 返回参数解析错误" + UnicodeUtil.decodeUnicode(this.body));
        }
    }
}
