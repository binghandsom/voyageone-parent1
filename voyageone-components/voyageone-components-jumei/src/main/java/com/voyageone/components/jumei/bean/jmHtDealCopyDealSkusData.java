package com.voyageone.components.jumei.bean;

import java.math.BigDecimal;

/**
 * Created by dell on 2016/5/24.
 */
public class jmHtDealCopyDealSkusData {

    // 聚美sku_no
    Integer sku_no;

    // 库存
    Integer stocks;

    // deal价格
    BigDecimal deal_price;

    // 市场价格
    BigDecimal market_price;

    public Integer getStocks() {
        return stocks;
    }

    public void setStocks(Integer stocks) {
        this.stocks = stocks;
    }

    public Integer getSku_no() {
        return sku_no;
    }

    public void setSku_no(Integer sku_no) {
        this.sku_no = sku_no;
    }

    public BigDecimal getDeal_price() {
        return deal_price;
    }

    public void setDeal_price(BigDecimal deal_price) {
        this.deal_price = deal_price;
    }

    public BigDecimal getMarket_price() {
        return market_price;
    }

    public void setMarket_price(BigDecimal market_price) {
        this.market_price = market_price;
    }
}
