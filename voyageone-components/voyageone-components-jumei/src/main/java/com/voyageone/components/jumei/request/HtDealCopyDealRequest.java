package com.voyageone.components.jumei.request;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * HtDealCopyDealRequest
 *
 * @author peitao.sun, 2016/3/29
 * @version 2.0.0
 * @since 2.0.0
 */
public class HtDealCopyDealRequest implements BaseJMRequest {
    private String url = "/v1/htDeal/copyDeal";
    private String jumei_hash_id;
    private long start_time;//	Number 售卖开始时间    参数范围: 注:
    private long end_time;//Number 售卖结束时间    参数范围: 注:

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

    @Override
    public Map<String, Object> getParameter() throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("jumei_hash_id", jumei_hash_id);
        params.put("start_time", Long.toString(start_time));
        params.put("end_time", Long.toString(end_time));
        return params;
    }
}
