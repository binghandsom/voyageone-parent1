package com.voyageone.service.bean.cms.businessmodel;
import com.voyageone.service.model.cms.CmsBtJmProductModel;

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