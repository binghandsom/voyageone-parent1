package com.voyageone.service.bean.cms.jumei;
/**
 * Created by dell on 2016/5/27.
 */
public class ProductImportBean {
    String productCode;
    long appId;
    long pcId;
    int limit;
    String promotionTag;
    String errorMsg;
    Double discount;
//    double maxMsrpUsd; //海外官网价格;
//    double maxMsrpRmb; //中国官网价格
//    double maxRetailPrice; //中国指导价格
//    double maxSalePrice; //中国最终售价
//
//    double minMsrpUsd; //海外官网价格;
//    double minMsrpRmb; //中国官网价格
//    double minRetailPrice; //中国指导价格
//    double minSalePrice; //中国最终售价
    public long getAppId() {
        return appId;
    }

    public void setAppId(long appId) {
        this.appId = appId;
    }

    public long getPcId() {
        return pcId;
    }

    public void setPcId(long pcId) {
        this.pcId = pcId;
    }



    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getPromotionTag() {
        return promotionTag;
    }

    public void setPromotionTag(String promotionTag) {
        this.promotionTag = promotionTag;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }
}
