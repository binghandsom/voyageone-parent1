package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;

/**
 * @author jerry 15/12/30
 * @version 2.0.0
 */
public class CmsBtPlatformImagesModel extends BaseModel {

    private Integer id;
    private String channelId;
    private Integer cartId;
    private String searchId;
    private String imgName;
    private String originalImgUrl;
    private String platformImgUrl;
    private String platformImgId;
    private String updFlg;
    private int active;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public String getSearchId() {
        return searchId;
    }

    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public String getOriginalImgUrl() {
        return originalImgUrl;
    }

    public void setOriginalImgUrl(String originalImgUrl) {
        this.originalImgUrl = originalImgUrl;
    }

    public String getPlatformImgUrl() {
        return platformImgUrl;
    }

    public void setPlatformImgUrl(String platformImgUrl) {
        this.platformImgUrl = platformImgUrl;
    }

    public String getPlatformImgId() {
        return platformImgId;
    }

    public void setPlatformImgId(String platformImgId) {
        this.platformImgId = platformImgId;
    }

    public String getUpdFlg() {
        return updFlg;
    }

    public void setUpdFlg(String updFlg) {
        this.updFlg = updFlg;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }
}
