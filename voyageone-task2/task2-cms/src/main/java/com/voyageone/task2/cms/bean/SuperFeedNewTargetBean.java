package com.voyageone.task2.cms.bean;

import java.util.HashSet;
import java.util.Set;

public class SuperFeedNewTargetBean extends  SuperFeedBean{

    private String tcin;

    private String upc;

    private String md5;

    private Integer updateflag;

    private String parentTcins;

    private String manufacturerId;

    private String manufacturerPartNumber;

    private String manufacturerModelNumber;

    private String productDescriptionTitle;

    private String productDescriptionDownstreamDescription;

    private String productDescriptionLongDescription;

    private String regprice;

    private String enrichmentBuyUrl;

    private String productClassificationItemTypeCategoryType;

    private String productClassificationItemTypeName;

    private String productClassificationItemTypeType;

    private String productClassificationMerchandiseType;

    private String productClassificationMerchandiseTypeName;

    private String productClassificationProductSubtype;

    private String productClassificationProductSubtypeName;

    private String productClassificationProductType;

    private String productClassificationProductTypeName;

    private String availabilityStatus;

    private String enrichmentImagesBaseUrl;

    private String enrichmentImagesPrimaryImage;

    private String productBrandManufacturerBrand;

    private String displayOptionKeywords;

    private String productDescriptionSellingFeature;

    private String packageDimensionsWeight;

    private String packageDimensionsWeightUnitOfMeasure;

    private String variationTheme;

    private String variationSize;

    private String variationColor;

    private String variationMaterialType;

    private String merchandiseTypeAttributes;

    private String relationshipTypeCode;

    private String dpci;

    private String ecomDivisional;

    private String launchDateTime;

    private String averageoverallrating;

    private String totalreviewcount;

    private String productBrandBrand;

    private String handlingIsBackOrderEligible;

    private String listprice;

    private String enrichmentImagesAlternateImages;

    private String displayOptionHasSizeChart;

    private String enrichmentSizeChart;

    private String taxCategoryTaxCodeId;

    private String packageDimensionsDimensionUnitOfMeasure;

    private String packageDimensionsHeight;

    private String packageDimensionsDepth;

    private String packageDimensionsWidth;

    private String handlingReceivingFacilityType;

    private String productDescriptionBulletDescription;

    private String handlingBackOrderType;

    private String fulfillmentPurchaseLimit;

    private String environmentalSegmentationIsHazardousMaterial;

    private String environmentalSegmentationIsIngestible;

    private String fulfillmentIsWheneverShippingEligible;

    private String fulfillmentShippingRestriction;

    private String fulfillmentIsPoBoxProhibited;

    private String enrichmentNutritionFactsIngredients;

    private String enrichmentNutritionFactsWarning;

    private String enrichmentNutritionFactsPreparedCount;

    private String enrichmentNutritionFactsValuePreparedList;

    private String enrichmentDrugFactsProductDetails;

    private String enrichmentDrugFactsProductIngredients;

    private String enrichmentDrugFactsLabel;

    private String enrichmentDrugFactsPurposeLabel;

    private String enrichmentDrugFactsActiveIngredientLabel;

    private String enrichmentDrugFactsActiveIngredients;

    private String enrichmentDrugFactsUsesLabel;

    private String enrichmentDrugFactsUsesTreatedSymptoms;

    private String enrichmentDrugFactsWarningText;

    private String enrichmentDrugFactsWarningLabel;

    private String enrichmentDrugFactsWarningUsage;

    private String enrichmentDrugFactsWarningOtherWarnings;

    private String enrichmentDrugFactsDirectionLabel;

    private String enrichmentDrugFactsDirectionGeneralDirections;

    private String enrichmentDrugFactsDirectionAgeGroups;

    private String enrichmentDrugFactsOtherInformationLabel;

    private String enrichmentDrugFactsOtherInformationBullets;

    private String enrichmentDrugFactsInactiveIngredients;

    private String enrichmentDrugFactsQuestionsCommentsSectionLabel;

    private String enrichmentDrugFactsQuestionsCommentsSectionPhoneNumber;

    private String energyGuideCapacityMeasurement;

    private String energyGuideEstimatedOperatingCost;

    private String energyGuideSliderLowRange;

    private String energyGuideSliderHighRange;

    private String energyGuideSliderLabel;

    private String energyGuideEstimatedEnergyUseTitle;

    private String energyGuideEstimatedEnergyUseValue;

    private String energyGuideEstimatedEnergyUseUnitOfMeasurement;

    private String energyGuideLegalDisclaimerText;

    private String energyGuideLegalDisclaimerInformation;

    private String energyGuideHasEnergyStar;

    private String returnMethod;

    private String countryOfOriginCode;

    private String perordercharges;

    private String perunitcharges;

    private String weight;

    private String itembulky;

    private String fixedprice;

    private String handlingfee;

    private String handlingfeemessage;

    private String handlingfeelegaldisclaimer;

    private String isbn;

    private String pattern;

    private String ageGroup;

    private String gender;

    private String barcodeType;

    private String iacAttributes;

    private String secBarcode;

    private String mozartVendorId;

    private String specialitemsource;

