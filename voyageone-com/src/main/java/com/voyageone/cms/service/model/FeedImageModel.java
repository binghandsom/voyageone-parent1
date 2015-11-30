package com.voyageone.cms.service.model;

import com.voyageone.base.dao.mongodb.model.BaseMongoModel;
import com.voyageone.base.dao.mongodb.model.ChannelPartitionModel;

/**
 * Created by james.li on 2015/11/30.
 */
public class FeedImageModel extends BaseMongoModel {
    private String channelId;
    private String url;
    private int status = 0;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }
}
