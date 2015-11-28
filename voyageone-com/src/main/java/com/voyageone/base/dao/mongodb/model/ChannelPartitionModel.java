package com.voyageone.base.dao.mongodb.model;

public class ChannelPartitionModel extends BaseMongoModel {
    protected String channelId = null;
    public String getChannelId() {
        return channelId;
    }
    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public ChannelPartitionModel(String channelId) {
        this.channelId = channelId;
    }

    public static String getPartitionValue(String channelId) {
        String result = "";
        if (channelId != null) {
            result = "_c" + channelId;
        }
        return result;
    }
}
