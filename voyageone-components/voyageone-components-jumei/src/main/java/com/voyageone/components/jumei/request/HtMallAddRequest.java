package com.voyageone.components.jumei.request;

import com.voyageone.components.jumei.bean.JmProductBean;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * HtMallAddRequest 特卖商品绑定到商城[MALL]
 *
 * @author morse on 2016/8/29
 * @version 2.5.0
 */
public class HtMallAddRequest implements BaseJMRequest {
    private String url = "/v1/htProduct/dealToMall";

    private String jumeiHashId; // 聚美Deal ID.

    @Override
    public String getUrl() {
        return url;
    }

    public String getJumeiHashId() {
        return jumeiHashId;
    }

    public void setJumeiHashId(String jumeiHashId) {
        this.jumeiHashId = jumeiHashId;
    }

    @Override
    public Map<String, Object> getParameter() {
        Map<String, Object> params = new HashMap<>();
        params.put("jumei_hash_id", jumeiHashId);
        return params;
    }
}
