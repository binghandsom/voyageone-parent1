package com.voyageone.service.bean.cms.jumei2;

/**
 * Created by dell on 2016/5/31.
 */
 public class SkuPriceBean {
    private double dealPrice;

    private double marketPrice;

    private String jmSkuNo;

    public double getDealPrice() {
        return dealPrice;
    }
    public void setDealPrice(double dealPrice) {
        this.dealPrice = dealPrice;
    }
    public double getMarketPrice() {
        return marketPrice;
    }
    public void setMarketPrice(double marketPrice) {
        this.marketPrice = marketPrice;
    }
    public String getJmSkuNo() {
        return jmSkuNo;
    }
    public void setJmSkuNo(String jmSkuNo) {
        this.jmSkuNo = jmSkuNo;
    }
}
