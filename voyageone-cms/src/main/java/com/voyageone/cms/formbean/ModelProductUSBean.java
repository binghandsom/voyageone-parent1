package com.voyageone.cms.formbean;

import com.voyageone.cms.annotation.Extends;

public class ModelProductUSBean extends ModelBaseBean{

	private String productId;
	private String code;
	private String productImgUrl;
	private String name;
	private String displayOrder;
	private boolean isHighLightProduct;
	private String isApprovedFor;
	private String productType;
	private String brand;
	private String sizeType;
	private boolean isNewArrival;
	private boolean isRewardEligible;
	private boolean isDiscountEligible;
	private boolean isPhoneOrderOnly;
	private boolean isApprovedDescription;
	private boolean isEffective;
	private String colorMap;
	private String color;
	private String madeInCountry;
	private String materialFabric1;
	private String materialFabric2;
	private String materialFabric3;
	@Extends
	private String usAbstract;
	private String accessory;
	@Extends
	private String shortDescription;
	@Extends
	private String longDescription;
	private String orderLimitCount;
	private String promotionTag;
	private String quantity;
	private String urlKey;
	private String msrp;
	private String usOfficialPrice;
	private String usOfficialFreeShippingType;
	private boolean usOfficialIsOnSale;
	private boolean usOfficialIsApproved;
	private String usOfficialSneakerRXSales7Days;
	private String usOfficialSneakerRXSales30Days;
	private String usOfficialSneakerRXSalesThisYear;
	private String usOfficialSneakerSales7Days;
	private String usOfficialSneakerSales30Days;
	private String usOfficialSneakerSalesThisYear;
	private String usOfficialSneakerWSSales7Days;
	private String usOfficialSneakerWSSales30Days;
	private String usOfficialSneakerWSSalesThisYear;
	private String usOfficialSneakerMobileSales7Days;
	private String usOfficialSneakerMobileSales30Days;
	private String usOfficialSneakerMobileSalesThisYear;
	private String usAmazonPrice;
	private String usAmazonFreeShippingType;
	private boolean usAmazonIsOnSale;
	private boolean usAmazonIsApproved;
	private String usAmazonPrePublishDateTime;
	private String usAmazonSales7Days;
	private String usAmazonSales30Days;
	private String usAmazonSalesThisYear;
	private String cnPrice;
	private String cnPriceRmb;
	private String cnPriceAdjustmentRmb;
	private String cnPriceFinalRmb;
	private String cnFreeShippingType;
	private boolean cnIsOnSale;
	private boolean cnIsApproved;
	private String cnPrePublishDateTime;
	private String cnSales7Days;
	private String cnSales30Days;
	private String cnSalesThisYear;
	
	private Boolean isPrimaryProduct;

	/**
	 * @return the isPrimaryProduct
	 */
	public Boolean getIsPrimaryProduct() {
		return isPrimaryProduct;
	}

	/**
	 * @param isPrimaryProduct the isPrimaryProduct to set
	 */
	public void setIsPrimaryProduct(Boolean isPrimaryProduct) {
		this.isPrimaryProduct = isPrimaryProduct;
	}

	/**
	 * @return the productId
	 */
	public String getProductId() {
		return productId;
	}

