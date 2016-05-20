package com.voyageone.components.jumei.reponse;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.StringUtils;

import java.io.IOException;
import java.util.Map;

/**
 * HtProductAddResponse(国际POP - 创建商品并同时创建Deal)
 *
 * @author desmond, 2016/5/19
 * @version 2.0.0
 * @since 2.0.0
 */
public class HtProductAddResponse extends BaseJMResponse {
    private boolean is_Success;
    private String jumei_product_id;  // 聚美生成的产品Id
    private String error_code;
    private String errorMsg;
    private String body;

    public boolean getIs_Success() {
        return is_Success;
    }

    public void setIs_Success(boolean is_Success) {
        this.is_Success = is_Success;
    }

    public String getJumei_Product_Id() {
        return jumei_product_id;
    }

    public void setJumei_Product_Id(String jumei_product_id) {
        this.jumei_product_id = jumei_product_id;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
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
            // 取得error code
            if (map.containsKey("error")) {
                Map<String, Object> errorMap = (Map<String, Object>) map.get("error");
                if (errorMap.containsKey("code")) {
                    this.setError_code(String.valueOf(errorMap.get("code")));
                }
            }
            // 取得聚美生成的产品Id jumei_product_id
            if (map.containsKey("product")) {
                Map<String, Object> productMap = (Map<String, Object>) map.get("product");
                if (productMap.containsKey("jumei_product_id")) {
                    this.setJumei_Product_Id(String.valueOf(productMap.get("jumei_product_id")));
                }
            }
            // error_code为空 并且 聚美生成的产品Id不为空的时候，设为成功
            if (StringUtils.isEmpty(this.error_code) && !StringUtils.isEmpty(this.jumei_product_id)) {
                this.setIs_Success(true);
            } else {
                this.setIs_Success(false);
                this.setErrorMsg(this.body);
            }
        } catch (Exception ex) {
            this.setIs_Success(false);
            this.setErrorMsg("返回参数解析错误" + this.body);
        }
    }

}
