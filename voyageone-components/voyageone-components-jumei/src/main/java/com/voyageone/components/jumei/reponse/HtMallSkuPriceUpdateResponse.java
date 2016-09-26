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
                StringBuffer sbMsg = new StringBuffer("批量修改商城商品价格[MALL](/v1/htMall/updateMallPriceBatch)时,");
                switch (this.error_code) {
                    case "10002":
                        sbMsg.append("client_id,client_key,sign 认证失败");
                    case "302":
                        sbMsg.append("未全部成功,包括全部失败(successCount对应成功的条数, msg对应失败原因, errorList对应失败的jumei_sku_no)");
                    case "100001":
                        sbMsg.append("update_data,参数错误,必须为合法的JSON, 一次最多修改20个");
                    case "100002":
                        sbMsg.append("jumei_sku_no,参数错误");
                    case "100003":
                        sbMsg.append("mall_price,参数错误,商城价大于15");
                    case "100004":
                        sbMsg.append("market_price,参数错误");
                    case "100005":
                        sbMsg.append("market_price 和 mall_price至少存在一个, 且市场价大于等于商城价");
                    case "100006":
                        sbMsg.append("该sku_no不存在");
                    case "100007":
                        sbMsg.append("该sku_no没有所属产品");
                    case "100008":
                        sbMsg.append("该sku_no所属产品没有发布");
                    case "100009":
                        sbMsg.append("该sku_no没有商城详情数据, 请核实商城详情");
                    case "100010":
                        sbMsg.append("该sku_no不在售卖状态, 请核实");
                    case "100011":
                        sbMsg.append("该sku_no不存在关系售卖数据, 请核实");
                    case "100012":
                        sbMsg.append("该sku有在售的deal,不允许修改市场价");
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
            this.setErrorMsg("HtMallSkuPriceUpdateResponse 返回参数解析错误" + UnicodeUtil.decodeUnicode(this.body));
        }
    }

}
