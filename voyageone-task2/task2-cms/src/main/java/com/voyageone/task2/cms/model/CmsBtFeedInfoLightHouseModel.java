package com.voyageone.task2.cms.model;

import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;

/**
 * Created by gjl on 2016/7/5.
 */
public class CmsBtFeedInfoLightHouseModel extends CmsBtFeedInfoModel{

    private String sku;

    private String category;

    private String upc;

    private Integer updateflag;

    private String title;

    private String brand;

    private String manufacturer;

    private String description;

    private String bulletpoint1;

    private String bulletpoint2;

    private String bulletpoint3;

    private String bulletpoint4;

    private String bulletpoint5;

    private String unitcount;

    private String color;

    private String directions;

    private String hairtype;

    private String indications;

    private String ingredients;

    private String skintone;

    private String skintype;

    private String targetgender;

    private String countryoforigin;

    private String weight;

    private String njtotalinventory;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public void setCategory(String category) {
        this.category = category;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
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

    @Override
    public String getBrand() {
        return brand;
    }

    @Override
    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBulletpoint1() {
        return bulletpoint1;
    }

    public void setBulletpoint1(String bulletpoint1) {
        this.bulletpoint1 = bulletpoint1;
    }

    public String getBulletpoint2() {
        return bulletpoint2;
    }

    public void setBulletpoint2(String bulletpoint2) {
        this.bulletpoint2 = bulletpoint2;
    }

    public String getBulletpoint3() {
        return bulletpoint3;
    }

    public void setBulletpoint3(String bulletpoint3) {
        this.bulletpoint3 = bulletpoint3;
    }

    public String getBulletpoint4() {
        return bulletpoint4;
    }

    public void setBulletpoint4(String bulletpoint4) {
        this.bulletpoint4 = bulletpoint4;
    }

    public String getBulletpoint5() {
        return bulletpoint5;
    }

    public void setBulletpoint5(String bulletpoint5) {
        this.bulletpoint5 = bulletpoint5;
    }

    public String getUnitcount() {
        return unitcount;
    }

    public void setUnitcount(String unitcount) {
        this.unitcount = unitcount;
    }

    @Override
    public String getColor() {
        return color;
    }

    @Override
    public void setColor(String color) {
        this.color = color;
    }

    public String getDirections() {
        return directions;
    }

    public void setDirections(String directions) {
        this.directions = directions;
    }

    public String getHairtype() {
        return hairtype;
    }

    public void setHairtype(String hairtype) {
        this.hairtype = hairtype;
    }

    public String getIndications() {
        return indications;
    }

    public void setIndications(String indications) {
        this.indications = indications;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getSkintone() {
        return skintone;
    }

    public void setSkintone(String skintone) {
        this.skintone = skintone;
    }

    public String getSkintype() {
        return skintype;
    }

    public void setSkintype(String skintype) {
        this.skintype = skintype;
    }

    public String getTargetgender() {
        return targetgender;
    }

    public void setTargetgender(String targetgender) {
        this.targetgender = targetgender;
    }

    public String getCountryoforigin() {
        return countryoforigin;
    }

    public void setCountryoforigin(String countryoforigin) {
        this.countryoforigin = countryoforigin;
    }

    @Override
    public String getWeight() {
        return weight;
    }

    @Override
    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getNjtotalinventory() {
        return njtotalinventory;
    }

    public void setNjtotalinventory(String njtotalinventory) {
        this.njtotalinventory = njtotalinventory;
    }
}
