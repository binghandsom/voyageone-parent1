package com.voyageone.service.model.jumei.businessmodel;
import com.voyageone.service.model.jumei.CmsBtJmProductModel;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
public class CmsBtJmImportProduct extends CmsBtJmProductModel {
    String appId;
    String pcId;
    int limit;
    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getPcId() {
        return pcId;
    }

    public void setPcId(String pcId) {
        this.pcId = pcId;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}