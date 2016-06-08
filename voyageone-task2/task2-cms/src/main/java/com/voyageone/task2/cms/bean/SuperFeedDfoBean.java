package com.voyageone.task2.cms.bean;

import java.util.HashSet;
import java.util.Set;

public class SuperFeedDfoBean extends SuperFeedBean{
    private String sku;

    private String brand;

    private String upc;

    private String classification;

    private String model;

    private String manufacturerColor;

    private String generalColor;

    private String size;

    private String lensSize;

    private String bridgeSize;

    private String templeSize;

    private String verticalSize;

    private String gender;

    private String material;

    private String countryOfOrigin;

    private String lensColor;

    private String lensTechnology;

    private String frameType;

    private String style;

    private String url1;

    private String url2;

    private String url3;

    private String url4;

    private String url5;

    private String url6;

    private String url7;

    private String url8;

    private String voyageonePrice;

    private String retailPrice;

    private String qty;

    private String md5;

    private Integer updateflag;


    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku == null ? null : sku.trim();
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification == null ? null : classification.trim();
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model == null ? null : model.trim();
    }

    public String getManufacturerColor() {
        return manufacturerColor;
    }

    public void setManufacturerColor(String manufacturerColor) {
        this.manufacturerColor = manufacturerColor == null ? null : manufacturerColor.trim();
    }

    public String getGeneralColor() {
        return generalColor;
    }

    public void setGeneralColor(String generalColor) {
        this.generalColor = generalColor == null ? null : generalColor.trim();
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size == null ? null : size.trim();
    }

    public String getLensSize() {
        return lensSize;
    }

    public void setLensSize(String lensSize) {
        this.lensSize = lensSize == null ? null : lensSize.trim();
    }

    public String getBridgeSize() {
        return bridgeSize;
    }

    public void setBridgeSize(String bridgeSize) {
        this.bridgeSize = bridgeSize == null ? null : bridgeSize.trim();
    }

    public String getTempleSize() {
        return templeSize;
    }

    public void setTempleSize(String templeSize) {
        this.templeSize = templeSize == null ? null : templeSize.trim();
    }

    public String getVerticalSize() {
        return verticalSize;
    }

    public void setVerticalSize(String verticalSize) {
        this.verticalSize = verticalSize == null ? null : verticalSize.trim();
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender == null ? null : gender.trim();
    }

    public String getVoyageonePrice() {
        return voyageonePrice;
    }

    public void setVoyageonePrice(String voyageonePrice) {
        this.voyageonePrice = voyageonePrice == null ? null : voyageonePrice.trim();
    }

    public String getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(String retailPrice) {
        this.retailPrice = retailPrice == null ? null : retailPrice.trim();
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty == null ? null : qty.trim();
    }

    public String getMd5() {
        StringBuffer temp = new StringBuffer();
        Set<String> noMd5Fields = new HashSet<>();
        noMd5Fields.add("md5");
        noMd5Fields.add("updateflag");


        return  beanToMd5(this,noMd5Fields);
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

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getCountryOfOrigin() {
        return countryOfOrigin;
    }

    public void setCountryOfOrigin(String countryOfOrigin) {
        this.countryOfOrigin = countryOfOrigin;
    }

    public String getLensColor() {
        return lensColor;
    }

    public void setLensColor(String lensColor) {
        this.lensColor = lensColor;
    }

    public String getLensTechnology() {
        return lensTechnology;
    }

    public void setLensTechnology(String lensTechnology) {
        this.lensTechnology = lensTechnology;
    }

    public String getFrameType() {
        return frameType;
    }

    public void setFrameType(String frameType) {
        this.frameType = frameType;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getUrl1() {
        return url1;
    }

    public void setUrl1(String url1) {
        this.url1 = url1;
    }

    public String getUrl2() {
        return url2;
    }

    public void setUrl2(String url2) {
        this.url2 = url2;
    }

    public String getUrl3() {
        return url3;
    }

    public void setUrl3(String url3) {
        this.url3 = url3;
    }

    public String getUrl4() {
        return url4;
    }

    public void setUrl4(String url4) {
        this.url4 = url4;
    }

    public String getUrl5() {
        return url5;
    }

    public void setUrl5(String url5) {
        this.url5 = url5;
    }

    public String getUrl6() {
        return url6;
    }

    public void setUrl6(String url6) {
        this.url6 = url6;
    }

    public String getUrl7() {
        return url7;
    }

    public void setUrl7(String url7) {
        this.url7 = url7;
    }

    public String getUrl8() {
        return url8;
    }

    public void setUrl8(String url8) {
        this.url8 = url8;
    }
}