    private String enrichmentReturnPolicies;

    private String productVendors;

    public String getTcin() {
        return tcin;
    }

    public void setTcin(String tcin) {
        this.tcin = tcin == null ? null : tcin.trim();
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc == null ? null : upc.trim();
    }

    public String getMd5() {
        StringBuffer temp = new StringBuffer();
        Set<String> noMd5Fields = new HashSet<>();
        noMd5Fields.add("md5");
        noMd5Fields.add("updateflag");
        return  beanToMd5(this,noMd5Fields);
    }

    public void setMd5(String md5) {
        this.md5 = md5 == null ? null : md5.trim();
    }

    public Integer getUpdateflag() {
        return updateflag;
    }

    public void setUpdateflag(Integer updateflag) {
        this.updateflag = updateflag;
    }
    public String getParentTcins() {
        return parentTcins;
    }

    public void setParentTcins(String parentTcins) {
        this.parentTcins = parentTcins == null ? null : parentTcins.trim();
    }

    public String getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturerId(String manufacturerId) {
        this.manufacturerId = manufacturerId == null ? null : manufacturerId.trim();
    }

    public String getManufacturerPartNumber() {
        return manufacturerPartNumber;
    }

    public void setManufacturerPartNumber(String manufacturerPartNumber) {
        this.manufacturerPartNumber = manufacturerPartNumber == null ? null : manufacturerPartNumber.trim();
    }

    public String getManufacturerModelNumber() {
        return manufacturerModelNumber;
    }

    public void setManufacturerModelNumber(String manufacturerModelNumber) {
        this.manufacturerModelNumber = manufacturerModelNumber == null ? null : manufacturerModelNumber.trim();
    }

    public String getProductDescriptionTitle() {
        return productDescriptionTitle;
    }

    public void setProductDescriptionTitle(String productDescriptionTitle) {
        this.productDescriptionTitle = productDescriptionTitle == null ? null : productDescriptionTitle.trim();
    }

    public String getProductDescriptionDownstreamDescription() {
        return productDescriptionDownstreamDescription;
    }

    public void setProductDescriptionDownstreamDescription(String productDescriptionDownstreamDescription) {
        this.productDescriptionDownstreamDescription = productDescriptionDownstreamDescription == null ? null : productDescriptionDownstreamDescription.trim();
    }

    public String getProductDescriptionLongDescription() {
        return productDescriptionLongDescription;
    }

    public void setProductDescriptionLongDescription(String productDescriptionLongDescription) {
        this.productDescriptionLongDescription = productDescriptionLongDescription == null ? null : productDescriptionLongDescription.trim();
    }

    public String getRegprice() {
        return regprice;
    }

    public void setRegprice(String regprice) {
        this.regprice = regprice == null ? null : regprice.trim();
    }

    public String getEnrichmentBuyUrl() {
        return enrichmentBuyUrl;
    }

    public void setEnrichmentBuyUrl(String enrichmentBuyUrl) {
        this.enrichmentBuyUrl = enrichmentBuyUrl == null ? null : enrichmentBuyUrl.trim();
    }

    public String getProductClassificationItemTypeCategoryType() {
        return productClassificationItemTypeCategoryType;
    }

    public void setProductClassificationItemTypeCategoryType(String productClassificationItemTypeCategoryType) {
        this.productClassificationItemTypeCategoryType = productClassificationItemTypeCategoryType == null ? null : productClassificationItemTypeCategoryType.trim();
    }

    public String getProductClassificationItemTypeName() {
        return productClassificationItemTypeName;
    }

    public void setProductClassificationItemTypeName(String productClassificationItemTypeName) {
        this.productClassificationItemTypeName = productClassificationItemTypeName == null ? null : productClassificationItemTypeName.trim();
    }

    public String getProductClassificationItemTypeType() {
        return productClassificationItemTypeType;
    }

    public void setProductClassificationItemTypeType(String productClassificationItemTypeType) {
        this.productClassificationItemTypeType = productClassificationItemTypeType == null ? null : productClassificationItemTypeType.trim();
    }

    public String getProductClassificationMerchandiseType() {
        return productClassificationMerchandiseType;
    }

    public void setProductClassificationMerchandiseType(String productClassificationMerchandiseType) {
        this.productClassificationMerchandiseType = productClassificationMerchandiseType == null ? null : productClassificationMerchandiseType.trim();
    }

    public String getProductClassificationMerchandiseTypeName() {
        return productClassificationMerchandiseTypeName;
    }

    public void setProductClassificationMerchandiseTypeName(String productClassificationMerchandiseTypeName) {
        this.productClassificationMerchandiseTypeName = productClassificationMerchandiseTypeName == null ? null : productClassificationMerchandiseTypeName.trim();
    }

    public String getProductClassificationProductSubtype() {
        return productClassificationProductSubtype;
    }

    public void setProductClassificationProductSubtype(String productClassificationProductSubtype) {
        this.productClassificationProductSubtype = productClassificationProductSubtype == null ? null : productClassificationProductSubtype.trim();
    }

    public String getProductClassificationProductSubtypeName() {
        return productClassificationProductSubtypeName;
    }

