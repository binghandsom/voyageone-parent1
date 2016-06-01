package com.voyageone.task2.cms.bean;

import com.voyageone.common.util.MD5;

import java.util.HashSet;
import java.util.Set;

public class SuperfeedBhfoBean extends SuperFeedBean{
    private String inventoryNumber;

    private String title;

    private String msrp;

    private String voyageonePrice;

    private String shippingCost;

    private String weight;

    private String description;

    private String mainImageUrl;

    private String quantity;

    private String additionalImages;

    private String parentChildStandalone;

    private String itemGroupId;

    private String variationSwapImageUrl;

    private String varyingAttributeNames;

    private String varyingAttribute1;

    private String varyingAttribute2;

    private String brand1;

    private String countryOfOrigin;

    private String seriesName;

    private String gtin;

    private String mpn;

    private String storeCategory;

    private String material;

    private String pattern;

    private String color;

    private String size;

    private String gender;

    private String ageGroup;

    private String length;

    private String width;

    private String height;

    private String amazonBeautyItemType;

    private String amazonDepartment;

    private String amazonHeelHeight;

    private String amazonHomeItemType;

    private String amazonInseam;

    private String amazonItemType;

    private String amazonKeywords1;

    private String amazonKeywords2;

    private String amazonKeywords3;

    private String amazonKeywords4;

    private String amazonLegStyle;

    private String amazonMensItemType;

    private String amazonPhotoReady;

    private String amazonProductType;

    private String amazonRise;

    private String amazonShoeItemType;

    private String amazonShoeSize;

    private String amazonShoeSizeMap;

    private String amazonShoeWidth;

    private String amazonSpecialSize;

    private String amazonVariantDetails;

    private String amazonWaistSize;

    private String amazonWomensItemType;

    private String apparelStyle;

    private String application;

    private String applicationArea;

    private String amazonCategory;

    private String applicationType;

    private String asin;

    private String assembly;

    private String attachment;

    private String auctionDescription;

    private String audioOutputs;

    private String backCoverage;

    private String backPockets;

    private String bagDepthInches;

    private String bagHeightInches;

    private String bagLengthInches;

    private String bagWidthInches;

    private String bakeShape;

    private String beddingSize;

    private String bhfoEbayCategory;

    private String bhfoEbayStoreCategory;

    private String bhfoAccessory1DescriptionC;

    private String bhfoAccessory1TitleC;

    private String bhfoColorC;

    private String bhfoColorTextC;

    private String bhfoCountryOfOriginC;

    private String bhfoFlawDescriptionC;

    private String bhfoSizeC;

    private String bhfoSizeAdditionalC;

    private String bhfoSizeTypeC;

    private String bhfoStyleC;

    private String bhfoStyleNameC;

    private String bhfoStyleTypeC;

    private String bottleMaterial;

    private String bottleSizeOunces;

    private String bottomClosure;

    private String bottomRise;

    private String braClosure;

    private String braFeatures;

    private String braStraps;

    private String brand2;

    private String caStoreCategory;

    private String candleSize;

    private String capacityOunces;

    private String classification;

    private String closure;

    private String collar;

    private String collection;

    private String color2;

    private String compartment;

    private String compatibleBrands;

    private String compatibleProducts;

    private String condition;

    private String connectivity;

    private String cookingFunctions;

    private String countryOfOrigin2;

    private String cupSize;

    private String denimWash;

    private String depthInches;

    private String dimensions;

    private String dimensions2;

    private String dirtCapture;

    private String display;

    private String dressLength;

    private String dressType;

    private String dropLength;

    private String ean;

    private String earpieceDesign;

    private String ebayHeelHeight;

    private String fabricType;

    private String features;

    private String featuresAndFastening;

    private String features2;

    private String finish;

    private String fit;

    private String fitDesign;

    private String flagstyle;

    private String formulation;

    private String frameMaterialType;

    private String frameMetal;

    private String frontStyle;

    private String fuelSource;

    private String function;

    private String furType;

    private String gender2;

    private String genre;

    private String handleType;

    private String harmonizedCode;

    private String heelHeightInches;

    private String height2;

    private String imageUrl1;

    private String imageUrl2;

    private String imageUrl3;

    private String imageUrl4;

    private String includes;

    private String includes2;

    private String inseamInches;

    private String insertMaterial;

    private String isAChildItem;

    private String isAParentItem;

    private String isblocked;

    private String isbn;

    private String isfba;

    private String itemId;

    private String itemShape;

    private String itemSubtitle;

    private String itemTitle;

    private String itemauctiondescription;

    private String itemimageurl10;

    private String itemimageurl5;

    private String itemimageurl6;

    private String itemimageurl7;

    private String jacketLegth;

    private String jacketSleeveLengthInches;

    private String length2;

    private String lensColor;

    private String lensMaterialType;

    private String lensWidthMillimeters;

    private String lid;

    private String lightbulbType;

    private String manufacturer;

    private String manufacturerDescription;

    private String material2;

    private String materialSpecialty;

    private String maxMagnification;

    private String meliCategory;

    private String meliColor;

    private String meliSeason;

    private String meliSize;

    private String meliTitle;

    private String modelNumber;

    private String modelNumber2;

    private String movement;

    private String mpn2;

    private String neckSize;

    private String neckline;

    private String itemlevelshipping;

    private String nonStick;

    private String numberOfPieces;

    private String numberOfSlices;

    private String occasion;

    private String openQuantity;

    private String openquantitypooled;

    private String padding;

    private String pantType;

    private String pantyStyle;

    private String parentSku;

    private String pattern2;

    private String photoSize;

    private String pillowSize;

    private String platform;

    private String platformHeightInches;

    private String pocketStyle;

    private String polarizationType;

    private String power;

    private String productMargin;

    private String protection;

    private String quantityAvailable;

    private String rating;

    private String receivedInInventory;

    private String recommendedAge;

    private String recommendedAgeRange;

    private String relationshipName;

    private String retailPrice;

    private String riseInches;

    private String ropDateFirstComplete;

    private String rugStyle;

    private String rykaConsignment;

    private String scentType;

    private String season;

    private String sellerLogoUrl;

    private String set;

    private String shaftHeightInches;

    private String shaftWidthInches;

    private String shape;

    private String shoeClosure;

    private String shortDescription;

    private String silhouette;

    private String sizeOrigin;

    private String skirtLength;

    private String skirtType;

    private String sku;

    private String sleeveLength;

    private String sleeveLengthInches;

    private String specialty;

    private String sport;

    private String stKeyword;

    private String stone;

    private String storeTitle;

    private String strapDropInches;

    private String straps;

    private String style;

    private String supplier;

    private String supplierPo;

    private String suppliercode;

    private String supplierid;

    private String swimwearBottom;

    private String swimwearTop;

    private String taxproductcode;

    private String theme;

    private String threadCount;

    private String tieLength;

    private String tieWidth;

    private String totalDressLengthInches;

    private String totalJacketLengthInches;

    private String totalLengthInches;

    private String totalSkirtLengthInches;

    private String totalquantitypooled;

    private String umbrellaOpener;

    private String upc;

    private String uploadhaslastmodifiedgmt;

    private String vents;

    private String videoInputs;

    private String waistAcrossInches;

    private String walletHeightInches;

    private String warehouselocation;

