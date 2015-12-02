package com.voyageone.cms.service.model;

import com.voyageone.base.dao.mongodb.model.ChannelPartitionModel;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.Enums.FeedEnums;
import com.voyageone.common.configs.Feed;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by james.li on 2015/11/26.
 */
public class CmsBtFeedInfoModel extends ChannelPartitionModel {

    public CmsBtFeedInfoModel() {}

    public CmsBtFeedInfoModel(String channelId) {
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
    private List<CmsBtFeedInfoModel_Sku> skus;
    private List<Map> attributeList;
    private Map attribute = new HashMap<>();

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

    public List<CmsBtFeedInfoModel_Sku> getSkus() {
        return skus;
    }

    public void setSkus(List<CmsBtFeedInfoModel_Sku> skus) {
        this.skus = skus;
    }

    public List<Map> getAttributeList() {
        return attributeList;
    }

    public void setAttributeList(List<Map> attributeList) {
        this.attributeList = attributeList;
    }

    public Map getAttribute() {
        return attribute;
    }

    public void setAttribute(Map attribute) {
        this.attribute = attribute;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void attributeListToMap() {

        Pattern special_symbol = Pattern.compile("[~@'.#$%&*_''/‘’^\\()]");
        this.attributeList.forEach(
                map -> {
                    String key = special_symbol.matcher(map.get("attribute").toString()).replaceAll(Constants.EmptyString);
                    if (this.attribute.containsKey(key)) {
                        List<String> value = (List<String>) this.attribute.get(key);
                        String newValue = map.get("value").toString();
                        if (!value.contains(newValue)) {
                            value.add(newValue);
                        }
                    } else {
                        List<String> value = new ArrayList<String>();
                        value.add(map.get("value").toString());
                        this.attribute.put(key, value);
                    }
                }
        );
        this.attributeList = null;
    }
}