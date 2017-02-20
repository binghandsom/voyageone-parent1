package com.voyageone.task2.cms.model;

import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by gjl on 2017/2/15.
 */
public class CmsBtFeedInfoKitBagModel extends CmsBtFeedInfoModel {


    private String variationsku;

    private String multivariationgroupid;

    private String weight;

    private String width;

    private String length;

    private String categories;

    private String productid;

    private String variationid;

    private String mainvid;

    private String sitename;

    private String siteid;

    private String territories;

    private String ean;

    private String title;

    private String detaileddescription;

    private String productsku;

    private String gender;

    private String size;

    private String depth;

    private String itembrand;

    private String colour;

    private String images;

    private String commoditycode;

    private String countryoforigin;

    private String composition;

    public String getVariationsku() {
        return variationsku;
    }

    public void setVariationsku(String variationsku) {
        this.variationsku = variationsku == null ? null : variationsku.trim();
    }

    public String getMultivariationgroupid() {
        return multivariationgroupid;
    }

    public void setMultivariationgroupid(String multivariationgroupid) {
        this.multivariationgroupid = multivariationgroupid == null ? null : multivariationgroupid.trim();
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight == null ? null : weight.trim();
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width == null ? null : width.trim();
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length == null ? null : length.trim();
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories == null ? null : categories.trim();
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid == null ? null : productid.trim();
    }

    public String getVariationid() {
        return variationid;
    }

    public void setVariationid(String variationid) {
        this.variationid = variationid == null ? null : variationid.trim();
    }

    public String getMainvid() {
        return mainvid;
    }

    public void setMainvid(String mainvid) {
        this.mainvid = mainvid == null ? null : mainvid.trim();
    }

    public String getSitename() {
        return sitename;
    }

    public void setSitename(String sitename) {
        this.sitename = sitename == null ? null : sitename.trim();
    }

    public String getSiteid() {
        return siteid;
    }

    public void setSiteid(String siteid) {
        this.siteid = siteid == null ? null : siteid.trim();
    }

    public String getTerritories() {
        return territories;
    }

    public void setTerritories(String territories) {
        this.territories = territories == null ? null : territories.trim();
    }

    public String getEan() {
        return ean;
    }

    public void setEan(String ean) {
        this.ean = ean == null ? null : ean.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getDetaileddescription() {
        return detaileddescription;
    }

    public void setDetaileddescription(String detaileddescription) {
        this.detaileddescription = detaileddescription == null ? null : detaileddescription.trim();
    }

    public String getProductsku() {
        return productsku;
    }

    public void setProductsku(String productsku) {
        this.productsku = productsku == null ? null : productsku.trim();
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender == null ? null : gender.trim();
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size == null ? null : size.trim();
    }

    public String getDepth() {
        return depth;
    }

    public void setDepth(String depth) {
        this.depth = depth == null ? null : depth.trim();
    }

    public String getItembrand() {
        return itembrand;
    }

    public void setItembrand(String itembrand) {
        this.itembrand = itembrand == null ? null : itembrand.trim();
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour == null ? null : colour.trim();
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images == null ? null : images.trim();
    }

    public String getCommoditycode() {
        return commoditycode;
    }

    public void setCommoditycode(String commoditycode) {
        this.commoditycode = commoditycode == null ? null : commoditycode.trim();
    }

    public String getCountryoforigin() {
        return countryoforigin;
    }

    public void setCountryoforigin(String countryoforigin) {
        this.countryoforigin = countryoforigin == null ? null : countryoforigin.trim();
    }

    public String getComposition() {
        return composition;
    }

    public void setComposition(String composition) {
        this.composition = composition == null ? null : composition.trim();
    }
}