    private String warranty;

    private String weight2;

    private String width2;

    private String year;

    private String room;

    private String latestSupplier;

    private String bandSize;

    private String walletWidthInches;

    private String md5;




    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getMainImageUrl() {
        return mainImageUrl;
    }

    public void setMainImageUrl(String mainImageUrl) {
        this.mainImageUrl = mainImageUrl == null ? null : mainImageUrl.trim();
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity == null ? null : quantity.trim();
    }

    public String getAdditionalImages() {
        return additionalImages;
    }

    public void setAdditionalImages(String additionalImages) {
        this.additionalImages = additionalImages == null ? null : additionalImages.trim();
    }

    public String getParentChildStandalone() {
        return parentChildStandalone;
    }

    public void setParentChildStandalone(String parentChildStandalone) {
        this.parentChildStandalone = parentChildStandalone == null ? null : parentChildStandalone.trim();
    }

    public String getItemGroupId() {
        return itemGroupId;
    }

    public void setItemGroupId(String itemGroupId) {
        this.itemGroupId = itemGroupId == null ? null : itemGroupId.trim();
    }

    public String getVariationSwapImageUrl() {
        return variationSwapImageUrl;
    }

    public void setVariationSwapImageUrl(String variationSwapImageUrl) {
        this.variationSwapImageUrl = variationSwapImageUrl == null ? null : variationSwapImageUrl.trim();
    }

    public String getVaryingAttributeNames() {
        return varyingAttributeNames;
    }

    public void setVaryingAttributeNames(String varyingAttributeNames) {
        this.varyingAttributeNames = varyingAttributeNames == null ? null : varyingAttributeNames.trim();
    }

    public String getVaryingAttribute1() {
        return varyingAttribute1;
    }

    public void setVaryingAttribute1(String varyingAttribute1) {
        this.varyingAttribute1 = varyingAttribute1 == null ? null : varyingAttribute1.trim();
    }

    public String getVaryingAttribute2() {
        return varyingAttribute2;
    }

    public void setVaryingAttribute2(String varyingAttribute2) {
        this.varyingAttribute2 = varyingAttribute2 == null ? null : varyingAttribute2.trim();
    }

    public String getBrand1() {
        return brand1;
    }

    public void setBrand1(String brand1) {
        this.brand1 = brand1 == null ? null : brand1.trim();
    }

    public String getCountryOfOrigin() {
        return countryOfOrigin;
    }

    public void setCountryOfOrigin(String countryOfOrigin) {
        this.countryOfOrigin = countryOfOrigin == null ? null : countryOfOrigin.trim();
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName == null ? null : seriesName.trim();
    }

    public String getGtin() {
        return gtin;
    }

    public void setGtin(String gtin) {
        this.gtin = gtin == null ? null : gtin.trim();
    }

    public String getMpn() {
        return mpn;
    }

    public void setMpn(String mpn) {
        this.mpn = mpn == null ? null : mpn.trim();
    }

    public String getStoreCategory() {
        return storeCategory;
    }

    public void setStoreCategory(String storeCategory) {
        this.storeCategory = storeCategory == null ? null : storeCategory.trim();
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material == null ? null : material.trim();
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern == null ? null : pattern.trim();
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color == null ? null : color.trim();
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size == null ? null : size.trim();
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender == null ? null : gender.trim();
    }

    public String getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(String ageGroup) {
        this.ageGroup = ageGroup == null ? null : ageGroup.trim();
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length == null ? null : length.trim();
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width == null ? null : width.trim();
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height == null ? null : height.trim();
    }

    public String getAmazonBeautyItemType() {
        return amazonBeautyItemType;
    }

    public void setAmazonBeautyItemType(String amazonBeautyItemType) {
        this.amazonBeautyItemType = amazonBeautyItemType == null ? null : amazonBeautyItemType.trim();
    }

    public String getAmazonDepartment() {
        return amazonDepartment;
    }

    public void setAmazonDepartment(String amazonDepartment) {
        this.amazonDepartment = amazonDepartment == null ? null : amazonDepartment.trim();
    }

    public String getAmazonHeelHeight() {
        return amazonHeelHeight;
    }

    public void setAmazonHeelHeight(String amazonHeelHeight) {
        this.amazonHeelHeight = amazonHeelHeight == null ? null : amazonHeelHeight.trim();
    }

    public String getAmazonHomeItemType() {
        return amazonHomeItemType;
    }

    public void setAmazonHomeItemType(String amazonHomeItemType) {
        this.amazonHomeItemType = amazonHomeItemType == null ? null : amazonHomeItemType.trim();
    }

    public String getAmazonInseam() {
        return amazonInseam;
    }

    public void setAmazonInseam(String amazonInseam) {
        this.amazonInseam = amazonInseam == null ? null : amazonInseam.trim();
    }

    public String getAmazonItemType() {
        return amazonItemType;
    }

    public void setAmazonItemType(String amazonItemType) {
        this.amazonItemType = amazonItemType == null ? null : amazonItemType.trim();
    }

    public String getAmazonKeywords1() {
        return amazonKeywords1;
    }

    public void setAmazonKeywords1(String amazonKeywords1) {
        this.amazonKeywords1 = amazonKeywords1 == null ? null : amazonKeywords1.trim();
    }

    public String getAmazonKeywords2() {
        return amazonKeywords2;
    }

    public void setAmazonKeywords2(String amazonKeywords2) {
        this.amazonKeywords2 = amazonKeywords2 == null ? null : amazonKeywords2.trim();
    }

    public String getAmazonKeywords3() {
        return amazonKeywords3;
    }

    public void setAmazonKeywords3(String amazonKeywords3) {
        this.amazonKeywords3 = amazonKeywords3 == null ? null : amazonKeywords3.trim();
    }

    public String getAmazonKeywords4() {
        return amazonKeywords4;
    }

    public void setAmazonKeywords4(String amazonKeywords4) {
        this.amazonKeywords4 = amazonKeywords4 == null ? null : amazonKeywords4.trim();
    }

    public String getAmazonLegStyle() {
        return amazonLegStyle;
    }

    public void setAmazonLegStyle(String amazonLegStyle) {
        this.amazonLegStyle = amazonLegStyle == null ? null : amazonLegStyle.trim();
    }

    public String getAmazonMensItemType() {
        return amazonMensItemType;
    }

    public void setAmazonMensItemType(String amazonMensItemType) {
        this.amazonMensItemType = amazonMensItemType == null ? null : amazonMensItemType.trim();
    }

    public String getAmazonPhotoReady() {
        return amazonPhotoReady;
    }

    public void setAmazonPhotoReady(String amazonPhotoReady) {
        this.amazonPhotoReady = amazonPhotoReady == null ? null : amazonPhotoReady.trim();
    }

    public String getAmazonProductType() {
        return amazonProductType;
    }

    public void setAmazonProductType(String amazonProductType) {
        this.amazonProductType = amazonProductType == null ? null : amazonProductType.trim();
    }

    public String getAmazonRise() {
        return amazonRise;
    }

    public void setAmazonRise(String amazonRise) {
        this.amazonRise = amazonRise == null ? null : amazonRise.trim();
    }

    public String getAmazonShoeItemType() {
        return amazonShoeItemType;
    }

