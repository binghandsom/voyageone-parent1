package com.voyageone.components.jumei.request;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * HtSkuUpdateRequest
 *
 * @author peitao.sun, 2016/3/29
 * @version 2.0.0
 * @since 2.0.0
 */
public class HtSkuUpdateRequest implements BaseJMRequest {
    private String url = "/v1/htSku/update";

    //聚美Sku_No.
    private String jumei_sku_no;

    //聚美生成的deal 唯一值.
    private String jumei_hash_id;

    //update_data	Json修改数据.参数范围: 只传需要修改的字段
    // 可选	Number 海关备案商品编码 //参数范围: 注:(发货仓库为保税区仓库时，此处必填) 获取仓库接口返回bonded_area_id字段 大于０表示保税区仓库
    private String customs_product_number;

    //可选	Number  商家商品编码 参数范围: 注:确保唯一
    private String businessman_num;

    @Override
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getJumei_sku_no() {
        return jumei_sku_no;
    }

    public void setJumei_sku_no(String jumei_sku_no) {
        this.jumei_sku_no = jumei_sku_no;
    }

    public String getJumei_hash_id() {
        return jumei_hash_id;
    }

    public void setJumei_hash_id(String jumei_hash_id) {
        this.jumei_hash_id = jumei_hash_id;
    }

    public String getCustoms_product_number() {
        return customs_product_number;
    }

    public void setCustoms_product_number(String customs_product_number) {
        this.customs_product_number = customs_product_number;
    }

    public String getBusinessman_num() {
        return businessman_num;
    }

    public void setBusinessman_num(String businessman_num) {
        this.businessman_num = businessman_num;
    }

    @Override
    public Map<String, Object> getParameter() throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("jumei_sku_no", this.getJumei_sku_no());
        params.put("jumei_hash_id", this.getJumei_hash_id());

        Map<String, Object> mapUpdateData = new HashMap<>();
        if (!StringUtils.isEmpty(getCustoms_product_number()))  mapUpdateData.put("customs_product_number", this.getCustoms_product_number());
        if (!StringUtils.isEmpty(getBusinessman_num()))         mapUpdateData.put("businessman_num", this.getBusinessman_num());
        params.put("update_data", JacksonUtil.bean2JsonNotNull(mapUpdateData));
        return params;
    }
}
