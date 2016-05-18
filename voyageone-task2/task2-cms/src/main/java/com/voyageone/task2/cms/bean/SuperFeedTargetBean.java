package com.voyageone.task2.cms.bean;

import com.voyageone.common.util.MD5;
import com.voyageone.common.util.excel.ReflectUtil;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SuperFeedTargetBean extends  SuperFeedBean{
    private String sku;

    private String parentSku;

    private String manufacturerName;

    private String mpn;

    private String modelNumber;

    private String name;

    private String description;

    private String shortDescription;

    private String regularPrice;

    private String salePrice;

    private String map;

    private String category;

    private String availability;

    private String brand;

    private String upc;

    private String isbn;

    private String currency;

    private String shippingWeight;

    private String margin;

    private String marginPercent;

    private String catentryid;

    private String webclass;

    private String subclass;

    private String mozartVendorId;

    private String sellingChannel;

    private String itemStatus;

    private String launchdate;

    private String availabilitydate;

    private String averageOverallRating;

    private String totalItemReviews;

    private String md5;

    private Integer updateflag;

    private String buyUrl;

    private String largeImageUrl;

    private String swatchImage;

    private String keywords;

    private String specifications;

    private String variationThemes;

    private String attributeNames;

    private String attributeValues;

    private String mozartVendorName;

    private String dpci;

    private String division;

    private String itemKind;

    private String streetdate;

    private String ratableAttribute;

    private String webclassName;

    private String subclassName;

    private String manufacturingBrand;

    private String contributorType;

    private String contributor;

    private String mediaFormat;

    private String barcodeType;

    private String specialitemsource;

    private String esrbagerating;

    private String tvrating;

    private String bopoFlag;

    private String soiPriceDisplay;

    private String pricecode;

    private String listprice;

    private String listPriceRange;

    private String salePriceRange;

    private String salesCategory;

    private String largeImageAlternate;

    private String sizingChart;

    private String sizeChart;

    private String shippingService;

    private String packageLength;

    private String packageWidth;

    private String packageHeight;

    private String taxCategory;

    private String facility;

    private String bulky;

    private String expertReviewFlag;

    private String thirdpartyhostedlink;

    private String pickupinstore;

    private String subscription;

    private String secattributes;

    private String secBarcode;

    private String autoBullets;

    private String iacAttributes;

    private String categoryIacid;

    private String calloutMsg;

    private String buyable;

    private String backorderType;

    private String maxOrderQty;

    private String isHazmat;

    private String isFood;

    private String wheneverShippingEligible;

    private String shipToRestriction;

    private String poBoxProhibited;

    private String nutritionFactsFlag;

    private String drugFactsFlag;

    private String energyGuideCms;

    private String videoCount;

    private String ffPickupinstoreRushdeliveryShiptostoreShipfromstore;

    private String saveStory;

    private String giftWrapable;

    private String signRequired;

    private String returnMethod;

    private String defaultReturnPolicy;

    private String normalReturnPolicy;

    private String bestReturnPolicy;

    private String displayEligibility;

    private String ageRestriction;

    private String hasWarranty;

    private String marketprice;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription == null ? null : shortDescription.trim();
    }

    public String getBuyUrl() {
        return buyUrl;
    }

    public void setBuyUrl(String buyUrl) {
        this.buyUrl = buyUrl == null ? null : buyUrl.trim();
    }

    public String getLargeImageUrl() {
        return largeImageUrl;
    }

    public void setLargeImageUrl(String largeImageUrl) {
        this.largeImageUrl = largeImageUrl == null ? null : largeImageUrl.trim();
    }

    public String getSwatchImage() {
        return swatchImage;
    }

    public void setSwatchImage(String swatchImage) {
        this.swatchImage = swatchImage == null ? null : swatchImage.trim();
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords == null ? null : keywords.trim();
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications == null ? null : specifications.trim();
    }

    public String getVariationThemes() {
        return variationThemes;
    }

    public void setVariationThemes(String variationThemes) {
        this.variationThemes = variationThemes == null ? null : variationThemes.trim();
    }

    public String getAttributeNames() {
        return attributeNames;
    }

    public void setAttributeNames(String attributeNames) {
        this.attributeNames = attributeNames == null ? null : attributeNames.trim();
    }

    public String getAttributeValues() {
        return attributeValues;
    }

    public void setAttributeValues(String attributeValues) {
        this.attributeValues = attributeValues == null ? null : attributeValues.trim();
    }

    public String getMozartVendorName() {
        return mozartVendorName;
    }

    public void setMozartVendorName(String mozartVendorName) {
        this.mozartVendorName = mozartVendorName == null ? null : mozartVendorName.trim();
    }

    public String getDpci() {
        return dpci;
    }

    public void setDpci(String dpci) {
        this.dpci = dpci == null ? null : dpci.trim();
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division == null ? null : division.trim();
    }

    public String getItemKind() {
        return itemKind;
    }

    public void setItemKind(String itemKind) {
        this.itemKind = itemKind == null ? null : itemKind.trim();
    }

    public String getStreetdate() {
        return streetdate;
    }

    public void setStreetdate(String streetdate) {
        this.streetdate = streetdate == null ? null : streetdate.trim();
    }

    public String getRatableAttribute() {
        return ratableAttribute;
    }

    public void setRatableAttribute(String ratableAttribute) {
        this.ratableAttribute = ratableAttribute == null ? null : ratableAttribute.trim();
    }

    public String getWebclassName() {
        return webclassName;
    }

    public void setWebclassName(String webclassName) {
        this.webclassName = webclassName == null ? null : webclassName.trim();
    }

    public String getSubclassName() {
        return subclassName;
    }

    public void setSubclassName(String subclassName) {
        this.subclassName = subclassName == null ? null : subclassName.trim();
    }

    public String getManufacturingBrand() {
        return manufacturingBrand;
    }

    public void setManufacturingBrand(String manufacturingBrand) {
        this.manufacturingBrand = manufacturingBrand == null ? null : manufacturingBrand.trim();
    }

    public String getContributorType() {
        return contributorType;
    }

    public void setContributorType(String contributorType) {
        this.contributorType = contributorType == null ? null : contributorType.trim();
    }

    public String getContributor() {
        return contributor;
    }

    public void setContributor(String contributor) {
        this.contributor = contributor == null ? null : contributor.trim();
    }

    public String getMediaFormat() {
        return mediaFormat;
    }

    public void setMediaFormat(String mediaFormat) {
        this.mediaFormat = mediaFormat == null ? null : mediaFormat.trim();
    }

    public String getBarcodeType() {
        return barcodeType;
    }

    public void setBarcodeType(String barcodeType) {
        this.barcodeType = barcodeType == null ? null : barcodeType.trim();
    }

    public String getSpecialitemsource() {
        return specialitemsource;
    }

    public void setSpecialitemsource(String specialitemsource) {
        this.specialitemsource = specialitemsource == null ? null : specialitemsource.trim();
    }

    public String getEsrbagerating() {
        return esrbagerating;
    }

    public void setEsrbagerating(String esrbagerating) {
        this.esrbagerating = esrbagerating == null ? null : esrbagerating.trim();
    }

    public String getTvrating() {
        return tvrating;
    }

    public void setTvrating(String tvrating) {
        this.tvrating = tvrating == null ? null : tvrating.trim();
    }

    public String getBopoFlag() {
        return bopoFlag;
    }

    public void setBopoFlag(String bopoFlag) {
        this.bopoFlag = bopoFlag == null ? null : bopoFlag.trim();
    }

    public String getSoiPriceDisplay() {
        return soiPriceDisplay;
    }

    public void setSoiPriceDisplay(String soiPriceDisplay) {
        this.soiPriceDisplay = soiPriceDisplay == null ? null : soiPriceDisplay.trim();
    }

    public String getPricecode() {
        return pricecode;
    }

    public void setPricecode(String pricecode) {
        this.pricecode = pricecode == null ? null : pricecode.trim();
    }

    public String getListprice() {
        return listprice;
    }

    public void setListprice(String listprice) {
        this.listprice = listprice == null ? null : listprice.trim();
    }

    public String getListPriceRange() {
        return listPriceRange;
    }

    public void setListPriceRange(String listPriceRange) {
        this.listPriceRange = listPriceRange == null ? null : listPriceRange.trim();
    }

    public String getSalePriceRange() {
        return salePriceRange;
    }

    public void setSalePriceRange(String salePriceRange) {
        this.salePriceRange = salePriceRange == null ? null : salePriceRange.trim();
    }

    public String getSalesCategory() {
        return salesCategory;
    }

    public void setSalesCategory(String salesCategory) {
        this.salesCategory = salesCategory == null ? null : salesCategory.trim();
    }

    public String getLargeImageAlternate() {
        return largeImageAlternate;
    }

    public void setLargeImageAlternate(String largeImageAlternate) {
        this.largeImageAlternate = largeImageAlternate == null ? null : largeImageAlternate.trim();
    }

    public String getSizingChart() {
        return sizingChart;
    }

    public void setSizingChart(String sizingChart) {
        this.sizingChart = sizingChart == null ? null : sizingChart.trim();
    }

    public String getSizeChart() {
        return sizeChart;
    }

    public void setSizeChart(String sizeChart) {
        this.sizeChart = sizeChart == null ? null : sizeChart.trim();
    }

    public String getShippingService() {
        return shippingService;
    }

    public void setShippingService(String shippingService) {
        this.shippingService = shippingService == null ? null : shippingService.trim();
    }

    public String getPackageLength() {
        return packageLength;
    }

    public void setPackageLength(String packageLength) {
        this.packageLength = packageLength == null ? null : packageLength.trim();
    }

    public String getPackageWidth() {
        return packageWidth;
    }

    public void setPackageWidth(String packageWidth) {
        this.packageWidth = packageWidth == null ? null : packageWidth.trim();
    }

    public String getPackageHeight() {
        return packageHeight;
    }

    public void setPackageHeight(String packageHeight) {
        this.packageHeight = packageHeight == null ? null : packageHeight.trim();
    }

    public String getTaxCategory() {
        return taxCategory;
    }

    public void setTaxCategory(String taxCategory) {
        this.taxCategory = taxCategory == null ? null : taxCategory.trim();
    }

    public String getFacility() {
        return facility;
    }

    public void setFacility(String facility) {
        this.facility = facility == null ? null : facility.trim();
    }

    public String getBulky() {
        return bulky;
    }

    public void setBulky(String bulky) {
        this.bulky = bulky == null ? null : bulky.trim();
    }

    public String getExpertReviewFlag() {
        return expertReviewFlag;
    }

    public void setExpertReviewFlag(String expertReviewFlag) {
        this.expertReviewFlag = expertReviewFlag == null ? null : expertReviewFlag.trim();
    }

    public String getThirdpartyhostedlink() {
        return thirdpartyhostedlink;
    }

    public void setThirdpartyhostedlink(String thirdpartyhostedlink) {
        this.thirdpartyhostedlink = thirdpartyhostedlink == null ? null : thirdpartyhostedlink.trim();
    }

    public String getPickupinstore() {
        return pickupinstore;
    }

    public void setPickupinstore(String pickupinstore) {
        this.pickupinstore = pickupinstore == null ? null : pickupinstore.trim();
    }

    public String getSubscription() {
        return subscription;
    }

    public void setSubscription(String subscription) {
        this.subscription = subscription == null ? null : subscription.trim();
    }

    public String getSecattributes() {
        return secattributes;
    }

    public void setSecattributes(String secattributes) {
        this.secattributes = secattributes == null ? null : secattributes.trim();
    }

    public String getSecBarcode() {
        return secBarcode;
    }

    public void setSecBarcode(String secBarcode) {
        this.secBarcode = secBarcode == null ? null : secBarcode.trim();
    }

    public String getAutoBullets() {
        return autoBullets;
    }

    public void setAutoBullets(String autoBullets) {
        this.autoBullets = autoBullets == null ? null : autoBullets.trim();
    }

    public String getIacAttributes() {
        return iacAttributes;
    }

    public void setIacAttributes(String iacAttributes) {
        this.iacAttributes = iacAttributes == null ? null : iacAttributes.trim();
    }

    public String getCategoryIacid() {
        return categoryIacid;
    }

    public void setCategoryIacid(String categoryIacid) {
        this.categoryIacid = categoryIacid == null ? null : categoryIacid.trim();
    }

    public String getCalloutMsg() {
        return calloutMsg;
    }

    public void setCalloutMsg(String calloutMsg) {
        this.calloutMsg = calloutMsg == null ? null : calloutMsg.trim();
    }

    public String getBuyable() {
        return buyable;
    }

    public void setBuyable(String buyable) {
        this.buyable = buyable == null ? null : buyable.trim();
    }

    public String getBackorderType() {
        return backorderType;
    }

    public void setBackorderType(String backorderType) {
        this.backorderType = backorderType == null ? null : backorderType.trim();
    }

    public String getMaxOrderQty() {
        return maxOrderQty;
    }

    public void setMaxOrderQty(String maxOrderQty) {
        this.maxOrderQty = maxOrderQty == null ? null : maxOrderQty.trim();
    }

    public String getIsHazmat() {
        return isHazmat;
    }

    public void setIsHazmat(String isHazmat) {
        this.isHazmat = isHazmat == null ? null : isHazmat.trim();
    }

    public String getIsFood() {
        return isFood;
    }

    public void setIsFood(String isFood) {
        this.isFood = isFood == null ? null : isFood.trim();
    }

    public String getWheneverShippingEligible() {
        return wheneverShippingEligible;
    }

    public void setWheneverShippingEligible(String wheneverShippingEligible) {
        this.wheneverShippingEligible = wheneverShippingEligible == null ? null : wheneverShippingEligible.trim();
    }

    public String getShipToRestriction() {
        return shipToRestriction;
    }

    public void setShipToRestriction(String shipToRestriction) {
        this.shipToRestriction = shipToRestriction == null ? null : shipToRestriction.trim();
    }

    public String getPoBoxProhibited() {
        return poBoxProhibited;
    }

    public void setPoBoxProhibited(String poBoxProhibited) {
        this.poBoxProhibited = poBoxProhibited == null ? null : poBoxProhibited.trim();
    }

    public String getNutritionFactsFlag() {
        return nutritionFactsFlag;
    }

    public void setNutritionFactsFlag(String nutritionFactsFlag) {
        this.nutritionFactsFlag = nutritionFactsFlag == null ? null : nutritionFactsFlag.trim();
    }

    public String getDrugFactsFlag() {
        return drugFactsFlag;
    }

    public void setDrugFactsFlag(String drugFactsFlag) {
        this.drugFactsFlag = drugFactsFlag == null ? null : drugFactsFlag.trim();
    }

    public String getEnergyGuideCms() {
        return energyGuideCms;
    }

    public void setEnergyGuideCms(String energyGuideCms) {
        this.energyGuideCms = energyGuideCms == null ? null : energyGuideCms.trim();
    }

    public String getVideoCount() {
        return videoCount;
    }

    public void setVideoCount(String videoCount) {
        this.videoCount = videoCount == null ? null : videoCount.trim();
    }

    public String getFfPickupinstoreRushdeliveryShiptostoreShipfromstore() {
        return ffPickupinstoreRushdeliveryShiptostoreShipfromstore;
    }

    public void setFfPickupinstoreRushdeliveryShiptostoreShipfromstore(String ffPickupinstoreRushdeliveryShiptostoreShipfromstore) {
        this.ffPickupinstoreRushdeliveryShiptostoreShipfromstore = ffPickupinstoreRushdeliveryShiptostoreShipfromstore == null ? null : ffPickupinstoreRushdeliveryShiptostoreShipfromstore.trim();
    }

    public String getSaveStory() {
        return saveStory;
    }

    public void setSaveStory(String saveStory) {
        this.saveStory = saveStory == null ? null : saveStory.trim();
    }

    public String getGiftWrapable() {
        return giftWrapable;
    }

    public void setGiftWrapable(String giftWrapable) {
        this.giftWrapable = giftWrapable == null ? null : giftWrapable.trim();
    }

    public String getSignRequired() {
        return signRequired;
    }

    public void setSignRequired(String signRequired) {
        this.signRequired = signRequired == null ? null : signRequired.trim();
    }

    public String getReturnMethod() {
        return returnMethod;
    }

    public void setReturnMethod(String returnMethod) {
        this.returnMethod = returnMethod == null ? null : returnMethod.trim();
    }

    public String getDefaultReturnPolicy() {
        return defaultReturnPolicy;
    }

    public void setDefaultReturnPolicy(String defaultReturnPolicy) {
        this.defaultReturnPolicy = defaultReturnPolicy == null ? null : defaultReturnPolicy.trim();
    }

    public String getNormalReturnPolicy() {
        return normalReturnPolicy;
    }

    public void setNormalReturnPolicy(String normalReturnPolicy) {
        this.normalReturnPolicy = normalReturnPolicy == null ? null : normalReturnPolicy.trim();
    }

    public String getBestReturnPolicy() {
        return bestReturnPolicy;
    }

    public void setBestReturnPolicy(String bestReturnPolicy) {
        this.bestReturnPolicy = bestReturnPolicy == null ? null : bestReturnPolicy.trim();
    }

    public String getDisplayEligibility() {
        return displayEligibility;
    }

    public void setDisplayEligibility(String displayEligibility) {
        this.displayEligibility = displayEligibility == null ? null : displayEligibility.trim();
    }

    public String getAgeRestriction() {
        return ageRestriction;
    }

    public void setAgeRestriction(String ageRestriction) {
        this.ageRestriction = ageRestriction == null ? null : ageRestriction.trim();
    }

    public String getHasWarranty() {
        return hasWarranty;
    }

    public void setHasWarranty(String hasWarranty) {
        this.hasWarranty = hasWarranty == null ? null : hasWarranty.trim();
    }
    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku == null ? null : sku.trim();
    }

    public String getParentSku() {
        return parentSku;
    }

    public void setParentSku(String parentSku) {
        this.parentSku = parentSku == null ? null : parentSku.trim();
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName == null ? null : manufacturerName.trim();
    }

    public String getMpn() {
        return mpn;
    }

    public void setMpn(String mpn) {
        this.mpn = mpn == null ? null : mpn.trim();
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber == null ? null : modelNumber.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getRegularPrice() {
        return regularPrice;
    }

    public void setRegularPrice(String regularPrice) {
        this.regularPrice = regularPrice == null ? null : regularPrice.trim();
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice == null ? null : salePrice.trim();
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map == null ? null : map.trim();
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category == null ? null : category.trim();
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability == null ? null : availability.trim();
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand == null ? null : brand.trim();
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc == null ? null : upc.trim();
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn == null ? null : isbn.trim();
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency == null ? null : currency.trim();
    }

    public String getShippingWeight() {
        return shippingWeight;
    }

    public void setShippingWeight(String shippingWeight) {
        this.shippingWeight = shippingWeight == null ? null : shippingWeight.trim();
    }

    public String getMargin() {
        return margin;
    }

    public void setMargin(String margin) {
        this.margin = margin == null ? null : margin.trim();
    }

    public String getMarginPercent() {
        return marginPercent;
    }

    public void setMarginPercent(String marginPercent) {
        this.marginPercent = marginPercent == null ? null : marginPercent.trim();
    }

    public String getCatentryid() {
        return catentryid;
    }

    public void setCatentryid(String catentryid) {
        this.catentryid = catentryid == null ? null : catentryid.trim();
    }

    public String getWebclass() {
        return webclass;
    }

    public void setWebclass(String webclass) {
        this.webclass = webclass == null ? null : webclass.trim();
    }

    public String getSubclass() {
        return subclass;
    }

    public void setSubclass(String subclass) {
        this.subclass = subclass == null ? null : subclass.trim();
    }

    public String getMozartVendorId() {
        return mozartVendorId;
    }

    public void setMozartVendorId(String mozartVendorId) {
        this.mozartVendorId = mozartVendorId == null ? null : mozartVendorId.trim();
    }

    public String getSellingChannel() {
        return sellingChannel;
    }

    public void setSellingChannel(String sellingChannel) {
        this.sellingChannel = sellingChannel == null ? null : sellingChannel.trim();
    }

    public String getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(String itemStatus) {
        this.itemStatus = itemStatus == null ? null : itemStatus.trim();
    }

    public String getLaunchdate() {
        return launchdate;
    }

    public void setLaunchdate(String launchdate) {
        this.launchdate = launchdate == null ? null : launchdate.trim();
    }

    public String getAvailabilitydate() {
        return availabilitydate;
    }

    public void setAvailabilitydate(String availabilitydate) {
        this.availabilitydate = availabilitydate == null ? null : availabilitydate.trim();
    }

    public String getAverageOverallRating() {
        return averageOverallRating;
    }

    public void setAverageOverallRating(String averageOverallRating) {
        this.averageOverallRating = averageOverallRating == null ? null : averageOverallRating.trim();
    }

    public String getTotalItemReviews() {
        return totalItemReviews;
    }

    public void setTotalItemReviews(String totalItemReviews) {
        this.totalItemReviews = totalItemReviews == null ? null : totalItemReviews.trim();
    }

    public String getMarketprice() {
        return marketprice;
    }

    public void setMarketprice(String marketprice) {
        this.marketprice = marketprice;
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

}