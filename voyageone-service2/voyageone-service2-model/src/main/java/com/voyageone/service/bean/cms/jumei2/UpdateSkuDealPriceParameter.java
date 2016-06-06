package com.voyageone.service.bean.cms.jumei2;

/**
 * Created by dell on 2016/6/3.
 */
public class UpdateSkuDealPriceParameter {
    int promotionSkuId;
    double dealPrice;

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