    public void setProductClassificationProductSubtypeName(String productClassificationProductSubtypeName) {
        this.productClassificationProductSubtypeName = productClassificationProductSubtypeName == null ? null : productClassificationProductSubtypeName.trim();
    }

    public String getProductClassificationProductType() {
        return productClassificationProductType;
    }

    public void setProductClassificationProductType(String productClassificationProductType) {
        this.productClassificationProductType = productClassificationProductType == null ? null : productClassificationProductType.trim();
    }

    public String getProductClassificationProductTypeName() {
        return productClassificationProductTypeName;
    }

    public void setProductClassificationProductTypeName(String productClassificationProductTypeName) {
        this.productClassificationProductTypeName = productClassificationProductTypeName == null ? null : productClassificationProductTypeName.trim();
    }

    public String getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(String availabilityStatus) {
        this.availabilityStatus = availabilityStatus == null ? null : availabilityStatus.trim();
    }

    public String getEnrichmentImagesBaseUrl() {
        return enrichmentImagesBaseUrl;
    }

    public void setEnrichmentImagesBaseUrl(String enrichmentImagesBaseUrl) {
        this.enrichmentImagesBaseUrl = enrichmentImagesBaseUrl == null ? null : enrichmentImagesBaseUrl.trim();
    }

    public String getEnrichmentImagesPrimaryImage() {
        return enrichmentImagesPrimaryImage;
    }

    public void setEnrichmentImagesPrimaryImage(String enrichmentImagesPrimaryImage) {
        this.enrichmentImagesPrimaryImage = enrichmentImagesPrimaryImage == null ? null : enrichmentImagesPrimaryImage.trim();
    }

    public String getProductBrandManufacturerBrand() {
        return productBrandManufacturerBrand;
    }

    public void setProductBrandManufacturerBrand(String productBrandManufacturerBrand) {
        this.productBrandManufacturerBrand = productBrandManufacturerBrand == null ? null : productBrandManufacturerBrand.trim();
    }

    public String getDisplayOptionKeywords() {
        return displayOptionKeywords;
    }

    public void setDisplayOptionKeywords(String displayOptionKeywords) {
        this.displayOptionKeywords = displayOptionKeywords == null ? null : displayOptionKeywords.trim();
    }

    public String getProductDescriptionSellingFeature() {
        return productDescriptionSellingFeature;
    }

    public void setProductDescriptionSellingFeature(String productDescriptionSellingFeature) {
        this.productDescriptionSellingFeature = productDescriptionSellingFeature == null ? null : productDescriptionSellingFeature.trim();
    }

    public String getPackageDimensionsWeight() {
        return packageDimensionsWeight;
    }

    public void setPackageDimensionsWeight(String packageDimensionsWeight) {
        this.packageDimensionsWeight = packageDimensionsWeight == null ? null : packageDimensionsWeight.trim();
    }

    public String getPackageDimensionsWeightUnitOfMeasure() {
        return packageDimensionsWeightUnitOfMeasure;
    }

    public void setPackageDimensionsWeightUnitOfMeasure(String packageDimensionsWeightUnitOfMeasure) {
        this.packageDimensionsWeightUnitOfMeasure = packageDimensionsWeightUnitOfMeasure == null ? null : packageDimensionsWeightUnitOfMeasure.trim();
    }

    public String getVariationTheme() {
        return variationTheme;
    }

    public void setVariationTheme(String variationTheme) {
        this.variationTheme = variationTheme == null ? null : variationTheme.trim();
    }

    public String getVariationSize() {
        return variationSize;
    }

    public void setVariationSize(String variationSize) {
        this.variationSize = variationSize == null ? null : variationSize.trim();
    }

    public String getVariationColor() {
        return variationColor;
    }

    public void setVariationColor(String variationColor) {
        this.variationColor = variationColor == null ? null : variationColor.trim();
    }

    public String getVariationMaterialType() {
        return variationMaterialType;
    }

    public void setVariationMaterialType(String variationMaterialType) {
        this.variationMaterialType = variationMaterialType == null ? null : variationMaterialType.trim();
    }

    public String getMerchandiseTypeAttributes() {
        return merchandiseTypeAttributes;
    }

    public void setMerchandiseTypeAttributes(String merchandiseTypeAttributes) {
        this.merchandiseTypeAttributes = merchandiseTypeAttributes == null ? null : merchandiseTypeAttributes.trim();
    }

    public String getRelationshipTypeCode() {
        return relationshipTypeCode;
    }

    public void setRelationshipTypeCode(String relationshipTypeCode) {
        this.relationshipTypeCode = relationshipTypeCode == null ? null : relationshipTypeCode.trim();
    }

    public String getDpci() {
        return dpci;
    }

    public void setDpci(String dpci) {
        this.dpci = dpci == null ? null : dpci.trim();
    }

    public String getEcomDivisional() {
        return ecomDivisional;
    }

    public void setEcomDivisional(String ecomDivisional) {
        this.ecomDivisional = ecomDivisional == null ? null : ecomDivisional.trim();
    }

    public String getLaunchDateTime() {
        return launchDateTime;
    }

    public void setLaunchDateTime(String launchDateTime) {
        this.launchDateTime = launchDateTime == null ? null : launchDateTime.trim();
    }

