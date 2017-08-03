package com.voyageone.service.model.cms;

import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field_Image;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Charis on 2017/7/20.
 */
public class TransferUsProductModel {

    private String channelId;
    private int storeId;
    private String sizeType;
    private String name;
    private String description;
    private String shortDescription;
    private String code;
    private String abstractInfo;
    private String accessory;
    private String color;
    private String urlKey;
    private String created;
    private String model;
    private List<CmsBtProductModel_Field_Image> images;
    private List<CmsBtProductModel_Field_Image> boxImages;
    private List<CmsBtProductModel_Field_Image> marketingImages;
    private String brand;
    private boolean isTaxable;
    private String colorMap;
    private String material;
    private String origin;
    private String amazonBrowseTree;
    private String googleCategory;
    private String googleDepartment;
    private String priceGrabberCategory;
    private String status;
    private List<TransferUsProductModel_Sku> items;
    private Map<String, Object> storeAttributes;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getSizeType() {
        return sizeType;
    }

    public void setSizeType(String sizeType) {
        this.sizeType = sizeType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAbstractInfo() {
        return abstractInfo;
    }

    public void setAbstractInfo(String abstractInfo) {
        this.abstractInfo = abstractInfo;
    }

    public String getAccessory() {
        return accessory;
    }

    public void setAccessory(String accessory) {
        this.accessory = accessory;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getUrlKey() {
        return urlKey;
    }

    public void setUrlKey(String urlKey) {
        this.urlKey = urlKey;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<CmsBtProductModel_Field_Image> getImages() {
        return images;
    }

    public void setImages(List<CmsBtProductModel_Field_Image> images) {
        this.images = images;
    }

    public List<CmsBtProductModel_Field_Image> getBoxImages() {
        return boxImages;
    }

    public void setBoxImages(List<CmsBtProductModel_Field_Image> boxImages) {
        this.boxImages = boxImages;
    }

    public List<CmsBtProductModel_Field_Image> getMarketingImages() {
        return marketingImages;
    }

    public void setMarketingImages(List<CmsBtProductModel_Field_Image> marketingImages) {
        this.marketingImages = marketingImages;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getColorMap() {
        return colorMap;
    }

    public void setColorMap(String colorMap) {
        this.colorMap = colorMap;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getAmazonBrowseTree() {
        return amazonBrowseTree;
    }

    public void setAmazonBrowseTree(String amazonBrowseTree) {
        this.amazonBrowseTree = amazonBrowseTree;
    }

    public String getGoogleCategory() {
        return googleCategory;
    }

    public void setGoogleCategory(String googleCategory) {
        this.googleCategory = googleCategory;
    }

    public String getGoogleDepartment() {
        return googleDepartment;
    }

    public void setGoogleDepartment(String googleDepartment) {
        this.googleDepartment = googleDepartment;
    }

    public String getPriceGrabberCategory() {
        return priceGrabberCategory;
    }

    public void setPriceGrabberCategory(String priceGrabberCategory) {
        this.priceGrabberCategory = priceGrabberCategory;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<TransferUsProductModel_Sku> getItems() {
        return items;
    }

    public void setItems(List<TransferUsProductModel_Sku> items) {
        this.items = items;
    }

    public Map<String, Object> getStoreAttributes() {
        return storeAttributes;
    }

    public void setStoreAttributes(Map<String, Object> storeAttributes) {
        this.storeAttributes = storeAttributes;
    }

    public boolean isTaxable() {
        return isTaxable;
    }

    public void setTaxable(boolean taxable) {
        isTaxable = taxable;
    }
}