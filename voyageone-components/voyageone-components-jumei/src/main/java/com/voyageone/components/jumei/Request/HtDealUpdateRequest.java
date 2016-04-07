package com.voyageone.components.jumei.Request;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.components.jumei.Bean.HtDealUpdate_DealInfo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dell on 2016/3/29.
 */
public class HtDealUpdateRequest implements JMRequest {
    public String Url = "/v1/htDeal/update";

    public String getUrl() {
        return Url;
    }

    String jumei_hash_id;
    HtDealUpdate_DealInfo update_data;

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

    public Map<String, Object> getParameter() throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("jumei_hash_id", jumei_hash_id);
        params.put("update_data", JacksonUtil.bean2JsonNotNull(update_data));
        return params;
    }
}
