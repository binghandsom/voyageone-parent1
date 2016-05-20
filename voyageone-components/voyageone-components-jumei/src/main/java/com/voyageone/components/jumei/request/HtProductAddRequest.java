package com.voyageone.components.jumei.request;

import com.voyageone.components.jumei.bean.JmProductBean;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * HtProductAddRequest(国际POP - 创建商品并同时创建Deal)
 *
 * @author desmond, 2016/5/19
 * @version 2.0.0
 * @since 2.0.0
 */
public class HtProductAddRequest implements BaseJMRequest {
    private String url = "/v1/htProduct/addProductAndDeal";

    // 商品信息
    private JmProductBean jmProduct;

    @Override
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public JmProductBean getJmProduct() {
        return jmProduct;
    }

    public void setJmProduct(JmProductBean jmProduct) {
        this.jmProduct = jmProduct;
    }

    @Override
    public Map<String, Object> getParameter() throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("product", jmProduct.toJsonStr());
        params.put("spus", jmProduct.getSpusString());
        params.put("dealInfo", jmProduct.getDealInfoString());
        return params;
    }
}
