package com.voyageone.components.jumei.request;

import java.util.HashMap;
import java.util.Map;

/**
 * HtMallUpdateSkuForMallRequest 编辑商城的sku[MALL]
 *
 * @author desmond on 2016/9/23
 * @version 2.6.0
 */
public class HtMallUpdateSkuForMallRequest implements BaseJMRequest {
    private String url = "/v1/htSku/updateSkuForMall";

    // 聚美Sku_No(必须)
    private String jumei_sku_no;

    // 是否启用，enabled-是，disabled-否
    private String status;

    // 海关备案商品编码 参数范围: 注:获取仓库接口返回bonded_area_id字段 大于０表示保税区仓库
    private String customs_product_number;

    // 商家商品编码  参数范围: 注:确保唯一
    private String businessman_num;

    @Override
    public String getUrl() {
        return url;
    }

    public String getJumei_sku_no() {
        return jumei_sku_no;
    }

    public void setJumei_sku_no(String jumei_sku_no) {
        this.jumei_sku_no = jumei_sku_no;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
    public Map<String, Object> getParameter() {
        Map<String, Object> params = new HashMap<>();
        params.put("jumei_sku_no", jumei_sku_no);
        params.put("status", status);
        params.put("customs_product_number", customs_product_number);
        params.put("businessman_num", businessman_num);
        return params;
    }
}
