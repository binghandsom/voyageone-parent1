package com.voyageone.components.jumei.request;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dell on 2016/9/23.
 */
public class HtDealUpdateSkuIsEnableRequest implements BaseJMRequest {
    private String url = "/v1/htDeal/updateSkuIsEnable";

    private String jumei_hash_id;  // 聚美的Deal唯一标识.
    private String jumei_sku_no;   // 聚美的Sku编号
    private int is_enable;         // Number 是否启用.    参数范围: 枚举值：0,1

    @Override
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getJumei_hash_id() {
        return jumei_hash_id;
    }

    public void setJumei_hash_id(String jumei_hash_id) {
        this.jumei_hash_id = jumei_hash_id;
    }

    public String getJumei_sku_no() {
        return jumei_sku_no;
    }

    public void setJumei_sku_no(String jumei_sku_no) {
        this.jumei_sku_no = jumei_sku_no;
    }

    public int getIs_enable() {
        return is_enable;
    }

    public void setIs_enable(int is_enable) {
        this.is_enable = is_enable;
    }

    @Override
    public Map<String, Object> getParameter() throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("jumei_hash_id", jumei_hash_id);
        params.put("jumei_sku_no", jumei_sku_no);
        params.put("is_enable", is_enable);
        return params;
    }

}
