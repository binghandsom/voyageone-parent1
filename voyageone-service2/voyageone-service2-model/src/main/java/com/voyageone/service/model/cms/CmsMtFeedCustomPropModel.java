package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;

public class CmsMtFeedCustomPropModel extends BaseModel {

    private String channelId;


    /**
     * 如果该店是渠道级属性一致，那么就设置为0
     */
    private String feedCatPath;


    /**

     */
    private String feedPropOriginal;


    /**

     */
    private String feedPropTranslation;


    /**

     */
    private int displayOrder;


    /**

     */
    public String getChannelId() {

        return this.channelId;
    }

    public void setChannelId(String channelId) {
        if (channelId != null) {
            this.channelId = channelId;
        } else {
            this.channelId = "";
        }

    }


    /**
     * 如果该店是渠道级属性一致，那么就设置为0
     */
    public String getFeedCatPath() {

        return this.feedCatPath;
    }

    public void setFeedCatPath(String feedCatPath) {
        if (feedCatPath != null) {
            this.feedCatPath = feedCatPath;
        } else {
            this.feedCatPath = "";
        }

    }


    /**

     */
    public String getFeedPropOriginal() {

        return this.feedPropOriginal;
    }

    public void setFeedPropOriginal(String feedPropOriginal) {
        if (feedPropOriginal != null) {
            this.feedPropOriginal = feedPropOriginal;
        } else {
            this.feedPropOriginal = "";
        }

    }


    /**

     */
    public String getFeedPropTranslation() {

        return this.feedPropTranslation;
    }

    public void setFeedPropTranslation(String feedPropTranslation) {
        if (feedPropTranslation != null) {
            this.feedPropTranslation = feedPropTranslation;
        } else {
            this.feedPropTranslation = "";
        }

    }


    /**

     */
    public int getDisplayOrder() {

        return this.displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }


}