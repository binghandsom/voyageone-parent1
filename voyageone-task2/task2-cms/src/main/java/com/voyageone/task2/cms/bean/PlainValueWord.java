package com.voyageone.task2.cms.bean;

/**
 * Created by Leo on 15-6-12.
 */
public class PlainValueWord {
    private boolean isUrl;
    private String cmsFieldName;
    private String value;

    public String getCmsFieldName() {
        return cmsFieldName;
    }

    public void setCmsFieldName(String cmsFieldName) {
        this.cmsFieldName = cmsFieldName;
    }

    public boolean isUrl() {
        return isUrl;
    }

    public void setIsUrl(boolean isUrl) {
        this.isUrl = isUrl;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
