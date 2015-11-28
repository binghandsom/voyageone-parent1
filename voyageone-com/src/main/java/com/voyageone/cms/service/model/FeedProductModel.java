package com.voyageone.cms.service.model;

import com.voyageone.base.dao.mongodb.model.ChannelPartitionModel;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.List;
import java.util.Map;

/**
 * Created by james.li on 2015/11/26.
 */
public class FeedProductModel extends ChannelPartitionModel {
    public static final String COLLECTION_NAME = "cms_bt_product";

    public FeedProductModel(String channelId) {
        super(channelId);
    }

    private String category;
    private String code;
    private String name;
    private String model;
    private String color;
    private String origin;
    private String sizeType;
    private List<String> image;
    private String brand;
    private String weight;
    private String short_description;
    private String long_description;
    private List<FeedSkuModel> skus;
    @JsonIgnore
    private List<Map> attribute;

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

    public List<FeedSkuModel> getSkus() {
        return skus;
    }

    public void setSkus(List<FeedSkuModel> skus) {
        this.skus = skus;
    }

    public List<Map> getAttribute() {
        return attribute;
    }

    public void setAttribute(List<Map> attribute) {
        this.attribute = attribute;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getCollectionName() {
        return getCollectionName(this.channelId);
    }

    public static String getCollectionName(String channelId) {
        return COLLECTION_NAME + getPartitionValue(channelId);
    }
}