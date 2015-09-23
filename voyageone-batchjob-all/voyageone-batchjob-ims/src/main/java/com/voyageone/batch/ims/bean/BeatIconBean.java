package com.voyageone.batch.ims.bean;

import com.voyageone.batch.ims.enums.BeatFlg;

import java.math.BigDecimal;

/**
 * 还没有完成的价格披露任务，包含 ActivityBeatIcon 和 CartActivityBeatIcon 信息
 *
 * Created by Jonas on 7/16/15.
 */
public class BeatIconBean {
    private String channel_id;

    private int cart_id;

    private String code;

    private String url_key;

    private String image_name;

    private long cart_activity_beat_icon_id;

    private long num_iid;

    private int main_flg;

    private int main_flg_org;

    private String price;

    private String temp_url;

    private BeatFlg beat_flg;

    private String image_url_org;

    private String price_org;

    private String modified;

    private String modifier;

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

    public long getCart_activity_beat_icon_id() {
        return cart_activity_beat_icon_id;
    }

    public void setCart_activity_beat_icon_id(long cart_activity_beat_icon_id) {
        this.cart_activity_beat_icon_id = cart_activity_beat_icon_id;
    }

    public long getNum_iid() {
        return num_iid;
    }

    public void setNum_iid(long num_iid) {
        this.num_iid = num_iid;
    }

    public int getMain_flg() {
        return main_flg;
    }

    public void setMain_flg(int main_flg) {
        this.main_flg = main_flg;
    }

    public int getMain_flg_org() {
        return main_flg_org;
    }

    public void setMain_flg_org(int main_flg_org) {
        this.main_flg_org = main_flg_org;
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

    public String getImage_url_org() {
        return image_url_org;
    }

    public void setImage_url_org(String image_url_org) {
        this.image_url_org = image_url_org;
    }

    public String getPrice_org() {
        return price_org;
    }

    public void setPrice_org(String price_org) {
        this.price_org = price_org;
    }
}
