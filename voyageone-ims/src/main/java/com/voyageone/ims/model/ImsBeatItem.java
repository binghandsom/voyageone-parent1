package com.voyageone.ims.model;

import com.voyageone.ims.service.impl.BeatFlg;

import static com.voyageone.common.Constants.EmptyString;

/**
 * 对应 ims_task_beat_item 表
 * <p>
 * Created by Jonas on 6/30/15.
 */
public class ImsBeatItem {

    private long beat_item_id;

    private long beat_id;

    private String code;

    private String num_iid;

    private String url_key;

    private String image_name;

    private String price;

    private BeatFlg beat_flg;

    private String comment;

    private String created;

    private String creater;

    private String modified;

    private String modifier;

    public long getBeat_item_id() {
        return beat_item_id;
    }

    public void setBeat_item_id(long beat_item_id) {
        this.beat_item_id = beat_item_id;
    }

    public long getBeat_id() {
        return beat_id;
    }

    public void setBeat_id(long beat_id) {
        this.beat_id = beat_id;
    }

    public String getCode() {
        return code == null ? EmptyString : code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNum_iid() {
        return num_iid == null ? EmptyString : num_iid;
    }

    public void setNum_iid(String num_iid) {
        this.num_iid = num_iid;
    }

    public String getUrl_key() {
        return url_key;
    }

    public void setUrl_key(String url_key) {
        this.url_key = url_key;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    public String getPrice() {
        return price == null ? EmptyString : price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getBeat_flg() {
        return beat_flg.value();
    }

    public void setBeat_flg(int beat_flg) {
        this.beat_flg = BeatFlg.valueOf(beat_flg);
    }

    public BeatFlg getBeatFlg() {
        return this.beat_flg;
    }

    public void setBeatFlg(BeatFlg beat_flg) {
        this.beat_flg = beat_flg;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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