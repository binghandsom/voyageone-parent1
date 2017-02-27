package com.voyageone.components.jumei.bean;

/**
 * Created by dell on 2016/5/24.
 */
public class jmHtDealCopyDealSkusData {

    // 聚美sku_no
    String sku_no;

    // 库存
    String stocks;

    // deal价格
    String deal_price;

    // 市场价格
    String market_price;

    public String getStocks() {
        return stocks;
    }

    public void setStocks(String stocks) {
        this.stocks = stocks;
    }

    public String getSku_no() {
        return sku_no;
    }

    public void setSku_no(String sku_no) {
        this.sku_no = sku_no;
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
}
