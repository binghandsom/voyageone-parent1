package com.voyageone.components.jumei.Request;

import com.voyageone.common.util.JacksonUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dell on 2016/3/29.
 */
public class HtSkuUpdateRequest implements JMRequest {
    public String Url = "/v1/htSku/update";

    public String getUrl() {
        return Url;
    }

    String jumei_sku_id;    //Number;//聚美Sku Id.
    //update_data	Json修改数据.参数范围: 只传需要修改的字段
    int customs_product_number;// 可选	Number 海关备案商品编码 //参数范围: 注:(发货仓库为保税区仓库时，此处必填) 获取仓库接口返回bonded_area_id字段 大于０表示保税区仓库

    int businessman_num;  //可选	Number  商家商品编码 参数范围: 注:确保唯一
    public void setUrl(String url) {
        Url = url;
    }

    public String getJumei_sku_id() {
        return jumei_sku_id;
    }

    public void setJumei_sku_id(String jumei_sku_id) {
        this.jumei_sku_id = jumei_sku_id;
    }

    public int getCustoms_product_number() {
        return customs_product_number;
    }

    public void setCustoms_product_number(int customs_product_number) {
        this.customs_product_number = customs_product_number;
    }

    public int getBusinessman_num() {
        return businessman_num;
    }

    public void setBusinessman_num(int businessman_num) {
        this.businessman_num = businessman_num;
    }
    public Map<String, Object> getParameter() throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("jumei_sku_id", this.getJumei_sku_id());
        Map<String, Object> mapUpdateData = new HashMap<>();
        mapUpdateData.put("customs_product_number", this.getCustoms_product_number());
        mapUpdateData.put("businessman_num", this.getBusinessman_num());
        params.put("update_data", JacksonUtil.bean2JsonNotNull(mapUpdateData));
        return params;
    }
}