    public String getAverageoverallrating() {
        return averageoverallrating;
    }

    public void setAverageoverallrating(String averageoverallrating) {
        this.averageoverallrating = averageoverallrating == null ? null : averageoverallrating.trim();
    }

    public String getTotalreviewcount() {
        return totalreviewcount;
    }

    public void setTotalreviewcount(String totalreviewcount) {
        this.totalreviewcount = totalreviewcount == null ? null : totalreviewcount.trim();
    }

    public String getProductBrandBrand() {
        return productBrandBrand;
    }

    public void setProductBrandBrand(String productBrandBrand) {
        this.productBrandBrand = productBrandBrand == null ? null : productBrandBrand.trim();
    }

    public String getHandlingIsBackOrderEligible() {
        return handlingIsBackOrderEligible;
    }

    public void setHandlingIsBackOrderEligible(String handlingIsBackOrderEligible) {
        this.handlingIsBackOrderEligible = handlingIsBackOrderEligible == null ? null : handlingIsBackOrderEligible.trim();
    }

    public String getListprice() {
        return listprice;
    }

    public void setListprice(String listprice) {
        this.listprice = listprice == null ? null : listprice.trim();
    }

    public String getEnrichmentImagesAlternateImages() {
        return enrichmentImagesAlternateImages;
    }

    public void setEnrichmentImagesAlternateImages(String enrichmentImagesAlternateImages) {
        this.enrichmentImagesAlternateImages = enrichmentImagesAlternateImages == null ? null : enrichmentImagesAlternateImages.trim();
    }

    public String getDisplayOptionHasSizeChart() {
        return displayOptionHasSizeChart;
    }

    public void setDisplayOptionHasSizeChart(String displayOptionHasSizeChart) {
        this.displayOptionHasSizeChart = displayOptionHasSizeChart == null ? null : displayOptionHasSizeChart.trim();
    }

    public String getEnrichmentSizeChart() {
        return enrichmentSizeChart;
    }

    public void setEnrichmentSizeChart(String enrichmentSizeChart) {
        this.enrichmentSizeChart = enrichmentSizeChart == null ? null : enrichmentSizeChart.trim();
    }

    public String getTaxCategoryTaxCodeId() {
        return taxCategoryTaxCodeId;
    }

    public void setTaxCategoryTaxCodeId(String taxCategoryTaxCodeId) {
        this.taxCategoryTaxCodeId = taxCategoryTaxCodeId == null ? null : taxCategoryTaxCodeId.trim();
    }

    public String getPackageDimensionsDimensionUnitOfMeasure() {
        return packageDimensionsDimensionUnitOfMeasure;
    }

    public void setPackageDimensionsDimensionUnitOfMeasure(String packageDimensionsDimensionUnitOfMeasure) {
        this.packageDimensionsDimensionUnitOfMeasure = packageDimensionsDimensionUnitOfMeasure == null ? null : packageDimensionsDimensionUnitOfMeasure.trim();
    }

    public String getPackageDimensionsHeight() {
        return packageDimensionsHeight;
    }

    public void setPackageDimensionsHeight(String packageDimensionsHeight) {
        this.packageDimensionsHeight = packageDimensionsHeight == null ? null : packageDimensionsHeight.trim();
    }

    public String getPackageDimensionsDepth() {
        return packageDimensionsDepth;
    }

    public void setPackageDimensionsDepth(String packageDimensionsDepth) {
        this.packageDimensionsDepth = packageDimensionsDepth == null ? null : packageDimensionsDepth.trim();
    }

    public String getPackageDimensionsWidth() {
        return packageDimensionsWidth;
    }

    public void setPackageDimensionsWidth(String packageDimensionsWidth) {
        this.packageDimensionsWidth = packageDimensionsWidth == null ? null : packageDimensionsWidth.trim();
    }

    public String getHandlingReceivingFacilityType() {
        return handlingReceivingFacilityType;
    }

    public void setHandlingReceivingFacilityType(String handlingReceivingFacilityType) {
        this.handlingReceivingFacilityType = handlingReceivingFacilityType == null ? null : handlingReceivingFacilityType.trim();
    }

    public String getProductDescriptionBulletDescription() {
        return productDescriptionBulletDescription;
    }

    public void setProductDescriptionBulletDescription(String productDescriptionBulletDescription) {
        this.productDescriptionBulletDescription = productDescriptionBulletDescription == null ? null : productDescriptionBulletDescription.trim();
    }

    public String getHandlingBackOrderType() {
        return handlingBackOrderType;
    }

    public void setHandlingBackOrderType(String handlingBackOrderType) {
        this.handlingBackOrderType = handlingBackOrderType == null ? null : handlingBackOrderType.trim();
    }

    public String getFulfillmentPurchaseLimit() {
        return fulfillmentPurchaseLimit;
    }

    public void setFulfillmentPurchaseLimit(String fulfillmentPurchaseLimit) {
        this.fulfillmentPurchaseLimit = fulfillmentPurchaseLimit == null ? null : fulfillmentPurchaseLimit.trim();
    }

    public String getEnvironmentalSegmentationIsHazardousMaterial() {
        return environmentalSegmentationIsHazardousMaterial;
    }

