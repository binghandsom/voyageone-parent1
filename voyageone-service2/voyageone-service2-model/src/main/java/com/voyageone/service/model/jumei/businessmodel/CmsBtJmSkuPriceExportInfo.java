package com.voyageone.service.model.jumei.businessmodel;

/**
 * Created by dell on 2016/4/9.
 */
public class CmsBtJmSkuPriceExportInfo {

    public String getJmHashId() {
        return jmHashId;
    }

    public void setJmHashId(String jmHashId) {
        this.jmHashId = jmHashId;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getDealPrice() {
        return dealPrice;
    }

    public void setDealPrice(String dealPrice) {
        this.dealPrice = dealPrice;
    }

    public String getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(String marketPrice) {
        this.marketPrice = marketPrice;
    }

    String jmHashId;
    String skuCode;
    String dealPrice;
    String marketPrice;
}
