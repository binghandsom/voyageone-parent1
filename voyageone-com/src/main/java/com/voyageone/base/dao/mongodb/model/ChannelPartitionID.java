package com.voyageone.base.dao.mongodb.model;

public class ChannelPartitionID extends BaseMongoModel{

    protected String channelID;

    public ChannelPartitionID(String channelID) {
        this.channelID = channelID;
    }

    public String getChannelID() {
        return channelID;
    }
}
