package com.voyageone.components.solr.bean;

import com.voyageone.components.solr.annotation.SolrField;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CmsProductSearchModel
 *
 * @author chuanyu.liang 2016/10/8
 * @version 2.0.0
 * @since 2.0.0
 */
public class CmsProductSearchModel {

    @SolrField(required = true)
    private String id;
    private Long lastVer;
    @SolrField(type = "lowercase")
    private String productModel;
    private String nameEn;
    private String nameCn;
    @SolrField(type = "lowercase")
    private String productCode;
    private String productChannel;
    private String orgChannelId;
    private List<String> skuCode;
    private String translateStatus;
    private String brand;
    private String productType;
    private String sizeType;
    private Integer quantity;
    private String catPath;
    private List<String>freeTags;
    private List<String>tags;
    private String categoryStatus;
    private String hsCodeStatus;
    private String feedCat;
    private String created;
    private String lock;
    private Double priceMsrpEd;
    private Double priceRetailEd;

    private Map<String, CmsProductSearchPlatformModel> platform;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getLastVer() {
        return lastVer;
    }

    public void setLastVer(Long lastVer) {
        this.lastVer = lastVer;
    }

    public String getProductModel() {
        return productModel;
    }

    public void setProductModel(String productModel) {
        this.productModel = productModel;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductChannel() {
        return productChannel;
    }

    public void setProductChannel(String productChannel) {
        this.productChannel = productChannel;
    }

    public List<String> getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(List<String> skuCode) {
        this.skuCode = skuCode;
    }

    public String getTranslateStatus() {
        return translateStatus;
    }

    public void setTranslateStatus(String translateStatus) {
        this.translateStatus = translateStatus;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getSizeType() {
        return sizeType;
    }

    public void setSizeType(String sizeType) {
        this.sizeType = sizeType;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getCatPath() {
        return catPath;
    }

    public void setCatPath(String catPath) {
        this.catPath = catPath;
    }

    public List<String> getFreeTags() {
        return freeTags;
    }

    public void setFreeTags(List<String> freeTags) {
        this.freeTags = freeTags;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getCategoryStatus() {
        return categoryStatus;
    }

    public void setCategoryStatus(String categoryStatus) {
        this.categoryStatus = categoryStatus;
    }

    public String getHsCodeStatus() {
        return hsCodeStatus;
    }

    public void setHsCodeStatus(String hsCodeStatus) {
        this.hsCodeStatus = hsCodeStatus;
    }

    public String getFeedCat() {
        return feedCat;
    }

    public void setFeedCat(String feedCat) {
        this.feedCat = feedCat;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public Double getPriceMsrpEd() {
        return priceMsrpEd;
    }

    public void setPriceMsrpEd(Double priceMsrpEd) {
        this.priceMsrpEd = priceMsrpEd;
    }

    public Double getPriceRetailEd() {
        return priceRetailEd;
    }

    public void setPriceRetailEd(Double priceRetailEd) {
        this.priceRetailEd = priceRetailEd;
    }

    public Map<String, CmsProductSearchPlatformModel> getPlatform() {
        if(platform == null) platform = new HashMap<>();
        return platform;
    }

    public void setPlatform(Map<String, CmsProductSearchPlatformModel> platform) {
        this.platform = platform;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    public String getOrgChannelId() {
        return orgChannelId;
    }

    public void setOrgChannelId(String orgChannelId) {
        this.orgChannelId = orgChannelId;
    }

    public String getLock() {
        return lock;
    }

    public void setLock(String lock) {
        this.lock = lock;
    }
}
