package com.voyageone.task2.cms.model;

/**
 * Created by jonasvlag on 16/3/7.
 *
 * @version 2.0.0
 * @since 2.0.0
 */
public class CmsMtImageCategoryModel {

    private int category_id;

    private String category_tid;

    private String category_name;

    private int type;

    private String channel_id;

    private int cart_id;

    private String created;

    private String creater;

    private String modified;

    private String modifier;

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getCategory_tid() {
        return category_tid;
    }

    public void setCategory_tid(String category_tid) {
        this.category_tid = category_tid;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
