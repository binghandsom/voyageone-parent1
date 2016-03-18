package com.voyageone.web2.cms.bean;

import java.util.List;
import java.util.Map;

/**
 * Created by lewis on 2016/2/21.
 */
public class ProductTranslationBean {
    /**
     * 产品id.
     */
    private Long prodId;

    /**
     * 产品code.
     */
    private String productCode;

    /**
     * 英文产品名称.
     */
    private String productName;

    /**
     * 英文产品长描述.
     */
    private String longDescription;

    /**
     * 英文产品短描述.
     */
    private String shortDescription;

    /**
     * 中文长标题：fields.longTitle.
     */
    private String longTitle;

    /**
     * 中文中标题：fields.middleTitle
     */
    private String middleTitle;

    /**
     * 中文短标题：fields.shortTitle
     */
    private String shortTitle;

    /**
     * 中文长描述：fields.longDesCn
     */
    private String longDesCn;

    /**
     * 中文短描述：fields.shortDesCn.
     */
    private String shortDesCn;

    public List<Map<String, String>> getGroupImgList() {
        return grpImgList;
    }

    public void setGroupImgList(List<Map<String, String>> grpImgList) {
        this.grpImgList = grpImgList;
    }

    /**
     * 该商品所在组的所有商品的图片
     */
    private List<Map<String, String>> grpImgList;
    /**
     * 产品图片.
     */
    private String productImage;

    private String translator;

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    /**
     * 产品分组.
     */
    private String model;

    /**
     * 更新时间.
     */
    private String modifiedTime;

    public String getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(String modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Long getProdId() {
        return prodId;
    }

    public void setProdId(Long prodId) {
        this.prodId = prodId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongTitle() {
        return longTitle;
    }

    public void setLongTitle(String longTitle) {
        this.longTitle = longTitle;
    }

    public String getMiddleTitle() {
        return middleTitle;
    }

    public void setMiddleTitle(String middleTitle) {
        this.middleTitle = middleTitle;
    }

    public String getShortTitle() {
        return shortTitle;
    }

    public void setShortTitle(String shortTitle) {
        this.shortTitle = shortTitle;
    }

    public String getLongDesCn() {
        return longDesCn;
    }

    public void setLongDesCn(String longDesCn) {
        this.longDesCn = longDesCn;
    }

    public String getShortDesCn() {
        return shortDesCn;
    }

    public void setShortDesCn(String shortDesCn) {
        this.shortDesCn = shortDesCn;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getTranslator() {
        return translator;
    }

    public void setTranslator(String translator) {
        this.translator = translator;
    }
}