    public void setEnvironmentalSegmentationIsHazardousMaterial(String environmentalSegmentationIsHazardousMaterial) {
        this.environmentalSegmentationIsHazardousMaterial = environmentalSegmentationIsHazardousMaterial == null ? null : environmentalSegmentationIsHazardousMaterial.trim();
    }

    public String getEnvironmentalSegmentationIsIngestible() {
        return environmentalSegmentationIsIngestible;
    }

    public void setEnvironmentalSegmentationIsIngestible(String environmentalSegmentationIsIngestible) {
        this.environmentalSegmentationIsIngestible = environmentalSegmentationIsIngestible == null ? null : environmentalSegmentationIsIngestible.trim();
    }

    public String getFulfillmentIsWheneverShippingEligible() {
        return fulfillmentIsWheneverShippingEligible;
    }

    public void setFulfillmentIsWheneverShippingEligible(String fulfillmentIsWheneverShippingEligible) {
        this.fulfillmentIsWheneverShippingEligible = fulfillmentIsWheneverShippingEligible == null ? null : fulfillmentIsWheneverShippingEligible.trim();
    }

    public String getFulfillmentShippingRestriction() {
        return fulfillmentShippingRestriction;
    }

    public void setFulfillmentShippingRestriction(String fulfillmentShippingRestriction) {
        this.fulfillmentShippingRestriction = fulfillmentShippingRestriction == null ? null : fulfillmentShippingRestriction.trim();
    }

    public String getFulfillmentIsPoBoxProhibited() {
        return fulfillmentIsPoBoxProhibited;
    }

    public void setFulfillmentIsPoBoxProhibited(String fulfillmentIsPoBoxProhibited) {
        this.fulfillmentIsPoBoxProhibited = fulfillmentIsPoBoxProhibited == null ? null : fulfillmentIsPoBoxProhibited.trim();
    }

    public String getEnrichmentNutritionFactsIngredients() {
        return enrichmentNutritionFactsIngredients;
    }

    public void setEnrichmentNutritionFactsIngredients(String enrichmentNutritionFactsIngredients) {
        this.enrichmentNutritionFactsIngredients = enrichmentNutritionFactsIngredients == null ? null : enrichmentNutritionFactsIngredients.trim();
    }

    public String getEnrichmentNutritionFactsWarning() {
        return enrichmentNutritionFactsWarning;
    }

    public void setEnrichmentNutritionFactsWarning(String enrichmentNutritionFactsWarning) {
        this.enrichmentNutritionFactsWarning = enrichmentNutritionFactsWarning == null ? null : enrichmentNutritionFactsWarning.trim();
    }

    public String getEnrichmentNutritionFactsPreparedCount() {
        return enrichmentNutritionFactsPreparedCount;
    }

    public void setEnrichmentNutritionFactsPreparedCount(String enrichmentNutritionFactsPreparedCount) {
        this.enrichmentNutritionFactsPreparedCount = enrichmentNutritionFactsPreparedCount == null ? null : enrichmentNutritionFactsPreparedCount.trim();
    }

    public String getEnrichmentNutritionFactsValuePreparedList() {
        return enrichmentNutritionFactsValuePreparedList;
    }

    public void setEnrichmentNutritionFactsValuePreparedList(String enrichmentNutritionFactsValuePreparedList) {
        this.enrichmentNutritionFactsValuePreparedList = enrichmentNutritionFactsValuePreparedList == null ? null : enrichmentNutritionFactsValuePreparedList.trim();
    }

    public String getEnrichmentDrugFactsProductDetails() {
        return enrichmentDrugFactsProductDetails;
    }

    public void setEnrichmentDrugFactsProductDetails(String enrichmentDrugFactsProductDetails) {
        this.enrichmentDrugFactsProductDetails = enrichmentDrugFactsProductDetails == null ? null : enrichmentDrugFactsProductDetails.trim();
    }

    public String getEnrichmentDrugFactsProductIngredients() {
        return enrichmentDrugFactsProductIngredients;
    }

    public void setEnrichmentDrugFactsProductIngredients(String enrichmentDrugFactsProductIngredients) {
        this.enrichmentDrugFactsProductIngredients = enrichmentDrugFactsProductIngredients == null ? null : enrichmentDrugFactsProductIngredients.trim();
    }

    public String getEnrichmentDrugFactsLabel() {
        return enrichmentDrugFactsLabel;
    }

    public void setEnrichmentDrugFactsLabel(String enrichmentDrugFactsLabel) {
        this.enrichmentDrugFactsLabel = enrichmentDrugFactsLabel == null ? null : enrichmentDrugFactsLabel.trim();
    }

    public String getEnrichmentDrugFactsPurposeLabel() {
        return enrichmentDrugFactsPurposeLabel;
    }

    public void setEnrichmentDrugFactsPurposeLabel(String enrichmentDrugFactsPurposeLabel) {
        this.enrichmentDrugFactsPurposeLabel = enrichmentDrugFactsPurposeLabel == null ? null : enrichmentDrugFactsPurposeLabel.trim();
    }

