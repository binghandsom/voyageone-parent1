package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;

public class CmsBtChannelCategoryModel extends BaseModel {

    /**
     * Channel ID
     */
    private String channelId;


    /**
     * 一级Category ID
     */
    private String categoryId;


    /**
     * Channel ID
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
     * 一级Category ID
     */
    public String getCategoryId() {

        return this.categoryId;
    }

    public void setCategoryId(String categoryId) {
        if (categoryId != null) {
            this.categoryId = categoryId;
        } else {
            this.categoryId = "";
        }

    }

}