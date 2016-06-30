package com.voyageone.service.bean.cms.translation;

/**
 * Created by Ethan Shi on 2016/6/28.
 *
 * @author Ethan Shi
 * @version 2.2.0
 * @since 2.2.0
 */
public class TranslationTaskBean_CustomProps {
    private String feedAttrCn;
    private String feedAttrEn;
    private String feedAttrValueCn;
    private String feedAttrValueEn;
    private boolean isfeedAttr;

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
        return feedAttrValueCn;
    }

    public void setFeedAttrValueCn(String feedAttrValueCn) {
        this.feedAttrValueCn = feedAttrValueCn == null ? null : feedAttrValueCn.trim();
    }

    public String getFeedAttrValueEn() {
        return feedAttrValueEn;
    }

    public void setFeedAttrValueEn(String feedAttrValueEn) {
        this.feedAttrValueEn = feedAttrValueEn == null ? null : feedAttrValueEn.trim();
    }

    public boolean isfeedAttr() {
        return isfeedAttr;
    }

    public void setIsfeedAttr(boolean isfeedAttr) {
        this.isfeedAttr = isfeedAttr;
    }
}