    public void setAmazonShoeItemType(String amazonShoeItemType) {
        this.amazonShoeItemType = amazonShoeItemType == null ? null : amazonShoeItemType.trim();
    }

    public String getAmazonShoeSize() {
        return amazonShoeSize;
    }

    public void setAmazonShoeSize(String amazonShoeSize) {
        this.amazonShoeSize = amazonShoeSize == null ? null : amazonShoeSize.trim();
    }

    public String getAmazonShoeSizeMap() {
        return amazonShoeSizeMap;
    }

    public void setAmazonShoeSizeMap(String amazonShoeSizeMap) {
        this.amazonShoeSizeMap = amazonShoeSizeMap == null ? null : amazonShoeSizeMap.trim();
    }

    public String getAmazonShoeWidth() {
        return amazonShoeWidth;
    }

    public void setAmazonShoeWidth(String amazonShoeWidth) {
        this.amazonShoeWidth = amazonShoeWidth == null ? null : amazonShoeWidth.trim();
    }

    public String getAmazonSpecialSize() {
        return amazonSpecialSize;
    }

    public void setAmazonSpecialSize(String amazonSpecialSize) {
        this.amazonSpecialSize = amazonSpecialSize == null ? null : amazonSpecialSize.trim();
    }

    public String getAmazonVariantDetails() {
        return amazonVariantDetails;
    }

    public void setAmazonVariantDetails(String amazonVariantDetails) {
        this.amazonVariantDetails = amazonVariantDetails == null ? null : amazonVariantDetails.trim();
    }

    public String getAmazonWaistSize() {
        return amazonWaistSize;
    }

    public void setAmazonWaistSize(String amazonWaistSize) {
        this.amazonWaistSize = amazonWaistSize == null ? null : amazonWaistSize.trim();
    }

    public String getAmazonWomensItemType() {
        return amazonWomensItemType;
    }

    public void setAmazonWomensItemType(String amazonWomensItemType) {
        this.amazonWomensItemType = amazonWomensItemType == null ? null : amazonWomensItemType.trim();
    }

    public String getApparelStyle() {
        return apparelStyle;
    }

    public void setApparelStyle(String apparelStyle) {
        this.apparelStyle = apparelStyle == null ? null : apparelStyle.trim();
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application == null ? null : application.trim();
    }

    public String getApplicationArea() {
        return applicationArea;
    }

    public void setApplicationArea(String applicationArea) {
        this.applicationArea = applicationArea == null ? null : applicationArea.trim();
    }

    public String getAmazonCategory() {
        return amazonCategory;
    }

    public void setAmazonCategory(String amazonCategory) {
        this.amazonCategory = amazonCategory == null ? null : amazonCategory.trim();
    }

    public String getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(String applicationType) {
        this.applicationType = applicationType == null ? null : applicationType.trim();
    }

    public String getAsin() {
        return asin;
    }

    public void setAsin(String asin) {
        this.asin = asin == null ? null : asin.trim();
    }

    public String getAssembly() {
        return assembly;
    }

    public void setAssembly(String assembly) {
        this.assembly = assembly == null ? null : assembly.trim();
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment == null ? null : attachment.trim();
    }

    public String getAuctionDescription() {
        return auctionDescription;
    }

    public void setAuctionDescription(String auctionDescription) {
        this.auctionDescription = auctionDescription == null ? null : auctionDescription.trim();
    }

    public String getAudioOutputs() {
        return audioOutputs;
    }

    public void setAudioOutputs(String audioOutputs) {
        this.audioOutputs = audioOutputs == null ? null : audioOutputs.trim();
    }

    public String getBackCoverage() {
        return backCoverage;
    }

    public void setBackCoverage(String backCoverage) {
        this.backCoverage = backCoverage == null ? null : backCoverage.trim();
    }

    public String getBackPockets() {
        return backPockets;
    }

    public void setBackPockets(String backPockets) {
        this.backPockets = backPockets == null ? null : backPockets.trim();
    }

    public String getBagDepthInches() {
        return bagDepthInches;
    }

    public void setBagDepthInches(String bagDepthInches) {
        this.bagDepthInches = bagDepthInches == null ? null : bagDepthInches.trim();
    }

    public String getBagHeightInches() {
        return bagHeightInches;
    }

    public void setBagHeightInches(String bagHeightInches) {
        this.bagHeightInches = bagHeightInches == null ? null : bagHeightInches.trim();
    }

    public String getBagLengthInches() {
        return bagLengthInches;
    }

    public void setBagLengthInches(String bagLengthInches) {
        this.bagLengthInches = bagLengthInches == null ? null : bagLengthInches.trim();
    }

    public String getBagWidthInches() {
        return bagWidthInches;
    }

    public void setBagWidthInches(String bagWidthInches) {
        this.bagWidthInches = bagWidthInches == null ? null : bagWidthInches.trim();
    }

    public String getBakeShape() {
        return bakeShape;
    }

    public void setBakeShape(String bakeShape) {
        this.bakeShape = bakeShape == null ? null : bakeShape.trim();
    }

    public String getBeddingSize() {
        return beddingSize;
    }

    public void setBeddingSize(String beddingSize) {
        this.beddingSize = beddingSize == null ? null : beddingSize.trim();
    }

    public String getBhfoEbayCategory() {
        return bhfoEbayCategory;
    }

    public void setBhfoEbayCategory(String bhfoEbayCategory) {
        this.bhfoEbayCategory = bhfoEbayCategory == null ? null : bhfoEbayCategory.trim();
    }

    public String getBhfoEbayStoreCategory() {
        return bhfoEbayStoreCategory;
    }

    public void setBhfoEbayStoreCategory(String bhfoEbayStoreCategory) {
        this.bhfoEbayStoreCategory = bhfoEbayStoreCategory == null ? null : bhfoEbayStoreCategory.trim();
    }

    public String getBhfoAccessory1DescriptionC() {
        return bhfoAccessory1DescriptionC;
    }

    public void setBhfoAccessory1DescriptionC(String bhfoAccessory1DescriptionC) {
        this.bhfoAccessory1DescriptionC = bhfoAccessory1DescriptionC == null ? null : bhfoAccessory1DescriptionC.trim();
    }

    public String getBhfoAccessory1TitleC() {
        return bhfoAccessory1TitleC;
    }

    public void setBhfoAccessory1TitleC(String bhfoAccessory1TitleC) {
        this.bhfoAccessory1TitleC = bhfoAccessory1TitleC == null ? null : bhfoAccessory1TitleC.trim();
    }

    public String getBhfoColorC() {
        return bhfoColorC;
    }

    public void setBhfoColorC(String bhfoColorC) {
        this.bhfoColorC = bhfoColorC == null ? null : bhfoColorC.trim();
    }

    public String getBhfoColorTextC() {
        return bhfoColorTextC;
    }

    public void setBhfoColorTextC(String bhfoColorTextC) {
        this.bhfoColorTextC = bhfoColorTextC == null ? null : bhfoColorTextC.trim();
    }

    public String getBhfoCountryOfOriginC() {
        return bhfoCountryOfOriginC;
    }

    public void setBhfoCountryOfOriginC(String bhfoCountryOfOriginC) {
        this.bhfoCountryOfOriginC = bhfoCountryOfOriginC == null ? null : bhfoCountryOfOriginC.trim();
    }

