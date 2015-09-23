package com.voyageone.cms.formbean;

import com.voyageone.cms.annotation.Extends;

public class CategoryProductCNBean {
	private String categoryId;
	private String productId;
	private String channelId;
	private String modelId;
	private String code		;
	private String productImgUrl;
	private String cnName		;
	private String cnDisplayOrder;
	private boolean cnIsHightLightProduct;
	private String isApprovedFor;
	private String productType	;
	private String brand;
	private String cnSizeType;
	private boolean isApprovedDescription;
	private boolean isEffective;
	private String colorMap;
	private String cnColor;
	private String madeInCountry;
	private String materialFabric;
	@Extends
	private String cnAbstract;
	@Extends
	private String cnShortDescription;
	@Extends
	private String cnLongDescription;
	private String cutCnShortDescription;
	private String cutCnLongDescription;
	private String quantity;
	private String urlKey;
	private String created;
	private String creater;
	private String modified;
	private String modifier;
	@Extends
	private String referenceMsrp;
	@Extends
	private String referencePrice;
	private String firstSalePrice;
	private String cnPrice;
	private String effectivePrice;
	private String cnPriceRmb;
	private String cnPriceAdjustmentRmb;
	private String cnPriceFinalRmb;
	private boolean cnIsOnSale;
	private boolean cnIsApproved;
	private String cnOfficialPriceAdjustmentRmb;
	private String cnOfficialPriceFinalRmb;
	private String cnOfficialFreeShippingType;
	private boolean cnOfficialIsOnSale;
	private boolean cnOfficialIsApproved;
	private String cnOfficialPrePublishDateTime;
	private String cnOfficialSalesThisYear;
	private String tbPriceAdjustmentRmb;
	private String tbPriceFinalRmb;
	private String tbFreeShippingType;
	private boolean tbIsOnSale;
	private boolean tbIsApproved;
	private String tbPrePublishDateTime;
	private String tbSalesThisYear;
	private String tmPriceAdjustmentRmb;
	private String tmPriceFinalRmb;
	private String tmFreeShippingType;
	private boolean tmIsOnSale;
	private boolean tmIsApproved;
	private String tmPrePublishDateTime;
	private String tmSalesThisYear;
	private String tgPriceAdjustmentRmb	;
	private String tgPriceFinalRmb;
	private String tgFreeShippingType;
	private boolean tgIsOnSale;
	private boolean tgIsApproved;
	private String tgPrePublishDateTime	;
	private String tgSalesThisYear;
	private String jdPriceAdjustmentRmb;
	private String jdPriceFinalRmb;
	private String jdFreeShippingType;
	private boolean jdIsOnSale;
	private boolean jdIsApproved;
	private String jdPrePublishDateTime;
	private String jgPriceAdjustmentRmb;
	private String jgPriceFinalRmb;
	private String jgFreeShippingType;
	private boolean jgIsOnSale;
	private boolean jgIsApproved;
	private String jgPrePublishDateTime;
	private String imageName;

