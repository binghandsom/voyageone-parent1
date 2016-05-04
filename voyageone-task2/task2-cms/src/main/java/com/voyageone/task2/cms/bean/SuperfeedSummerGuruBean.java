package com.voyageone.task2.cms.bean;

import com.voyageone.common.util.MD5;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SuperfeedSummerGuruBean {
    private String sku;

    private String title;

    private String description;

    private String featureBullets;

    private String images;

    private String bodyMeasurements;

    private String relationshipName;

    private String shortDescription;

    private String condition;

    private String conditionNotes;

    private String brand;

    private String price;

    private String msrp;

    private String size;

    private String color;

    private String countryOfOrigin;

    private String productId;

    private String weight;

    private String material;

    private String parentId;

    private String voyageonePrice;

    private String category;

    private String sizeType;

    private String md5;

    private Integer updateflag;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku == null ? null : sku.trim();
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price == null ? null : price.trim();
    }

    public String getMsrp() {
        return msrp;
    }

    public void setMsrp(String msrp) {
        this.msrp = msrp == null ? null : msrp.trim();
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size == null ? null : size.trim();
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color == null ? null : color.trim();
    }

    public String getCountryOfOrigin() {
        return countryOfOrigin;
    }

    public void setCountryOfOrigin(String countryOfOrigin) {
        this.countryOfOrigin = countryOfOrigin == null ? null : countryOfOrigin.trim();
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId == null ? null : productId.trim();
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight == null ? null : weight.trim();
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material == null ? null : material.trim();
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId == null ? null : parentId.trim();
    }

    public String getVoyageonePrice() {
        return voyageonePrice;
    }

    public void setVoyageonePrice(String voyageonePrice) {
        this.voyageonePrice = voyageonePrice == null ? null : voyageonePrice.trim();
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category == null ? null : category.trim();
    }

    public String getSizeType() {
        return sizeType;
    }

    public void setSizeType(String sizeType) {
        this.sizeType = sizeType == null ? null : sizeType.trim();
    }

    public String getMd5() {
        StringBuffer temp = new StringBuffer();
        temp.append(this.getSku());
        temp.append(this.getTitle());
        temp.append(this.getDescription());
        temp.append(this.getFeatureBullets());
        temp.append(this.getPrice());
        temp.append(this.getMsrp());
        temp.append(this.getSize());
        temp.append(this.getColor());
        temp.append(this.getCountryOfOrigin());
        temp.append(this.getProductId());
        temp.append(this.getWeight());
        temp.append(this.getMaterial());
        List<String> images = Arrays.asList(this.images.split(","));
        images.stream()
                .map(s -> s.substring(s.lastIndexOf("/")).trim())
                .sorted()
                .collect(Collectors.toList())
                .forEach(temp::append);
        temp.append(this.getBodyMeasurements());
        temp.append(this.getRelationshipName());
        temp.append(this.getParentId());
        temp.append(this.getVoyageonePrice());
        temp.append(this.getShortDescription());
        temp.append(this.getCategory());
        temp.append(this.getSizeType());
        temp.append(this.getCondition());
        temp.append(this.getConditionNotes());
        temp.append(this.getBrand());
        return MD5.getMD5(temp.toString());
    }

    public void setMd5(String md5) {
        this.md5 = md5 == null ? null : md5.trim();
    }

    public Integer getUpdateflag() {
        return updateflag;
    }

    public void setUpdateflag(Integer updateflag) {
        this.updateflag = updateflag;
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

    public String getFeatureBullets() {
        return featureBullets;
    }

    public void setFeatureBullets(String featureBullets) {
        this.featureBullets = featureBullets;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getBodyMeasurements() {
        return bodyMeasurements;
    }

    public void setBodyMeasurements(String bodyMeasurements) {
        this.bodyMeasurements = bodyMeasurements;
    }

    public String getRelationshipName() {
        return relationshipName;
    }

    public void setRelationshipName(String relationshipName) {
        this.relationshipName = relationshipName;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getConditionNotes() {
        return conditionNotes;
    }

    public void setConditionNotes(String conditionNotes) {
        this.conditionNotes = conditionNotes;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}