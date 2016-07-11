package com.voyageone.components.jumei.request;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.components.jumei.bean.HtDealUpdate_DealInfo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * HtDealUpdateRequest
 *
 * @author peitao.sun, 2016/3/29
 * @version 2.0.0
 * @since 2.0.0JumeiProductService
 */
public class HtDealUpdateRequest implements BaseJMRequest {

    private String url = "/v1/htDeal/update";
    private String jumei_hash_id;
    private HtDealUpdate_DealInfo update_data;

    @Override
    public String getUrl() {
        return url;
    }

    public String getJumei_hash_id() {
        return jumei_hash_id;
    }

    public void setJumei_hash_id(String jumei_hash_id) {
        this.jumei_hash_id = jumei_hash_id;
    }

    public HtDealUpdate_DealInfo getUpdate_data() {
        return update_data;
    }

    public void setUpdate_data(HtDealUpdate_DealInfo update_data) {
        this.update_data = update_data;
    }

    @Override
    public Map<String, Object> getParameter() throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("jumei_hash_id", jumei_hash_id);
        params.put("update_data", JacksonUtil.bean2JsonNotNull(update_data));
        return params;
    }
}
