package com.voyageone.service.bean.cms.jumei2;

import java.util.List;

public class UpdatePriceParameterBean {
    //价格值类型 1：价格 0：折扣
    int priceValueType;
    //价格类型  0：市场价 1：团购价
    int priceType;
    double discount;
    double price;
    List<Long> listPromotionProductId;


    public int getPriceValueType() {
        return priceValueType;
    }

    public void setPriceValueType(int priceValueType) {
        this.priceValueType = priceValueType;
    }

    public int getPriceType() {
        return priceType;
    }

    public void setPriceType(int priceType) {
        this.priceType = priceType;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<Long> getListPromotionProductId() {
        return listPromotionProductId;
    }

    public void setListPromotionProductId(List<Long> listPromotionProductId) {
        this.listPromotionProductId = listPromotionProductId;
    }
}
