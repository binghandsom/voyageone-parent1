package com.voyageone.components.jumei.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.voyageone.common.util.JacksonUtil;

/**
 * 聚美商城 批量修改商城价格[MALL]
 *
 * @author morse on 2016/8/29
 * @version 2.5.0
 */
public class HtMallSkuPriceUpdateInfo {

    private String jumei_sku_no; // 聚美Sku_no
    private Double market_price; // 市场价
    private Double mall_price; // 商城价

    public String getJumei_sku_no() {
        return jumei_sku_no;
    }

    public void setJumei_sku_no(String jumei_sku_no) {
        this.jumei_sku_no = jumei_sku_no;
    }

    public Double getMarket_price() {
        return market_price;
    }

    public void setMarket_price(Double market_price) {
        this.market_price = market_price;
    }

    public Double getMall_price() {
        return mall_price;
    }

    public void setMall_price(Double mall_price) {
        this.mall_price = mall_price;
    }
}
