package com.voyageone.service.bean.cms.businessmodel;
import com.voyageone.service.model.cms.CmsBtJmProductModel;

public class CmsBtJmImportProduct extends CmsBtJmProductModel {
    long appId;
    long pcId;
    int limit;

    public long getAppId() {
        return appId;
    }

    public void setAppId(long appId) {
        this.appId = appId;
    }

    public long getPcId() {
        return pcId;
    }

    public void setPcId(int pcId) {
        this.pcId = pcId;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}