package com.voyageone.ims.model;

/**
 * ims_bt_activity_beat_icon
 *
 * Created by Jonas on 6/30/15.
 */
public class ImsBeat {

    private long beat_id;

    private String description;

    private String channel_id;

    private int cart_id;

    private String end;

    private String template_url;

    private String targets;

    private int[] targetIndexes;

    private String created;

    private String creater;

    private String modified;

    private String modifier;

    public long getBeat_id() {
        return beat_id;
    }

    public void setBeat_id(long beat_id) {
        this.beat_id = beat_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public int getCart_id() {
        return cart_id;
    }

    public void setCart_id(int cart_id) {
        this.cart_id = cart_id;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getTemplate_url() {
        return template_url;
    }

    public void setTemplate_url(String template_url) {
        this.template_url = template_url;
    }

    public String getTargets() {
        return targets;
    }

    public void setTargets(String targets) {
        this.targets = targets;
    }

    public int[] getTargetIndexes() {
        return targetIndexes;
    }

    public void setTargetIndexes(int[] targetIndexes) {
        this.targetIndexes = targetIndexes;
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
}