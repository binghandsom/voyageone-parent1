package com.voyageone.service.bean.cms.jumei;

/**
 * Created by dell on 2016/5/27.
 */
public class SkuImportBean {
    String productCode;
    String skuCode;
    double dealPrice;
    double  marketPrice;
//    double msrpUsd; //海外官网价格;
//    double msrpRmb; //中国官网价格
//    double retailPrice; //中国指导价格
//    double salePrice; //中国最终售价
    Double discount;
    String errorMsg;//错误消息
//    public double getSalePrice() {
//        return salePrice;
//    }
//
//    public void setSalePrice(double salePrice) {
//        this.salePrice = salePrice;
//    }
//
//    public double getRetailPrice() {
//        return retailPrice;
//    }
//
//    public void setRetailPrice(double retailPrice) {
//        this.retailPrice = retailPrice;
//    }
//
//    public double getMsrpRmb() {
//        return msrpRmb;
//    }
//
//    public void setMsrpRmb(double msrpRmb) {
//        this.msrpRmb = msrpRmb;
//    }
//
//    public double getMsrpUsd() {
//        return msrpUsd;
//    }
//
//    public void setMsrpUsd(double msrpUsd) {
//        this.msrpUsd = msrpUsd;
//    }


    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

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

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }
    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
