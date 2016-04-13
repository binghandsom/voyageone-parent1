package com.voyageone.service.model.cms;

/**
 * Created by Leo on 15-12-7.
 */
public class CmsBtSxWorkloadModel {
    private long seq;
    private String channel_id;
    private Long group_id;
    private int cartId;
    private int publish_status;
    private String created;
    private String creater;
    private String modifier;
    private String modified;


    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

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

    public Long getGroupId() {
        return group_id;
    }

    public void setGroupId(Long groupId) {
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

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public Long getGroup_id() {
        return group_id;
    }

    public void setGroup_id(Long group_id) {
        this.group_id = group_id;
    }

    public long getSeq() {
        return seq;
    }

    public void setSeq(long seq) {
        this.seq = seq;
    }

    public int getPublishStatus() {
        return publish_status;
    }

    public void setPublishStatus(int publishStatus) {
        this.publish_status = publishStatus;
    }
}