    public String getBhfoFlawDescriptionC() {
        return bhfoFlawDescriptionC;
    }

    public void setBhfoFlawDescriptionC(String bhfoFlawDescriptionC) {
        this.bhfoFlawDescriptionC = bhfoFlawDescriptionC == null ? null : bhfoFlawDescriptionC.trim();
    }

    public String getBhfoSizeC() {
        return bhfoSizeC;
    }

    public void setBhfoSizeC(String bhfoSizeC) {
        this.bhfoSizeC = bhfoSizeC == null ? null : bhfoSizeC.trim();
    }

    public String getBhfoSizeAdditionalC() {
        return bhfoSizeAdditionalC;
    }

    public void setBhfoSizeAdditionalC(String bhfoSizeAdditionalC) {
        this.bhfoSizeAdditionalC = bhfoSizeAdditionalC == null ? null : bhfoSizeAdditionalC.trim();
    }

    public String getBhfoSizeTypeC() {
        return bhfoSizeTypeC;
    }

    public void setBhfoSizeTypeC(String bhfoSizeTypeC) {
        this.bhfoSizeTypeC = bhfoSizeTypeC == null ? null : bhfoSizeTypeC.trim();
    }

    public String getBhfoStyleC() {
        return bhfoStyleC;
    }

    public void setBhfoStyleC(String bhfoStyleC) {
        this.bhfoStyleC = bhfoStyleC == null ? null : bhfoStyleC.trim();
    }

    public String getBhfoStyleNameC() {
        return bhfoStyleNameC;
    }

    public void setBhfoStyleNameC(String bhfoStyleNameC) {
        this.bhfoStyleNameC = bhfoStyleNameC == null ? null : bhfoStyleNameC.trim();
    }

    public String getBhfoStyleTypeC() {
        return bhfoStyleTypeC;
    }

    public void setBhfoStyleTypeC(String bhfoStyleTypeC) {
        this.bhfoStyleTypeC = bhfoStyleTypeC == null ? null : bhfoStyleTypeC.trim();
    }

    public String getBottleMaterial() {
        return bottleMaterial;
    }

    public void setBottleMaterial(String bottleMaterial) {
        this.bottleMaterial = bottleMaterial == null ? null : bottleMaterial.trim();
    }

    public String getBottleSizeOunces() {
        return bottleSizeOunces;
    }

    public void setBottleSizeOunces(String bottleSizeOunces) {
        this.bottleSizeOunces = bottleSizeOunces == null ? null : bottleSizeOunces.trim();
    }

    public String getBottomClosure() {
        return bottomClosure;
    }

    public void setBottomClosure(String bottomClosure) {
        this.bottomClosure = bottomClosure == null ? null : bottomClosure.trim();
    }

    public String getBottomRise() {
        return bottomRise;
    }

    public void setBottomRise(String bottomRise) {
        this.bottomRise = bottomRise == null ? null : bottomRise.trim();
    }

    public String getBraClosure() {
        return braClosure;
    }

    public void setBraClosure(String braClosure) {
        this.braClosure = braClosure == null ? null : braClosure.trim();
    }

    public String getBraFeatures() {
        return braFeatures;
    }

    public void setBraFeatures(String braFeatures) {
        this.braFeatures = braFeatures == null ? null : braFeatures.trim();
    }

    public String getBraStraps() {
        return braStraps;
    }

    public void setBraStraps(String braStraps) {
        this.braStraps = braStraps == null ? null : braStraps.trim();
    }

    public String getBrand2() {
        return brand2;
    }

    public void setBrand2(String brand2) {
        this.brand2 = brand2 == null ? null : brand2.trim();
    }

    public String getCaStoreCategory() {
        return caStoreCategory;
    }

    public void setCaStoreCategory(String caStoreCategory) {
        this.caStoreCategory = caStoreCategory == null ? null : caStoreCategory.trim();
    }

    public String getCandleSize() {
        return candleSize;
    }

    public void setCandleSize(String candleSize) {
        this.candleSize = candleSize == null ? null : candleSize.trim();
    }

    public String getCapacityOunces() {
        return capacityOunces;
    }

    public void setCapacityOunces(String capacityOunces) {
        this.capacityOunces = capacityOunces == null ? null : capacityOunces.trim();
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification == null ? null : classification.trim();
    }

    public String getClosure() {
        return closure;
    }

    public void setClosure(String closure) {
        this.closure = closure == null ? null : closure.trim();
    }

    public String getCollar() {
        return collar;
    }

    public void setCollar(String collar) {
        this.collar = collar == null ? null : collar.trim();
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection == null ? null : collection.trim();
    }

    public String getColor2() {
        return color2;
    }

    public void setColor2(String color2) {
        this.color2 = color2 == null ? null : color2.trim();
    }

    public String getCompartment() {
        return compartment;
    }

    public void setCompartment(String compartment) {
        this.compartment = compartment == null ? null : compartment.trim();
    }

    public String getCompatibleBrands() {
        return compatibleBrands;
    }

    public void setCompatibleBrands(String compatibleBrands) {
        this.compatibleBrands = compatibleBrands == null ? null : compatibleBrands.trim();
    }

    public String getCompatibleProducts() {
        return compatibleProducts;
    }

    public void setCompatibleProducts(String compatibleProducts) {
        this.compatibleProducts = compatibleProducts == null ? null : compatibleProducts.trim();
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition == null ? null : condition.trim();
    }

    public String getConnectivity() {
        return connectivity;
    }

    public void setConnectivity(String connectivity) {
        this.connectivity = connectivity == null ? null : connectivity.trim();
    }

    public String getCookingFunctions() {
        return cookingFunctions;
    }

    public void setCookingFunctions(String cookingFunctions) {
        this.cookingFunctions = cookingFunctions == null ? null : cookingFunctions.trim();
    }

    public String getCountryOfOrigin2() {
        return countryOfOrigin2;
    }

    public void setCountryOfOrigin2(String countryOfOrigin2) {
        this.countryOfOrigin2 = countryOfOrigin2 == null ? null : countryOfOrigin2.trim();
    }

    public String getCupSize() {
        return cupSize;
    }

    public void setCupSize(String cupSize) {
        this.cupSize = cupSize == null ? null : cupSize.trim();
    }

    public String getDenimWash() {
        return denimWash;
    }

    public void setDenimWash(String denimWash) {
        this.denimWash = denimWash == null ? null : denimWash.trim();
    }

    public String getDepthInches() {
        return depthInches;
    }

    public void setDepthInches(String depthInches) {
        this.depthInches = depthInches == null ? null : depthInches.trim();
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions == null ? null : dimensions.trim();
    }

    public String getDimensions2() {
        return dimensions2;
    }

    public void setDimensions2(String dimensions2) {
        this.dimensions2 = dimensions2 == null ? null : dimensions2.trim();
    }

    public String getDirtCapture() {
        return dirtCapture;
    }

    public void setDirtCapture(String dirtCapture) {
        this.dirtCapture = dirtCapture == null ? null : dirtCapture.trim();
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display == null ? null : display.trim();
    }

    public String getDressLength() {
        return dressLength;
    }

    public void setDressLength(String dressLength) {
        this.dressLength = dressLength == null ? null : dressLength.trim();
    }

