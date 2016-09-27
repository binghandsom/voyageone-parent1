package com.voyageone.components.jumei.reponse;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.UnicodeUtil;

import java.io.IOException;
import java.util.Map;

/**
 * HtMallUpdateSkuForMallResponse 编辑商城的sku[MALL]
 *
 * @author desmond on 2016/9/23
 * @version 2.6.0
 */
public class HtMallUpdateSkuForMallResponse extends BaseJMResponse {
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
                StringBuffer sbMsg = new StringBuffer(" 编辑商城的sku[MALL](/v1/htSku/updateSkuForMall)时,发生错误[" + this.error_code + ":");
                switch (this.error_code) {
                    case "10002":
                        sbMsg.append("client_id,client_key,sign 认证失败");
                        break;
                    case "100001":
                        sbMsg.append("jumei_sku_no, 聚美sku_no错误");
                        break;
                    case "100002":
                        sbMsg.append("status参数错误,只要设置了就只能是enabled或disabled");
                        break;
                    case "100003":
                        sbMsg.append("businessman_num参数错误,只要设置了就不能为空");
                        break;
                    case "100004":
                        sbMsg.append("customs_product_number参数错误,只要设置了就不能为空");
                        break;
                    case "100005":
                        sbMsg.append("status,businessman_code,customs_product_number不能全部同时为空");
                        break;
                    case "100006":
                        sbMsg.append("该sku无所属的spu");
                        break;
                    case "100007":
                        sbMsg.append("该sku所属的spu无所属产品");
                        break;
                    case "100008":
                        sbMsg.append("该jumei_sku_no不属于商家");
                        break;
                    case "100009":
                        sbMsg.append("该sku没有商城详情数据");
                        break;
                    case "100010":
                        sbMsg.append("没有售卖该sku");
                        break;
                    case "100011":
                        sbMsg.append("businessman_code不能重复，必须唯一");
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
        } catch (Exception ex) {
            logger.error("setBody ",ex);
            this.setSuccess(false);
            this.setErrorMsg("HtMallUpdateSkuForMallResponse 返回参数解析错误" + UnicodeUtil.decodeUnicode(this.body));
        }
    }

}
