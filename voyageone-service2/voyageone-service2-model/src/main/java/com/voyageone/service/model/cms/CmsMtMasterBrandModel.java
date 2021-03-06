/*
 * CmsMtMasterBrandModel.java
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
public class CmsMtMasterBrandModel extends BaseModel {
    protected String channelId;

    protected String feedBrand;

    protected String masterBrandEn;

    protected String masterBrandCn;

    protected String masterBrandName;

    protected String brandLogo;

    protected String productType;

    /**
     * 0.等待审很 1.审核通过 2 驳回 3.暂存
     */
    protected Integer masterFlag;

    protected Integer cartId;

    protected String platformBrandId;

    protected String comment;

    protected String cartName;

    protected String platformBrandName;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId == null ? null : channelId.trim();
    }

    public String getFeedBrand() {
        return feedBrand;
    }

    public void setFeedBrand(String feedBrand) {
        this.feedBrand = feedBrand == null ? null : feedBrand.trim();
    }

    public String getMasterBrandEn() {
        return masterBrandEn;
    }

    public void setMasterBrandEn(String masterBrandEn) {
        this.masterBrandEn = masterBrandEn == null ? null : masterBrandEn.trim();
    }

    public String getMasterBrandCn() {
        return masterBrandCn;
    }

    public void setMasterBrandCn(String masterBrandCn) {
        this.masterBrandCn = masterBrandCn == null ? null : masterBrandCn.trim();
    }

    public String getMasterBrandName() {
        return masterBrandName;
    }

    public void setMasterBrandName(String masterBrandName) {
        this.masterBrandName = masterBrandName == null ? null : masterBrandName.trim();
    }

    public String getBrandLogo() {
        return brandLogo;
    }

    public void setBrandLogo(String brandLogo) {
        this.brandLogo = brandLogo == null ? null : brandLogo.trim();
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType == null ? null : productType.trim();
    }

    public Integer getMasterFlag() {
        return masterFlag;
    }

    public void setMasterFlag(Integer masterFlag) {
        this.masterFlag = masterFlag;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public String getPlatformBrandId() {
        return platformBrandId;
    }

    public void setPlatformBrandId(String platformBrandId) {
        this.platformBrandId = platformBrandId == null ? null : platformBrandId.trim();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
    }

    public String getCartName() {
        return cartName;
    }

    public void setCartName(String cartName) {
        this.cartName = cartName == null ? null : cartName.trim();
    }

    public String getPlatformBrandName() {
        return platformBrandName;
    }

    public void setPlatformBrandName(String platformBrandName) {
        this.platformBrandName = platformBrandName == null ? null : platformBrandName.trim();
    }
}