    public String getDressType() {
        return dressType;
    }

    public void setDressType(String dressType) {
        this.dressType = dressType == null ? null : dressType.trim();
    }

    public String getDropLength() {
        return dropLength;
    }

    public void setDropLength(String dropLength) {
        this.dropLength = dropLength == null ? null : dropLength.trim();
    }

    public String getEan() {
        return ean;
    }

    public void setEan(String ean) {
        this.ean = ean == null ? null : ean.trim();
    }

    public String getEarpieceDesign() {
        return earpieceDesign;
    }

    public void setEarpieceDesign(String earpieceDesign) {
        this.earpieceDesign = earpieceDesign == null ? null : earpieceDesign.trim();
    }

    public String getEbayHeelHeight() {
        return ebayHeelHeight;
    }

    public void setEbayHeelHeight(String ebayHeelHeight) {
        this.ebayHeelHeight = ebayHeelHeight == null ? null : ebayHeelHeight.trim();
    }

    public String getFabricType() {
        return fabricType;
    }

    public void setFabricType(String fabricType) {
        this.fabricType = fabricType == null ? null : fabricType.trim();
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features == null ? null : features.trim();
    }

    public String getFeaturesAndFastening() {
        return featuresAndFastening;
    }

    public void setFeaturesAndFastening(String featuresAndFastening) {
        this.featuresAndFastening = featuresAndFastening == null ? null : featuresAndFastening.trim();
    }

    public String getFeatures2() {
        return features2;
    }

    public void setFeatures2(String features2) {
        this.features2 = features2 == null ? null : features2.trim();
    }

    public String getFinish() {
        return finish;
    }

    public void setFinish(String finish) {
        this.finish = finish == null ? null : finish.trim();
    }

    public String getFit() {
        return fit;
    }

    public void setFit(String fit) {
        this.fit = fit == null ? null : fit.trim();
    }

    public String getFitDesign() {
        return fitDesign;
    }

    public void setFitDesign(String fitDesign) {
        this.fitDesign = fitDesign == null ? null : fitDesign.trim();
    }

    public String getFlagstyle() {
        return flagstyle;
    }

    public void setFlagstyle(String flagstyle) {
        this.flagstyle = flagstyle == null ? null : flagstyle.trim();
    }

    public String getFormulation() {
        return formulation;
    }

    public void setFormulation(String formulation) {
        this.formulation = formulation == null ? null : formulation.trim();
    }

    public String getFrameMaterialType() {
        return frameMaterialType;
    }

    public void setFrameMaterialType(String frameMaterialType) {
        this.frameMaterialType = frameMaterialType == null ? null : frameMaterialType.trim();
    }

    public String getFrameMetal() {
        return frameMetal;
    }

    public void setFrameMetal(String frameMetal) {
        this.frameMetal = frameMetal == null ? null : frameMetal.trim();
    }

    public String getFrontStyle() {
        return frontStyle;
    }

    public void setFrontStyle(String frontStyle) {
        this.frontStyle = frontStyle == null ? null : frontStyle.trim();
    }

    public String getFuelSource() {
        return fuelSource;
    }

    public void setFuelSource(String fuelSource) {
        this.fuelSource = fuelSource == null ? null : fuelSource.trim();
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function == null ? null : function.trim();
    }

    public String getFurType() {
        return furType;
    }

    public void setFurType(String furType) {
        this.furType = furType == null ? null : furType.trim();
    }

    public String getGender2() {
        return gender2;
    }

    public void setGender2(String gender2) {
        this.gender2 = gender2 == null ? null : gender2.trim();
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre == null ? null : genre.trim();
    }

    public String getHandleType() {
        return handleType;
    }

    public void setHandleType(String handleType) {
        this.handleType = handleType == null ? null : handleType.trim();
    }

    public String getHarmonizedCode() {
        return harmonizedCode;
    }

    public void setHarmonizedCode(String harmonizedCode) {
        this.harmonizedCode = harmonizedCode == null ? null : harmonizedCode.trim();
    }

    public String getHeelHeightInches() {
        return heelHeightInches;
    }

    public void setHeelHeightInches(String heelHeightInches) {
        this.heelHeightInches = heelHeightInches == null ? null : heelHeightInches.trim();
    }

    public String getHeight2() {
        return height2;
    }

    public void setHeight2(String height2) {
        this.height2 = height2 == null ? null : height2.trim();
    }

    public String getImageUrl1() {
        return imageUrl1;
    }

    public void setImageUrl1(String imageUrl1) {
        this.imageUrl1 = imageUrl1 == null ? null : imageUrl1.trim();
    }

    public String getImageUrl2() {
        return imageUrl2;
    }

    public void setImageUrl2(String imageUrl2) {
        this.imageUrl2 = imageUrl2 == null ? null : imageUrl2.trim();
    }

    public String getImageUrl3() {
        return imageUrl3;
    }

    public void setImageUrl3(String imageUrl3) {
        this.imageUrl3 = imageUrl3 == null ? null : imageUrl3.trim();
    }

    public String getImageUrl4() {
        return imageUrl4;
    }

    public void setImageUrl4(String imageUrl4) {
        this.imageUrl4 = imageUrl4 == null ? null : imageUrl4.trim();
    }

    public String getIncludes() {
        return includes;
    }

    public void setIncludes(String includes) {
        this.includes = includes == null ? null : includes.trim();
    }

    public String getIncludes2() {
        return includes2;
    }

    public void setIncludes2(String includes2) {
        this.includes2 = includes2 == null ? null : includes2.trim();
    }

    public String getInseamInches() {
        return inseamInches;
    }

    public void setInseamInches(String inseamInches) {
        this.inseamInches = inseamInches == null ? null : inseamInches.trim();
    }

    public String getInsertMaterial() {
        return insertMaterial;
    }

    public void setInsertMaterial(String insertMaterial) {
        this.insertMaterial = insertMaterial == null ? null : insertMaterial.trim();
    }

    public String getIsAChildItem() {
        return isAChildItem;
    }

    public void setIsAChildItem(String isAChildItem) {
        this.isAChildItem = isAChildItem == null ? null : isAChildItem.trim();
    }

    public String getIsAParentItem() {
        return isAParentItem;
    }

    public void setIsAParentItem(String isAParentItem) {
        this.isAParentItem = isAParentItem == null ? null : isAParentItem.trim();
    }

    public String getIsblocked() {
        return isblocked;
    }

    public void setIsblocked(String isblocked) {
        this.isblocked = isblocked == null ? null : isblocked.trim();
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn == null ? null : isbn.trim();
    }

    public String getIsfba() {
        return isfba;
    }

    public void setIsfba(String isfba) {
        this.isfba = isfba == null ? null : isfba.trim();
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId == null ? null : itemId.trim();
    }

    public String getItemShape() {
        return itemShape;
    }

    public void setItemShape(String itemShape) {
        this.itemShape = itemShape == null ? null : itemShape.trim();
    }

    public String getItemSubtitle() {
        return itemSubtitle;
    }

    public void setItemSubtitle(String itemSubtitle) {
        this.itemSubtitle = itemSubtitle == null ? null : itemSubtitle.trim();
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle == null ? null : itemTitle.trim();
    }

    public String getItemauctiondescription() {
        return itemauctiondescription;
    }

