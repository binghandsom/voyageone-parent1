package com.voyageone.common.configs.beans;

/**
 * @author aooer 2016/4/5.
 * @version 2.0.0
 * @since 2.0.0
 */
public class UsJoiBean {

    private int id;
    private String sub_channel_id;
    private String name;
    private int active;
    private String cart_ids;
    private String created;
    private String creater;
    private String modified;
    private String modifier;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSub_channel_id() {
        return sub_channel_id;
    }

    public void setSub_channel_id(String sub_channel_id) {
        this.sub_channel_id = sub_channel_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
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

    public String getCart_ids() {
        return cart_ids;
    }

    public void setCart_ids(String cart_ids) {
        this.cart_ids = cart_ids;
    }
}
