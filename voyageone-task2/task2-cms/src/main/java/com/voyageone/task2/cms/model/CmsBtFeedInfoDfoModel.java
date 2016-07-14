package com.voyageone.task2.cms.model;

import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;

/**
 * @author james.li on 2016/1/15.
 * @version 2.0.0
 */
public class CmsBtFeedInfoDfoModel extends CmsBtFeedInfoModel {

    private String sku;
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
    private String countryOfOrigin;
    private String lensColor;
    private String lensTechnology;
    private String frameType;
    private String style;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }
    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    @Override
    public String getModel() {
        return model;
    }

    @Override
    public void setModel(String model) {
        this.model = model;
    }

    public String getManufacturerColor() {
        return manufacturerColor;
    }

    public void setManufacturerColor(String manufacturerColor) {
        this.manufacturerColor = manufacturerColor;
    }

    public String getGeneralColor() {
        return generalColor;
    }

    public void setGeneralColor(String generalColor) {
        this.generalColor = generalColor;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getLensSize() {
        return lensSize;
    }

    public void setLensSize(String lensSize) {
        this.lensSize = lensSize;
    }

    public String getBridgeSize() {
        return bridgeSize;
    }

    public void setBridgeSize(String bridgeSize) {
        this.bridgeSize = bridgeSize;
    }

    public String getTempleSize() {
        return templeSize;
    }

    public void setTempleSize(String templeSize) {
        this.templeSize = templeSize;
    }

    public String getVerticalSize() {
        return verticalSize;
    }

    public void setVerticalSize(String verticalSize) {
        this.verticalSize = verticalSize;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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
}
