package com.voyageone.components.jumei.request;

import com.voyageone.common.util.StringUtils;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dell on 2016/7/7.
 */
public class HtDealGetDealByHashIDRequest implements BaseJMRequest {

    private String url = "/v1/htDeal/getDealByHashID";
    private String jumei_hash_id;
    /**
     *  需要查询的字段.；可选字段见“返回字段”列表
     *  默认值: product_id,categorys,brand_id,brand_name,name,foreign_language_name
     *  参数范围: 多个参数以","隔开，不存在的字段将被忽略
     * */
    private String fields;
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

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
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
        if (!StringUtils.isEmpty(fields)) {
            params.put("fields", fields);
        } else {
            params.put("fields","start_time,end_time,deal_status,product_id");
        }

        return params;
    }
}