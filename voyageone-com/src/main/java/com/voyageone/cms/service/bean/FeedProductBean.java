package com.voyageone.cms.service.bean;

import com.voyageone.base.dao.mongodb.model.BaseMongoModel;
import com.voyageone.base.dao.mongodb.model.ChannelPartitionModel;

import java.util.List;
import java.util.Map;

/**
 * Created by james.li on 2015/11/26.
 */
public class FeedProductBean extends ChannelPartitionModel {
    private String category;
    private String code;
    private String name;
    private String model;
    private String color;
    private String origin;
    private String sizeType;
    private List<String> image;
    private String brand;
    private String short_description;
    private String long_description;
    private List<FeedSkuBean> skus;
    private Map attribute;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getSizeType() {
        return sizeType;
    }

    public void setSizeType(String sizeType) {
        this.sizeType = sizeType;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getShort_description() {
        return short_description;
    }

    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }

    public String getLong_description() {
        return long_description;
    }

    public void setLong_description(String long_description) {
        this.long_description = long_description;
    }

    public List<FeedSkuBean> getSkus() {
        return skus;
    }

    public void setSkus(List<FeedSkuBean> skus) {
        this.skus = skus;
    }

    public Map getAttribute() {
        return attribute;
    }

    public void setAttribute(Map attribute) {
        this.attribute = attribute;
    }

    @Override
    public String getCollectionName() {
        return getCollectionName(this.channel_id);
    }
    public static String getCollectionName(String channel_id) {
        return "feed_code_info" + getPartitionValue(channel_id);
    }
}