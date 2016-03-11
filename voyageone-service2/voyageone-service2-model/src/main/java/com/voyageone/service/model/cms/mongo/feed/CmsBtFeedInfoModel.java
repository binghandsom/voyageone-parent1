package com.voyageone.service.model.cms.mongo.feed;

import com.voyageone.base.dao.mongodb.model.ChannelPartitionModel;
import com.voyageone.common.Constants;

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
    private Map<String,List<String>> attribute = new HashMap<>();
    private Map<String, Object> fullAttribute = new HashMap<>();
    private int updFlg;

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

    public Map<String,List<String>> getAttribute() {
        return attribute;
    }

    public void setAttribute(Map<String,List<String>> attribute) {
        this.attribute = attribute;
    }

    public Map<String, Object> getFullAttribute() {
        return this.fullAttribute;
    }

    public void setFullAttribute() {
        Map<String, Object> attribute = new HashMap<>();

        // 增加了code级别的共通属性的支持
        attribute.put("category", this.category);
        attribute.put("code", this.code);
        attribute.put("name", this.name);
        attribute.put("model", this.model);
        attribute.put("color", this.color);
        attribute.put("origin", this.origin);
        attribute.put("sizeType", this.sizeType);
        attribute.put("image", this.image);
        attribute.put("brand", this.brand);
        attribute.put("weight", this.weight);
        attribute.put("short_description", this.short_description);
        attribute.put("long_description", this.long_description);

        // 增加了sku级别的价格属性的支持
        if (this.getSkus() != null && this.getSkus().size() > 0) {
            attribute.put("price_current", this.getSkus().get(0).getPrice_current());
            attribute.put("price_msrp", this.getSkus().get(0).getPrice_msrp());
        } else {
            attribute.put("price_current", "0");
            attribute.put("price_msrp", "0");
        }

        this.fullAttribute = attribute;
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

    public int getUpdFlg() {
        return updFlg;
    }

    public void setUpdFlg(int updFlg) {
        this.updFlg = updFlg;
    }

}