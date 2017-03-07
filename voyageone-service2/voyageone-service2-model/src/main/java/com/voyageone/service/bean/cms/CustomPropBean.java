package com.voyageone.service.bean.cms;

/**
 * Created by Ethan Shi on 2016/6/28.
 *
 * @author Ethan Shi
 * @version 2.2.0
 * @since 2.2.0
 */
public class CustomPropBean {
    private String feedAttrCn;
    private String feedAttrEn;
    private String feedAttrValueCn;
    private String feedAttrValueEn;
    private boolean feedAttr;
    private boolean customPropActive;

    public String getFeedAttrCn() {
        return feedAttrCn;
    }

    public void setFeedAttrCn(String feedAttrCn) {
        this.feedAttrCn = feedAttrCn == null ? null : feedAttrCn.trim();
    }

    public String getFeedAttrEn() {
        return feedAttrEn;
    }

    public void setFeedAttrEn(String feedAttrEn) {
        this.feedAttrEn = feedAttrEn == null ? null : feedAttrEn.trim();
    }

    public String getFeedAttrValueCn() {
        if(feedAttrValueCn == null) feedAttrValueCn="";
        return feedAttrValueCn;
    }

    public void setFeedAttrValueCn(String feedAttrValueCn) {
        this.feedAttrValueCn = feedAttrValueCn == null ? null : feedAttrValueCn.trim();
    }

    public String getFeedAttrValueEn() {
        if(feedAttrValueEn == null) feedAttrValueEn="";
        return feedAttrValueEn;
    }

    public void setFeedAttrValueEn(String feedAttrValueEn) {
        this.feedAttrValueEn = feedAttrValueEn == null ? null : feedAttrValueEn.trim();
    }

    public boolean isFeedAttr() {
        return feedAttr;
    }

    public void setFeedAttr(boolean feedAttr) {
        this.feedAttr = feedAttr;
    }

    public boolean isCustomPropActive() {
        return customPropActive;
    }

    public void setCustomPropActive(boolean customPropActive) {
        this.customPropActive = customPropActive;
    }
}
