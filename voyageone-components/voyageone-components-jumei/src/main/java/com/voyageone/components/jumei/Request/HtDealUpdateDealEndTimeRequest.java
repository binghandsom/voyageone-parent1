package com.voyageone.components.jumei.Request;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
/**
 * Created by dell on 2016/3/29.
 */
public class HtDealUpdateDealEndTimeRequest implements JMRequest {
    public String Url = "/v1/htDeal/updateDealEndTime";
    public String getUrl() {
        return Url;
    }
    String jumei_hash_id;
    public long getEnd_time() {
        return end_time;
    }
    public void setEnd_time(long end_time) {
        this.end_time = end_time;
    }
    long end_time;//Number 售卖结束时间    参数范围: 注:
    public String getJumei_hash_id() {
        return jumei_hash_id;
    }
    public void setJumei_hash_id(String jumei_hash_id) {
        this.jumei_hash_id = jumei_hash_id;
    }
    public Map<String, Object> getParameter() throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("jumei_hash_id", jumei_hash_id);
        params.put("end_time", Long.toString(end_time));
        return params;
    }
}
