/*
 * CmsBtJmProductModel.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;
import java.math.BigDecimal;

/**
 * JMBTProduct||聚美商品
 */
public class CmsBtJmProductModel extends BaseModel {
    /**
     * 渠道id
     */
    protected String channelId;

    /**
     * 聚美的商品id
     */
    protected String jumeiProductId;

    protected String originJmHashId;

    /**
     * 商品code 唯一标识一个商品
     */
    protected String productCode;

    /**
     * 英文产地
     */
    protected String origin;

    /**
     * 商品中文名称
     */
    protected String productNameCn;

    /**
     * vo系统自用类目（Feed类目或CMS类目）
     */
    protected String voCategoryName;

    /**
     * 主数据品牌
     */
    protected String voBrandName;

    /**
     * 品牌名
     */
    protected String brandName;

    /**
     * 商品类别
     */
    protected String productType;

    /**
     * 尺码类别
     */
    protected String sizeType;

    /**
     * 商品英文描述
     */
    protected String productDesEn;

    /**
     * 中文颜色
     */
    protected String attribute;

    /**
     * 商品英文名称
     */
    protected String foreignLanguageName;

    /**
     * 中文产地
     */
    protected String addressOfProduce;

    /**
     * 保质期
     */
    protected String availablePeriod;

    /**
     * 商品中文描述
     */
    protected String productDesCn;

    /**
     * 适用人群
     */
    protected String applicableCrowd;

    /**
     * 特殊说明
     */
    protected String specialnote;

    /**
     * 英文颜色
     */
    protected String colorEn;

    /**
     * 商品图片
     */
    protected String image1;

    /**
     * 长标题
     */
    protected String productLongName;

    /**
     * 中标题
     */
    protected String productMediumName;

    /**
     * 短标题
     */
    protected String productShortName;

    /**
     * 自定义自定义搜索词
     */
    protected String searchMetaTextCustom;

    /**
     * 海外官网价格
     */
    protected BigDecimal msrpUsd;

    /**
     * 中国官网价格
     */
    protected BigDecimal msrpRmb;

    /**
     * 中国指导价格
     */
    protected BigDecimal retailPrice;

    /**
     * 中国最终售价
     */
    protected BigDecimal salePrice;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId == null ? null : channelId.trim();
    }

    public String getJumeiProductId() {
        return jumeiProductId;
    }

    public void setJumeiProductId(String jumeiProductId) {
        this.jumeiProductId = jumeiProductId == null ? null : jumeiProductId.trim();
    }

    public String getOriginJmHashId() {
        return originJmHashId;
    }

    public void setOriginJmHashId(String originJmHashId) {
        this.originJmHashId = originJmHashId == null ? null : originJmHashId.trim();
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode == null ? null : productCode.trim();
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin == null ? null : origin.trim();
    }

    public String getProductNameCn() {
        return productNameCn;
    }

    public void setProductNameCn(String productNameCn) {
        this.productNameCn = productNameCn == null ? null : productNameCn.trim();
    }

    public String getVoCategoryName() {
        return voCategoryName;
    }

    public void setVoCategoryName(String voCategoryName) {
        this.voCategoryName = voCategoryName == null ? null : voCategoryName.trim();
    }

    public String getVoBrandName() {
        return voBrandName;
    }

    public void setVoBrandName(String voBrandName) {
        this.voBrandName = voBrandName == null ? null : voBrandName.trim();
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName == null ? null : brandName.trim();
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType == null ? null : productType.trim();
    }

    public String getSizeType() {
        return sizeType;
    }

    public void setSizeType(String sizeType) {
        this.sizeType = sizeType == null ? null : sizeType.trim();
    }

    public String getProductDesEn() {
        return productDesEn;
    }

    public void setProductDesEn(String productDesEn) {
        this.productDesEn = productDesEn == null ? null : productDesEn.trim();
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute == null ? null : attribute.trim();
    }

    public String getForeignLanguageName() {
        return foreignLanguageName;
    }

    public void setForeignLanguageName(String foreignLanguageName) {
        this.foreignLanguageName = foreignLanguageName == null ? null : foreignLanguageName.trim();
    }

    public String getAddressOfProduce() {
        return addressOfProduce;
    }

    public void setAddressOfProduce(String addressOfProduce) {
        this.addressOfProduce = addressOfProduce == null ? null : addressOfProduce.trim();
    }

    public String getAvailablePeriod() {
        return availablePeriod;
    }

    public void setAvailablePeriod(String availablePeriod) {
        this.availablePeriod = availablePeriod == null ? null : availablePeriod.trim();
    }

    public String getProductDesCn() {
        return productDesCn;
    }

    public void setProductDesCn(String productDesCn) {
        this.productDesCn = productDesCn == null ? null : productDesCn.trim();
    }

    public String getApplicableCrowd() {
        return applicableCrowd;
    }

    public void setApplicableCrowd(String applicableCrowd) {
        this.applicableCrowd = applicableCrowd == null ? null : applicableCrowd.trim();
    }

    public String getSpecialnote() {
        return specialnote;
    }

    public void setSpecialnote(String specialnote) {
        this.specialnote = specialnote == null ? null : specialnote.trim();
    }

    public String getColorEn() {
        return colorEn;
    }

    public void setColorEn(String colorEn) {
        this.colorEn = colorEn == null ? null : colorEn.trim();
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1 == null ? null : image1.trim();
    }

    public String getProductLongName() {
        return productLongName;
    }

    public void setProductLongName(String productLongName) {
        this.productLongName = productLongName == null ? null : productLongName.trim();
    }

    public String getProductMediumName() {
        return productMediumName;
    }

    public void setProductMediumName(String productMediumName) {
        this.productMediumName = productMediumName == null ? null : productMediumName.trim();
    }

    public String getProductShortName() {
        return productShortName;
    }

    public void setProductShortName(String productShortName) {
        this.productShortName = productShortName == null ? null : productShortName.trim();
    }

    public String getSearchMetaTextCustom() {
        return searchMetaTextCustom;
    }

    public void setSearchMetaTextCustom(String searchMetaTextCustom) {
        this.searchMetaTextCustom = searchMetaTextCustom == null ? null : searchMetaTextCustom.trim();
    }

    public BigDecimal getMsrpUsd() {
        return msrpUsd;
    }

    public void setMsrpUsd(BigDecimal msrpUsd) {
        this.msrpUsd = msrpUsd;
    }

    public BigDecimal getMsrpRmb() {
        return msrpRmb;
    }

    public void setMsrpRmb(BigDecimal msrpRmb) {
        this.msrpRmb = msrpRmb;
    }

    public BigDecimal getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(BigDecimal retailPrice) {
        this.retailPrice = retailPrice;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }
}