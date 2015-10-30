package com.voyageone.batch.ims.bean;

import com.voyageone.batch.ims.enums.BeatFlg;
import org.apache.commons.lang3.StringUtils;

/**
 * 汇集 beat 和 beatItem 表的信息
 * Created by sky on 20150814
 */
public class BeatPicBean {

    private String channel_id;

    private int cart_id;

    private String code;

    private String targets;

    private long num_iid;

    private String price;

    private String temp_url;

    private BeatFlg beat_flg;

    private String url_key;

    private String modified;

    private String modifier;

    private String end;

    private int beat_item_id;

    private String comment;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        if (this.comment == null)
            this.comment = comment;
        else if (!StringUtils.isEmpty(comment))
            this.comment += ";" + comment;
    }

    public int getBeat_item_id() {
        return beat_item_id;
    }

    public void setBeat_item_id(int beat_item_id) {
        this.beat_item_id = beat_item_id;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getNum_iid() {
        return num_iid;
    }

    public void setNum_iid(long num_iid) {
        this.num_iid = num_iid;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTemp_url() {
        return temp_url;
    }

    public void setTemp_url(String temp_url) {
        this.temp_url = temp_url;
    }

    public BeatFlg getBeat_flg() {
        return beat_flg;
    }

    public void setBeat_flg(BeatFlg beat_flg) {
        this.beat_flg = beat_flg;
    }

    public void setDbBeat_flg(int beat_flg) {
        setBeat_flg(BeatFlg.valueOf(beat_flg));
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

    public String getTargets() {
        return targets;
    }

    public void setTargets(String targets) {
        this.targets = targets;
    }

    public String getUrl_key() {
        return url_key;
    }

    public void setUrl_key(String url_key) {
        this.url_key = url_key;
    }
}
