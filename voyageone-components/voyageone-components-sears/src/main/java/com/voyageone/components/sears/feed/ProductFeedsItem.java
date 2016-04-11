package com.voyageone.components.sears.feed;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Created by james on 2015/10/21.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class ProductFeedsItem {

    private String itemId;
    private String productId;

    private String brand;
    private String description;
    private String featureDescription;
    private String manufacturerName;
    private String manufacturerPartNumber;
    private String uniqueManufacturerPartNumber;
    private String parentStore;
    private String imageUrl;
    private String feedAction;
    private String upc;
    private float weight;
    private String subcategory_id;
    private Boolean mailable;
    private Boolean storePickupEligible;
    private Boolean deliveryEligible;

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


    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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

    public String getUniqueManufacturerPartNumber() {
        return uniqueManufacturerPartNumber;
    }

    public void setUniqueManufacturerPartNumber(String uniqueManufacturerPartNumber) {
        this.uniqueManufacturerPartNumber = uniqueManufacturerPartNumber;
    }

    public String getParentStore() {
        return parentStore;
    }

    public void setParentStore(String parentStore) {
        this.parentStore = parentStore;
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

    public String getSubcategory_id() {
        return subcategory_id;
    }

    public void setSubcategory_id(String subcategory_id) {
        this.subcategory_id = subcategory_id;
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

    public Boolean getDeliveryEligible() {
        return deliveryEligible;
    }

    public void setDeliveryEligible(Boolean deliveryEligible) {
        this.deliveryEligible = deliveryEligible;
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
}
