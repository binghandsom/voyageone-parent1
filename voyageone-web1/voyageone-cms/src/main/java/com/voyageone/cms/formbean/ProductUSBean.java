package com.voyageone.cms.formbean;

import com.voyageone.cms.annotation.Extends;
import com.voyageone.cms.annotation.Table;

public class ProductUSBean extends ProductBaseBean{
	 	private String code;

	    private String name;

	    private String productTypeId;
	    
	    private String productType;
	    
	    private String brand;
	    
	    private String colorMapId;

	    private String color;

	    private String madeInCountryId;

	    private String materialFabric1Id;

	    private String materialFabric2Id;

	    private String materialFabric3Id;

	    private String imageItemCount;

	    private String imageBoxCount;

	    private String imageAngleCount;

	    private String availableTime;
	    @Extends
	    private String promotionTag;
	    
	    private String msrp;

	    private String urlKey;

	    private Boolean isNewArrival;

	    private Boolean isOutletsOnSale;

	    private Boolean isRewardEligible;

	    private Boolean isDiscountEligible;

	    private Boolean isPhoneOrderOnly;

	    private String orderLimitCount;

	    private Boolean isApprovedDescription;

	    private Boolean isEffective;
	    @Extends
	    private String mainCategoryId;
	    
	    private String amazonBrowseTreeId;
	    
	    private String googleCategoryId;
	    
	    private String pricegrabberCategoryId;
	    @Extends
	    @Table(name="cms_bt_product_extend")
	    private String usAbstract;
	    @Extends
	    @Table(name="cms_bt_product_extend")
	    private String shortDescription;
	    @Extends
	    @Table(name="cms_bt_product_extend")
	    private String longDescription;
	    @Extends
	    @Table(name="cms_bt_product_extend")
	    private String accessory;
	    
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

		public String getColorMapId() {
			return colorMapId;
		}

		public void setColorMapId(String colorMapId) {
			this.colorMapId = colorMapId;
		}

		public String getColor() {
			return color;
		}

		public void setColor(String color) {
			this.color = color;
		}

		public String getMadeInCountryId() {
			return madeInCountryId;
		}

		public void setMadeInCountryId(String madeInCountryId) {
			this.madeInCountryId = madeInCountryId;
		}

		public String getMaterialFabric1Id() {
			return materialFabric1Id;
		}

		public void setMaterialFabric1Id(String materialFabric1Id) {
			this.materialFabric1Id = materialFabric1Id;
		}

		public String getMaterialFabric2Id() {
			return materialFabric2Id;
		}

		public void setMaterialFabric2Id(String materialFabric2Id) {
			this.materialFabric2Id = materialFabric2Id;
		}

		public String getMaterialFabric3Id() {
			return materialFabric3Id;
		}

		public void setMaterialFabric3Id(String materialFabric3Id) {
			this.materialFabric3Id = materialFabric3Id;
		}

		public String getImageItemCount() {
			return imageItemCount;
		}

		public void setImageItemCount(String imageItemCount) {
			this.imageItemCount = imageItemCount;
		}

		public String getImageBoxCount() {
			return imageBoxCount;
		}

		public void setImageBoxCount(String imageBoxCount) {
			this.imageBoxCount = imageBoxCount;
		}

		public String getImageAngleCount() {
			return imageAngleCount;
		}

		public void setImageAngleCount(String imageAngleCount) {
			this.imageAngleCount = imageAngleCount;
		}

		public String getAvailableTime() {
			return availableTime;
		}

		public void setAvailableTime(String availableTime) {
			this.availableTime = availableTime;
		}

		public String getPromotionTag() {
			return promotionTag;
		}

		public void setPromotionTag(String promotionTag) {
			this.promotionTag = promotionTag;
		}

		public String getUrlKey() {
			return urlKey;
		}

		public void setUrlKey(String urlKey) {
			this.urlKey = urlKey;
		}


		/**
		 * @return the isNewArrival
		 */
		public Boolean getIsNewArrival() {
			return isNewArrival;
		}

		/**
		 * @param isNewArrival the isNewArrival to set
		 */
		public void setIsNewArrival(Boolean isNewArrival) {
			this.isNewArrival = isNewArrival;
		}

		

		public String getOrderLimitCount() {
			return orderLimitCount;
		}

		public void setOrderLimitCount(String orderLimitCount) {
			this.orderLimitCount = orderLimitCount;
		}

		public boolean isApprovedDescription() {
			return isApprovedDescription;
		}

