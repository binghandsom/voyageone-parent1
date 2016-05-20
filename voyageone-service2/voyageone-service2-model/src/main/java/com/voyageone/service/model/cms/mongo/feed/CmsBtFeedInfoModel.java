package com.voyageone.service.model.cms.mongo.feed;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.voyageone.base.dao.mongodb.model.ChannelPartitionModel;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Enums.FeedEnums;
import com.voyageone.common.configs.Feeds;
import com.voyageone.common.masterdate.schema.utils.StringUtil;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by james.li on 2015/11/26.
 */
public class CmsBtFeedInfoModel extends ChannelPartitionModel {

    public CmsBtFeedInfoModel() {}

    public CmsBtFeedInfoModel(String channelId) {
        super(channelId);
    }

    private String catId;
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
    private String shortDescription;
    private String longDescription;
    private List<CmsBtFeedInfoModel_Sku> skus;
    private List<Map> attributeList;
    private Map<String,List<String>> attribute = new HashMap<>();
    private Map<String, Object> fullAttribute = new HashMap<>();
    private int updFlg;
    private int qty;
    private String clientProductURL = "";

    private String productType;

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

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
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

    public void setFullAttribute(Map<String, Object> fullAttribute) {
        this.fullAttribute = fullAttribute;
    }

    public String getClientProductURL() {
        return clientProductURL;
    }

    public void setClientProductURL(String clientProductURL) {
        this.clientProductURL = clientProductURL;
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
        attribute.put("productType", this.productType);
        attribute.put("sizeType", this.sizeType);
        attribute.put("image", this.image);
        attribute.put("brand", this.brand);
        attribute.put("weight", this.weight);
        attribute.put("shortDescription", this.shortDescription);
        attribute.put("longDescription", this.longDescription);

        // 增加了sku级别的价格属性的支持
        if (this.getSkus() != null && this.getSkus().size() > 0) {
            attribute.put("priceCurrent", this.getSkus().get(0).getPriceCurrent());
            attribute.put("priceMsrp", this.getSkus().get(0).getPriceMsrp());
        } else {
            attribute.put("priceCurrent", "0");
            attribute.put("priceMsrp", "0");
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

    @JsonIgnore
    public CmsBtFeedInfoModel getCmsBtFeedInfoModel(ChannelConfigEnums.Channel channel){
        CmsBtFeedInfoModel cmsBtFeedInfoModel =new CmsBtFeedInfoModel(this.channelId);
        cmsBtFeedInfoModel.setCategory(this.getCategory());
        cmsBtFeedInfoModel.setCode(this.getCode());
        cmsBtFeedInfoModel.setName(this.getName());
        cmsBtFeedInfoModel.setModel(this.getModel());
        cmsBtFeedInfoModel.setColor(this.getColor());
        cmsBtFeedInfoModel.setOrigin(this.getOrigin());
        cmsBtFeedInfoModel.setSizeType(this.getSizeType());
        String imageSplit = Feeds.getVal1(channel, FeedEnums.Name.image_split);
        if(StringUtil.isEmpty(imageSplit)){
            imageSplit = ",";
        }
        if(this.getImage().size()>0){
            cmsBtFeedInfoModel.setImage(Arrays.asList(this.getImage().get(0).split(imageSplit)).stream().map(s -> s.trim()).collect(Collectors.toList()));
        }else{
            cmsBtFeedInfoModel.setImage(new ArrayList<>());
        }
        cmsBtFeedInfoModel.setBrand(this.getBrand());
        cmsBtFeedInfoModel.setWeight(this.getWeight());
        cmsBtFeedInfoModel.setShortDescription(this.getShortDescription());
        cmsBtFeedInfoModel.setLongDescription(this.getLongDescription());
        cmsBtFeedInfoModel.setSkus(this.getSkus());
        cmsBtFeedInfoModel.setUpdFlg(0);
        cmsBtFeedInfoModel.setClientProductURL(this.clientProductURL);
        cmsBtFeedInfoModel.setProductType(this.productType);
        return  cmsBtFeedInfoModel;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}