    public void setItemauctiondescription(String itemauctiondescription) {
        this.itemauctiondescription = itemauctiondescription == null ? null : itemauctiondescription.trim();
    }

    public String getItemimageurl10() {
        return itemimageurl10;
    }

    public void setItemimageurl10(String itemimageurl10) {
        this.itemimageurl10 = itemimageurl10 == null ? null : itemimageurl10.trim();
    }

    public String getItemimageurl5() {
        return itemimageurl5;
    }

    public void setItemimageurl5(String itemimageurl5) {
        this.itemimageurl5 = itemimageurl5 == null ? null : itemimageurl5.trim();
    }

    public String getItemimageurl6() {
        return itemimageurl6;
    }

    public void setItemimageurl6(String itemimageurl6) {
        this.itemimageurl6 = itemimageurl6 == null ? null : itemimageurl6.trim();
    }

    public String getItemimageurl7() {
        return itemimageurl7;
    }

    public void setItemimageurl7(String itemimageurl7) {
        this.itemimageurl7 = itemimageurl7 == null ? null : itemimageurl7.trim();
    }

    public String getJacketLegth() {
        return jacketLegth;
    }

    public void setJacketLegth(String jacketLegth) {
        this.jacketLegth = jacketLegth == null ? null : jacketLegth.trim();
    }

    public String getJacketSleeveLengthInches() {
        return jacketSleeveLengthInches;
    }

    public void setJacketSleeveLengthInches(String jacketSleeveLengthInches) {
        this.jacketSleeveLengthInches = jacketSleeveLengthInches == null ? null : jacketSleeveLengthInches.trim();
    }

    public String getLength2() {
        return length2;
    }

    public void setLength2(String length2) {
        this.length2 = length2 == null ? null : length2.trim();
    }

    public String getLensColor() {
        return lensColor;
    }

    public void setLensColor(String lensColor) {
        this.lensColor = lensColor == null ? null : lensColor.trim();
    }

    public String getLensMaterialType() {
        return lensMaterialType;
    }

    public void setLensMaterialType(String lensMaterialType) {
        this.lensMaterialType = lensMaterialType == null ? null : lensMaterialType.trim();
    }

    public String getLensWidthMillimeters() {
        return lensWidthMillimeters;
    }

    public void setLensWidthMillimeters(String lensWidthMillimeters) {
        this.lensWidthMillimeters = lensWidthMillimeters == null ? null : lensWidthMillimeters.trim();
    }

    public String getLid() {
        return lid;
    }

    public void setLid(String lid) {
        this.lid = lid == null ? null : lid.trim();
    }

    public String getLightbulbType() {
        return lightbulbType;
    }

    public void setLightbulbType(String lightbulbType) {
        this.lightbulbType = lightbulbType == null ? null : lightbulbType.trim();
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer == null ? null : manufacturer.trim();
    }

    public String getManufacturerDescription() {
        return manufacturerDescription;
    }

    public void setManufacturerDescription(String manufacturerDescription) {
        this.manufacturerDescription = manufacturerDescription == null ? null : manufacturerDescription.trim();
    }

    public String getMaterial2() {
        return material2;
    }

    public void setMaterial2(String material2) {
        this.material2 = material2 == null ? null : material2.trim();
    }

    public String getMaterialSpecialty() {
        return materialSpecialty;
    }

    public void setMaterialSpecialty(String materialSpecialty) {
        this.materialSpecialty = materialSpecialty == null ? null : materialSpecialty.trim();
    }

    public String getMaxMagnification() {
        return maxMagnification;
    }

    public void setMaxMagnification(String maxMagnification) {
        this.maxMagnification = maxMagnification == null ? null : maxMagnification.trim();
    }

    public String getMeliCategory() {
        return meliCategory;
    }

    public void setMeliCategory(String meliCategory) {
        this.meliCategory = meliCategory == null ? null : meliCategory.trim();
    }

    public String getMeliColor() {
        return meliColor;
    }

    public void setMeliColor(String meliColor) {
        this.meliColor = meliColor == null ? null : meliColor.trim();
    }

    public String getMeliSeason() {
        return meliSeason;
    }

    public void setMeliSeason(String meliSeason) {
        this.meliSeason = meliSeason == null ? null : meliSeason.trim();
    }

    public String getMeliSize() {
        return meliSize;
    }

    public void setMeliSize(String meliSize) {
        this.meliSize = meliSize == null ? null : meliSize.trim();
    }

    public String getMeliTitle() {
        return meliTitle;
    }

    public void setMeliTitle(String meliTitle) {
        this.meliTitle = meliTitle == null ? null : meliTitle.trim();
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber == null ? null : modelNumber.trim();
    }

    public String getModelNumber2() {
        return modelNumber2;
    }

    public void setModelNumber2(String modelNumber2) {
        this.modelNumber2 = modelNumber2 == null ? null : modelNumber2.trim();
    }

    public String getMovement() {
        return movement;
    }

    public void setMovement(String movement) {
        this.movement = movement == null ? null : movement.trim();
    }

    public String getMpn2() {
        return mpn2;
    }

    public void setMpn2(String mpn2) {
        this.mpn2 = mpn2 == null ? null : mpn2.trim();
    }

    public String getNeckSize() {
        return neckSize;
    }

    public void setNeckSize(String neckSize) {
        this.neckSize = neckSize == null ? null : neckSize.trim();
    }

    public String getNeckline() {
        return neckline;
    }

    public void setNeckline(String neckline) {
        this.neckline = neckline == null ? null : neckline.trim();
    }

    public String getItemlevelshipping() {
        return itemlevelshipping;
    }

    public void setItemlevelshipping(String itemlevelshipping) {
        this.itemlevelshipping = itemlevelshipping == null ? null : itemlevelshipping.trim();
    }

    public String getNonStick() {
        return nonStick;
    }

    public void setNonStick(String nonStick) {
        this.nonStick = nonStick == null ? null : nonStick.trim();
    }

    public String getNumberOfPieces() {
        return numberOfPieces;
    }

    public void setNumberOfPieces(String numberOfPieces) {
        this.numberOfPieces = numberOfPieces == null ? null : numberOfPieces.trim();
    }

    public String getNumberOfSlices() {
        return numberOfSlices;
    }

    public void setNumberOfSlices(String numberOfSlices) {
        this.numberOfSlices = numberOfSlices == null ? null : numberOfSlices.trim();
    }

    public String getOccasion() {
        return occasion;
    }

    public void setOccasion(String occasion) {
        this.occasion = occasion == null ? null : occasion.trim();
    }

    public String getOpenQuantity() {
        return openQuantity;
    }

    public void setOpenQuantity(String openQuantity) {
        this.openQuantity = openQuantity == null ? null : openQuantity.trim();
    }

    public String getOpenquantitypooled() {
        return openquantitypooled;
    }

    public void setOpenquantitypooled(String openquantitypooled) {
        this.openquantitypooled = openquantitypooled == null ? null : openquantitypooled.trim();
    }

    public String getPadding() {
        return padding;
    }

    public void setPadding(String padding) {
        this.padding = padding == null ? null : padding.trim();
    }

    public String getPantType() {
        return pantType;
    }

    public void setPantType(String pantType) {
        this.pantType = pantType == null ? null : pantType.trim();
    }

