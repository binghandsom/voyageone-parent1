/*
 * CmsMtPlatformCategoryExtendInfoModel.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;

/**
 * 
 */
public class CmsMtPlatformCategoryExtendInfoModel extends BaseModel {
    protected String channelId;

    protected Integer cartId;

    protected String categoryId;

    protected String platformBrandId;

    protected String platformProductId;

    protected Boolean active;

    protected String comment;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId == null ? null : channelId.trim();
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId == null ? null : categoryId.trim();
    }

    public String getPlatformBrandId() {
        return platformBrandId;
    }

    public void setPlatformBrandId(String platformBrandId) {
        this.platformBrandId = platformBrandId == null ? null : platformBrandId.trim();
    }

    public String getPlatformProductId() {
        return platformProductId;
    }

    public void setPlatformProductId(String platformProductId) {
        this.platformProductId = platformProductId == null ? null : platformProductId.trim();
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
    }
}