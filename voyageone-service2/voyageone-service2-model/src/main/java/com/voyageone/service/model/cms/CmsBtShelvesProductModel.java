/*
 * CmsBtShelvesProductModel.java
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
public class CmsBtShelvesProductModel extends BaseModel {
    /**
     * 货架ID
     */
    protected Integer shelvesId;

    /**
     * 商品code
     */
    protected String productCode;

    protected String productName;

    /**
     * CMS库存
     */
    protected Integer cmsInventory;

    /**
     * 平台库存
     */
    protected Integer cartInventory;

    /**
     * 最终售价
     */
    protected Double salePrice;

    /**
     * 商品平台状态
     */
    protected Integer status;

    /**
     * 图片
     */
    protected String image;

    /**
     * 第三方平台id
     */
    protected String numIid;

    /**
     * 第三方图片空间URL
     */
    protected String platformImageUrl;

    /**
     * 第三方图片Id
     */
    protected String platformImageId;

    /**
     * 商品在货架中序号
     */
    protected Integer sort;

    public Integer getShelvesId() {
        return shelvesId;
    }

    public void setShelvesId(Integer shelvesId) {
        this.shelvesId = shelvesId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode == null ? null : productCode.trim();
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName == null ? null : productName.trim();
    }

    public Integer getCmsInventory() {
        return cmsInventory;
    }

    public void setCmsInventory(Integer cmsInventory) {
        this.cmsInventory = cmsInventory;
    }

    public Integer getCartInventory() {
        return cartInventory;
    }

    public void setCartInventory(Integer cartInventory) {
        this.cartInventory = cartInventory;
    }

    public Double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }

    public String getNumIid() {
        return numIid;
    }

    public void setNumIid(String numIid) {
        this.numIid = numIid == null ? null : numIid.trim();
    }

    public String getPlatformImageUrl() {
        return platformImageUrl;
    }

    public void setPlatformImageUrl(String platformImageUrl) {
        this.platformImageUrl = platformImageUrl == null ? null : platformImageUrl.trim();
    }

    public String getPlatformImageId() {
        return platformImageId;
    }

    public void setPlatformImageId(String platformImageId) {
        this.platformImageId = platformImageId == null ? null : platformImageId.trim();
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}