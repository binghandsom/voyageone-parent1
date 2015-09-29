package com.voyageone.cms.formbean;

import com.voyageone.cms.annotation.Extends;

public class CategoryProductUSBean extends BaseBean{
	private int categoryId;
	private String channelId;
	private String productId;
	private String modelId;
	private String code	;
	private String productImgUrl;
	private String name;
	private String displayOrder	;
	private boolean isHighLightProduct;
	private String isApprovedFor;
	private String productType;
	private String brand;
	private String sizeType	;
	private boolean isNewArrival	;
	private boolean isRewardEligible	;
	private boolean isDiscountEligible;
	private boolean isPhoneOrderOnly	;
	private boolean isApprovedDescription;
	private boolean isEffective;
	private String colorMap	;
	private String color;
	private String madeInCountry;
	private String materialFabric;
	@Extends
	private String usAbstract	;
	@Extends
	private String accessory;
	@Extends
	private String shortDescription	;
	@Extends
	private String longDescription;

	private String cutShortDescription;

	private String cutLongDescription;

	private String orderLimitCount;
	@Extends
	private String promotionTag	;
	private String quantity	;
	private String urlKey;
	private String msrp	;
	private String usOfficialPrice	;
	private String usOfficialFreeShippingType;
	private boolean usOfficialIsOnSale	;
	private boolean usOfficialIsApproved		;

