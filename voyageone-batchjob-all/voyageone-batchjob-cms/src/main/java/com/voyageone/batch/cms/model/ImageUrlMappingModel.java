package com.voyageone.batch.cms.model;

/**
 * Created by lewis on 15-11-16.
 */
public class ImageUrlMappingModel {

    private int cartId;

    private String channelId;

    private String modelId;

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

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
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

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }
}
