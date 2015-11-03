package com.voyageone.batch.ims.service.beat;

import com.voyageone.batch.ims.bean.BeatPicBean;
import com.voyageone.common.configs.beans.ShopBean;

import static com.voyageone.batch.ims.service.beat.ImsBeatImageNameFormat.formatImageName;
import static com.voyageone.common.util.StringUtils.isEmpty;

/**
 * 价格披露使用的,商品的简要图片信息
 * Created by Jonas on 10/29/15.
 */
public class ImsBeatImageInfo {

    private String channel_id;

    private String code;

    private String url_key;

    private String image_name;

    private int image_id;

    private BeatPicBean beatInfo;

    private String categoryTid;

    private ShopBean shop;

    private boolean noImage;

    private String imageUrl;

    public String getChannel_id() {

        if (!isEmpty(channel_id)) return channel_id;

        if (getBeatInfo() == null) return null;

        return getBeatInfo().getChannel_id();
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
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

        if (!isEmpty(image_name)) return image_name;

        return formatImageName(this);
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    public void setBeatInfo(BeatPicBean beatInfo) {
        this.beatInfo = beatInfo;
    }

    public BeatPicBean getBeatInfo() {
        return beatInfo;
    }

    public void setCategoryTid(String categoryTid) {
        this.categoryTid = categoryTid;
    }

    public String getCategoryTid() {
        return categoryTid;
    }

    public void setShop(ShopBean shop) {
        this.shop = shop;
    }

    public ShopBean getShop() {
        return shop;
    }

    public String getBeatTitle() {
        String titleKey = getImage_name();
        return titleKey + ".beat.jpg";
    }

    public String getOrgTitle() {
        String titleKey = getImage_name();
        return titleKey + ".jpg";
    }

    public void setNoImage(boolean noImage) {
        this.noImage = noImage;
    }

    /**
     * 获取标记,是否该位置应有图片.仅还原时使用
     */
    public boolean isNoImage() {
        return noImage;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
