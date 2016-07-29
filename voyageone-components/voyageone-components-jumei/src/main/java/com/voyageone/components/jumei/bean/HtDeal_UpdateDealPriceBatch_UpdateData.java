package com.voyageone.components.jumei.bean;

import java.math.BigDecimal;

/**
 * Created by dell on 2016/5/24.
 */
public class HtDeal_UpdateDealPriceBatch_UpdateData {
   String jumei_sku_no;//Number  聚美Sku_no;
    String  jumei_hash_id;//	String    聚美Deal唯一值;
    double market_price;// 可选	Float            市场价;    参数范围: 注:市场价和团购价不能同时为空,市场价必须大于等于团购价
    double  deal_price;// 可选	Float       团购价; 参数范围: 注:市场价和团购价不能同时为空,团购价至少大于15元
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
    public double getMarket_price() {
        return market_price;
    }
    public void setMarket_price(double market_price) {
        this.market_price = market_price;
    }
    public double getDeal_price() {
        return deal_price;
    }
    public void setDeal_price(double deal_price) {
        this.deal_price = deal_price;
    }
}
