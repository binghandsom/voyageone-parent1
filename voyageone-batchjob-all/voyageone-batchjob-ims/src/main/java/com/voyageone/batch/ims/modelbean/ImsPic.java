package com.voyageone.batch.ims.modelbean;

/**
 * 表 ims_bt_pic
 * <p>
 * Created by Jonas on 8/10/15.
 */
public class ImsPic {

    private int pic_id;

    private String pic_tid;

    private String title;

    private String category_tid;

    private String pic_url;

    private String created;

    private String creater;

    private String modified;

    private String modifier;

    public int getPic_id() {
        return pic_id;
    }

    public void setPic_id(int pic_id) {
        this.pic_id = pic_id;
    }

    public String getPic_tid() {
        return pic_tid;
    }

    public void setPic_tid(String pic_tid) {
        this.pic_tid = pic_tid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory_tid() {
        return category_tid;
    }

    public void setCategory_tid(String category_tid) {
        this.category_tid = category_tid;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
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