    public String getPantyStyle() {
        return pantyStyle;
    }

    public void setPantyStyle(String pantyStyle) {
        this.pantyStyle = pantyStyle == null ? null : pantyStyle.trim();
    }

    public String getParentSku() {
        return parentSku;
    }

    public void setParentSku(String parentSku) {
        this.parentSku = parentSku == null ? null : parentSku.trim();
    }

    public String getPattern2() {
        return pattern2;
    }

    public void setPattern2(String pattern2) {
        this.pattern2 = pattern2 == null ? null : pattern2.trim();
    }

    public String getPhotoSize() {
        return photoSize;
    }

    public void setPhotoSize(String photoSize) {
        this.photoSize = photoSize == null ? null : photoSize.trim();
    }

    public String getPillowSize() {
        return pillowSize;
    }

    public void setPillowSize(String pillowSize) {
        this.pillowSize = pillowSize == null ? null : pillowSize.trim();
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform == null ? null : platform.trim();
    }

    public String getPlatformHeightInches() {
        return platformHeightInches;
    }

    public void setPlatformHeightInches(String platformHeightInches) {
        this.platformHeightInches = platformHeightInches == null ? null : platformHeightInches.trim();
    }

    public String getPocketStyle() {
        return pocketStyle;
    }

    public void setPocketStyle(String pocketStyle) {
        this.pocketStyle = pocketStyle == null ? null : pocketStyle.trim();
    }

    public String getPolarizationType() {
        return polarizationType;
    }

    public void setPolarizationType(String polarizationType) {
        this.polarizationType = polarizationType == null ? null : polarizationType.trim();
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power == null ? null : power.trim();
    }

    public String getProductMargin() {
        return productMargin;
    }

    public void setProductMargin(String productMargin) {
        this.productMargin = productMargin == null ? null : productMargin.trim();
    }

    public String getProtection() {
        return protection;
    }

    public void setProtection(String protection) {
        this.protection = protection == null ? null : protection.trim();
    }

    public String getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(String quantityAvailable) {
        this.quantityAvailable = quantityAvailable == null ? null : quantityAvailable.trim();
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating == null ? null : rating.trim();
    }

    public String getReceivedInInventory() {
        return receivedInInventory;
    }

    public void setReceivedInInventory(String receivedInInventory) {
        this.receivedInInventory = receivedInInventory == null ? null : receivedInInventory.trim();
    }

    public String getRecommendedAge() {
        return recommendedAge;
    }

    public void setRecommendedAge(String recommendedAge) {
        this.recommendedAge = recommendedAge == null ? null : recommendedAge.trim();
    }

    public String getRecommendedAgeRange() {
        return recommendedAgeRange;
    }

    public void setRecommendedAgeRange(String recommendedAgeRange) {
        this.recommendedAgeRange = recommendedAgeRange == null ? null : recommendedAgeRange.trim();
    }

    public String getRelationshipName() {
        return relationshipName;
    }

    public void setRelationshipName(String relationshipName) {
        this.relationshipName = relationshipName == null ? null : relationshipName.trim();
    }

    public String getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(String retailPrice) {
        this.retailPrice = retailPrice == null ? null : retailPrice.trim();
    }

    public String getRiseInches() {
        return riseInches;
    }

    public void setRiseInches(String riseInches) {
        this.riseInches = riseInches == null ? null : riseInches.trim();
    }

    public String getRopDateFirstComplete() {
        return ropDateFirstComplete;
    }

    public void setRopDateFirstComplete(String ropDateFirstComplete) {
        this.ropDateFirstComplete = ropDateFirstComplete == null ? null : ropDateFirstComplete.trim();
    }

    public String getRugStyle() {
        return rugStyle;
    }

    public void setRugStyle(String rugStyle) {
        this.rugStyle = rugStyle == null ? null : rugStyle.trim();
    }

    public String getRykaConsignment() {
        return rykaConsignment;
    }

    public void setRykaConsignment(String rykaConsignment) {
        this.rykaConsignment = rykaConsignment == null ? null : rykaConsignment.trim();
    }

    public String getScentType() {
        return scentType;
    }

    public void setScentType(String scentType) {
        this.scentType = scentType == null ? null : scentType.trim();
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season == null ? null : season.trim();
    }

    public String getSellerLogoUrl() {
        return sellerLogoUrl;
    }

    public void setSellerLogoUrl(String sellerLogoUrl) {
        this.sellerLogoUrl = sellerLogoUrl == null ? null : sellerLogoUrl.trim();
    }

    public String getSet() {
        return set;
    }

    public void setSet(String set) {
        this.set = set == null ? null : set.trim();
    }

    public String getShaftHeightInches() {
        return shaftHeightInches;
    }

    public void setShaftHeightInches(String shaftHeightInches) {
        this.shaftHeightInches = shaftHeightInches == null ? null : shaftHeightInches.trim();
    }

    public String getShaftWidthInches() {
        return shaftWidthInches;
    }

    public void setShaftWidthInches(String shaftWidthInches) {
        this.shaftWidthInches = shaftWidthInches == null ? null : shaftWidthInches.trim();
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape == null ? null : shape.trim();
    }

    public String getShoeClosure() {
        return shoeClosure;
    }

    public void setShoeClosure(String shoeClosure) {
        this.shoeClosure = shoeClosure == null ? null : shoeClosure.trim();
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription == null ? null : shortDescription.trim();
    }

    public String getSilhouette() {
        return silhouette;
    }

    public void setSilhouette(String silhouette) {
        this.silhouette = silhouette == null ? null : silhouette.trim();
    }

    public String getSizeOrigin() {
        return sizeOrigin;
    }

    public void setSizeOrigin(String sizeOrigin) {
        this.sizeOrigin = sizeOrigin == null ? null : sizeOrigin.trim();
    }

    public String getSkirtLength() {
        return skirtLength;
    }

    public void setSkirtLength(String skirtLength) {
        this.skirtLength = skirtLength == null ? null : skirtLength.trim();
    }

    public String getSkirtType() {
        return skirtType;
    }

    public void setSkirtType(String skirtType) {
        this.skirtType = skirtType == null ? null : skirtType.trim();
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku == null ? null : sku.trim();
    }

    public String getSleeveLength() {
        return sleeveLength;
    }

    public void setSleeveLength(String sleeveLength) {
        this.sleeveLength = sleeveLength == null ? null : sleeveLength.trim();
    }

    public String getSleeveLengthInches() {
        return sleeveLengthInches;
    }

    public void setSleeveLengthInches(String sleeveLengthInches) {
        this.sleeveLengthInches = sleeveLengthInches == null ? null : sleeveLengthInches.trim();
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty == null ? null : specialty.trim();
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport == null ? null : sport.trim();
    }

    public String getStKeyword() {
        return stKeyword;
    }

    public void setStKeyword(String stKeyword) {
        this.stKeyword = stKeyword == null ? null : stKeyword.trim();
    }

    public String getStone() {
        return stone;
    }

    public void setStone(String stone) {
        this.stone = stone == null ? null : stone.trim();
    }

    public String getStoreTitle() {
        return storeTitle;
    }

    public void setStoreTitle(String storeTitle) {
        this.storeTitle = storeTitle == null ? null : storeTitle.trim();
    }

    public String getStrapDropInches() {
        return strapDropInches;
    }

