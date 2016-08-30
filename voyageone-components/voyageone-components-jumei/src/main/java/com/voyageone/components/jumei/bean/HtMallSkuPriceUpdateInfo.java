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

    private String jumeiSkuNo; // 聚美Sku_no
    private double marketPrice; // 市场价
    private double mallPrice; // 商城价

    public String getJumeiSkuNo() {
        return jumeiSkuNo;
    }

    public void setJumeiSkuNo(String jumeiSkuNo) {
        this.jumeiSkuNo = jumeiSkuNo;
    }

    public double getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(double marketPrice) {
        this.marketPrice = marketPrice;
    }

    public double getMallPrice() {
        return mallPrice;
    }

    public void setMallPrice(double mallPrice) {
        this.mallPrice = mallPrice;
    }
}
