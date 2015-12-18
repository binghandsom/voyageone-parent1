package com.voyageone.batch.cms.model;

/**
 * Created by Leo on 15-12-7.
 */
public class SxWorkloadModel {
    private String channel_id;
    private int group_id;
    private int publish_status;
    private String created;
    private String creater;
    private String modifier;
    private String modified;


    public String getChannelId() {
        return channel_id;
    }

    public void setChannelId(String channelId) {
        this.channel_id = channelId;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public int getGroupId() {
        return group_id;
    }

    public void setGroupId(int groupId) {
        this.group_id = groupId;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public int getPublishStatus() {
        return publish_status;
    }

    public void setPublishStatus(int publishStatus) {
        this.publish_status = publishStatus;
    }
}
