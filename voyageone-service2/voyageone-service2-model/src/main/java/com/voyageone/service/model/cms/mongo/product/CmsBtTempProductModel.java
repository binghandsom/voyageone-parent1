package com.voyageone.service.model.cms.mongo.product;

import com.voyageone.base.dao.mongodb.model.ChannelPartitionModel;

/**
 * {@link CmsBtTempProductModel} 的商品Model
 *
 * @author linanbin on 6/29/2016
 * @author chuanyu.liang, 12/11/15
 * @version 2.0.0
 * @since 2.2.0
 */
public class CmsBtTempProductModel extends ChannelPartitionModel implements Cloneable {

    private String code;
    private String Abstract;
    private String Accessory;
    private String amazonId;
    private String amazonPath;
    private String googleCategoryPath;
    private String googleDepartmentPath;
    private String priceGrabberCategory;
    private String taxable;
    private String seoTitle;
    private String seoDescription;
    private String seoKeywords;
    private String isNewArrival;
    private String orderLimitCount;
    private String phoneOrderOnlyMessage;
    private String freeShippingType;
    private String rewardEligible;
    private String discountEligible;
    private String onsale;
    private String magento;
    private String amazon;
    private String approvedForIkicks;
    private String MSRP;
    private String price;
    private String thirdPrice;
    private String colorMap;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAbstract() {
        return Abstract;
    }

    public void setAbstract(String anAbstract) {
        Abstract = anAbstract;
    }

    public String getAccessory() {
        return Accessory;
    }

    public void setAccessory(String accessory) {
        Accessory = accessory;
    }

    public String getAmazonId() {
        return amazonId;
    }

    public void setAmazonId(String amazonId) {
        this.amazonId = amazonId;
    }

    public String getAmazonPath() {
        return amazonPath;
    }

    public void setAmazonPath(String amazonPath) {
        this.amazonPath = amazonPath;
    }

    public String getGoogleCategoryPath() {
        return googleCategoryPath;
    }

    public void setGoogleCategoryPath(String googleCategoryPath) {
        this.googleCategoryPath = googleCategoryPath;
    }

    public String getGoogleDepartmentPath() {
        return googleDepartmentPath;
    }

    public void setGoogleDepartmentPath(String googleDepartmentPath) {
        this.googleDepartmentPath = googleDepartmentPath;
    }

    public String getPriceGrabberCategory() {
        return priceGrabberCategory;
    }

    public void setPriceGrabberCategory(String priceGrabberCategory) {
        this.priceGrabberCategory = priceGrabberCategory;
    }

    public String isTaxable() {
        return taxable;
    }

    public void setTaxable(String taxable) {
        this.taxable = taxable;
    }

    public String getSeoTitle() {
        return seoTitle;
    }

    public void setSeoTitle(String seoTitle) {
        this.seoTitle = seoTitle;
    }

    public String getSeoDescription() {
        return seoDescription;
    }

    public void setSeoDescription(String seoDescription) {
        this.seoDescription = seoDescription;
    }

    public String getSeoKeywords() {
        return seoKeywords;
    }

    public void setSeoKeywords(String seoKeywords) {
        this.seoKeywords = seoKeywords;
    }

    public String getIsNewArrival() {
        return isNewArrival;
    }

    public void setIsNewArrival(String isNewArrival) {
        this.isNewArrival = isNewArrival;
    }

    public String getOrderLimitCount() {
        return orderLimitCount;
    }

    public void setOrderLimitCount(String orderLimitCount) {
        this.orderLimitCount = orderLimitCount;
    }

    public String getPhoneOrderOnlyMessage() {
        return phoneOrderOnlyMessage;
    }

    public void setPhoneOrderOnlyMessage(String phoneOrderOnlyMessage) {
        this.phoneOrderOnlyMessage = phoneOrderOnlyMessage;
    }

    public String getFreeShippingType() {
        return freeShippingType;
    }

    public void setFreeShippingType(String freeShippingType) {
        this.freeShippingType = freeShippingType;
    }

    public String getRewardEligible() {
        return rewardEligible;
    }

    public void setRewardEligible(String rewardEligible) {
        this.rewardEligible = rewardEligible;
    }

    public String getDiscountEligible() {
        return discountEligible;
    }

    public void setDiscountEligible(String discountEligible) {
        this.discountEligible = discountEligible;
    }

    public String getOnsale() {
        return onsale;
    }

    public void setOnsale(String onsale) {
        this.onsale = onsale;
    }

    public String getMagento() {
        return magento;
    }

    public void setMagento(String magento) {
        this.magento = magento;
    }

    public String getAmazon() {
        return amazon;
    }

    public void setAmazon(String amazon) {
        this.amazon = amazon;
    }

    public String getApprovedForIkicks() {
        return approvedForIkicks;
    }

    public void setApprovedForIkicks(String approvedForIkicks) {
        this.approvedForIkicks = approvedForIkicks;
    }

    public String getMSRP() {
        return MSRP;
    }

    public void setMSRP(String MSRP) {
        this.MSRP = MSRP;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getColorMap() {
        return colorMap;
    }

    public void setColorMap(String colorMap) {
        this.colorMap = colorMap;
    }

    public String getThirdPrice() {
        return thirdPrice;
    }

    public void setThirdPrice(String thirdPrice) {
        this.thirdPrice = thirdPrice;
    }

    @Override
    public CmsBtTempProductModel clone() throws CloneNotSupportedException {
        return (CmsBtTempProductModel) super.clone();
    }
}