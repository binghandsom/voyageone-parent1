package com.voyageone.components.jumei.bean;

/**
 * Created by dell on 2016/5/24.
 */
public class HtDeal_UpdateDealStockBatch_UpdateData {
   String jumei_sku_no;//Number  聚美Sku_no;
    int stock;//库存  参数范围: 大于等于0的整数

    public String getJumei_sku_no() {
        return jumei_sku_no;
    }

    public void setJumei_sku_no(String jumei_sku_no) {
        this.jumei_sku_no = jumei_sku_no;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
