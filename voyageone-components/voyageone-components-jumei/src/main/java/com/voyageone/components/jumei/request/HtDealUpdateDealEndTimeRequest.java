package com.voyageone.components.jumei.request;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * HtDealUpdateDealEndTimeRequest
 *
 * @author peitao.sun, 2016/3/29
 * @version 2.0.0
 * @since 2.0.0
 */
public class HtDealUpdateDealEndTimeRequest implements BaseJMRequest {

    private String url = "/v1/htDeal/updateDealEndTime";
    private String jumei_hash_id;
    private Date end_time;//Number 售卖结束时间    参数范围: 注:

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

    public Date getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }

    @Override
    public Map<String, Object> getParameter() throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("jumei_hash_id", jumei_hash_id);
        params.put("end_time", Long.toString(getTime(end_time)));//DateTimeUtil.format(end_time,"yyyy-MM-dd HH:mm:ss"));//DateTimeUtil.format(end_time,"yyyy-MM-dd HH:mm:ss"));
        return params;
    }

    private static Long getTime(Date d) {
        return d.getTime() / 1000- 8 * 3600;
        //Calendar now = Calendar.getInstance();
        // 取得系统时间和格林威治时间之间的偏移值
       // int diffsecond = now.getTimeZone().getRawOffset();
        // d.getTime() currentTimeMillis
        // Returns the number of milliseconds since January 1, 1970, 00:00:00 GMT represented by this Date object.
        //  DateTimeUtil.getLocalTime(d,8);
       // if (diffsecond == 0) {
           // return d.getTime() / 1000;// - 8 * 3600;
//        } else {
//            return d.getTime() / 1000;
//        }
    }
}
