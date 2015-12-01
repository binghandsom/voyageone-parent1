package com.voyageone.base.dao.mongodb.model;

public class ChannelPartitionModel extends BaseMongoModel {

    protected String channelId = null;

    public String getChannelId() {
        return channelId;
    }
    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public ChannelPartitionModel() {
    }

    public ChannelPartitionModel(String channelId) {
        this.channelId = channelId;
    }

}