    public String getEnrichmentDrugFactsActiveIngredientLabel() {
        return enrichmentDrugFactsActiveIngredientLabel;
    }

    public void setEnrichmentDrugFactsActiveIngredientLabel(String enrichmentDrugFactsActiveIngredientLabel) {
        this.enrichmentDrugFactsActiveIngredientLabel = enrichmentDrugFactsActiveIngredientLabel == null ? null : enrichmentDrugFactsActiveIngredientLabel.trim();
    }

    public String getEnrichmentDrugFactsActiveIngredients() {
        return enrichmentDrugFactsActiveIngredients;
    }

    public void setEnrichmentDrugFactsActiveIngredients(String enrichmentDrugFactsActiveIngredients) {
        this.enrichmentDrugFactsActiveIngredients = enrichmentDrugFactsActiveIngredients == null ? null : enrichmentDrugFactsActiveIngredients.trim();
    }

    public String getEnrichmentDrugFactsUsesLabel() {
        return enrichmentDrugFactsUsesLabel;
    }

    public void setEnrichmentDrugFactsUsesLabel(String enrichmentDrugFactsUsesLabel) {
        this.enrichmentDrugFactsUsesLabel = enrichmentDrugFactsUsesLabel == null ? null : enrichmentDrugFactsUsesLabel.trim();
    }

    public String getEnrichmentDrugFactsUsesTreatedSymptoms() {
        return enrichmentDrugFactsUsesTreatedSymptoms;
    }

    public void setEnrichmentDrugFactsUsesTreatedSymptoms(String enrichmentDrugFactsUsesTreatedSymptoms) {
        this.enrichmentDrugFactsUsesTreatedSymptoms = enrichmentDrugFactsUsesTreatedSymptoms == null ? null : enrichmentDrugFactsUsesTreatedSymptoms.trim();
    }

    public String getEnrichmentDrugFactsWarningText() {
        return enrichmentDrugFactsWarningText;
    }

    public void setEnrichmentDrugFactsWarningText(String enrichmentDrugFactsWarningText) {
        this.enrichmentDrugFactsWarningText = enrichmentDrugFactsWarningText == null ? null : enrichmentDrugFactsWarningText.trim();
    }

    public String getEnrichmentDrugFactsWarningLabel() {
        return enrichmentDrugFactsWarningLabel;
    }

    public void setEnrichmentDrugFactsWarningLabel(String enrichmentDrugFactsWarningLabel) {
        this.enrichmentDrugFactsWarningLabel = enrichmentDrugFactsWarningLabel == null ? null : enrichmentDrugFactsWarningLabel.trim();
    }

    public String getEnrichmentDrugFactsWarningUsage() {
        return enrichmentDrugFactsWarningUsage;
    }

    public void setEnrichmentDrugFactsWarningUsage(String enrichmentDrugFactsWarningUsage) {
        this.enrichmentDrugFactsWarningUsage = enrichmentDrugFactsWarningUsage == null ? null : enrichmentDrugFactsWarningUsage.trim();
    }

    public String getEnrichmentDrugFactsWarningOtherWarnings() {
        return enrichmentDrugFactsWarningOtherWarnings;
    }

    public void setEnrichmentDrugFactsWarningOtherWarnings(String enrichmentDrugFactsWarningOtherWarnings) {
        this.enrichmentDrugFactsWarningOtherWarnings = enrichmentDrugFactsWarningOtherWarnings == null ? null : enrichmentDrugFactsWarningOtherWarnings.trim();
    }

    public String getEnrichmentDrugFactsDirectionLabel() {
        return enrichmentDrugFactsDirectionLabel;
    }

    public void setEnrichmentDrugFactsDirectionLabel(String enrichmentDrugFactsDirectionLabel) {
        this.enrichmentDrugFactsDirectionLabel = enrichmentDrugFactsDirectionLabel == null ? null : enrichmentDrugFactsDirectionLabel.trim();
    }

    public String getEnrichmentDrugFactsDirectionGeneralDirections() {
        return enrichmentDrugFactsDirectionGeneralDirections;
    }

    public void setEnrichmentDrugFactsDirectionGeneralDirections(String enrichmentDrugFactsDirectionGeneralDirections) {
        this.enrichmentDrugFactsDirectionGeneralDirections = enrichmentDrugFactsDirectionGeneralDirections == null ? null : enrichmentDrugFactsDirectionGeneralDirections.trim();
    }

    public String getEnrichmentDrugFactsDirectionAgeGroups() {
        return enrichmentDrugFactsDirectionAgeGroups;
    }

    public void setEnrichmentDrugFactsDirectionAgeGroups(String enrichmentDrugFactsDirectionAgeGroups) {
        this.enrichmentDrugFactsDirectionAgeGroups = enrichmentDrugFactsDirectionAgeGroups == null ? null : enrichmentDrugFactsDirectionAgeGroups.trim();
    }

    public String getEnrichmentDrugFactsOtherInformationLabel() {
        return enrichmentDrugFactsOtherInformationLabel;
    }

    public void setEnrichmentDrugFactsOtherInformationLabel(String enrichmentDrugFactsOtherInformationLabel) {
        this.enrichmentDrugFactsOtherInformationLabel = enrichmentDrugFactsOtherInformationLabel == null ? null : enrichmentDrugFactsOtherInformationLabel.trim();
    }

