package com.voyageone.components.jumei.reponse;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.common.util.UnicodeUtil;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * HtMallSkuAddResponse 商城商品追加sku[MALL]
 *
 * @author morse on 2016/8/29
 * @version 2.5.0
 */
public class HtMallSkuAddResponse extends BaseJMResponse {
    private String jumeiSkuNo;
    private boolean isSuccess;
    private String error_code;
    private String reason;
    private String errorMsg;
    private String body;

    public String getJumeiSkuNo() {
        return jumeiSkuNo;
    }

    public void setJumeiSkuNo(String jumeiSkuNo) {
        this.jumeiSkuNo = jumeiSkuNo;
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
            if ("0".equals(this.error_code)) {
                this.setSuccess(true);
            } else {
                this.setSuccess(false);
                StringBuffer sbMsg = new StringBuffer(" 商城商品追加sku[MALL](v1/htSku/addMallSku)时,发生错误[" + this.error_code + ":");
                switch (this.error_code) {
                    case "10002":
                        sbMsg.append("client_id,client_key,sign 认证失败");
                        break;
                    case "100001":
                        sbMsg.append("skujumei_spu_no,聚美SPU_NO错误");
                        break;
                    case "100002":
                        sbMsg.append("sku_info参数错误,只要被设置就只能是json数据且当spu_no下无sku时必填");
                        break;
                    case "100003":
                        sbMsg.append("businessman_num参数错误");
                        break;
                    case "100004":
                        sbMsg.append("stocks库存参数错误");
                        break;
                    case "100005":
                        sbMsg.append("mall_price参数错误,必须是Float类型且大于15元");
                        break;
                    case "100006":
                        sbMsg.append("market_price参数错误,必须是Float类型且大于等于商城价");
                        break;
                    case "100007":
                        sbMsg.append("只要被设置就不能为空且当发货仓库为保税区仓库时, customs_product_number不能为空");
                        break;
                    case "100008":
                        sbMsg.append("该jumei_spu_no不存在");
                        break;
                    case "100009":
                        sbMsg.append("spu下存在的sku参数不完整，请重新添加完整信息后再来追加");
                        break;
                    case "100010":
                        sbMsg.append("spu下存在的sku的deal_price(团购价格)不能小于15元,请修改后再来追加");
                        break;
                    default:
                        sbMsg.append(map.get("reason").toString());
                }
                sbMsg.append("] ");
                this.setErrorMsg(sbMsg.toString() + this.body);
            }
            if (map.containsKey("reason")) {
                this.setReason(map.get("reason").toString());
            }
            if (map.containsKey("response") && !StringUtils.isEmpty(StringUtils.toString(map.get("response")))) {
                LinkedHashMap<String, Object> mapSesponse = (LinkedHashMap<String, Object>)map.get("response");
                if (mapSesponse.containsKey("jumei_sku_no")) {
                    this.setJumeiSkuNo(mapSesponse.get("jumei_sku_no").toString());
                }
            }
        } catch (Exception ex) {
            logger.error("setBody ",ex);
            this.setSuccess(false);
            this.setErrorMsg("HtMallSkuAddResponse 返回参数解析错误" + UnicodeUtil.decodeUnicode(this.body));
        }
    }

}