    public void setStrapDropInches(String strapDropInches) {
        this.strapDropInches = strapDropInches == null ? null : strapDropInches.trim();
    }

    public String getStraps() {
        return straps;
    }

    public void setStraps(String straps) {
        this.straps = straps == null ? null : straps.trim();
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style == null ? null : style.trim();
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier == null ? null : supplier.trim();
    }

    public String getSupplierPo() {
        return supplierPo;
    }

    public void setSupplierPo(String supplierPo) {
        this.supplierPo = supplierPo == null ? null : supplierPo.trim();
    }

    public String getSuppliercode() {
        return suppliercode;
    }

    public void setSuppliercode(String suppliercode) {
        this.suppliercode = suppliercode == null ? null : suppliercode.trim();
    }

    public String getSupplierid() {
        return supplierid;
    }

    public void setSupplierid(String supplierid) {
        this.supplierid = supplierid == null ? null : supplierid.trim();
    }

    public String getSwimwearBottom() {
        return swimwearBottom;
    }

    public void setSwimwearBottom(String swimwearBottom) {
        this.swimwearBottom = swimwearBottom == null ? null : swimwearBottom.trim();
    }

    public String getSwimwearTop() {
        return swimwearTop;
    }

    public void setSwimwearTop(String swimwearTop) {
        this.swimwearTop = swimwearTop == null ? null : swimwearTop.trim();
    }

    public String getTaxproductcode() {
        return taxproductcode;
    }

    public void setTaxproductcode(String taxproductcode) {
        this.taxproductcode = taxproductcode == null ? null : taxproductcode.trim();
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme == null ? null : theme.trim();
    }

    public String getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(String threadCount) {
        this.threadCount = threadCount == null ? null : threadCount.trim();
    }

    public String getTieLength() {
        return tieLength;
    }

    public void setTieLength(String tieLength) {
        this.tieLength = tieLength == null ? null : tieLength.trim();
    }

    public String getTieWidth() {
        return tieWidth;
    }

    public void setTieWidth(String tieWidth) {
        this.tieWidth = tieWidth == null ? null : tieWidth.trim();
    }

    public String getTotalDressLengthInches() {
        return totalDressLengthInches;
    }

    public void setTotalDressLengthInches(String totalDressLengthInches) {
        this.totalDressLengthInches = totalDressLengthInches == null ? null : totalDressLengthInches.trim();
    }

    public String getTotalJacketLengthInches() {
        return totalJacketLengthInches;
    }

    public void setTotalJacketLengthInches(String totalJacketLengthInches) {
        this.totalJacketLengthInches = totalJacketLengthInches == null ? null : totalJacketLengthInches.trim();
    }

    public String getTotalLengthInches() {
        return totalLengthInches;
    }

    public void setTotalLengthInches(String totalLengthInches) {
        this.totalLengthInches = totalLengthInches == null ? null : totalLengthInches.trim();
    }

    public String getTotalSkirtLengthInches() {
        return totalSkirtLengthInches;
    }

    public void setTotalSkirtLengthInches(String totalSkirtLengthInches) {
        this.totalSkirtLengthInches = totalSkirtLengthInches == null ? null : totalSkirtLengthInches.trim();
    }

    public String getTotalquantitypooled() {
        return totalquantitypooled;
    }

    public void setTotalquantitypooled(String totalquantitypooled) {
        this.totalquantitypooled = totalquantitypooled == null ? null : totalquantitypooled.trim();
    }

    public String getUmbrellaOpener() {
        return umbrellaOpener;
    }

    public void setUmbrellaOpener(String umbrellaOpener) {
        this.umbrellaOpener = umbrellaOpener == null ? null : umbrellaOpener.trim();
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc == null ? null : upc.trim();
    }

    public String getUploadhaslastmodifiedgmt() {
        return uploadhaslastmodifiedgmt;
    }

    public void setUploadhaslastmodifiedgmt(String uploadhaslastmodifiedgmt) {
        this.uploadhaslastmodifiedgmt = uploadhaslastmodifiedgmt == null ? null : uploadhaslastmodifiedgmt.trim();
    }

    public String getVents() {
        return vents;
    }

    public void setVents(String vents) {
        this.vents = vents == null ? null : vents.trim();
    }

    public String getVideoInputs() {
        return videoInputs;
    }

    public void setVideoInputs(String videoInputs) {
        this.videoInputs = videoInputs == null ? null : videoInputs.trim();
    }

    public String getWaistAcrossInches() {
        return waistAcrossInches;
    }

    public void setWaistAcrossInches(String waistAcrossInches) {
        this.waistAcrossInches = waistAcrossInches == null ? null : waistAcrossInches.trim();
    }

    public String getWalletHeightInches() {
        return walletHeightInches;
    }

    public void setWalletHeightInches(String walletHeightInches) {
        this.walletHeightInches = walletHeightInches == null ? null : walletHeightInches.trim();
    }

    public String getWarehouselocation() {
        return warehouselocation;
    }

    public void setWarehouselocation(String warehouselocation) {
        this.warehouselocation = warehouselocation == null ? null : warehouselocation.trim();
    }

    public String getWarranty() {
        return warranty;
    }

    public void setWarranty(String warranty) {
        this.warranty = warranty == null ? null : warranty.trim();
    }

    public String getWeight2() {
        return weight2;
    }

    public void setWeight2(String weight2) {
        this.weight2 = weight2 == null ? null : weight2.trim();
    }

    public String getWidth2() {
        return width2;
    }

    public void setWidth2(String width2) {
        this.width2 = width2 == null ? null : width2.trim();
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year == null ? null : year.trim();
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room == null ? null : room.trim();
    }

    public String getLatestSupplier() {
        return latestSupplier;
    }

    public void setLatestSupplier(String latestSupplier) {
        this.latestSupplier = latestSupplier == null ? null : latestSupplier.trim();
    }

    public String getBandSize() {
        return bandSize;
    }

    public void setBandSize(String bandSize) {
        this.bandSize = bandSize == null ? null : bandSize.trim();
    }

    public String getWalletWidthInches() {
        return walletWidthInches;
    }

    public void setWalletWidthInches(String walletWidthInches) {
        this.walletWidthInches = walletWidthInches == null ? null : walletWidthInches.trim();
    }
    public String getInventoryNumber() {
        return inventoryNumber;
    }

    public void setInventoryNumber(String inventoryNumber) {
        this.inventoryNumber = inventoryNumber == null ? null : inventoryNumber.trim();
    }

    public String getMsrp() {
        return msrp;
    }

    public void setMsrp(String msrp) {
        this.msrp = msrp == null ? null : msrp.trim();
    }

    public String getVoyageonePrice() {
        return voyageonePrice;
    }

    public void setVoyageonePrice(String voyageonePrice) {
        this.voyageonePrice = voyageonePrice == null ? null : voyageonePrice.trim();
    }

    public String getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(String shippingCost) {
        this.shippingCost = shippingCost == null ? null : shippingCost.trim();
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight == null ? null : weight.trim();
    }

    public String getMd5() {
        Set<String> noMd5Fields = new HashSet<>();
        return  beanToMd5(this,noMd5Fields);
    }

    public void setMd5(String md5) {
        this.md5 = md5 == null ? null : md5.trim();
    }
}