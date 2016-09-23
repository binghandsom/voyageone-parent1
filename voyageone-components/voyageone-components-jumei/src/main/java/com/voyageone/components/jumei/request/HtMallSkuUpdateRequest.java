package com.voyageone.components.jumei.request;

import java.util.HashMap;
import java.util.Map;

/**
 * HtMallAddRequest 特卖商品绑定到商城[MALL]
 *
 * @author morse on 2016/8/29
 * @version 2.5.0
 */
public class HtMallSkuUpdateRequest implements BaseJMRequest {
    private String url = "/v1/htSku/updateSkuForMall";

    private String jumei_sku_no; // 聚美Sku_No
    private boolean enabled; // enabled-是，disabled-否.

    @Override
    public String getUrl() {
        return url;
    }

    public String getJumei_sku_no() {
        return jumei_sku_no;
    }

    public void setJumei_sku_no(String jumei_sku_no) {
        this.jumei_sku_no = jumei_sku_no;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public Map<String, Object> getParameter() {
        Map<String, Object> params = new HashMap<>();
        params.put("jumei_sku_no", jumei_sku_no);
        if (enabled) {
            params.put("status", "enabled");
        } else {
            params.put("status", "disabled");
        }
        return params;
    }
}
