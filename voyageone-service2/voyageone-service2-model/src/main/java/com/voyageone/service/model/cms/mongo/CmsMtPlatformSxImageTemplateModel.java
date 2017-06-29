package com.voyageone.service.model.cms.mongo;

import com.voyageone.base.dao.mongodb.model.BaseMongoModel;

/**
 * Created by james on 2017/6/29.
 * 上新用的图片模板
 */
public class CmsMtPlatformSxImageTemplateModel extends BaseMongoModel {
    private String channelId;
    private Integer cartId;
    private String imageTemplate;
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

    public String getImageTemplate() {
        return imageTemplate;
    }

    public void setImageTemplate(String imageTemplate) {
        this.imageTemplate = imageTemplate;
    }
}
