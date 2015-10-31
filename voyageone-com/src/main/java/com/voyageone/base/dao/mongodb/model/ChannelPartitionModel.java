package com.voyageone.base.dao.mongodb.model;

/**
 * Created by DELL on 2015/10/27.
 */
public class ChannelPartitionModel extends BaseMongoModel {

    protected String channel_id = null;
    public String getChannel_id() {
        return channel_id;
    }
    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public String getPartitionName() {
        String result = "";
        if (channel_id != null) {
            result = "_c" + channel_id;
        }
        return result;
    }
}
