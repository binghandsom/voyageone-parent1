package com.voyageone.components.jumei.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.voyageone.common.util.JacksonUtil;

/**
 * 聚美商城 商城商品追加sku[MALL]
 *
 * @author morse on 2016/8/29
 * @version 2.5.0
 */
public class HtMallSkuAddInfo {

    private String jumeiSpuNo;  // 聚美Spu_No

    private SkuInfo skuInfo;

    public class SkuInfo {
        private String customs_product_number; // 海关备案商品编码
        private String businessman_num; // 商家商品编码
        private int stocks; // 库存
        private double mall_price; // 商城价
        private double market_price; // 市场价

        private SkuInfo(){}

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

        public int getStocks() {
            return stocks;
        }

        public void setStocks(int stocks) {
            this.stocks = stocks;
        }

        public double getMall_price() {
            return mall_price;
        }

        public void setMall_price(double mall_price) {
            this.mall_price = mall_price;
        }

        public double getMarket_price() {
            return market_price;
        }

        public void setMarket_price(double market_price) {
            this.market_price = market_price;
        }
    }

    public String getJumeiSpuNo() {
        return jumeiSpuNo;
    }

    public void setJumeiSpuNo(String jumeiSpuNo) {
        this.jumeiSpuNo = jumeiSpuNo;
    }

    public SkuInfo getSkuInfo() {
        if (skuInfo == null) {
            skuInfo = new SkuInfo();
        }
        return skuInfo;
    }

    public void setSkuInfo(SkuInfo skuInfo) {
        this.skuInfo = skuInfo;
    }

    @JsonIgnore
    public String getSkuInfoString() {
        return JacksonUtil.bean2JsonNotNull(skuInfo);
    }

}
