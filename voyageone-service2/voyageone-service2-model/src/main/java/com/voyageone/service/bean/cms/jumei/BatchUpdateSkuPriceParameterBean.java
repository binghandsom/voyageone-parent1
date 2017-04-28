package com.voyageone.service.bean.cms.jumei;

import java.util.List;
import java.util.Map;

public class BatchUpdateSkuPriceParameterBean {
    //价格类型    1 建议售价   2指导售价  3最终售价  4固定售价
    int priceTypeId;

    // 1小数点向上取整    2个位向下取整    3个位向上取整    4无特殊处理
    int roundType;

    // 1.商品内，SKU统一最高价    2.商品内，SKU统一最低价    3.商品内，SKU价格不统一
    int skuUpdType;
     // + -  *  =
    String optType;
    //price value
    double priceValue;
    List<Integer> listPromotionProductId;
    int jmPromotionId;
    private Map<String, Object> searchInfo;
    private boolean selAll;
    public int getPriceTypeId() {
        return priceTypeId;
    }

    public void setPriceTypeId(int priceTypeId) {
        this.priceTypeId = priceTypeId;
    }

    public int getRoundType() {
        return roundType;
    }

    public void setRoundType(int roundType) {
        this.roundType = roundType;
    }

    public int getSkuUpdType() {
        return skuUpdType;
    }

    public void setSkuUpdType(int skuUpdType) {
        this.skuUpdType = skuUpdType;
    }

    public String getOptType() {
        return optType;
    }

    public void setOptType(String optType) {
        this.optType = optType;
    }

    public double getPriceValue() {
        return priceValue;
    }

    public void setPriceValue(double priceValue) {
        this.priceValue = priceValue;
    }

    public List<Integer> getListPromotionProductId() {
        return listPromotionProductId;
    }

    public void setListPromotionProductId(List<Integer> listPromotionProductId) {
        this.listPromotionProductId = listPromotionProductId;
    }

    public int getJmPromotionId() {
        return jmPromotionId;
    }

    public void setJmPromotionId(int jmPromotionId) {
        this.jmPromotionId = jmPromotionId;
    }

    public Map<String, Object> getSearchInfo() {
        return searchInfo;
    }

    public void setSearchInfo(Map<String, Object> searchInfo) {
        this.searchInfo = searchInfo;
    }

    public boolean isSelAll() {
        return selAll;
    }

    public void setSelAll(boolean selAll) {
        this.selAll = selAll;
    }
}
