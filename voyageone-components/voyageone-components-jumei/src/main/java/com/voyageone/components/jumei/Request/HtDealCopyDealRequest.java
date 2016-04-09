package com.voyageone.components.jumei.Request;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.components.jumei.bean.HtDealCopyDeal_DealInfo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dell on 2016/3/29.
 */
public class HtDealCopyDealRequest implements JMRequest {
    public String Url = "/v1/htDeal/copyDeal";

    public String getUrl() {
        return Url;
    }

    String jumei_hash_id;
    public long getStart_time() {
        return start_time;
    }

    public void setStart_time(long start_time) {
        this.start_time = start_time;
    }

    public long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(long end_time) {
        this.end_time = end_time;
    }

    long start_time;//	Number 售卖开始时间    参数范围: 注:
    long end_time;//Number 售卖结束时间    参数范围: 注:
    HtDealCopyDeal_DealInfo update_data;

    public String getJumei_hash_id() {
        return jumei_hash_id;
    }

    public void setJumei_hash_id(String jumei_hash_id) {
        this.jumei_hash_id = jumei_hash_id;
    }

    public HtDealCopyDeal_DealInfo getUpdate_data() {
        return update_data;
    }

    public void setUpdate_data(HtDealCopyDeal_DealInfo update_data) {
        this.update_data = update_data;
    }

    public Map<String, Object> getParameter() throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("jumei_hash_id", jumei_hash_id);
        params.put("start_time",Long.toString(start_time));
        params.put("end_time", Long.toString(end_time));
        params.put("update_data", JacksonUtil.bean2JsonNotNull(update_data));
        return params;
    }
}