	/**
	 * @param productId
	 *            the productId to set
	 */
	public void setProductId(String productId) {
		this.productId = productId;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
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
	 * @param productImgUrl
	 *            the productImgUrl to set
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
	 * @param name
	 *            the name to set
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
	 * @param displayOrder
	 *            the displayOrder to set
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
	 * @param isHighLightProduct
	 *            the isHighLightProduct to set
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
	 * @param isNewArrival
	 *            the isNewArrival to set
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
	 * @param isRewardEligible
	 *            the isRewardEligible to set
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
	 * @param isDiscountEligible
	 *            the isDiscountEligible to set
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
	 * @param isPhoneOrderOnly
	 *            the isPhoneOrderOnly to set
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
	 * @param isApprovedDescription
	 *            the isApprovedDescription to set
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
	 * @param isEffective
	 *            the isEffective to set
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
	 * @param isApprovedFor
	 *            the isApprovedFor to set
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
	 * @param productType
	 *            the productType to set
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
	 * @param brand
	 *            the brand to set
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
	 * @param sizeType
	 *            the sizeType to set
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
	 * @param colorMap
	 *            the colorMap to set
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
	 * @param color
	 *            the color to set
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
	 * @param madeInCountry
	 *            the madeInCountry to set
	 */
	public void setMadeInCountry(String madeInCountry) {
		this.madeInCountry = madeInCountry;
	}

	/**
	 * @return the materialFabric1
	 */
	public String getMaterialFabric1() {
		return materialFabric1;
	}

	/**
	 * @param materialFabric1
	 *            the materialFabric1 to set
	 */
	public void setMaterialFabric1(String materialFabric1) {
		this.materialFabric1 = materialFabric1;
	}

	/**
	 * @return the materialFabric2
	 */
	public String getMaterialFabric2() {
		return materialFabric2;
	}

	/**
	 * @param materialFabric2
	 *            the materialFabric2 to set
	 */
	public void setMaterialFabric2(String materialFabric2) {
		this.materialFabric2 = materialFabric2;
	}

	/**
	 * @return the materialFabric3
	 */
	public String getMaterialFabric3() {
		return materialFabric3;
	}

	/**
	 * @return the usAbstract
	 */
	public String getUsAbstract() {
		return usAbstract;
	}

	/**
	 * @param usAbstract
	 *            the usAbstract to set
	 */
	public void setUsAbstract(String usAbstract) {
		this.usAbstract = usAbstract;
	}

	/**
	 * @param materialFabric3
	 *            the materialFabric3 to set
	 */
	public void setMaterialFabric3(String materialFabric3) {
		this.materialFabric3 = materialFabric3;
	}

	/**
	 * @return the accessory
	 */
	public String getAccessory() {
		return accessory;
	}

	/**
	 * @param accessory
	 *            the accessory to set
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
	 * @param shortDescription
	 *            the shortDescription to set
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
	 * @param longDescription
	 *            the longDescription to set
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
	 * @param orderLimitCount
	 *            the orderLimitCount to set
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
	 * @param promotionTag
	 *            the promotionTag to set
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
	 * @param quantity
	 *            the quantity to set
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
	 * @param urlKey
	 *            the urlKey to set
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
	 * @param msrp
	 *            the msrp to set
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
	 * @param usOfficialPrice
	 *            the usOfficialPrice to set
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
	 * @param usOfficialFreeShippingType
	 *            the usOfficialFreeShippingType to set
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
	 * @param usOfficialSneakerRXSales7Days
	 *            the usOfficialSneakerRXSales7Days to set
	 */
	public void setUsOfficialSneakerRXSales7Days(String usOfficialSneakerRXSales7Days) {
		this.usOfficialSneakerRXSales7Days = usOfficialSneakerRXSales7Days;
	}

	/**
	 * @return the usOfficialSneakerRXSales30Days
	 */
	public String getUsOfficialSneakerRXSales30Days() {
		return usOfficialSneakerRXSales30Days;
	}

	/**
	 * @param usOfficialSneakerRXSales30Days
	 *            the usOfficialSneakerRXSales30Days to set
	 */
	public void setUsOfficialSneakerRXSales30Days(String usOfficialSneakerRXSales30Days) {
		this.usOfficialSneakerRXSales30Days = usOfficialSneakerRXSales30Days;
	}

	/**
	 * @return the usOfficialSneakerRXSalesThisYear
	 */
	public String getUsOfficialSneakerRXSalesThisYear() {
		return usOfficialSneakerRXSalesThisYear;
	}

	/**
	 * @param usOfficialSneakerRXSalesThisYear
	 *            the usOfficialSneakerRXSalesThisYear to set
	 */
	public void setUsOfficialSneakerRXSalesThisYear(String usOfficialSneakerRXSalesThisYear) {
		this.usOfficialSneakerRXSalesThisYear = usOfficialSneakerRXSalesThisYear;
	}

	/**
	 * @return the usOfficialSneakerSales7Days
	 */
	public String getUsOfficialSneakerSales7Days() {
		return usOfficialSneakerSales7Days;
	}

	/**
	 * @param usOfficialSneakerSales7Days
	 *            the usOfficialSneakerSales7Days to set
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
	 * @param usOfficialSneakerSales30Days
	 *            the usOfficialSneakerSales30Days to set
	 */
	public void setUsOfficialSneakerSales30Days(String usOfficialSneakerSales30Days) {
		this.usOfficialSneakerSales30Days = usOfficialSneakerSales30Days;
	}

	/**
	 * @return the usOfficialSneakerSalesThisYear
	 */
	public String getUsOfficialSneakerSalesThisYear() {
		return usOfficialSneakerSalesThisYear;
	}

	/**
	 * @param usOfficialSneakerSalesThisYear
	 *            the usOfficialSneakerSalesThisYear to set
	 */
	public void setUsOfficialSneakerSalesThisYear(String usOfficialSneakerSalesThisYear) {
		this.usOfficialSneakerSalesThisYear = usOfficialSneakerSalesThisYear;
	}

	/**
	 * @return the usOfficialSneakerWSSales7Days
	 */
	public String getUsOfficialSneakerWSSales7Days() {
		return usOfficialSneakerWSSales7Days;
	}

	/**
	 * @param usOfficialSneakerWSSales7Days
	 *            the usOfficialSneakerWSSales7Days to set
	 */
	public void setUsOfficialSneakerWSSales7Days(String usOfficialSneakerWSSales7Days) {
		this.usOfficialSneakerWSSales7Days = usOfficialSneakerWSSales7Days;
	}

	/**
	 * @return the usOfficialSneakerWSSales30Days
	 */
	public String getUsOfficialSneakerWSSales30Days() {
		return usOfficialSneakerWSSales30Days;
	}

	/**
	 * @param usOfficialSneakerWSSales30Days
	 *            the usOfficialSneakerWSSales30Days to set
	 */
	public void setUsOfficialSneakerWSSales30Days(String usOfficialSneakerWSSales30Days) {
		this.usOfficialSneakerWSSales30Days = usOfficialSneakerWSSales30Days;
	}

	/**
	 * @return the usOfficialSneakerWSSalesThisYear
	 */
	public String getUsOfficialSneakerWSSalesThisYear() {
		return usOfficialSneakerWSSalesThisYear;
	}

	/**
	 * @param usOfficialSneakerWSSalesThisYear
	 *            the usOfficialSneakerWSSalesThisYear to set
	 */
	public void setUsOfficialSneakerWSSalesThisYear(String usOfficialSneakerWSSalesThisYear) {
		this.usOfficialSneakerWSSalesThisYear = usOfficialSneakerWSSalesThisYear;
	}

	/**
	 * @return the usOfficialSneakerMobileSales7Days
	 */
	public String getUsOfficialSneakerMobileSales7Days() {
		return usOfficialSneakerMobileSales7Days;
	}

	/**
	 * @param usOfficialSneakerMobileSales7Days
	 *            the usOfficialSneakerMobileSales7Days to set
	 */
	public void setUsOfficialSneakerMobileSales7Days(String usOfficialSneakerMobileSales7Days) {
		this.usOfficialSneakerMobileSales7Days = usOfficialSneakerMobileSales7Days;
	}

	/**
	 * @return the usOfficialSneakerMobileSales30Days
	 */
	public String getUsOfficialSneakerMobileSales30Days() {
		return usOfficialSneakerMobileSales30Days;
	}

	/**
	 * @param usOfficialSneakerMobileSales30Days
	 *            the usOfficialSneakerMobileSales30Days to set
	 */
	public void setUsOfficialSneakerMobileSales30Days(String usOfficialSneakerMobileSales30Days) {
		this.usOfficialSneakerMobileSales30Days = usOfficialSneakerMobileSales30Days;
	}

	/**
	 * @return the usOfficialSneakerMobileSalesThisYear
	 */
	public String getUsOfficialSneakerMobileSalesThisYear() {
		return usOfficialSneakerMobileSalesThisYear;
	}

	/**
	 * @param usOfficialSneakerMobileSalesThisYear
	 *            the usOfficialSneakerMobileSalesThisYear to set
	 */
	public void setUsOfficialSneakerMobileSalesThisYear(String usOfficialSneakerMobileSalesThisYear) {
		this.usOfficialSneakerMobileSalesThisYear = usOfficialSneakerMobileSalesThisYear;
	}

	/**
	 * @return the usAmazonPrice
	 */
	public String getUsAmazonPrice() {
		return usAmazonPrice;
	}

	/**
	 * @param usAmazonPrice
	 *            the usAmazonPrice to set
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
	 * @param usAmazonFreeShippingType
	 *            the usAmazonFreeShippingType to set
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
	 * @param usAmazonPrePublishDateTime
	 *            the usAmazonPrePublishDateTime to set
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
	 * @param usAmazonSales7Days
	 *            the usAmazonSales7Days to set
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
	 * @param usAmazonSales30Days
	 *            the usAmazonSales30Days to set
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
	 * @param usAmazonSalesThisYear
	 *            the usAmazonSalesThisYear to set
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
	 * @param cnPrice
	 *            the cnPrice to set
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
	 * @param cnPriceRmb
	 *            the cnPriceRmb to set
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
	 * @param cnPriceAdjustmentRmb
	 *            the cnPriceAdjustmentRmb to set
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
	 * @param cnPriceFinalRmb
	 *            the cnPriceFinalRmb to set
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
	 * @param cnFreeShippingType
	 *            the cnFreeShippingType to set
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
	 * @param cnPrePublishDateTime
	 *            the cnPrePublishDateTime to set
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
	 * @param cnSales7Days
	 *            the cnSales7Days to set
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
	 * @param cnSales30Days
	 *            the cnSales30Days to set
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
	 * @param cnSalesThisYear
	 *            the cnSalesThisYear to set
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
	 * @param usOfficialIsOnSale
	 *            the usOfficialIsOnSale to set
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
	 * @param usOfficialIsApproved
	 *            the usOfficialIsApproved to set
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
	 * @param usAmazonIsOnSale
	 *            the usAmazonIsOnSale to set
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
	 * @param usAmazonIsApproved
	 *            the usAmazonIsApproved to set
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
	 * @param cnIsOnSale
	 *            the cnIsOnSale to set
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
	 * @param cnIsApproved
	 *            the cnIsApproved to set
	 */
	public void setCnIsApproved(boolean cnIsApproved) {
		this.cnIsApproved = cnIsApproved;
	}

}
