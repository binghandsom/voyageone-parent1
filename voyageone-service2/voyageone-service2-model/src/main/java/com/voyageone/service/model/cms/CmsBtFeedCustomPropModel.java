package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;

/**
 * 对表 voyageone_ims.ims_mt_custom_word 的映射
 *
 * Created by Jonas on 9/11/15.
 */
public class CmsBtFeedCustomPropModel extends BaseModel {

    private Integer id;

    private String channelId;

    private String feedCatPath;

    private String feedProp;

    private String feedPropTranslate;

    private String displayOrder;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getFeedCatPath() {
        return feedCatPath;
    }

    public void setFeedCatPath(String feedCatPath) {
        this.feedCatPath = feedCatPath;
    }

    public String getFeedProp() {
        return feedProp;
    }

    public void setFeedProp(String feedProp) {
        this.feedProp = feedProp;
    }

    public String getFeedPropTranslate() {
        return feedPropTranslate;
    }

    public void setFeedPropTranslate(String feedPropTranslate) {
        this.feedPropTranslate = feedPropTranslate;
    }

    public String getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(String displayOrder) {
        this.displayOrder = displayOrder;
    }
}
