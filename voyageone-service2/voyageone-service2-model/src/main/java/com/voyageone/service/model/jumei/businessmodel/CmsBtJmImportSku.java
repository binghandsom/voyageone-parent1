package com.voyageone.service.model.jumei.businessmodel;


import com.voyageone.service.model.jumei.CmsBtJmSkuModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class CmsBtJmImportSku extends CmsBtJmSkuModel {
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
}