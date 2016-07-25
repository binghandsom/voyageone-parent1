package com.voyageone.task2.cms.model;

import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;

/**
 * Created by gjl on 2016/7/5.
 */
public class CmsBtFeedInfoFragranceNetModel extends CmsBtFeedInfoModel{
    private String sku;
    private String title;
    private String description;
    private String shortDescriptions;
    private String upc;
    private String height;
    private String length;
    private String width;
    private String productWeight;
    private String manufacturer;
    private String itemGender;
    private String msrp;
    private String countryOfOrigin;
    private String quantity;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShortDescriptions() {
        return shortDescriptions;
    }

    public void setShortDescriptions(String shortDescriptions) {
        this.shortDescriptions = shortDescriptions;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getProductWeight() {
        return productWeight;
    }

    public void setProductWeight(String productWeight) {
        this.productWeight = productWeight;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getItemGender() {
        return itemGender;
    }

    public void setItemGender(String itemGender) {
        this.itemGender = itemGender;
    }

    public String getMsrp() {
        return msrp;
    }

    public void setMsrp(String msrp) {
        this.msrp = msrp;
    }

    public String getCountryOfOrigin() {
        return countryOfOrigin;
    }

    public void setCountryOfOrigin(String countryOfOrigin) {
        this.countryOfOrigin = countryOfOrigin;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
