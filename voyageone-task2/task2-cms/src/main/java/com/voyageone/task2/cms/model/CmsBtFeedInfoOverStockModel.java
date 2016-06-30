package com.voyageone.task2.cms.model;

import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;

/**
 * Created by gjl on 2016/6/21.
 */
public class CmsBtFeedInfoOverStockModel extends CmsBtFeedInfoModel {
    private String retailerId;
    private String sku;
    private String client_sku;
    private String title;
    private String brand;
    private String manufacturerName;
    private String shortDescription;
    private String longDescription;
    private String leadTime;
    private String adultContent;
    private String countryOfOrigin;
    private String productActiveStatus;
    private String shippingSiteSale;
    private String discountSiteSale;
    private String condition;
    private String returnPolicy;
    private String category;
    private String description;
    private String inventoryAvailable;
    private String msrpExpirationDate;
    private String compareAtExpirationDate;
    private String shippingWidth;
    private String shippingHeight;
    private String shippingLength;
    private String shippingWeight;
    private String upc;
    private String shipsViaLtl;
    private String attribute_name;

    public String getRetailerId() {
        return retailerId;
    }

    public void setRetailerId(String retailerId) {
        this.retailerId = retailerId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getClient_sku() {
        return client_sku;
    }

    public void setClient_sku(String client_sku) {
        this.client_sku = client_sku;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getBrand() {
        return brand;
    }

    @Override
    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    @Override
    public String getShortDescription() {
        return shortDescription;
    }

    @Override
    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    @Override
    public String getLongDescription() {
        return longDescription;
    }

    @Override
    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getLeadTime() {
        return leadTime;
    }

    public void setLeadTime(String leadTime) {
        this.leadTime = leadTime;
    }

    public String getAdultContent() {
        return adultContent;
    }

    public void setAdultContent(String adultContent) {
        this.adultContent = adultContent;
    }

    public String getCountryOfOrigin() {
        return countryOfOrigin;
    }

    public void setCountryOfOrigin(String countryOfOrigin) {
        this.countryOfOrigin = countryOfOrigin;
    }

    public String getProductActiveStatus() {
        return productActiveStatus;
    }

    public void setProductActiveStatus(String productActiveStatus) {
        this.productActiveStatus = productActiveStatus;
    }

    public String getShippingSiteSale() {
        return shippingSiteSale;
    }

    public void setShippingSiteSale(String shippingSiteSale) {
        this.shippingSiteSale = shippingSiteSale;
    }

    public String getDiscountSiteSale() {
        return discountSiteSale;
    }

    public void setDiscountSiteSale(String discountSiteSale) {
        this.discountSiteSale = discountSiteSale;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getReturnPolicy() {
        return returnPolicy;
    }

    public void setReturnPolicy(String returnPolicy) {
        this.returnPolicy = returnPolicy;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInventoryAvailable() {
        return inventoryAvailable;
    }

    public void setInventoryAvailable(String inventoryAvailable) {
        this.inventoryAvailable = inventoryAvailable;
    }

    public String getMsrpExpirationDate() {
        return msrpExpirationDate;
    }

    public void setMsrpExpirationDate(String msrpExpirationDate) {
        this.msrpExpirationDate = msrpExpirationDate;
    }

    public String getCompareAtExpirationDate() {
        return compareAtExpirationDate;
    }

    public void setCompareAtExpirationDate(String compareAtExpirationDate) {
        this.compareAtExpirationDate = compareAtExpirationDate;
    }

    public String getShippingWidth() {
        return shippingWidth;
    }

    public void setShippingWidth(String shippingWidth) {
        this.shippingWidth = shippingWidth;
    }

    public String getShippingHeight() {
        return shippingHeight;
    }

    public void setShippingHeight(String shippingHeight) {
        this.shippingHeight = shippingHeight;
    }

    public String getShippingLength() {
        return shippingLength;
    }

    public void setShippingLength(String shippingLength) {
        this.shippingLength = shippingLength;
    }

    public String getShippingWeight() {
        return shippingWeight;
    }

    public void setShippingWeight(String shippingWeight) {
        this.shippingWeight = shippingWeight;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public String getShipsViaLtl() {
        return shipsViaLtl;
    }

    public void setShipsViaLtl(String shipsViaLtl) {
        this.shipsViaLtl = shipsViaLtl;
    }

    public String getAttribute_name() {
        return attribute_name;
    }

    public void setAttribute_name(String attribute_name) {
        this.attribute_name = attribute_name;
    }

}