		public void setApprovedDescription(boolean isApprovedDescription) {
			this.isApprovedDescription = isApprovedDescription;
		}


		public String getMainCategoryId() {
			return mainCategoryId;
		}

		public void setMainCategoryId(String mainCategoryId) {
			this.mainCategoryId = mainCategoryId;
		}

		public String getAmazonBrowseTreeId() {
			return amazonBrowseTreeId;
		}

		public void setAmazonBrowseTreeId(String amazonBrowseTreeId) {
			this.amazonBrowseTreeId = amazonBrowseTreeId;
		}

		public String getGoogleCategoryId() {
			return googleCategoryId;
		}

		public void setGoogleCategoryId(String googleCategoryId) {
			this.googleCategoryId = googleCategoryId;
		}

		public String getPricegrabberCategoryId() {
			return pricegrabberCategoryId;
		}

		public void setPricegrabberCategoryId(String pricegrabberCategoryId) {
			this.pricegrabberCategoryId = pricegrabberCategoryId;
		}
		/**
		 * @return the usAbstract
		 */
		public String getUsAbstract() {
			return usAbstract;
		}

		/**
		 * @param usAbstract the usAbstract to set
		 */
		public void setUsAbstract(String usAbstract) {
			this.usAbstract = usAbstract;
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

		/**
		 * @return the productTypeId
		 */
		public String getProductTypeId() {
			return productTypeId;
		}

		/**
		 * @param productTypeId the productTypeId to set
		 */
		public void setProductTypeId(String productTypeId) {
			this.productTypeId = productTypeId;
		}

		/**
		 * @return the msrp
		 */
		public String getMsrp() {
			return msrp;
		}

		/**
		 * @param msrp the msrp to set
		 */
		public void setMsrp(String msrp) {
			this.msrp = msrp;
		}

		/**
		 * @return the accessory
		 */
		public String getAccessory() {
			return accessory;
		}

		/**
		 * @param accessory the accessory to set
		 */
		public void setAccessory(String accessory) {
			this.accessory = accessory;
		}

		/**
		 * @return the isOutletsOnSale
		 */
		public Boolean getIsOutletsOnSale() {
			return isOutletsOnSale;
		}

		/**
		 * @param isOutletsOnSale the isOutletsOnSale to set
		 */
		public void setIsOutletsOnSale(Boolean isOutletsOnSale) {
			this.isOutletsOnSale = isOutletsOnSale;
		}

		/**
		 * @return the isRewardEligible
		 */
		public Boolean getIsRewardEligible() {
			return isRewardEligible;
		}

		/**
		 * @param isRewardEligible the isRewardEligible to set
		 */
		public void setIsRewardEligible(Boolean isRewardEligible) {
			this.isRewardEligible = isRewardEligible;
		}

		/**
		 * @return the isDiscountEligible
		 */
		public Boolean getIsDiscountEligible() {
			return isDiscountEligible;
		}

		/**
		 * @param isDiscountEligible the isDiscountEligible to set
		 */
		public void setIsDiscountEligible(Boolean isDiscountEligible) {
			this.isDiscountEligible = isDiscountEligible;
		}

		/**
		 * @return the isPhoneOrderOnly
		 */
		public Boolean getIsPhoneOrderOnly() {
			return isPhoneOrderOnly;
		}

		/**
		 * @param isPhoneOrderOnly the isPhoneOrderOnly to set
		 */
		public void setIsPhoneOrderOnly(Boolean isPhoneOrderOnly) {
			this.isPhoneOrderOnly = isPhoneOrderOnly;
		}

		/**
		 * @return the isApprovedDescription
		 */
		public Boolean getIsApprovedDescription() {
			return isApprovedDescription;
		}

		/**
		 * @param isApprovedDescription the isApprovedDescription to set
		 */
		public void setIsApprovedDescription(Boolean isApprovedDescription) {
			this.isApprovedDescription = isApprovedDescription;
		}

		/**
		 * @return the isEffective
		 */
		public Boolean getIsEffective() {
			return isEffective;
		}

		/**
		 * @param isEffective the isEffective to set
		 */
		public void setIsEffective(Boolean isEffective) {
			this.isEffective = isEffective;
		}

		public String getProductType() {
			return productType;
		}

		public void setProductType(String productType) {
			this.productType = productType;
		}

		public String getBrand() {
			return brand;
		}

		public void setBrand(String brand) {
			this.brand = brand;
		}		
}
