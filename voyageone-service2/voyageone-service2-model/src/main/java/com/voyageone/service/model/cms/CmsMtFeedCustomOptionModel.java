package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;

public class CmsMtFeedCustomOptionModel extends BaseModel {

    /**

     */
    private String channelId;
    /**
     * 如果该店是渠道级属性一致，那么就设置为0
     */
    private int propId;
    /**

     */
    private String feedValueOriginal;
    /**

     */
    private String feedValueTranslation;


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
    public int getPropId() {

        return this.propId;
    }

    public void setPropId(int propId) {
        this.propId = propId;
    }


    /**

     */
    public String getFeedValueOriginal() {

        return this.feedValueOriginal;
    }

    public void setFeedValueOriginal(String feedValueOriginal) {
        if (feedValueOriginal != null) {
            this.feedValueOriginal = feedValueOriginal;
        } else {
            this.feedValueOriginal = "";
        }

    }


    /**

     */
    public String getFeedValueTranslation() {

        return this.feedValueTranslation;
    }

    public void setFeedValueTranslation(String feedValueTranslation) {
        if (feedValueTranslation != null) {
            this.feedValueTranslation = feedValueTranslation;
        } else {
            this.feedValueTranslation = "";
        }

    }


}