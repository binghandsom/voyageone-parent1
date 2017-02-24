package com.voyageone.components.jumei.request;

import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.components.jumei.bean.jmHtDealCopyDealSkusData;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
    private Date start_time;//	Number 售卖开始时间    参数范围: 注:
    private Date end_time;//Number 售卖结束时间    参数范围: 注:
    private List<jmHtDealCopyDealSkusData> skus_data;

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

    public Date getStart_time() {
        return start_time;
    }

    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }

    public Date getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }

    public List<jmHtDealCopyDealSkusData> getSkus_data() {
        return skus_data;
    }

    public void setSkus_data(List<jmHtDealCopyDealSkusData> skus_data) {
        this.skus_data = skus_data;
    }

    @Override
    public Map<String, Object> getParameter() throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("jumei_hash_id", jumei_hash_id);
        params.put("start_time", DateTimeUtil.format(start_time,"yyyy-MM-dd HH:mm:ss"));
        params.put("end_time", DateTimeUtil.format(end_time,"yyyy-MM-dd HH:mm:ss"));
        params.put("skus_data", skus_data);
        return params;
    }

    public static Long getTime(Date d) throws Exception {
        long l = d.getTime() / 1000 - 8 * 3600;
        return l;
    }
}
