package com.voyageone.components.jumei.reponse;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.StringUtils;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * HtProductGetByStatusResponse(国际POP - 根据状态批量获取商城商品[MALL])
 *
 * @author jiangjusheng, 2016/08/31
 * @version 2.0.0
 * @since 2.0.0
 */
public class HtProductGetByStatusResponse extends BaseJMResponse {
    private boolean isSuccess;
    private String errorCode;
    private String errorMsg;
    private String body;
    private List<Map<String, Object>> prodInfos;

    public boolean getIsSuccess() {
        return isSuccess;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) throws IOException {
        this.body = body;
        isSuccess = false;
        try {
            Map<String, Object> map = JacksonUtil.jsonToMap(body);
            // 取得error code
            if (map.get("error_code") != null && !"0".equals(map.get("error_code").toString())) {
                errorMsg = body;
                errorCode = map.get("error_code").toString();
                return;
            }

            // 取得聚美生成的产品Id jumei_product_id
            if (map.containsKey("response") && !StringUtils.isEmpty(StringUtils.toString(map.get("response")))) {
                LinkedHashMap<String, Object> response = (LinkedHashMap<String, Object>) map.get("response");
                if (response.containsKey("productInfo")) {
                    isSuccess = true;
                    prodInfos = (List<Map<String, Object>>) response.get("productInfo");
                }
            }

        } catch (Exception ex) {
            logger.error("setBody ",ex);
            isSuccess = false;
            errorMsg = "HtProductGetByStatusResponse 返回参数解析错误" + this.body;
        }
    }

    public List<Map<String, Object>> getProdInfos() {
        return prodInfos;
    }

}