    public String getEnrichmentDrugFactsOtherInformationBullets() {
        return enrichmentDrugFactsOtherInformationBullets;
    }

    public void setEnrichmentDrugFactsOtherInformationBullets(String enrichmentDrugFactsOtherInformationBullets) {
        this.enrichmentDrugFactsOtherInformationBullets = enrichmentDrugFactsOtherInformationBullets == null ? null : enrichmentDrugFactsOtherInformationBullets.trim();
    }

    public String getEnrichmentDrugFactsInactiveIngredients() {
        return enrichmentDrugFactsInactiveIngredients;
    }

    public void setEnrichmentDrugFactsInactiveIngredients(String enrichmentDrugFactsInactiveIngredients) {
        this.enrichmentDrugFactsInactiveIngredients = enrichmentDrugFactsInactiveIngredients == null ? null : enrichmentDrugFactsInactiveIngredients.trim();
    }

    public String getEnrichmentDrugFactsQuestionsCommentsSectionLabel() {
        return enrichmentDrugFactsQuestionsCommentsSectionLabel;
    }

    public void setEnrichmentDrugFactsQuestionsCommentsSectionLabel(String enrichmentDrugFactsQuestionsCommentsSectionLabel) {
        this.enrichmentDrugFactsQuestionsCommentsSectionLabel = enrichmentDrugFactsQuestionsCommentsSectionLabel == null ? null : enrichmentDrugFactsQuestionsCommentsSectionLabel.trim();
    }

    public String getEnrichmentDrugFactsQuestionsCommentsSectionPhoneNumber() {
        return enrichmentDrugFactsQuestionsCommentsSectionPhoneNumber;
    }

    public void setEnrichmentDrugFactsQuestionsCommentsSectionPhoneNumber(String enrichmentDrugFactsQuestionsCommentsSectionPhoneNumber) {
        this.enrichmentDrugFactsQuestionsCommentsSectionPhoneNumber = enrichmentDrugFactsQuestionsCommentsSectionPhoneNumber == null ? null : enrichmentDrugFactsQuestionsCommentsSectionPhoneNumber.trim();
    }

    public String getEnergyGuideCapacityMeasurement() {
        return energyGuideCapacityMeasurement;
    }

    public void setEnergyGuideCapacityMeasurement(String energyGuideCapacityMeasurement) {
        this.energyGuideCapacityMeasurement = energyGuideCapacityMeasurement == null ? null : energyGuideCapacityMeasurement.trim();
    }

    public String getEnergyGuideEstimatedOperatingCost() {
        return energyGuideEstimatedOperatingCost;
    }

    public void setEnergyGuideEstimatedOperatingCost(String energyGuideEstimatedOperatingCost) {
        this.energyGuideEstimatedOperatingCost = energyGuideEstimatedOperatingCost == null ? null : energyGuideEstimatedOperatingCost.trim();
    }

    public String getEnergyGuideSliderLowRange() {
        return energyGuideSliderLowRange;
    }

    public void setEnergyGuideSliderLowRange(String energyGuideSliderLowRange) {
        this.energyGuideSliderLowRange = energyGuideSliderLowRange == null ? null : energyGuideSliderLowRange.trim();
    }

    public String getEnergyGuideSliderHighRange() {
        return energyGuideSliderHighRange;
    }

    public void setEnergyGuideSliderHighRange(String energyGuideSliderHighRange) {
        this.energyGuideSliderHighRange = energyGuideSliderHighRange == null ? null : energyGuideSliderHighRange.trim();
    }

    public String getEnergyGuideSliderLabel() {
        return energyGuideSliderLabel;
    }

    public void setEnergyGuideSliderLabel(String energyGuideSliderLabel) {
        this.energyGuideSliderLabel = energyGuideSliderLabel == null ? null : energyGuideSliderLabel.trim();
    }

    public String getEnergyGuideEstimatedEnergyUseTitle() {
        return energyGuideEstimatedEnergyUseTitle;
    }

    public void setEnergyGuideEstimatedEnergyUseTitle(String energyGuideEstimatedEnergyUseTitle) {
        this.energyGuideEstimatedEnergyUseTitle = energyGuideEstimatedEnergyUseTitle == null ? null : energyGuideEstimatedEnergyUseTitle.trim();
    }

    public String getEnergyGuideEstimatedEnergyUseValue() {
        return energyGuideEstimatedEnergyUseValue;
    }

    public void setEnergyGuideEstimatedEnergyUseValue(String energyGuideEstimatedEnergyUseValue) {
        this.energyGuideEstimatedEnergyUseValue = energyGuideEstimatedEnergyUseValue == null ? null : energyGuideEstimatedEnergyUseValue.trim();
    }

    public String getEnergyGuideEstimatedEnergyUseUnitOfMeasurement() {
        return energyGuideEstimatedEnergyUseUnitOfMeasurement;
    }

