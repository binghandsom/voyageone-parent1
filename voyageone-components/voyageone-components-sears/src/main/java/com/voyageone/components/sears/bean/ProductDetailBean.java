package com.voyageone.components.sears.bean;

import com.voyageone.components.sears.feed.Categorization;
import com.voyageone.components.sears.feed.SalesRanking;
import com.voyageone.components.sears.feed.Specification;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/10/22.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class ProductDetailBean {

    private String productId;

    private String brand;
    private String productName;
    private String description;
    private String featureDescription;
    private String manufacturerName;
    private String manufacturerPartNumber;
    private String imageUrl;
    private String feedAction;
    private String upc;
    private float weight;

    @XmlElement(name = "category_id")
    private String categoryId;
    private Categorization categorization;

    private Boolean mailable;
    private Boolean storePickupEligible;

    private SalesRanking salesRanking;

    @XmlElementWrapper(name = "productSpecifications")
    @XmlElement(name = "specification")
    private List<Specification> productSpecifications;

    @XmlElementWrapper(name = "commonSpecifications")
    @XmlElement(name = "specification")
    private List<Specification> commonSpecifications;

    @XmlElementWrapper(name = "variantSpecifications ")
    @XmlElement(name = "specification")
    private List<Specification> variantSpecifications;

    private String htcCode;

    private String modelNumber;
    private String countryGroups;
    private String countryOfOrigin;
    private String imageWidth;
    private String imageHeight;


    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFeatureDescription() {
        return featureDescription;
    }

    public void setFeatureDescription(String featureDescription) {
        this.featureDescription = featureDescription;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public String getManufacturerPartNumber() {
        return manufacturerPartNumber;
    }

    public void setManufacturerPartNumber(String manufacturerPartNumber) {
        this.manufacturerPartNumber = manufacturerPartNumber;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFeedAction() {
        return feedAction;
    }

    public void setFeedAction(String feedAction) {
        this.feedAction = feedAction;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public Categorization getCategorization() {
        return categorization;
    }

    public void setCategorization(Categorization categorization) {
        this.categorization = categorization;
    }

    public Boolean getMailable() {
        return mailable;
    }

    public void setMailable(Boolean mailable) {
        this.mailable = mailable;
    }

    public Boolean getStorePickupEligible() {
        return storePickupEligible;
    }

    public void setStorePickupEligible(Boolean storePickupEligible) {
        this.storePickupEligible = storePickupEligible;
    }

    public SalesRanking getSalesRanking() {
        return salesRanking;
    }

    public void setSalesRanking(SalesRanking salesRanking) {
        this.salesRanking = salesRanking;
    }

    public List<Specification> getProductSpecifications() {
        return productSpecifications;
    }

    public void setProductSpecifications(List<Specification> productSpecifications) {
        this.productSpecifications = productSpecifications;
    }

    public List<Specification> getCommonSpecifications() {
        return commonSpecifications;
    }

    public void setCommonSpecifications(List<Specification> commonSpecifications) {
        this.commonSpecifications = commonSpecifications;
    }

    public List<Specification> getVariantSpecifications() {
        return variantSpecifications;
    }

    public void setVariantSpecifications(List<Specification> variantSpecifications) {
        this.variantSpecifications = variantSpecifications;
    }

    public String getHtcCode() {
        return htcCode;
    }

    public void setHtcCode(String htcCode) {
        this.htcCode = htcCode;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public String getCountryGroups() {
        return countryGroups;
    }

    public void setCountryGroups(String countryGroups) {
        this.countryGroups = countryGroups;
    }

    public String getCountryOfOrigin() {
        return countryOfOrigin;
    }

    public void setCountryOfOrigin(String countryOfOrigin) {
        this.countryOfOrigin = countryOfOrigin;
    }

    public String getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(String imageWidth) {
        this.imageWidth = imageWidth;
    }

    public String getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(String imageHeight) {
        this.imageHeight = imageHeight;
    }

    public String getCategory() {
        return categorization.toString();
    }

    public List<Specification> getSpecifications() {
        List<Specification> ret = new ArrayList<>();
        if (productSpecifications != null && !productSpecifications.isEmpty()) {
            ret.addAll(getProductSpecifications());
        }
        if (commonSpecifications != null && !commonSpecifications.isEmpty()) {
            ret.addAll(getCommonSpecifications());
        }
        if (variantSpecifications != null && !variantSpecifications.isEmpty()) {
            ret.addAll(getVariantSpecifications());
        }
        return ret;
    }
}
