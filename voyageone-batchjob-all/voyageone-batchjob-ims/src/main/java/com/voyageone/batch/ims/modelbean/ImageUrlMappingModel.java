package com.voyageone.batch.ims.modelbean;

/**
 * Created by lewis on 15-11-16.
 */
public class ImageUrlMappingModel {

    private int cartId;

    private String channelID;

    private String orgImageUrl;

    private String platformImageUrl;

    private String creater;

    private String modifier;

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public String getChannelID() {
        return channelID;
    }

    public void setChannelID(String channelID) {
        this.channelID = channelID;
    }

    public String getOrgImageUrl() {
        return orgImageUrl;
    }

    public void setOrgImageUrl(String orgImageUrl) {
        this.orgImageUrl = orgImageUrl;
    }

    public String getPlatformImageUrl() {
        return platformImageUrl;
    }

    public void setPlatformImageUrl(String platformImageUrl) {
        this.platformImageUrl = platformImageUrl;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }
}
