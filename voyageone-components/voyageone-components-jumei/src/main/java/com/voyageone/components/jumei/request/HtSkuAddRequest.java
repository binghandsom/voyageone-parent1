package com.voyageone.components.jumei.request;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * HtSkuAddRequest
 *
 * @author peitao.sun, 2016/3/29
 * @version 2.0.0
 * @since 2.0.0
 */
public class HtSkuAddRequest implements BaseJMRequest {
    private String url = "/v1/htSku/add";

    // 聚美Spu_No;
    private String jumei_spu_no;
    // 海关备案商品编码
    //  参数范围: 注:(发货仓库为保税区仓库时，此处必填) 获取仓库接口　增加返回bonded_area_id字段 大于０　表示　保税
    private String customs_product_number;
    // 商家商品编码 参数范围: 注:确保唯一
    private String businessman_num;
    // 库存     参数范围: 注：填写可供售卖的真实库存，无货超卖可能引起投诉与退款。无库存填写0
    private String stocks;
    // 团购价 参数范围: 注：至少大于15元
    private String deal_price;
    // 市场价  参数范围: 注：必须大于等于团购价
    private String market_price;

    @Override
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getJumei_spu_no() {
        return jumei_spu_no;
    }

    public void setJumei_spu_no(String jumei_spu_no) {
        this.jumei_spu_no = jumei_spu_no;
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

    public String getStocks() {
        return stocks;
    }

    public void setStocks(String stocks) {
        this.stocks = stocks;
    }

    public String getDeal_price() {
        return deal_price;
    }

    public void setDeal_price(String deal_price) {
        this.deal_price = deal_price;
    }

    public String getMarket_price() {
        return market_price;
    }

    public void setMarket_price(String market_price) {
        this.market_price = market_price;
    }

    @Override
    public Map<String, Object> getParameter() throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("jumei_spu_no", this.getJumei_spu_no());
        params.put("customs_product_number", this.getCustoms_product_number());
        params.put("businessman_num", this.getBusinessman_num());
        params.put("stocks", this.getStocks());
        params.put("deal_price", this.getDeal_price());
        params.put("market_price", this.getMarket_price());
        return params;
    }
}
