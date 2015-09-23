package com.voyageone.batch.ims.modelbean;

/**
 * 对应 ims 的 ims_bt_pic_category 表
 * <p>
 * Created by Jonas on 8/10/15.
 */
public class ImsPicCategory {

    private int category_id;

    private String category_tid;

    private String category_name;

    private int type;

    private String channel_id;

    private int cart_id;

    private String modified;

    private String modifier;

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public int getCart_id() {
        return cart_id;
    }

    public void setCart_id(int cart_id) {
        this.cart_id = cart_id;
    }

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_tid() {
        return category_tid;
    }

    public void setCategory_tid(String category_tid) {
        this.category_tid = category_tid;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }
}