    public void setEnergyGuideEstimatedEnergyUseUnitOfMeasurement(String energyGuideEstimatedEnergyUseUnitOfMeasurement) {
        this.energyGuideEstimatedEnergyUseUnitOfMeasurement = energyGuideEstimatedEnergyUseUnitOfMeasurement == null ? null : energyGuideEstimatedEnergyUseUnitOfMeasurement.trim();
    }

    public String getEnergyGuideLegalDisclaimerText() {
        return energyGuideLegalDisclaimerText;
    }

    public void setEnergyGuideLegalDisclaimerText(String energyGuideLegalDisclaimerText) {
        this.energyGuideLegalDisclaimerText = energyGuideLegalDisclaimerText == null ? null : energyGuideLegalDisclaimerText.trim();
    }

    public String getEnergyGuideLegalDisclaimerInformation() {
        return energyGuideLegalDisclaimerInformation;
    }

    public void setEnergyGuideLegalDisclaimerInformation(String energyGuideLegalDisclaimerInformation) {
        this.energyGuideLegalDisclaimerInformation = energyGuideLegalDisclaimerInformation == null ? null : energyGuideLegalDisclaimerInformation.trim();
    }

    public String getEnergyGuideHasEnergyStar() {
        return energyGuideHasEnergyStar;
    }

    public void setEnergyGuideHasEnergyStar(String energyGuideHasEnergyStar) {
        this.energyGuideHasEnergyStar = energyGuideHasEnergyStar == null ? null : energyGuideHasEnergyStar.trim();
    }

    public String getReturnMethod() {
        return returnMethod;
    }

    public void setReturnMethod(String returnMethod) {
        this.returnMethod = returnMethod == null ? null : returnMethod.trim();
    }

    public String getCountryOfOriginCode() {
        return countryOfOriginCode;
    }

    public void setCountryOfOriginCode(String countryOfOriginCode) {
        this.countryOfOriginCode = countryOfOriginCode == null ? null : countryOfOriginCode.trim();
    }

    public String getPerordercharges() {
        return perordercharges;
    }

    public void setPerordercharges(String perordercharges) {
        this.perordercharges = perordercharges == null ? null : perordercharges.trim();
    }

    public String getPerunitcharges() {
        return perunitcharges;
    }

    public void setPerunitcharges(String perunitcharges) {
        this.perunitcharges = perunitcharges == null ? null : perunitcharges.trim();
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight == null ? null : weight.trim();
    }

    public String getItembulky() {
        return itembulky;
    }

    public void setItembulky(String itembulky) {
        this.itembulky = itembulky == null ? null : itembulky.trim();
    }

    public String getFixedprice() {
        return fixedprice;
    }

    public void setFixedprice(String fixedprice) {
        this.fixedprice = fixedprice == null ? null : fixedprice.trim();
    }

    public String getHandlingfee() {
        return handlingfee;
    }

    public void setHandlingfee(String handlingfee) {
        this.handlingfee = handlingfee == null ? null : handlingfee.trim();
    }

    public String getHandlingfeemessage() {
        return handlingfeemessage;
    }

    public void setHandlingfeemessage(String handlingfeemessage) {
        this.handlingfeemessage = handlingfeemessage == null ? null : handlingfeemessage.trim();
    }

    public String getHandlingfeelegaldisclaimer() {
        return handlingfeelegaldisclaimer;
    }

    public void setHandlingfeelegaldisclaimer(String handlingfeelegaldisclaimer) {
        this.handlingfeelegaldisclaimer = handlingfeelegaldisclaimer == null ? null : handlingfeelegaldisclaimer.trim();
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn == null ? null : isbn.trim();
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern == null ? null : pattern.trim();
    }

    public String getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(String ageGroup) {
        this.ageGroup = ageGroup == null ? null : ageGroup.trim();
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender == null ? null : gender.trim();
    }

    public String getBarcodeType() {
        return barcodeType;
    }

    public void setBarcodeType(String barcodeType) {
        this.barcodeType = barcodeType == null ? null : barcodeType.trim();
    }

    public String getIacAttributes() {
        return iacAttributes;
    }

    public void setIacAttributes(String iacAttributes) {
        this.iacAttributes = iacAttributes == null ? null : iacAttributes.trim();
    }

    public String getSecBarcode() {
        return secBarcode;
    }

    public void setSecBarcode(String secBarcode) {
        this.secBarcode = secBarcode == null ? null : secBarcode.trim();
    }

    public String getMozartVendorId() {
        return mozartVendorId;
    }

    public void setMozartVendorId(String mozartVendorId) {
        this.mozartVendorId = mozartVendorId == null ? null : mozartVendorId.trim();
    }

    public String getSpecialitemsource() {
        return specialitemsource;
    }

    public void setSpecialitemsource(String specialitemsource) {
        this.specialitemsource = specialitemsource == null ? null : specialitemsource.trim();
    }

    public String getEnrichmentReturnPolicies() {
        return enrichmentReturnPolicies;
    }

    public void setEnrichmentReturnPolicies(String enrichmentReturnPolicies) {
        this.enrichmentReturnPolicies = enrichmentReturnPolicies == null ? null : enrichmentReturnPolicies.trim();
    }

    public String getProductVendors() {
        return productVendors;
    }

    public void setProductVendors(String productVendors) {
        this.productVendors = productVendors;
    }
}