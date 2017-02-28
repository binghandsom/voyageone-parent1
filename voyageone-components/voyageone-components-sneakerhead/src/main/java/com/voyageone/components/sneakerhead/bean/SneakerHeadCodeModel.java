package com.voyageone.components.sneakerhead.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * SneakerHeadCodeModel
 *
 * @author vantis
 * @version 1.0.0
 * @since 1.0.0
 */
public class SneakerHeadCodeModel {
    private List<SneakerHeadSkuModel> skus = new ArrayList<>();
    private String code;
    private String relationshipType;
    private String variationTheme;
    private String title;
    private BigDecimal price;
    private BigDecimal msrp;
    private String weight;
    private String images;
    private String description;
    private String shortDescription;
    private String productOrigin;
    private String category;
    private String brand;
    private String materials;
    private String vendorProductUrl;
    // additioal attributes
    private String model;
    private String productType;
    private String sizeType;
    private String color;
    private String boxImages;
    private String colorMap;
    private String abstractDescription;
    private String accessory;
    private String unisex;
    private String sizeOffset;
    private String blogUrl;
    private String isRewardEligible;
    private String isDiscountEligible;
    private String orderLimitCount;
    private String approvedDescriptions;
    private String urlKey;
    private Date lastReceivedOn;
    private BigDecimal cnRetailPrice;
    private BigDecimal cnMsrp;

    public List<SneakerHeadSkuModel> getSkus() {
        return skus;
    }

    public void setSkus(List<SneakerHeadSkuModel> skus) {
        this.skus = skus;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRelationshipType() {
        return relationshipType;
    }

    public void setRelationshipType(String relationshipType) {
        this.relationshipType = relationshipType;
    }

    public String getVariationTheme() {
        return variationTheme;
    }

    public void setVariationTheme(String variationTheme) {
        this.variationTheme = variationTheme;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getMsrp() {
        return msrp;
    }

    public void setMsrp(BigDecimal msrp) {
        this.msrp = msrp;
    }

    public BigDecimal getCnRetailPrice() {
        return cnRetailPrice;
    }

    public void setCnRetailPrice(BigDecimal cnRetailPrice) {
        this.cnRetailPrice = cnRetailPrice;
    }

    public BigDecimal getCnMsrp() {
        return cnMsrp;
    }

    public void setCnMsrp(BigDecimal cnMsrp) {
        this.cnMsrp = cnMsrp;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getProductOrigin() {
        return productOrigin;
    }

    public void setProductOrigin(String productOrigin) {
        this.productOrigin = productOrigin;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getMaterials() {
        return materials;
    }

    public void setMaterials(String materials) {
        this.materials = materials;
    }

    public String getVendorProductUrl() {
        return vendorProductUrl;
    }

    public void setVendorProductUrl(String vendorProductUrl) {
        this.vendorProductUrl = vendorProductUrl;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getSizeType() {
        return sizeType;
    }

    public void setSizeType(String sizeType) {
        this.sizeType = sizeType;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getBoxImages() {
        return boxImages;
    }

    public void setBoxImages(String boxImages) {
        this.boxImages = boxImages;
    }

    public String getColorMap() {
        return colorMap;
    }

    public void setColorMap(String colorMap) {
        this.colorMap = colorMap;
    }

    public String getAbstractDescription() {
        return abstractDescription;
    }

    public void setAbstractDescription(String abstractDescription) {
        this.abstractDescription = abstractDescription;
    }

    public String getAccessory() {
        return accessory;
    }

    public void setAccessory(String accessory) {
        this.accessory = accessory;
    }

    public String getUnisex() {
        return unisex;
    }

    public void setUnisex(String unisex) {
        this.unisex = unisex;
    }

    public String getSizeOffset() {
        return sizeOffset;
    }

    public void setSizeOffset(String sizeOffset) {
        this.sizeOffset = sizeOffset;
    }

    public String getBlogUrl() {
        return blogUrl;
    }

    public void setBlogUrl(String blogUrl) {
        this.blogUrl = blogUrl;
    }

    public String getIsRewardEligible() {
        return isRewardEligible;
    }

    public void setIsRewardEligible(String isRewardEligible) {
        this.isRewardEligible = isRewardEligible;
    }

    public String getIsDiscountEligible() {
        return isDiscountEligible;
    }

    public void setIsDiscountEligible(String isDiscountEligible) {
        this.isDiscountEligible = isDiscountEligible;
    }

    public String getOrderLimitCount() {
        return orderLimitCount;
    }

    public void setOrderLimitCount(String orderLimitCount) {
        this.orderLimitCount = orderLimitCount;
    }

    public String getApprovedDescriptions() {
        return approvedDescriptions;
    }

    public void setApprovedDescriptions(String approvedDescriptions) {
        this.approvedDescriptions = approvedDescriptions;
    }

    public String getUrlKey() {
        return urlKey;
    }

    public void setUrlKey(String urlKey) {
        this.urlKey = urlKey;
    }

    public Date getLastReceivedOn() {
        return lastReceivedOn;
    }

    public void setLastReceivedOn(Date lastReceivedOn) {
        this.lastReceivedOn = lastReceivedOn;
    }
}
