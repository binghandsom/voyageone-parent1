package com.voyageone.components.jumei.request;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.components.jumei.bean.HtProductUpdate_ProductInfo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * HtProductUpdateRequest
 *
 * @author peitao.sun, 2016/3/29
 * @version 2.0.0
 * @since 2.0.0
 */
public class HtProductUpdateRequest implements BaseJMRequest {

    private String url = "/v1/htProduct/update";
    // 可选	Number   聚美商品ID. 参数范围: 大于0的整数。jumei_product_id 和 jumei_product_name 必须存在一个;都存在时，忽略jumei_product_name参数
    private String jumei_product_id;
    // 可选	String     // 聚美产品名.
    private String jumei_product_name;
    private HtProductUpdate_ProductInfo update_data;

    @Override
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getJumei_product_id() {
        return jumei_product_id;
    }

    public void setJumei_product_id(String jumei_product_id) {
        this.jumei_product_id = jumei_product_id;
    }

    public String getJumei_product_name() {
        return jumei_product_name;
    }

    public void setJumei_product_name(String jumei_product_name) {
        this.jumei_product_name = jumei_product_name;
    }

    public HtProductUpdate_ProductInfo getUpdate_data() {
        return update_data;
    }

    public void setUpdate_data(HtProductUpdate_ProductInfo update_data) {
        this.update_data = update_data;
    }

    @Override
    public Map<String, Object> getParameter() throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("jumei_product_id", this.getJumei_product_id());
        params.put("jumei_product_name", this.getJumei_product_name());
        params.put("update_data", JacksonUtil.bean2JsonNotNull(update_data));
        return params;
    }
}
