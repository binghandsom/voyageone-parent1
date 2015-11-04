package com.voyageone.base.dao.mongodb.model;

public abstract class ChannelPartitionModel extends BaseMongoModel {

    protected String channel_id = null;
    public String getChannel_id() {
        return channel_id;
    }
    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    protected String getPartitionValue() {
        return getPartitionValue(channel_id);
    }

    public static String getPartitionValue(String channel_id) {
        String result = "";
        if (channel_id != null) {
            result = "_c" + channel_id;
        }
        return result;
    }

    public abstract String getCollectionName();
}
