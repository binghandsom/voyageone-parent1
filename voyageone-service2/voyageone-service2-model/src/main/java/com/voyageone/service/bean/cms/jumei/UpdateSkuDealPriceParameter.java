package com.voyageone.service.bean.cms.jumei;

/**
 * Created by dell on 2016/6/3.
 */
public class UpdateSkuDealPriceParameter {
    int promotionSkuId;
    double dealPrice;
    double marketPrice;

    public double getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(double marketPrice) {
        this.marketPrice = marketPrice;
    }

    public double getDealPrice() {
        return dealPrice;
    }
    public void setDealPrice(double dealPrice) {
        this.dealPrice = dealPrice;
    }
    public int getPromotionSkuId() {
        return promotionSkuId;
    }
    public void setPromotionSkuId(int promotionSkuId) {
        this.promotionSkuId = promotionSkuId;
    }
}
