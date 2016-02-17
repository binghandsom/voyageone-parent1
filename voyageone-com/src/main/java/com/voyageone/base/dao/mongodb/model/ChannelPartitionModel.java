package com.voyageone.base.dao.mongodb.model;

/**
 * ChannelPartitionModel
 * @author chuanyu.liang, 12/11/15
 * @version 2.0.0
 * @since 2.0.0
 */
public class ChannelPartitionModel extends BaseMongoModel {

    protected String channelId = null;

    public ChannelPartitionModel() {
    }

    public ChannelPartitionModel(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

}