	private String cnSales30Days;	
	private String cnSales30DaysPercent;
	private String cnSales7Days;
	private String cnSales7DaysPercent;
	private String cnSalesAllDays;
	private String cnSalesAllDaysPercent;
	private String cnOfficialSales30Days;
	private String cnOfficialSales30DaysPercent;
	private String cnOfficialSales7Days;
	private String cnOfficialSales7DaysPercent;
	private String cnOfficialSalesAllDays;
	private String cnOfficialSalesAllDaysPercent;	
	private String tbSales30Days;
	private String tbSales30DaysPercent;
	private String tbSales7Days;
	private String tbSales7DaysPercent;
	private String tbSalesAllDays;
	private String tbSalesAllDaysPercent;
	private String tmSales30Days;
	private String tmSales30DaysPercent;
	private String tmSales7Days;
	private String tmSales7DaysPercent;
	private String tmSalesAllDays;
	private String tmSalesAllDaysPercent;	
	private String tgSales30Days;
	private String tgSales30DaysPercent;
	private String tgSales7Days;
	private String tgSales7DaysPercent;
	private String tgSalesAllDays;
	private String tgSalesAllDaysPercent;
	private String jdSales30Days;
	private String jdSales30DaysPercent;
	private String jdSales7Days;
	private String jdSales7DaysPercent;
	private String jdSalesAllDays;
	private String jdSalesAllDaysPercent;	
	private String jgSales30Days;
	private String jgSales30DaysPercent;
	private String jgSales7Days;
	private String jgSales7DaysPercent;
	private String jgSalesAllDays;
	private String jgSalesAllDaysPercent;	
	private String cnSalesInThisYear;
	private String cnSalesInThisYearPercent;
	private String cnOfficialSalesInThisYear;
	private String cnOfficialSalesInThisYearPercent;
	private String tbSalesInThisYear;
	private String tbSalesInThisYearPercent;
	private String tmSalesInThisYear;
	private String tmSalesInThisYearPercent;
	private String tgSalesInThisYear;
	private String tgSalesInThisYearPercent;
	private String jdSalesInThisYear;
	private String jdSalesInThisYearPercent;
	private String jgSalesInThisYear;
	private String jgSalesInThisYearPercent;
	
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getModelId() {
		return modelId;
	}
	public void setModelId(String modelId) {
		this.modelId = modelId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getProductImgUrl() {
		return productImgUrl;
	}
	public void setProductImgUrl(String productImgUrl) {
		this.productImgUrl = productImgUrl;
	}
	public String getCnName() {
		return cnName;
	}
	public void setCnName(String cnName) {
		this.cnName = cnName;
	}
	public String getCnDisplayOrder() {
		return cnDisplayOrder;
	}
	public void setCnDisplayOrder(String cnDisplayOrder) {
		this.cnDisplayOrder = cnDisplayOrder;
	}
	public boolean isCnIsHightLightProduct() {
		return cnIsHightLightProduct;
	}
	public void setCnIsHightLightProduct(boolean cnIsHightLightProduct) {
		this.cnIsHightLightProduct = cnIsHightLightProduct;
	}
	public String getIsApprovedFor() {
		return isApprovedFor;
	}
	public void setIsApprovedFor(String isApprovedFor) {
		this.isApprovedFor = isApprovedFor;
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
	public String getCnSizeType() {
		return cnSizeType;
	}
	public void setCnSizeType(String cnSizeType) {
		this.cnSizeType = cnSizeType;
	}
	public boolean isApprovedDescription() {
		return isApprovedDescription;
	}
	public void setApprovedDescription(boolean isApprovedDescription) {
		this.isApprovedDescription = isApprovedDescription;
	}
	public boolean isEffective() {
		return isEffective;
	}
	public void setEffective(boolean isEffective) {
		this.isEffective = isEffective;
	}
	public String getColorMap() {
		return colorMap;
	}
	public void setColorMap(String colorMap) {
		this.colorMap = colorMap;
	}
	public String getCnColor() {
		return cnColor;
	}
	public void setCnColor(String cnColor) {
		this.cnColor = cnColor;
	}
	public String getMadeInCountry() {
		return madeInCountry;
	}
	public void setMadeInCountry(String madeInCountry) {
		this.madeInCountry = madeInCountry;
	}
	public String getMaterialFabric() {
		return materialFabric;
	}
	public void setMaterialFabric1(String materialFabric) {
		this.materialFabric = materialFabric;
	}
	
	public String getCnAbstract() {
		return cnAbstract;
	}
	public void setCnAbstract(String cnAbstract) {
		this.cnAbstract = cnAbstract;
	}
	public String getCnShortDescription() {
		return cnShortDescription;
	}
	public void setCnShortDescription(String cnShortDescription) {
		this.cnShortDescription = cnShortDescription;
	}
	public String getCnLongDescription() {
		return cnLongDescription;
	}
	public void setCnLongDescription(String cnLongDescription) {
		this.cnLongDescription = cnLongDescription;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getUrlKey() {
		return urlKey;
	}
	public void setUrlKey(String urlKey) {
		this.urlKey = urlKey;
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created==null?null:created.substring(0,19);
	}

	/**
	 * @return the creater
	 */
	public String getCreater() {
		return creater;
	}
	/**
	 * @param creater the creater to set
	 */
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public String getModified() {
		return modified;
	}
	public void setModified(String modified) {
		this.modified = modified==null?null:modified.substring(0,19);
	}
	public String getModifier() {
		return modifier;
	}
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
	public String getReferenceMsrp() {
		return referenceMsrp;
	}
	public void setReferenceMsrp(String referenceMsrp) {
		this.referenceMsrp = referenceMsrp;
	}
	public String getReferencePrice() {
		return referencePrice;
	}
	public void setReferencePrice(String referencePrice) {
		this.referencePrice = referencePrice;
	}
	public String getFirstSalePrice() {
		return firstSalePrice;
	}
	public void setFirstSalePrice(String firstSalePrice) {
		this.firstSalePrice = firstSalePrice;
	}
	public String getCnPrice() {
		return cnPrice;
	}
	public void setCnPrice(String cnPrice) {
		this.cnPrice = cnPrice;
	}
	public String getEffectivePrice() {
		return effectivePrice;
	}
	public void setEffectivePrice(String effectivePrice) {
		this.effectivePrice = effectivePrice;
	}
	public String getCnPriceRmb() {
		return cnPriceRmb;
	}
	public void setCnPriceRmb(String cnPriceRmb) {
		this.cnPriceRmb = cnPriceRmb;
	}
	public String getCnPriceAdjustmentRmb() {
		return cnPriceAdjustmentRmb;
	}
	public void setCnPriceAdjustmentRmb(String cnPriceAdjustmentRmb) {
		this.cnPriceAdjustmentRmb = cnPriceAdjustmentRmb;
	}
	public String getCnPriceFinalRmb() {
		return cnPriceFinalRmb;
	}
	public void setCnPriceFinalRmb(String cnPriceFinalRmb) {
		this.cnPriceFinalRmb = cnPriceFinalRmb;
	}
	public boolean isCnIsOnSale() {
		return cnIsOnSale;
	}
	public void setCnIsOnSale(boolean cnIsOnSale) {
		this.cnIsOnSale = cnIsOnSale;
	}
	public boolean isCnIsApproved() {
		return cnIsApproved;
	}
	public void setCnIsApproved(boolean cnIsApproved) {
		this.cnIsApproved = cnIsApproved;
	}
	public String getCnOfficialPriceAdjustmentRmb() {
		return cnOfficialPriceAdjustmentRmb;
	}
	public void setCnOfficialPriceAdjustmentRmb(String cnOfficialPriceAdjustmentRmb) {
		this.cnOfficialPriceAdjustmentRmb = cnOfficialPriceAdjustmentRmb;
	}
	public String getCnOfficialPriceFinalRmb() {
		return cnOfficialPriceFinalRmb;
	}
	public void setCnOfficialPriceFinalRmb(String cnOfficialPriceFinalRmb) {
		this.cnOfficialPriceFinalRmb = cnOfficialPriceFinalRmb;
	}
	public String getCnOfficialFreeShippingType() {
		return cnOfficialFreeShippingType;
	}
	public void setCnOfficialFreeShippingType(String cnOfficialFreeShippingType) {
		this.cnOfficialFreeShippingType = cnOfficialFreeShippingType;
	}
	public boolean isCnOfficialIsOnSale() {
		return cnOfficialIsOnSale;
	}
	public void setCnOfficialIsOnSale(boolean cnOfficialIsOnSale) {
		this.cnOfficialIsOnSale = cnOfficialIsOnSale;
	}
	public boolean isCnOfficialIsApproved() {
		return cnOfficialIsApproved;
	}
	public void setCnOfficialIsApproved(boolean cnOfficialIsApproved) {
		this.cnOfficialIsApproved = cnOfficialIsApproved;
	}
	public String getCnOfficialPrePublishDateTime() {
		return cnOfficialPrePublishDateTime;
	}
	public void setCnOfficialPrePublishDateTime(String cnOfficialPrePublishDateTime) {
		this.cnOfficialPrePublishDateTime = cnOfficialPrePublishDateTime==null?null:cnOfficialPrePublishDateTime.substring(0,19);
	}
	public String getCnOfficialSales7Days() {
		return cnOfficialSales7Days;
	}
	public void setCnOfficialSales7Days(String cnOfficialSales7Days) {
		this.cnOfficialSales7Days = cnOfficialSales7Days;
	}
	public String getCnOfficialSales30Days() {
		return cnOfficialSales30Days;
	}
	public void setCnOfficialSales30Days(String cnOfficialSales30Days) {
		this.cnOfficialSales30Days = cnOfficialSales30Days;
	}
	public String getCnOfficialSalesThisYear() {
		return cnOfficialSalesThisYear;
	}
	public void setCnOfficialSalesThisYear(String cnOfficialSalesThisYear) {
		this.cnOfficialSalesThisYear = cnOfficialSalesThisYear;
	}
	public String getTbPriceAdjustmentRmb() {
		return tbPriceAdjustmentRmb;
	}
	public void setTbPriceAdjustmentRmb(String tbPriceAdjustmentRmb) {
		this.tbPriceAdjustmentRmb = tbPriceAdjustmentRmb;
	}
	public String getTbPriceFinalRmb() {
		return tbPriceFinalRmb;
	}
	public void setTbPriceFinalRmb(String tbPriceFinalRmb) {
		this.tbPriceFinalRmb = tbPriceFinalRmb;
	}
	public String getTbFreeShippingType() {
		return tbFreeShippingType;
	}
	public void setTbFreeShippingType(String tbFreeShippingType) {
		this.tbFreeShippingType = tbFreeShippingType;
	}
	public boolean isTbIsOnSale() {
		return tbIsOnSale;
	}
	public void setTbIsOnSale(boolean tbIsOnSale) {
		this.tbIsOnSale = tbIsOnSale;
	}
	public boolean isTbIsApproved() {
		return tbIsApproved;
	}
	public void setTbIsApproved(boolean tbIsApproved) {
		this.tbIsApproved = tbIsApproved;
	}
	public String getTbPrePublishDateTime() {
		return tbPrePublishDateTime;
	}
	public void setTbPrePublishDateTime(String tbPrePublishDateTime) {
		this.tbPrePublishDateTime = tbPrePublishDateTime == null?null:tbPrePublishDateTime.substring(0,19);
	}
	public String getTbSales7Days() {
		return tbSales7Days;
	}
	public void setTbSales7Days(String tbSales7Days) {
		this.tbSales7Days = tbSales7Days;
	}
	public String getTbSales30Days() {
		return tbSales30Days;
	}
	public void setTbSales30Days(String tbSales30Days) {
		this.tbSales30Days = tbSales30Days;
	}
	public String getTbSalesThisYear() {
		return tbSalesThisYear;
	}
	public void setTbSalesThisYear(String tbSalesThisYear) {
		this.tbSalesThisYear = tbSalesThisYear;
	}
	public String getTmPriceAdjustmentRmb() {
		return tmPriceAdjustmentRmb;
	}
	public void setTmPriceAdjustmentRmb(String tmPriceAdjustmentRmb) {
		this.tmPriceAdjustmentRmb = tmPriceAdjustmentRmb;
	}
	public String getTmPriceFinalRmb() {
		return tmPriceFinalRmb;
	}
	public void setTmPriceFinalRmb(String tmPriceFinalRmb) {
		this.tmPriceFinalRmb = tmPriceFinalRmb;
	}
	public String getTmFreeShippingType() {
		return tmFreeShippingType;
	}
	public void setTmFreeShippingType(String tmFreeShippingType) {
		this.tmFreeShippingType = tmFreeShippingType;
	}
	public boolean isTmIsOnSale() {
		return tmIsOnSale;
	}
	public void setTmIsOnSale(boolean tmIsOnSale) {
		this.tmIsOnSale = tmIsOnSale;
	}
	public boolean isTmIsApproved() {
		return tmIsApproved;
	}
	public void setTmIsApproved(boolean tmIsApproved) {
		this.tmIsApproved = tmIsApproved;
	}
	public String getTmPrePublishDateTime() {
		return tmPrePublishDateTime;
	}
	public void setTmPrePublishDateTime(String tmPrePublishDateTime) {
		this.tmPrePublishDateTime = tmPrePublishDateTime == null?null:tmPrePublishDateTime.substring(0,19);
	}
	public String getTmSales7Days() {
		return tmSales7Days;
	}
	public void setTmSales7Days(String tmSales7Days) {
		this.tmSales7Days = tmSales7Days;
	}
	public String getTmSales30Days() {
		return tmSales30Days;
	}
	public void setTmSales30Days(String tmSales30Days) {
		this.tmSales30Days = tmSales30Days;
	}
	public String getTmSalesThisYear() {
		return tmSalesThisYear;
	}
	public void setTmSalesThisYear(String tmSalesThisYear) {
		this.tmSalesThisYear = tmSalesThisYear;
	}
	public String getTgPriceAdjustmentRmb() {
		return tgPriceAdjustmentRmb;
	}
	public void setTgPriceAdjustmentRmb(String tgPriceAdjustmentRmb) {
		this.tgPriceAdjustmentRmb = tgPriceAdjustmentRmb;
	}
	public String getTgPriceFinalRmb() {
		return tgPriceFinalRmb;
	}
	public void setTgPriceFinalRmb(String tgPriceFinalRmb) {
		this.tgPriceFinalRmb = tgPriceFinalRmb;
	}
	public String getTgFreeShippingType() {
		return tgFreeShippingType;
	}
	public void setTgFreeShippingType(String tgFreeShippingType) {
		this.tgFreeShippingType = tgFreeShippingType;
	}
	public boolean isTgIsOnSale() {
		return tgIsOnSale;
	}
	public void setTgIsOnSale(boolean tgIsOnSale) {
		this.tgIsOnSale = tgIsOnSale;
	}
	public boolean isTgIsApproved() {
		return tgIsApproved;
	}
	public void setTgIsApproved(boolean tgIsApproved) {
		this.tgIsApproved = tgIsApproved;
	}
	public String getTgPrePublishDateTime() {
		return tgPrePublishDateTime;
	}
	public void setTgPrePublishDateTime(String tgPrePublishDateTime) {
		this.tgPrePublishDateTime = tgPrePublishDateTime==null?null:tgPrePublishDateTime.substring(0,19);
	}
	public String getTgSales7Days() {
		return tgSales7Days;
	}
	public void setTgSales7Days(String tgSales7Days) {
		this.tgSales7Days = tgSales7Days;
	}
	public String getTgSales30Days() {
		return tgSales30Days;
	}
	public void setTgSales30Days(String tgSales30Days) {
		this.tgSales30Days = tgSales30Days;
	}
	public String getTgSalesThisYear() {
		return tgSalesThisYear;
	}
	public void setTgSalesThisYear(String tgSalesThisYear) {
		this.tgSalesThisYear = tgSalesThisYear;
	}
	public String getJdPriceAdjustmentRmb() {
		return jdPriceAdjustmentRmb;
	}
	public void setJdPriceAdjustmentRmb(String jdPriceAdjustmentRmb) {
		this.jdPriceAdjustmentRmb = jdPriceAdjustmentRmb;
	}
	public String getJdPriceFinalRmb() {
		return jdPriceFinalRmb;
	}
	public void setJdPriceFinalRmb(String jdPriceFinalRmb) {
		this.jdPriceFinalRmb = jdPriceFinalRmb;
	}
	public String getJdFreeShippingType() {
		return jdFreeShippingType;
	}
	public void setJdFreeShippingType(String jdFreeShippingType) {
		this.jdFreeShippingType = jdFreeShippingType;
	}
	public boolean isJdIsOnSale() {
		return jdIsOnSale;
	}
	public void setJdIsOnSale(boolean jdIsOnSale) {
		this.jdIsOnSale = jdIsOnSale;
	}
	public boolean isJdIsApproved() {
		return jdIsApproved;
	}
	public void setJdIsApproved(boolean jdIsApproved) {
		this.jdIsApproved = jdIsApproved;
	}
	public String getJdPrePublishDateTime() {
		return jdPrePublishDateTime;
	}
	public void setJdPrePublishDateTime(String jdPrePublishDateTime) {
		this.jdPrePublishDateTime = jdPrePublishDateTime==null?null:jdPrePublishDateTime.substring(0,19);
	}
	public String getJdSales7Days() {
		return jdSales7Days;
	}
	public void setJdSales7Days(String jdSales7Days) {
		this.jdSales7Days = jdSales7Days;
	}
	public String getJdSales30Days() {
		return jdSales30Days;
	}
	public void setJdSales30Days(String jdSales30Days) {
		this.jdSales30Days = jdSales30Days;
	}
	public String getJgPriceAdjustmentRmb() {
		return jgPriceAdjustmentRmb;
	}
	public void setJgPriceAdjustmentRmb(String jgPriceAdjustmentRmb) {
		this.jgPriceAdjustmentRmb = jgPriceAdjustmentRmb;
	}
	public String getJgPriceFinalRmb() {
		return jgPriceFinalRmb;
	}
	public void setJgPriceFinalRmb(String jgPriceFinalRmb) {
		this.jgPriceFinalRmb = jgPriceFinalRmb;
	}
	public String getJgFreeShippingType() {
		return jgFreeShippingType;
	}
	public void setJgFreeShippingType(String jgFreeShippingType) {
		this.jgFreeShippingType = jgFreeShippingType;
	}
	public boolean isJgIsOnSale() {
		return jgIsOnSale;
	}
	public void setJgIsOnSale(boolean jgIsOnSale) {
		this.jgIsOnSale = jgIsOnSale;
	}
	public boolean isJgIsApproved() {
		return jgIsApproved;
	}
	public void setJgIsApproved(boolean jgIsApproved) {
		this.jgIsApproved = jgIsApproved;
	}
	public String getJgPrePublishDateTime() {
		return jgPrePublishDateTime;
	}
	public void setJgPrePublishDateTime(String jgPrePublishDateTime) {
		this.jgPrePublishDateTime = jgPrePublishDateTime==null?null:jgPrePublishDateTime.substring(0,19);
	}
	public String getJgSales7Days() {
		return jgSales7Days;
	}
	public void setJgSales7Days(String jgSales7Days) {
		this.jgSales7Days = jgSales7Days;
	}
	public String getJgSales30Days() {
		return jgSales30Days;
	}
	public void setJgSales30Days(String jgSales30Days) {
		this.jgSales30Days = jgSales30Days;
	}

	public String getCutCnShortDescription() {
		return cutCnShortDescription;
	}
	public void setCutCnShortDescription(String cutCnShortDescription) {
		this.cutCnShortDescription = cutCnShortDescription;
	}
	public String getCutCnLongDescription() {
		return cutCnLongDescription;
	}
	public void setCutCnLongDescription(String cutCnLongDescription) {
		this.cutCnLongDescription = cutCnLongDescription;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
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
	 * @return the cnSalesAllDays
	 */
	public String getCnSalesAllDays() {
		return cnSalesAllDays;
	}
	/**
	 * @param cnSalesAllDays the cnSalesAllDays to set
	 */
	public void setCnSalesAllDays(String cnSalesAllDays) {
		this.cnSalesAllDays = cnSalesAllDays;
	}
	/**
	 * @return the cnSalesAllDaysPercent
	 */
	public String getCnSalesAllDaysPercent() {
		return cnSalesAllDaysPercent;
	}
	/**
	 * @param cnSalesAllDaysPercent the cnSalesAllDaysPercent to set
	 */
	public void setCnSalesAllDaysPercent(String cnSalesAllDaysPercent) {
		this.cnSalesAllDaysPercent = cnSalesAllDaysPercent;
	}
	/**
	 * @return the cnOfficialSales30DaysPercent
	 */
	public String getCnOfficialSales30DaysPercent() {
		return cnOfficialSales30DaysPercent;
	}
	/**
	 * @param cnOfficialSales30DaysPercent the cnOfficialSales30DaysPercent to set
	 */
	public void setCnOfficialSales30DaysPercent(String cnOfficialSales30DaysPercent) {
		this.cnOfficialSales30DaysPercent = cnOfficialSales30DaysPercent;
	}
	/**
	 * @return the cnOfficialSales7DaysPercent
	 */
	public String getCnOfficialSales7DaysPercent() {
		return cnOfficialSales7DaysPercent;
	}
	/**
	 * @param cnOfficialSales7DaysPercent the cnOfficialSales7DaysPercent to set
	 */
	public void setCnOfficialSales7DaysPercent(String cnOfficialSales7DaysPercent) {
		this.cnOfficialSales7DaysPercent = cnOfficialSales7DaysPercent;
	}
	/**
	 * @return the cnOfficialSalesAllDays
	 */
	public String getCnOfficialSalesAllDays() {
		return cnOfficialSalesAllDays;
	}
	/**
	 * @param cnOfficialSalesAllDays the cnOfficialSalesAllDays to set
	 */
	public void setCnOfficialSalesAllDays(String cnOfficialSalesAllDays) {
		this.cnOfficialSalesAllDays = cnOfficialSalesAllDays;
	}
	/**
	 * @return the cnOfficialSalesAllDaysPercent
	 */
	public String getCnOfficialSalesAllDaysPercent() {
		return cnOfficialSalesAllDaysPercent;
	}
	/**
	 * @param cnOfficialSalesAllDaysPercent the cnOfficialSalesAllDaysPercent to set
	 */
	public void setCnOfficialSalesAllDaysPercent(String cnOfficialSalesAllDaysPercent) {
		this.cnOfficialSalesAllDaysPercent = cnOfficialSalesAllDaysPercent;
	}
	/**
	 * @return the tbSales30DaysPercent
	 */
	public String getTbSales30DaysPercent() {
		return tbSales30DaysPercent;
	}
	/**
	 * @param tbSales30DaysPercent the tbSales30DaysPercent to set
	 */
	public void setTbSales30DaysPercent(String tbSales30DaysPercent) {
		this.tbSales30DaysPercent = tbSales30DaysPercent;
	}
	/**
	 * @return the tbSales7DaysPercent
	 */
	public String getTbSales7DaysPercent() {
		return tbSales7DaysPercent;
	}
	/**
	 * @param tbSales7DaysPercent the tbSales7DaysPercent to set
	 */
	public void setTbSales7DaysPercent(String tbSales7DaysPercent) {
		this.tbSales7DaysPercent = tbSales7DaysPercent;
	}
	/**
	 * @return the tbSalesAllDays
	 */
	public String getTbSalesAllDays() {
		return tbSalesAllDays;
	}
	/**
	 * @param tbSalesAllDays the tbSalesAllDays to set
	 */
	public void setTbSalesAllDays(String tbSalesAllDays) {
		this.tbSalesAllDays = tbSalesAllDays;
	}
	/**
	 * @return the tbSalesAllDaysPercent
	 */
	public String getTbSalesAllDaysPercent() {
		return tbSalesAllDaysPercent;
	}
	/**
	 * @param tbSalesAllDaysPercent the tbSalesAllDaysPercent to set
	 */
	public void setTbSalesAllDaysPercent(String tbSalesAllDaysPercent) {
		this.tbSalesAllDaysPercent = tbSalesAllDaysPercent;
	}
	/**
	 * @return the tmSales30DaysPercent
	 */
	public String getTmSales30DaysPercent() {
		return tmSales30DaysPercent;
	}
	/**
	 * @param tmSales30DaysPercent the tmSales30DaysPercent to set
	 */
	public void setTmSales30DaysPercent(String tmSales30DaysPercent) {
		this.tmSales30DaysPercent = tmSales30DaysPercent;
	}
	/**
	 * @return the tmSales7DaysPercent
	 */
	public String getTmSales7DaysPercent() {
		return tmSales7DaysPercent;
	}
	/**
	 * @param tmSales7DaysPercent the tmSales7DaysPercent to set
	 */
	public void setTmSales7DaysPercent(String tmSales7DaysPercent) {
		this.tmSales7DaysPercent = tmSales7DaysPercent;
	}
	/**
	 * @return the tmSalesAllDays
	 */
	public String getTmSalesAllDays() {
		return tmSalesAllDays;
	}
	/**
	 * @param tmSalesAllDays the tmSalesAllDays to set
	 */
	public void setTmSalesAllDays(String tmSalesAllDays) {
		this.tmSalesAllDays = tmSalesAllDays;
	}
	/**
	 * @return the tmSalesAllDaysPercent
	 */
	public String getTmSalesAllDaysPercent() {
		return tmSalesAllDaysPercent;
	}
	/**
	 * @param tmSalesAllDaysPercent the tmSalesAllDaysPercent to set
	 */
	public void setTmSalesAllDaysPercent(String tmSalesAllDaysPercent) {
		this.tmSalesAllDaysPercent = tmSalesAllDaysPercent;
	}
	/**
	 * @return the tgSales30DaysPercent
	 */
	public String getTgSales30DaysPercent() {
		return tgSales30DaysPercent;
	}
	/**
	 * @param tgSales30DaysPercent the tgSales30DaysPercent to set
	 */
	public void setTgSales30DaysPercent(String tgSales30DaysPercent) {
		this.tgSales30DaysPercent = tgSales30DaysPercent;
	}
	/**
	 * @return the tgSales7DaysPercent
	 */
	public String getTgSales7DaysPercent() {
		return tgSales7DaysPercent;
	}
	/**
	 * @param tgSales7DaysPercent the tgSales7DaysPercent to set
	 */
	public void setTgSales7DaysPercent(String tgSales7DaysPercent) {
		this.tgSales7DaysPercent = tgSales7DaysPercent;
	}
	/**
	 * @return the tgSalesAllDays
	 */
	public String getTgSalesAllDays() {
		return tgSalesAllDays;
	}
	/**
	 * @param tgSalesAllDays the tgSalesAllDays to set
	 */
	public void setTgSalesAllDays(String tgSalesAllDays) {
		this.tgSalesAllDays = tgSalesAllDays;
	}
	/**
	 * @return the tgSalesAllDaysPercent
	 */
	public String getTgSalesAllDaysPercent() {
		return tgSalesAllDaysPercent;
	}
	/**
	 * @param tgSalesAllDaysPercent the tgSalesAllDaysPercent to set
	 */
	public void setTgSalesAllDaysPercent(String tgSalesAllDaysPercent) {
		this.tgSalesAllDaysPercent = tgSalesAllDaysPercent;
	}
	/**
	 * @return the jdSales30DaysPercent
	 */
	public String getJdSales30DaysPercent() {
		return jdSales30DaysPercent;
	}
	/**
	 * @param jdSales30DaysPercent the jdSales30DaysPercent to set
	 */
	public void setJdSales30DaysPercent(String jdSales30DaysPercent) {
		this.jdSales30DaysPercent = jdSales30DaysPercent;
	}
	/**
	 * @return the jdSales7DaysPercent
	 */
	public String getJdSales7DaysPercent() {
		return jdSales7DaysPercent;
	}
	/**
	 * @param jdSales7DaysPercent the jdSales7DaysPercent to set
	 */
	public void setJdSales7DaysPercent(String jdSales7DaysPercent) {
		this.jdSales7DaysPercent = jdSales7DaysPercent;
	}
	/**
	 * @return the jdSalesAllDays
	 */
	public String getJdSalesAllDays() {
		return jdSalesAllDays;
	}
	/**
	 * @param jdSalesAllDays the jdSalesAllDays to set
	 */
	public void setJdSalesAllDays(String jdSalesAllDays) {
		this.jdSalesAllDays = jdSalesAllDays;
	}
	/**
	 * @return the jdSalesAllDaysPercent
	 */
	public String getJdSalesAllDaysPercent() {
		return jdSalesAllDaysPercent;
	}
	/**
	 * @param jdSalesAllDaysPercent the jdSalesAllDaysPercent to set
	 */
	public void setJdSalesAllDaysPercent(String jdSalesAllDaysPercent) {
		this.jdSalesAllDaysPercent = jdSalesAllDaysPercent;
	}
	/**
	 * @return the jgSales30DaysPercent
	 */
	public String getJgSales30DaysPercent() {
		return jgSales30DaysPercent;
	}
	/**
	 * @param jgSales30DaysPercent the jgSales30DaysPercent to set
	 */
	public void setJgSales30DaysPercent(String jgSales30DaysPercent) {
		this.jgSales30DaysPercent = jgSales30DaysPercent;
	}
	/**
	 * @return the jgSales7DaysPercent
	 */
	public String getJgSales7DaysPercent() {
		return jgSales7DaysPercent;
	}
	/**
	 * @param jgSales7DaysPercent the jgSales7DaysPercent to set
	 */
	public void setJgSales7DaysPercent(String jgSales7DaysPercent) {
		this.jgSales7DaysPercent = jgSales7DaysPercent;
	}
	/**
	 * @return the jgSalesAllDays
	 */
	public String getJgSalesAllDays() {
		return jgSalesAllDays;
	}
	/**
	 * @param jgSalesAllDays the jgSalesAllDays to set
	 */
	public void setJgSalesAllDays(String jgSalesAllDays) {
		this.jgSalesAllDays = jgSalesAllDays;
	}
	/**
	 * @return the jgSalesAllDaysPercent
	 */
	public String getJgSalesAllDaysPercent() {
		return jgSalesAllDaysPercent;
	}
	/**
	 * @param jgSalesAllDaysPercent the jgSalesAllDaysPercent to set
	 */
	public void setJgSalesAllDaysPercent(String jgSalesAllDaysPercent) {
		this.jgSalesAllDaysPercent = jgSalesAllDaysPercent;
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
	 * @return the cnOfficialSalesInThisYear
	 */
	public String getCnOfficialSalesInThisYear() {
		return cnOfficialSalesInThisYear;
	}
	/**
	 * @param cnOfficialSalesInThisYear the cnOfficialSalesInThisYear to set
	 */
	public void setCnOfficialSalesInThisYear(String cnOfficialSalesInThisYear) {
		this.cnOfficialSalesInThisYear = cnOfficialSalesInThisYear;
	}
	/**
	 * @return the cnOfficialSalesInThisYearPercent
	 */
	public String getCnOfficialSalesInThisYearPercent() {
		return cnOfficialSalesInThisYearPercent;
	}
	/**
	 * @param cnOfficialSalesInThisYearPercent the cnOfficialSalesInThisYearPercent to set
	 */
	public void setCnOfficialSalesInThisYearPercent(String cnOfficialSalesInThisYearPercent) {
		this.cnOfficialSalesInThisYearPercent = cnOfficialSalesInThisYearPercent;
	}
	/**
	 * @return the tbSalesInThisYear
	 */
	public String getTbSalesInThisYear() {
		return tbSalesInThisYear;
	}
	/**
	 * @param tbSalesInThisYear the tbSalesInThisYear to set
	 */
	public void setTbSalesInThisYear(String tbSalesInThisYear) {
		this.tbSalesInThisYear = tbSalesInThisYear;
	}
	/**
	 * @return the tbSalesInThisYearPercent
	 */
	public String getTbSalesInThisYearPercent() {
		return tbSalesInThisYearPercent;
	}
	/**
	 * @param tbSalesInThisYearPercent the tbSalesInThisYearPercent to set
	 */
	public void setTbSalesInThisYearPercent(String tbSalesInThisYearPercent) {
		this.tbSalesInThisYearPercent = tbSalesInThisYearPercent;
	}
	/**
	 * @return the tmSalesInThisYear
	 */
	public String getTmSalesInThisYear() {
		return tmSalesInThisYear;
	}
	/**
	 * @param tmSalesInThisYear the tmSalesInThisYear to set
	 */
	public void setTmSalesInThisYear(String tmSalesInThisYear) {
		this.tmSalesInThisYear = tmSalesInThisYear;
	}
	/**
	 * @return the tmSalesInThisYearPercent
	 */
	public String getTmSalesInThisYearPercent() {
		return tmSalesInThisYearPercent;
	}
	/**
	 * @param tmSalesInThisYearPercent the tmSalesInThisYearPercent to set
	 */
	public void setTmSalesInThisYearPercent(String tmSalesInThisYearPercent) {
		this.tmSalesInThisYearPercent = tmSalesInThisYearPercent;
	}
	/**
	 * @return the tgSalesInThisYear
	 */
	public String getTgSalesInThisYear() {
		return tgSalesInThisYear;
	}
	/**
	 * @param tgSalesInThisYear the tgSalesInThisYear to set
	 */
	public void setTgSalesInThisYear(String tgSalesInThisYear) {
		this.tgSalesInThisYear = tgSalesInThisYear;
	}
	/**
	 * @return the tgSalesInThisYearPercent
	 */
	public String getTgSalesInThisYearPercent() {
		return tgSalesInThisYearPercent;
	}
	/**
	 * @param tgSalesInThisYearPercent the tgSalesInThisYearPercent to set
	 */
	public void setTgSalesInThisYearPercent(String tgSalesInThisYearPercent) {
		this.tgSalesInThisYearPercent = tgSalesInThisYearPercent;
	}
	/**
	 * @return the jdSalesInThisYear
	 */
	public String getJdSalesInThisYear() {
		return jdSalesInThisYear;
	}
	/**
	 * @param jdSalesInThisYear the jdSalesInThisYear to set
	 */
	public void setJdSalesInThisYear(String jdSalesInThisYear) {
		this.jdSalesInThisYear = jdSalesInThisYear;
	}
	/**
	 * @return the jdSalesInThisYearPercent
	 */
	public String getJdSalesInThisYearPercent() {
		return jdSalesInThisYearPercent;
	}
	/**
	 * @param jdSalesInThisYearPercent the jdSalesInThisYearPercent to set
	 */
	public void setJdSalesInThisYearPercent(String jdSalesInThisYearPercent) {
		this.jdSalesInThisYearPercent = jdSalesInThisYearPercent;
	}
	/**
	 * @return the jgSalesInThisYear
	 */
	public String getJgSalesInThisYear() {
		return jgSalesInThisYear;
	}
	/**
	 * @param jgSalesInThisYear the jgSalesInThisYear to set
	 */
	public void setJgSalesInThisYear(String jgSalesInThisYear) {
		this.jgSalesInThisYear = jgSalesInThisYear;
	}
	/**
	 * @return the jgSalesInThisYearPercent
	 */
	public String getJgSalesInThisYearPercent() {
		return jgSalesInThisYearPercent;
	}
	/**
	 * @param jgSalesInThisYearPercent the jgSalesInThisYearPercent to set
	 */
	public void setJgSalesInThisYearPercent(String jgSalesInThisYearPercent) {
		this.jgSalesInThisYearPercent = jgSalesInThisYearPercent;
	}
	

}
