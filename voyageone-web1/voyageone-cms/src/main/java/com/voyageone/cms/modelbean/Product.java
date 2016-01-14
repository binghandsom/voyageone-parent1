package com.voyageone.cms.modelbean;

import java.math.BigDecimal;
import java.util.Date;

public class Product  {
    private String code;

    private String name;

    private Integer productTypeId;

    private Integer colorMapId;

    private String color;

    private BigDecimal msrp;

    private Integer madeInCountryId;

    private Integer materialFabric1Id;

    private Integer materialFabric2Id;

    private Integer materialFabric3Id;

    private Integer imageItemCount;

    private Integer imageBoxCount;

    private Integer imageAngleCount;

    private String availableTime;

    private String promotionTag;

    private String urlKey;

    private String isNewArrival;

    private String isOutletsOnSale;

    private String isRewardEligible;

    private String isDiscountEligible;

    private String isPhoneOrderOnly;

    private Integer orderLimitCount;

    private String isApprovedDescription;

    private String isEffective;

    private Date created;

    private String creater;

    private Date modified;

    private String modifier;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(Integer productTypeId) {
        this.productTypeId = productTypeId;
    }

    public Integer getColorMapId() {
        return colorMapId;
    }

    public void setColorMapId(Integer colorMapId) {
        this.colorMapId = colorMapId;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color == null ? null : color.trim();
    }

    public BigDecimal getMsrp() {
        return msrp;
    }

    public void setMsrp(BigDecimal msrp) {
        this.msrp = msrp;
    }

    public Integer getMadeInCountryId() {
        return madeInCountryId;
    }

    public void setMadeInCountryId(Integer madeInCountryId) {
        this.madeInCountryId = madeInCountryId;
    }

    public Integer getMaterialFabric1Id() {
        return materialFabric1Id;
    }

    public void setMaterialFabric1Id(Integer materialFabric1Id) {
        this.materialFabric1Id = materialFabric1Id;
    }

    public Integer getMaterialFabric2Id() {
        return materialFabric2Id;
    }

    public void setMaterialFabric2Id(Integer materialFabric2Id) {
        this.materialFabric2Id = materialFabric2Id;
    }

    public Integer getMaterialFabric3Id() {
        return materialFabric3Id;
    }

    public void setMaterialFabric3Id(Integer materialFabric3Id) {
        this.materialFabric3Id = materialFabric3Id;
    }

    public Integer getImageItemCount() {
        return imageItemCount;
    }

    public void setImageItemCount(Integer imageItemCount) {
        this.imageItemCount = imageItemCount;
    }

    public Integer getImageBoxCount() {
        return imageBoxCount;
    }

    public void setImageBoxCount(Integer imageBoxCount) {
        this.imageBoxCount = imageBoxCount;
    }

    public Integer getImageAngleCount() {
        return imageAngleCount;
    }

    public void setImageAngleCount(Integer imageAngleCount) {
        this.imageAngleCount = imageAngleCount;
    }

    public String getAvailableTime() {
        return availableTime;
    }

    public void setAvailableTime(String availableTime) {
        this.availableTime = availableTime == null ? null : availableTime.trim();
    }

    public String getPromotionTag() {
        return promotionTag;
    }

    public void setPromotionTag(String promotionTag) {
        this.promotionTag = promotionTag == null ? null : promotionTag.trim();
    }

    public String getUrlKey() {
        return urlKey;
    }

    public void setUrlKey(String urlKey) {
        this.urlKey = urlKey == null ? null : urlKey.trim();
    }

    public String getIsNewArrival() {
        return isNewArrival;
    }

    public void setIsNewArrival(String isNewArrival) {
        this.isNewArrival = isNewArrival == null ? null : isNewArrival.trim();
    }

    public String getIsOutletsOnSale() {
        return isOutletsOnSale;
    }

    public void setIsOutletsOnSale(String isOutletsOnSale) {
        this.isOutletsOnSale = isOutletsOnSale == null ? null : isOutletsOnSale.trim();
    }

    public String getIsRewardEligible() {
        return isRewardEligible;
    }

    public void setIsRewardEligible(String isRewardEligible) {
        this.isRewardEligible = isRewardEligible == null ? null : isRewardEligible.trim();
    }

    public String getIsDiscountEligible() {
        return isDiscountEligible;
    }

    public void setIsDiscountEligible(String isDiscountEligible) {
        this.isDiscountEligible = isDiscountEligible == null ? null : isDiscountEligible.trim();
    }

    public String getIsPhoneOrderOnly() {
        return isPhoneOrderOnly;
    }

    public void setIsPhoneOrderOnly(String isPhoneOrderOnly) {
        this.isPhoneOrderOnly = isPhoneOrderOnly == null ? null : isPhoneOrderOnly.trim();
    }

    public Integer getOrderLimitCount() {
        return orderLimitCount;
    }

    public void setOrderLimitCount(Integer orderLimitCount) {
        this.orderLimitCount = orderLimitCount;
    }

    public String getIsApprovedDescription() {
        return isApprovedDescription;
    }

    public void setIsApprovedDescription(String isApprovedDescription) {
        this.isApprovedDescription = isApprovedDescription == null ? null : isApprovedDescription.trim();
    }

    public String getIsEffective() {
        return isEffective;
    }

    public void setIsEffective(String isEffective) {
        this.isEffective = isEffective == null ? null : isEffective.trim();
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater == null ? null : creater.trim();
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier == null ? null : modifier.trim();
    }
}