	private String usAmazonPrice;
	private String usAmazonFreeShippingType;
	private boolean usAmazonIsOnSale	;
	private boolean usAmazonIsApproved;
	private String usAmazonPrePublishDateTime;
	private String usAmazonSalesThisYear;
	private String cnPrice	;
	private String cnPriceRmb;
	private String cnPriceAdjustmentRmb;
	private String cnPriceFinalRmb;
	private String cnFreeShippingType;
	private boolean cnIsOnSale;
	private boolean cnIsApproved	;
	private String cnPrePublishDateTime	;
	private String cnSalesThisYear;
	private String imageName;
	private String usOfficialSneakerSales7Days;
	private String usOfficialSneakerSales7DaysPercent;
	private String usOfficialSneakerSales30Days;
	private String usOfficialSneakerSales30DaysPercent;
	private String usOfficialSneakerSalesInThisYear;
	private String usOfficialSneakerSalesInThisYearPercent;
	private String usAmazonSales7Days;
	private String usAmazonSales7DaysPercent;
	private String usAmazonSales30Days;
	private String usAmazonSales30DaysPercent;
	private String usAmazonSalesInThisYear;
	private String usAmazonSalesInThisYearPercent;
	private String cnSales7Days;
	private String cnSales7DaysPercent;
	private String cnSales30Days;
	private String cnSales30DaysPercent;
	private String cnSalesInThisYear;
	private String cnSalesInThisYearPercent;
	private String usOfficialSneakerRXSales7Days;
	private String usOfficialSneakerRXSales7DaysPercent;
	private String usOfficialSneakerRXSales30Days;
	private String usOfficialSneakerRXSales30DaysPercent;
	private String usOfficialSneakerRXSalesInThisYear;
	private String usOfficialSneakerRXSalesInThisYearPercent;
	private String usOfficialSneakerWSSales7Days;
	private String usOfficialSneakerWSSales7DaysPercent;
	private String usOfficialSneakerWSSales30Days;
	private String usOfficialSneakerWSSales30DaysPercent;
	private String usOfficialSneakerWSSalesInThisYear;
	private String usOfficialSneakerWSSalesInThisYearPercent;
	private String usOfficialSneakerMobileSales7Days;
	private String usOfficialSneakerMobileSales7DaysPercent;
	private String usOfficialSneakerMobileSales30Days;
	private String usOfficialSneakerMobileSales30DaysPercent;
	private String usOfficialSneakerMobileSalesInThisYear;
	private String usOfficialSneakerMobileSalesInThisYearPercent;
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	/**
	 * @return the productId
	 */
	public String getProductId() {
		return productId;
	}
	/**
	 * @param productId the productId to set
	 */
	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getModelId() {
		return modelId;
	}
	public void setModelId(String modelId) {
		this.modelId = modelId;
	}
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return the productImgUrl
	 */
	public String getProductImgUrl() {
		return productImgUrl;
	}
	/**
	 * @param productImgUrl the productImgUrl to set
	 */
	public void setProductImgUrl(String productImgUrl) {
		this.productImgUrl = productImgUrl;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the displayOrder
	 */
	public String getDisplayOrder() {
		return displayOrder;
	}
	/**
	 * @param displayOrder the displayOrder to set
	 */
	public void setDisplayOrder(String displayOrder) {
		this.displayOrder = displayOrder;
	}

	/**
	 * @return the isHighLightProduct
	 */
	public boolean isHighLightProduct() {
		return isHighLightProduct;
	}
	/**
	 * @param isHighLightProduct the isHighLightProduct to set
	 */
	public void setHighLightProduct(boolean isHighLightProduct) {
		this.isHighLightProduct = isHighLightProduct;
	}
	/**
	 * @return the isNewArrival
	 */
	public boolean isNewArrival() {
		return isNewArrival;
	}
	/**
	 * @param isNewArrival the isNewArrival to set
	 */
	public void setNewArrival(boolean isNewArrival) {
		this.isNewArrival = isNewArrival;
	}
	/**
	 * @return the isRewardEligible
	 */
	public boolean isRewardEligible() {
		return isRewardEligible;
	}
	/**
	 * @param isRewardEligible the isRewardEligible to set
	 */
	public void setRewardEligible(boolean isRewardEligible) {
		this.isRewardEligible = isRewardEligible;
	}
	/**
	 * @return the isDiscountEligible
	 */
	public boolean isDiscountEligible() {
		return isDiscountEligible;
	}
	/**
	 * @param isDiscountEligible the isDiscountEligible to set
	 */
	public void setDiscountEligible(boolean isDiscountEligible) {
		this.isDiscountEligible = isDiscountEligible;
	}
	/**
	 * @return the isPhoneOrderOnly
	 */
	public boolean isPhoneOrderOnly() {
		return isPhoneOrderOnly;
	}
	/**
	 * @param isPhoneOrderOnly the isPhoneOrderOnly to set
	 */
	public void setPhoneOrderOnly(boolean isPhoneOrderOnly) {
		this.isPhoneOrderOnly = isPhoneOrderOnly;
	}
	/**
	 * @return the isApprovedDescription
	 */
	public boolean isApprovedDescription() {
		return isApprovedDescription;
	}
	/**
	 * @param isApprovedDescription the isApprovedDescription to set
	 */
	public void setApprovedDescription(boolean isApprovedDescription) {
		this.isApprovedDescription = isApprovedDescription;
	}
	/**
	 * @return the isEffective
	 */
	public boolean isEffective() {
		return isEffective;
	}
	/**
	 * @param isEffective the isEffective to set
	 */
	public void setEffective(boolean isEffective) {
		this.isEffective = isEffective;
	}
	/**
	 * @return the isApprovedFor
	 */
	public String getIsApprovedFor() {
		return isApprovedFor;
	}
	/**
	 * @param isApprovedFor the isApprovedFor to set
	 */
	public void setIsApprovedFor(String isApprovedFor) {
		this.isApprovedFor = isApprovedFor;
	}
	/**
	 * @return the productType
	 */
	public String getProductType() {
		return productType;
	}
	/**
	 * @param productType the productType to set
	 */
	public void setProductType(String productType) {
		this.productType = productType;
	}
	/**
	 * @return the brand
	 */
	public String getBrand() {
		return brand;
	}
	/**
	 * @param brand the brand to set
	 */
	public void setBrand(String brand) {
		this.brand = brand;
	}
	/**
	 * @return the sizeType
	 */
	public String getSizeType() {
		return sizeType;
	}
	/**
	 * @param sizeType the sizeType to set
	 */
	public void setSizeType(String sizeType) {
		this.sizeType = sizeType;
	}

	/**
	 * @return the colorMap
	 */
	public String getColorMap() {
		return colorMap;
	}
	/**
	 * @param colorMap the colorMap to set
	 */
	public void setColorMap(String colorMap) {
		this.colorMap = colorMap;
	}
	/**
	 * @return the color
	 */
	public String getColor() {
		return color;
	}
	/**
	 * @param color the color to set
	 */
	public void setColor(String color) {
		this.color = color;
	}
	/**
	 * @return the madeInCountry
	 */
	public String getMadeInCountry() {
		return madeInCountry;
	}
	/**
	 * @param madeInCountry the madeInCountry to set
	 */
	public void setMadeInCountry(String madeInCountry) {
		this.madeInCountry = madeInCountry;
	}
	/**
	 * @return the materialFabric
	 */
	public String getMaterialFabric() {
		return materialFabric;
	}
	/**
	 * @param materialFabric the materialFabric1 to set
	 */
	public void setMaterialFabric(String materialFabric) {
		this.materialFabric = materialFabric;
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
	 * @return the shortDescription
	 */
	public String getShortDescription() {
		return shortDescription;
	}
	/**
	 * @param shortDescription the shortDescription to set
	 */
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}
	/**
	 * @return the longDescription
	 */
	public String getLongDescription() {
		return longDescription;
	}
	/**
	 * @param longDescription the longDescription to set
	 */
	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}
	/**
	 * @return the orderLimitCount
	 */
	public String getOrderLimitCount() {
		return orderLimitCount;
	}
	/**
	 * @param orderLimitCount the orderLimitCount to set
	 */
	public void setOrderLimitCount(String orderLimitCount) {
		this.orderLimitCount = orderLimitCount;
	}
	/**
	 * @return the promotionTag
	 */
	public String getPromotionTag() {
		return promotionTag;
	}
	/**
	 * @param promotionTag the promotionTag to set
	 */
	public void setPromotionTag(String promotionTag) {
		this.promotionTag = promotionTag;
	}
	/**
	 * @return the quantity
	 */
	public String getQuantity() {
		return quantity;
	}
	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	/**
	 * @return the urlKey
	 */
	public String getUrlKey() {
		return urlKey;
	}
	/**
	 * @param urlKey the urlKey to set
	 */
	public void setUrlKey(String urlKey) {
		this.urlKey = urlKey;
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
	 * @return the usOfficialPrice
	 */
	public String getUsOfficialPrice() {
		return usOfficialPrice;
	}
	/**
	 * @param usOfficialPrice the usOfficialPrice to set
	 */
	public void setUsOfficialPrice(String usOfficialPrice) {
		this.usOfficialPrice = usOfficialPrice;
	}
	/**
	 * @return the usOfficialFreeShippingType
	 */
	public String getUsOfficialFreeShippingType() {
		return usOfficialFreeShippingType;
	}
	/**
	 * @param usOfficialFreeShippingType the usOfficialFreeShippingType to set
	 */
	public void setUsOfficialFreeShippingType(String usOfficialFreeShippingType) {
		this.usOfficialFreeShippingType = usOfficialFreeShippingType;
	}

	/**
	 * @return the usOfficialSneakerRXSales7Days
	 */
	public String getUsOfficialSneakerRXSales7Days() {
		return usOfficialSneakerRXSales7Days;
	}
	/**
	 * @param usOfficialSneakerRXSales7Days the usOfficialSneakerRXSales7Days to set
	 */
	public void setUsOfficialSneakerRXSales7Days(
			String usOfficialSneakerRXSales7Days) {
		this.usOfficialSneakerRXSales7Days = usOfficialSneakerRXSales7Days;
	}
	/**
	 * @return the usOfficialSneakerRXSales30Days
	 */
	public String getUsOfficialSneakerRXSales30Days() {
		return usOfficialSneakerRXSales30Days;
	}
	/**
	 * @param usOfficialSneakerRXSales30Days the usOfficialSneakerRXSales30Days to set
	 */
	public void setUsOfficialSneakerRXSales30Days(
			String usOfficialSneakerRXSales30Days) {
		this.usOfficialSneakerRXSales30Days = usOfficialSneakerRXSales30Days;
	}
	/**
	 * @return the usOfficialSneakerSales7Days
	 */
	public String getUsOfficialSneakerSales7Days() {
		return usOfficialSneakerSales7Days;
	}
	/**
	 * @param usOfficialSneakerSales7Days the usOfficialSneakerSales7Days to set
	 */
	public void setUsOfficialSneakerSales7Days(String usOfficialSneakerSales7Days) {
		this.usOfficialSneakerSales7Days = usOfficialSneakerSales7Days;
	}
	/**
	 * @return the usOfficialSneakerSales30Days
	 */
	public String getUsOfficialSneakerSales30Days() {
		return usOfficialSneakerSales30Days;
	}
	/**
	 * @param usOfficialSneakerSales30Days the usOfficialSneakerSales30Days to set
	 */
	public void setUsOfficialSneakerSales30Days(String usOfficialSneakerSales30Days) {
		this.usOfficialSneakerSales30Days = usOfficialSneakerSales30Days;
	}
	/**
	 * @return the usOfficialSneakerWSSales7Days
	 */
	public String getUsOfficialSneakerWSSales7Days() {
		return usOfficialSneakerWSSales7Days;
	}
	/**
	 * @param usOfficialSneakerWSSales7Days the usOfficialSneakerWSSales7Days to set
	 */
	public void setUsOfficialSneakerWSSales7Days(
			String usOfficialSneakerWSSales7Days) {
		this.usOfficialSneakerWSSales7Days = usOfficialSneakerWSSales7Days;
	}
	/**
	 * @return the usOfficialSneakerWSSales30Days
	 */
	public String getUsOfficialSneakerWSSales30Days() {
		return usOfficialSneakerWSSales30Days;
	}
	/**
	 * @param usOfficialSneakerWSSales30Days the usOfficialSneakerWSSales30Days to set
	 */
	public void setUsOfficialSneakerWSSales30Days(
			String usOfficialSneakerWSSales30Days) {
		this.usOfficialSneakerWSSales30Days = usOfficialSneakerWSSales30Days;
	}
	/**
	 * @return the usOfficialSneakerMobileSales7Days
	 */
	public String getUsOfficialSneakerMobileSales7Days() {
		return usOfficialSneakerMobileSales7Days;
	}
	/**
	 * @param usOfficialSneakerMobileSales7Days the usOfficialSneakerMobileSales7Days to set
	 */
	public void setUsOfficialSneakerMobileSales7Days(
			String usOfficialSneakerMobileSales7Days) {
		this.usOfficialSneakerMobileSales7Days = usOfficialSneakerMobileSales7Days;
	}
	/**
	 * @return the usOfficialSneakerMobileSales30Days
	 */
	public String getUsOfficialSneakerMobileSales30Days() {
		return usOfficialSneakerMobileSales30Days;
	}
	/**
	 * @param usOfficialSneakerMobileSales30Days the usOfficialSneakerMobileSales30Days to set
	 */
	public void setUsOfficialSneakerMobileSales30Days(
			String usOfficialSneakerMobileSales30Days) {
		this.usOfficialSneakerMobileSales30Days = usOfficialSneakerMobileSales30Days;
	}
	/**
	 * @return the usAmazonPrice
	 */
	public String getUsAmazonPrice() {
		return usAmazonPrice;
	}
	/**
	 * @param usAmazonPrice the usAmazonPrice to set
	 */
	public void setUsAmazonPrice(String usAmazonPrice) {
		this.usAmazonPrice = usAmazonPrice;
	}
	/**
	 * @return the usAmazonFreeShippingType
	 */
	public String getUsAmazonFreeShippingType() {
		return usAmazonFreeShippingType;
	}
	/**
	 * @param usAmazonFreeShippingType the usAmazonFreeShippingType to set
	 */
	public void setUsAmazonFreeShippingType(String usAmazonFreeShippingType) {
		this.usAmazonFreeShippingType = usAmazonFreeShippingType;
	}

	/**
	 * @return the usAmazonPrePublishDateTime
	 */
	public String getUsAmazonPrePublishDateTime() {
		return usAmazonPrePublishDateTime;
	}
	/**
	 * @param usAmazonPrePublishDateTime the usAmazonPrePublishDateTime to set
	 */
	public void setUsAmazonPrePublishDateTime(String usAmazonPrePublishDateTime) {
		this.usAmazonPrePublishDateTime = usAmazonPrePublishDateTime;
	}
	/**
	 * @return the usAmazonSales7Days
	 */
	public String getUsAmazonSales7Days() {
		return usAmazonSales7Days;
	}
	/**
	 * @param usAmazonSales7Days the usAmazonSales7Days to set
	 */
	public void setUsAmazonSales7Days(String usAmazonSales7Days) {
		this.usAmazonSales7Days = usAmazonSales7Days;
	}
	/**
	 * @return the usAmazonSales30Days
	 */
	public String getUsAmazonSales30Days() {
		return usAmazonSales30Days;
	}
	/**
	 * @param usAmazonSales30Days the usAmazonSales30Days to set
	 */
	public void setUsAmazonSales30Days(String usAmazonSales30Days) {
		this.usAmazonSales30Days = usAmazonSales30Days;
	}
	/**
	 * @return the usAmazonSalesThisYear
	 */
	public String getUsAmazonSalesThisYear() {
		return usAmazonSalesThisYear;
	}
	/**
	 * @param usAmazonSalesThisYear the usAmazonSalesThisYear to set
	 */
	public void setUsAmazonSalesThisYear(String usAmazonSalesThisYear) {
		this.usAmazonSalesThisYear = usAmazonSalesThisYear;
	}
	/**
	 * @return the cnPrice
	 */
	public String getCnPrice() {
		return cnPrice;
	}
	/**
	 * @param cnPrice the cnPrice to set
	 */
	public void setCnPrice(String cnPrice) {
		this.cnPrice = cnPrice;
	}
	/**
	 * @return the cnPriceRmb
	 */
	public String getCnPriceRmb() {
		return cnPriceRmb;
	}
	/**
	 * @param cnPriceRmb the cnPriceRmb to set
	 */
	public void setCnPriceRmb(String cnPriceRmb) {
		this.cnPriceRmb = cnPriceRmb;
	}
	/**
	 * @return the cnPriceAdjustmentRmb
	 */
	public String getCnPriceAdjustmentRmb() {
		return cnPriceAdjustmentRmb;
	}
	/**
	 * @param cnPriceAdjustmentRmb the cnPriceAdjustmentRmb to set
	 */
	public void setCnPriceAdjustmentRmb(String cnPriceAdjustmentRmb) {
		this.cnPriceAdjustmentRmb = cnPriceAdjustmentRmb;
	}
	/**
	 * @return the cnPriceFinalRmb
	 */
	public String getCnPriceFinalRmb() {
		return cnPriceFinalRmb;
	}
	/**
	 * @param cnPriceFinalRmb the cnPriceFinalRmb to set
	 */
	public void setCnPriceFinalRmb(String cnPriceFinalRmb) {
		this.cnPriceFinalRmb = cnPriceFinalRmb;
	}
	/**
	 * @return the cnFreeShippingType
	 */
	public String getCnFreeShippingType() {
		return cnFreeShippingType;
	}
	/**
	 * @param cnFreeShippingType the cnFreeShippingType to set
	 */
	public void setCnFreeShippingType(String cnFreeShippingType) {
		this.cnFreeShippingType = cnFreeShippingType;
	}

	/**
	 * @return the cnPrePublishDateTime
	 */
	public String getCnPrePublishDateTime() {
		return cnPrePublishDateTime;
	}
	/**
	 * @param cnPrePublishDateTime the cnPrePublishDateTime to set
	 */
	public void setCnPrePublishDateTime(String cnPrePublishDateTime) {
		this.cnPrePublishDateTime = cnPrePublishDateTime;
	}
	/**
	 * @return the cnSales7Days
	 */
	public String getCnSales7Days() {
		return cnSales7Days;
	}
	/**
	 * @param cnSales7Days the cnSales7Days to set
	 */
	public void setCnSales7Days(String cnSales7Days) {
		this.cnSales7Days = cnSales7Days;
	}
	/**
	 * @return the cnSales30Days
	 */
	public String getCnSales30Days() {
		return cnSales30Days;
	}
	/**
	 * @param cnSales30Days the cnSales30Days to set
	 */
	public void setCnSales30Days(String cnSales30Days) {
		this.cnSales30Days = cnSales30Days;
	}
	/**
	 * @return the cnSalesThisYear
	 */
	public String getCnSalesThisYear() {
		return cnSalesThisYear;
	}
	/**
	 * @param cnSalesThisYear the cnSalesThisYear to set
	 */
	public void setCnSalesThisYear(String cnSalesThisYear) {
		this.cnSalesThisYear = cnSalesThisYear;
	}
	/**
	 * @return the usOfficialIsOnSale
	 */
	public boolean isUsOfficialIsOnSale() {
		return usOfficialIsOnSale;
	}
	/**
	 * @param usOfficialIsOnSale the usOfficialIsOnSale to set
	 */
	public void setUsOfficialIsOnSale(boolean usOfficialIsOnSale) {
		this.usOfficialIsOnSale = usOfficialIsOnSale;
	}
	/**
	 * @return the usOfficialIsApproved
	 */
	public boolean isUsOfficialIsApproved() {
		return usOfficialIsApproved;
	}
	/**
	 * @param usOfficialIsApproved the usOfficialIsApproved to set
	 */
	public void setUsOfficialIsApproved(boolean usOfficialIsApproved) {
		this.usOfficialIsApproved = usOfficialIsApproved;
	}
	/**
	 * @return the usAmazonIsOnSale
	 */
	public boolean isUsAmazonIsOnSale() {
		return usAmazonIsOnSale;
	}
	/**
	 * @param usAmazonIsOnSale the usAmazonIsOnSale to set
	 */
	public void setUsAmazonIsOnSale(boolean usAmazonIsOnSale) {
		this.usAmazonIsOnSale = usAmazonIsOnSale;
	}
	/**
	 * @return the usAmazonIsApproved
	 */
	public boolean isUsAmazonIsApproved() {
		return usAmazonIsApproved;
	}
	/**
	 * @param usAmazonIsApproved the usAmazonIsApproved to set
	 */
	public void setUsAmazonIsApproved(boolean usAmazonIsApproved) {
		this.usAmazonIsApproved = usAmazonIsApproved;
	}
	/**
	 * @return the cnIsOnSale
	 */
	public boolean isCnIsOnSale() {
		return cnIsOnSale;
	}
	/**
	 * @param cnIsOnSale the cnIsOnSale to set
	 */
	public void setCnIsOnSale(boolean cnIsOnSale) {
		this.cnIsOnSale = cnIsOnSale;
	}
	/**
	 * @return the cnIsApproved
	 */
	public boolean isCnIsApproved() {
		return cnIsApproved;
	}
	/**
	 * @param cnIsApproved the cnIsApproved to set
	 */
	public void setCnIsApproved(boolean cnIsApproved) {
		this.cnIsApproved = cnIsApproved;
	}


	public String getCutShortDescription() {
		return cutShortDescription;
	}
	public void setCutShortDescription(String cutShortDescription) {
		this.cutShortDescription = cutShortDescription;
	}
	public String getCutLongDescription() {
		return cutLongDescription;
	}
	public void setCutLongDescription(String cutLongDescription) {
		this.cutLongDescription = cutLongDescription;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	/**
	 * @return the usOfficialSneakerSales7DaysPercent
	 */
	public String getUsOfficialSneakerSales7DaysPercent() {
		return usOfficialSneakerSales7DaysPercent;
	}
	/**
	 * @param usOfficialSneakerSales7DaysPercent the usOfficialSneakerSales7DaysPercent to set
	 */
	public void setUsOfficialSneakerSales7DaysPercent(String usOfficialSneakerSales7DaysPercent) {
		this.usOfficialSneakerSales7DaysPercent = usOfficialSneakerSales7DaysPercent;
	}
	/**
	 * @return the usOfficialSneakerSales30DaysPercent
	 */
	public String getUsOfficialSneakerSales30DaysPercent() {
		return usOfficialSneakerSales30DaysPercent;
	}
	/**
	 * @param usOfficialSneakerSales30DaysPercent the usOfficialSneakerSales30DaysPercent to set
	 */
	public void setUsOfficialSneakerSales30DaysPercent(String usOfficialSneakerSales30DaysPercent) {
		this.usOfficialSneakerSales30DaysPercent = usOfficialSneakerSales30DaysPercent;
	}
	/**
	 * @return the usOfficialSneakerSalesInThisYear
	 */
	public String getUsOfficialSneakerSalesInThisYear() {
		return usOfficialSneakerSalesInThisYear;
	}
	/**
	 * @param usOfficialSneakerSalesInThisYear the usOfficialSneakerSalesInThisYear to set
	 */
	public void setUsOfficialSneakerSalesInThisYear(String usOfficialSneakerSalesInThisYear) {
		this.usOfficialSneakerSalesInThisYear = usOfficialSneakerSalesInThisYear;
	}
	/**
	 * @return the usOfficialSneakerSalesInThisYearPercent
	 */
	public String getUsOfficialSneakerSalesInThisYearPercent() {
		return usOfficialSneakerSalesInThisYearPercent;
	}
	/**
	 * @param usOfficialSneakerSalesInThisYearPercent the usOfficialSneakerSalesInThisYearPercent to set
	 */
	public void setUsOfficialSneakerSalesInThisYearPercent(String usOfficialSneakerSalesInThisYearPercent) {
		this.usOfficialSneakerSalesInThisYearPercent = usOfficialSneakerSalesInThisYearPercent;
	}
	/**
	 * @return the usAmazonSales7DaysPercent
	 */
	public String getUsAmazonSales7DaysPercent() {
		return usAmazonSales7DaysPercent;
	}
	/**
	 * @param usAmazonSales7DaysPercent the usAmazonSales7DaysPercent to set
	 */
	public void setUsAmazonSales7DaysPercent(String usAmazonSales7DaysPercent) {
		this.usAmazonSales7DaysPercent = usAmazonSales7DaysPercent;
	}
	/**
	 * @return the usAmazonSales30DaysPercent
	 */
	public String getUsAmazonSales30DaysPercent() {
		return usAmazonSales30DaysPercent;
	}
	/**
	 * @param usAmazonSales30DaysPercent the usAmazonSales30DaysPercent to set
	 */
	public void setUsAmazonSales30DaysPercent(String usAmazonSales30DaysPercent) {
		this.usAmazonSales30DaysPercent = usAmazonSales30DaysPercent;
	}
	/**
	 * @return the usAmazonSalesInThisYear
	 */
	public String getUsAmazonSalesInThisYear() {
		return usAmazonSalesInThisYear;
	}
	/**
	 * @param usAmazonSalesInThisYear the usAmazonSalesInThisYear to set
	 */
	public void setUsAmazonSalesInThisYear(String usAmazonSalesInThisYear) {
		this.usAmazonSalesInThisYear = usAmazonSalesInThisYear;
	}
	/**
	 * @return the usAmazonSalesInThisYearPercent
	 */
	public String getUsAmazonSalesInThisYearPercent() {
		return usAmazonSalesInThisYearPercent;
	}
	/**
	 * @param usAmazonSalesInThisYearPercent the usAmazonSalesInThisYearPercent to set
	 */
	public void setUsAmazonSalesInThisYearPercent(String usAmazonSalesInThisYearPercent) {
		this.usAmazonSalesInThisYearPercent = usAmazonSalesInThisYearPercent;
	}
	/**
	 * @return the cnSales7DaysPercent
	 */
	public String getCnSales7DaysPercent() {
		return cnSales7DaysPercent;
	}
	/**
	 * @param cnSales7DaysPercent the cnSales7DaysPercent to set
	 */
	public void setCnSales7DaysPercent(String cnSales7DaysPercent) {
		this.cnSales7DaysPercent = cnSales7DaysPercent;
	}
	/**
	 * @return the cnSales30DaysPercent
	 */
	public String getCnSales30DaysPercent() {
		return cnSales30DaysPercent;
	}
	/**
	 * @param cnSales30DaysPercent the cnSales30DaysPercent to set
	 */
	public void setCnSales30DaysPercent(String cnSales30DaysPercent) {
		this.cnSales30DaysPercent = cnSales30DaysPercent;
	}
	/**
	 * @return the cnSalesInThisYear
	 */
	public String getCnSalesInThisYear() {
		return cnSalesInThisYear;
	}
	/**
	 * @param cnSalesInThisYear the cnSalesInThisYear to set
	 */
	public void setCnSalesInThisYear(String cnSalesInThisYear) {
		this.cnSalesInThisYear = cnSalesInThisYear;
	}
	/**
	 * @return the cnSalesInThisYearPercent
	 */
	public String getCnSalesInThisYearPercent() {
		return cnSalesInThisYearPercent;
	}
	/**
	 * @param cnSalesInThisYearPercent the cnSalesInThisYearPercent to set
	 */
	public void setCnSalesInThisYearPercent(String cnSalesInThisYearPercent) {
		this.cnSalesInThisYearPercent = cnSalesInThisYearPercent;
	}
	/**
	 * @return the usOfficialSneakerRXSales7DaysPercent
	 */
	public String getUsOfficialSneakerRXSales7DaysPercent() {
		return usOfficialSneakerRXSales7DaysPercent;
	}
	/**
	 * @param usOfficialSneakerRXSales7DaysPercent the usOfficialSneakerRXSales7DaysPercent to set
	 */
	public void setUsOfficialSneakerRXSales7DaysPercent(String usOfficialSneakerRXSales7DaysPercent) {
		this.usOfficialSneakerRXSales7DaysPercent = usOfficialSneakerRXSales7DaysPercent;
	}
	/**
	 * @return the usOfficialSneakerRXSales30DaysPercent
	 */
	public String getUsOfficialSneakerRXSales30DaysPercent() {
		return usOfficialSneakerRXSales30DaysPercent;
	}
	/**
	 * @param usOfficialSneakerRXSales30DaysPercent the usOfficialSneakerRXSales30DaysPercent to set
	 */
	public void setUsOfficialSneakerRXSales30DaysPercent(String usOfficialSneakerRXSales30DaysPercent) {
		this.usOfficialSneakerRXSales30DaysPercent = usOfficialSneakerRXSales30DaysPercent;
	}
	/**
	 * @return the usOfficialSneakerRXSalesInThisYear
	 */
	public String getUsOfficialSneakerRXSalesInThisYear() {
		return usOfficialSneakerRXSalesInThisYear;
	}
	/**
	 * @param usOfficialSneakerRXSalesInThisYear the usOfficialSneakerRXSalesInThisYear to set
	 */
	public void setUsOfficialSneakerRXSalesInThisYear(String usOfficialSneakerRXSalesInThisYear) {
		this.usOfficialSneakerRXSalesInThisYear = usOfficialSneakerRXSalesInThisYear;
	}
	/**
	 * @return the usOfficialSneakerRXSalesInThisYearPercent
	 */
	public String getUsOfficialSneakerRXSalesInThisYearPercent() {
		return usOfficialSneakerRXSalesInThisYearPercent;
	}
	/**
	 * @param usOfficialSneakerRXSalesInThisYearPercent the usOfficialSneakerRXSalesInThisYearPercent to set
	 */
	public void setUsOfficialSneakerRXSalesInThisYearPercent(String usOfficialSneakerRXSalesInThisYearPercent) {
		this.usOfficialSneakerRXSalesInThisYearPercent = usOfficialSneakerRXSalesInThisYearPercent;
	}
	/**
	 * @return the usOfficialSneakerWSSales7DaysPercent
	 */
	public String getUsOfficialSneakerWSSales7DaysPercent() {
		return usOfficialSneakerWSSales7DaysPercent;
	}
	/**
	 * @param usOfficialSneakerWSSales7DaysPercent the usOfficialSneakerWSSales7DaysPercent to set
	 */
	public void setUsOfficialSneakerWSSales7DaysPercent(String usOfficialSneakerWSSales7DaysPercent) {
		this.usOfficialSneakerWSSales7DaysPercent = usOfficialSneakerWSSales7DaysPercent;
	}
	/**
	 * @return the usOfficialSneakerWSSales30DaysPercent
	 */
	public String getUsOfficialSneakerWSSales30DaysPercent() {
		return usOfficialSneakerWSSales30DaysPercent;
	}
	/**
	 * @param usOfficialSneakerWSSales30DaysPercent the usOfficialSneakerWSSales30DaysPercent to set
	 */
	public void setUsOfficialSneakerWSSales30DaysPercent(String usOfficialSneakerWSSales30DaysPercent) {
		this.usOfficialSneakerWSSales30DaysPercent = usOfficialSneakerWSSales30DaysPercent;
	}
	/**
	 * @return the usOfficialSneakerWSSalesInThisYear
	 */
	public String getUsOfficialSneakerWSSalesInThisYear() {
		return usOfficialSneakerWSSalesInThisYear;
	}
	/**
	 * @param usOfficialSneakerWSSalesInThisYear the usOfficialSneakerWSSalesInThisYear to set
	 */
	public void setUsOfficialSneakerWSSalesInThisYear(String usOfficialSneakerWSSalesInThisYear) {
		this.usOfficialSneakerWSSalesInThisYear = usOfficialSneakerWSSalesInThisYear;
	}
	/**
	 * @return the usOfficialSneakerWSSalesInThisYearPercent
	 */
	public String getUsOfficialSneakerWSSalesInThisYearPercent() {
		return usOfficialSneakerWSSalesInThisYearPercent;
	}
	/**
	 * @param usOfficialSneakerWSSalesInThisYearPercent the usOfficialSneakerWSSalesInThisYearPercent to set
	 */
	public void setUsOfficialSneakerWSSalesInThisYearPercent(String usOfficialSneakerWSSalesInThisYearPercent) {
		this.usOfficialSneakerWSSalesInThisYearPercent = usOfficialSneakerWSSalesInThisYearPercent;
	}
	/**
	 * @return the usOfficialSneakerMobileSales7DaysPercent
	 */
	public String getUsOfficialSneakerMobileSales7DaysPercent() {
		return usOfficialSneakerMobileSales7DaysPercent;
	}
	/**
	 * @param usOfficialSneakerMobileSales7DaysPercent the usOfficialSneakerMobileSales7DaysPercent to set
	 */
	public void setUsOfficialSneakerMobileSales7DaysPercent(String usOfficialSneakerMobileSales7DaysPercent) {
		this.usOfficialSneakerMobileSales7DaysPercent = usOfficialSneakerMobileSales7DaysPercent;
	}
	/**
	 * @return the usOfficialSneakerMobileSales30DaysPercent
	 */
	public String getUsOfficialSneakerMobileSales30DaysPercent() {
		return usOfficialSneakerMobileSales30DaysPercent;
	}
	/**
	 * @param usOfficialSneakerMobileSales30DaysPercent the usOfficialSneakerMobileSales30DaysPercent to set
	 */
	public void setUsOfficialSneakerMobileSales30DaysPercent(String usOfficialSneakerMobileSales30DaysPercent) {
		this.usOfficialSneakerMobileSales30DaysPercent = usOfficialSneakerMobileSales30DaysPercent;
	}
	/**
	 * @return the usOfficialSneakerMobileSalesInThisYear
	 */
	public String getUsOfficialSneakerMobileSalesInThisYear() {
		return usOfficialSneakerMobileSalesInThisYear;
	}
	/**
	 * @param usOfficialSneakerMobileSalesInThisYear the usOfficialSneakerMobileSalesInThisYear to set
	 */
	public void setUsOfficialSneakerMobileSalesInThisYear(String usOfficialSneakerMobileSalesInThisYear) {
		this.usOfficialSneakerMobileSalesInThisYear = usOfficialSneakerMobileSalesInThisYear;
	}
	/**
	 * @return the usOfficialSneakerMobileSalesInThisYearPercent
	 */
	public String getUsOfficialSneakerMobileSalesInThisYearPercent() {
		return usOfficialSneakerMobileSalesInThisYearPercent;
	}
	/**
	 * @param usOfficialSneakerMobileSalesInThisYearPercent the usOfficialSneakerMobileSalesInThisYearPercent to set
	 */
	public void setUsOfficialSneakerMobileSalesInThisYearPercent(String usOfficialSneakerMobileSalesInThisYearPercent) {
		this.usOfficialSneakerMobileSalesInThisYearPercent = usOfficialSneakerMobileSalesInThisYearPercent;
	}
}
