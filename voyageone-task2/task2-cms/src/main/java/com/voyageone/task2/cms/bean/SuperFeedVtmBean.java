package com.voyageone.task2.cms.bean;

import com.voyageone.common.util.MD5;


/**
 * @author morse.lu
 * @version 0.0.1, 16/3/4
 */
public class SuperFeedVtmBean {
    private String SKU;
    private String UPC;
    private String EAN;
    private String MPN;
    private String Description;
    private String Manufacturer;
    private String Brand1;
    private String ShortTitle;
    private String ProductMargin;
    private String BuyItNowPrice;
    private String RetailPrice;
    private String RelationshipName;
    private String VariationParentSKU;
    private String Classification;
    private String AlternativeGroups;
    private String AmazonUSASIN;
    private String BaseSize;
    private String Brand2;
    private String CasinFree;
    private String CNMSRP;
    private String CNPrice;
    private String COLOUR;
    private String CONDITION;
    private String CountryofOrigin;
    private String CountryISOCode;
    private String DairyFree;
    private String DosageSize;
    private String DosageUnits;
    private String Dropship;
    private String EcoFriendly;
    private String EggFree;
    private String FullTitle;
    private String GenericColor;
    private String GenericFlavor;
    private String GlutenFree;
    private String HTMLMarketingDescription;
    private String HypoAllergenic;
    private String ImageList;
    private String IngredientsText;
    private String IsItemMAP;
    private String ItemCategBuyerGroup;
    private String ItemName;
    private String Keywords;
    private String Kosher;
    private String LactoseFree;
    private String LowCarb;
    private String LowestAvailablePrice;
    private String MAPPrice;
    private String MarketingDescription;
    private String MerchantPrimaryCategory;
    private String MSRP;
    private String NoAnimalTesting;
    private String NormalSellingPrice;
    private String NutFree;
    private String Organic;
    private String ParabenFree;
    private String PotencySize;
    private String PotencyUnits;
    private String PrimaryImage;
    private String ProductID;
    private String ProductMarginPercentage;
    private String ProductRating;
    private String ProductURL;
    private String Restricted;
    private String SalePrice;
    private String Scent;
    private String SecondaryCategories;
    private String SecondaryImages;
    private String Servings;
    private String ShippingSurcharge;
    private String Size;
    private String SizeUnits;
    private String SoyFree;
    private String SpecificColor;
    private String SpecificFlavor;
    private String SugarFree;
    private String SuggestedUse;
    private String Taxable;
    private String VariantID;
    private String Vegetarian;
    private String Warnings;
    private String WheatFree;
    private String Height;
    private String Length;
    private String Width;
    private String SellerCost;
    private String Weight;
    private String IsParent;
    private String VoyageOnePrice;
    private String Quantity;
    private String VoyageOneMSRP;
    private String md5;

    public String getSKU() {
        return SKU;
    }

    public void setSKU(String SKU) {
        this.SKU = SKU;
    }

    public String getUPC() {
        return UPC;
    }

    public void setUPC(String UPC) {
        this.UPC = UPC;
    }

    public String getEAN() {
        return EAN;
    }

    public void setEAN(String EAN) {
        this.EAN = EAN;
    }

    public String getMPN() {
        return MPN;
    }

    public void setMPN(String MPN) {
        this.MPN = MPN;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getManufacturer() {
        return Manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        Manufacturer = manufacturer;
    }

    public String getBrand1() {
        return Brand1;
    }

    public void setBrand1(String brand1) {
        Brand1 = brand1;
    }

    public String getShortTitle() {
        return ShortTitle;
    }

    public void setShortTitle(String shortTitle) {
        ShortTitle = shortTitle;
    }

    public String getProductMargin() {
        return ProductMargin;
    }

    public void setProductMargin(String productMargin) {
        ProductMargin = productMargin;
    }

    public String getBuyItNowPrice() {
        return BuyItNowPrice;
    }

    public void setBuyItNowPrice(String buyItNowPrice) {
        BuyItNowPrice = buyItNowPrice;
    }

    public String getRetailPrice() {
        return RetailPrice;
    }

    public void setRetailPrice(String retailPrice) {
        RetailPrice = retailPrice;
    }

    public String getRelationshipName() {
        return RelationshipName;
    }

    public void setRelationshipName(String relationshipName) {
        RelationshipName = relationshipName;
    }

    public String getVariationParentSKU() {
        return VariationParentSKU;
    }

    public void setVariationParentSKU(String variationParentSKU) {
        VariationParentSKU = variationParentSKU;
    }

    public String getClassification() {
        return Classification;
    }

    public void setClassification(String classification) {
        Classification = classification;
    }

    public String getAlternativeGroups() {
        return AlternativeGroups;
    }

    public void setAlternativeGroups(String alternativeGroups) {
        AlternativeGroups = alternativeGroups;
    }

    public String getAmazonUSASIN() {
        return AmazonUSASIN;
    }

    public void setAmazonUSASIN(String amazonUSASIN) {
        AmazonUSASIN = amazonUSASIN;
    }

    public String getBaseSize() {
        return BaseSize;
    }

    public void setBaseSize(String baseSize) {
        BaseSize = baseSize;
    }

    public String getBrand2() {
        return Brand2;
    }

    public void setBrand2(String brand2) {
        Brand2 = brand2;
    }

    public String getCasinFree() {
        return CasinFree;
    }

    public void setCasinFree(String casinFree) {
        CasinFree = casinFree;
    }

    public String getCNMSRP() {
        return CNMSRP;
    }

    public void setCNMSRP(String CNMSRP) {
        this.CNMSRP = CNMSRP;
    }

    public String getCNPrice() {
        return CNPrice;
    }

    public void setCNPrice(String CNPrice) {
        this.CNPrice = CNPrice;
    }

    public String getCOLOUR() {
        return COLOUR;
    }

    public void setCOLOUR(String COLOUR) {
        this.COLOUR = COLOUR;
    }

    public String getCONDITION() {
        return CONDITION;
    }

    public void setCONDITION(String CONDITION) {
        this.CONDITION = CONDITION;
    }

    public String getCountryofOrigin() {
        return CountryofOrigin;
    }

    public void setCountryofOrigin(String countryofOrigin) {
        CountryofOrigin = countryofOrigin;
    }

    public String getCountryISOCode() {
        return CountryISOCode;
    }

    public void setCountryISOCode(String countryISOCode) {
        CountryISOCode = countryISOCode;
    }

    public String getDairyFree() {
        return DairyFree;
    }

    public void setDairyFree(String dairyFree) {
        DairyFree = dairyFree;
    }

    public String getDosageSize() {
        return DosageSize;
    }

    public void setDosageSize(String dosageSize) {
        DosageSize = dosageSize;
    }

    public String getDosageUnits() {
        return DosageUnits;
    }

    public void setDosageUnits(String dosageUnits) {
        DosageUnits = dosageUnits;
    }

    public String getDropship() {
        return Dropship;
    }

    public void setDropship(String dropship) {
        Dropship = dropship;
    }

    public String getEcoFriendly() {
        return EcoFriendly;
    }

    public void setEcoFriendly(String ecoFriendly) {
        EcoFriendly = ecoFriendly;
    }

    public String getEggFree() {
        return EggFree;
    }

    public void setEggFree(String eggFree) {
        EggFree = eggFree;
    }

    public String getFullTitle() {
        return FullTitle;
    }

    public void setFullTitle(String fullTitle) {
        FullTitle = fullTitle;
    }

    public String getGenericColor() {
        return GenericColor;
    }

    public void setGenericColor(String genericColor) {
        GenericColor = genericColor;
    }

    public String getGenericFlavor() {
        return GenericFlavor;
    }

    public void setGenericFlavor(String genericFlavor) {
        GenericFlavor = genericFlavor;
    }

    public String getGlutenFree() {
        return GlutenFree;
    }

    public void setGlutenFree(String glutenFree) {
        GlutenFree = glutenFree;
    }

    public String getHTMLMarketingDescription() {
        return HTMLMarketingDescription;
    }

    public void setHTMLMarketingDescription(String HTMLMarketingDescription) {
        this.HTMLMarketingDescription = HTMLMarketingDescription;
    }

    public String getHypoAllergenic() {
        return HypoAllergenic;
    }

    public void setHypoAllergenic(String hypoAllergenic) {
        HypoAllergenic = hypoAllergenic;
    }

    public String getImageList() {
        return ImageList;
    }

    public void setImageList(String imageList) {
        ImageList = imageList;
    }

    public String getIngredientsText() {
        return IngredientsText;
    }

    public void setIngredientsText(String ingredientsText) {
        IngredientsText = ingredientsText;
    }

    public String getIsItemMAP() {
        return IsItemMAP;
    }

    public void setIsItemMAP(String isItemMAP) {
        IsItemMAP = isItemMAP;
    }

    public String getItemCategBuyerGroup() {
        return ItemCategBuyerGroup;
    }

    public void setItemCategBuyerGroup(String itemCategBuyerGroup) {
        ItemCategBuyerGroup = itemCategBuyerGroup;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getKeywords() {
        return Keywords;
    }

    public void setKeywords(String keywords) {
        Keywords = keywords;
    }

    public String getKosher() {
        return Kosher;
    }

    public void setKosher(String kosher) {
        Kosher = kosher;
    }

    public String getLactoseFree() {
        return LactoseFree;
    }

    public void setLactoseFree(String lactoseFree) {
        LactoseFree = lactoseFree;
    }

    public String getLowCarb() {
        return LowCarb;
    }

    public void setLowCarb(String lowCarb) {
        LowCarb = lowCarb;
    }

    public String getLowestAvailablePrice() {
        return LowestAvailablePrice;
    }

    public void setLowestAvailablePrice(String lowestAvailablePrice) {
        LowestAvailablePrice = lowestAvailablePrice;
    }

    public String getMAPPrice() {
        return MAPPrice;
    }

    public void setMAPPrice(String MAPPrice) {
        this.MAPPrice = MAPPrice;
    }

    public String getMarketingDescription() {
        return MarketingDescription;
    }

    public void setMarketingDescription(String marketingDescription) {
        MarketingDescription = marketingDescription;
    }

    public String getMerchantPrimaryCategory() {
        return MerchantPrimaryCategory;
    }

    public void setMerchantPrimaryCategory(String merchantPrimaryCategory) {
        MerchantPrimaryCategory = merchantPrimaryCategory;
    }

    public String getMSRP() {
        return MSRP;
    }

    public void setMSRP(String MSRP) {
        this.MSRP = MSRP;
    }

    public String getNoAnimalTesting() {
        return NoAnimalTesting;
    }

    public void setNoAnimalTesting(String noAnimalTesting) {
        NoAnimalTesting = noAnimalTesting;
    }

    public String getNormalSellingPrice() {
        return NormalSellingPrice;
    }

    public void setNormalSellingPrice(String normalSellingPrice) {
        NormalSellingPrice = normalSellingPrice;
    }

    public String getNutFree() {
        return NutFree;
    }

    public void setNutFree(String nutFree) {
        NutFree = nutFree;
    }

    public String getOrganic() {
        return Organic;
    }

    public void setOrganic(String organic) {
        Organic = organic;
    }

    public String getParabenFree() {
        return ParabenFree;
    }

    public void setParabenFree(String parabenFree) {
        ParabenFree = parabenFree;
    }

    public String getPotencySize() {
        return PotencySize;
    }

    public void setPotencySize(String potencySize) {
        PotencySize = potencySize;
    }

    public String getPotencyUnits() {
        return PotencyUnits;
    }

    public void setPotencyUnits(String potencyUnits) {
        PotencyUnits = potencyUnits;
    }

    public String getPrimaryImage() {
        return PrimaryImage;
    }

    public void setPrimaryImage(String primaryImage) {
        PrimaryImage = primaryImage;
    }

    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String productID) {
        ProductID = productID;
    }

    public String getProductMarginPercentage() {
        return ProductMarginPercentage;
    }

    public void setProductMarginPercentage(String productMarginPercentage) {
        ProductMarginPercentage = productMarginPercentage;
    }

    public String getProductRating() {
        return ProductRating;
    }

    public void setProductRating(String productRating) {
        ProductRating = productRating;
    }

    public String getProductURL() {
        return ProductURL;
    }

    public void setProductURL(String productURL) {
        ProductURL = productURL;
    }

    public String getRestricted() {
        return Restricted;
    }

    public void setRestricted(String restricted) {
        Restricted = restricted;
    }

    public String getSalePrice() {
        return SalePrice;
    }

    public void setSalePrice(String salePrice) {
        SalePrice = salePrice;
    }

    public String getScent() {
        return Scent;
    }

    public void setScent(String scent) {
        Scent = scent;
    }

    public String getSecondaryCategories() {
        return SecondaryCategories;
    }

    public void setSecondaryCategories(String secondaryCategories) {
        SecondaryCategories = secondaryCategories;
    }

    public String getSecondaryImages() {
        return SecondaryImages;
    }

    public void setSecondaryImages(String secondaryImages) {
        SecondaryImages = secondaryImages;
    }

    public String getServings() {
        return Servings;
    }

    public void setServings(String servings) {
        Servings = servings;
    }

    public String getShippingSurcharge() {
        return ShippingSurcharge;
    }

    public void setShippingSurcharge(String shippingSurcharge) {
        ShippingSurcharge = shippingSurcharge;
    }

    public String getSize() {
        return Size;
    }

    public void setSize(String size) {
        Size = size;
    }

    public String getSizeUnits() {
        return SizeUnits;
    }

    public void setSizeUnits(String sizeUnits) {
        SizeUnits = sizeUnits;
    }

    public String getSoyFree() {
        return SoyFree;
    }

    public void setSoyFree(String soyFree) {
        SoyFree = soyFree;
    }

    public String getSpecificColor() {
        return SpecificColor;
    }

    public void setSpecificColor(String specificColor) {
        SpecificColor = specificColor;
    }

    public String getSpecificFlavor() {
        return SpecificFlavor;
    }

    public void setSpecificFlavor(String specificFlavor) {
        SpecificFlavor = specificFlavor;
    }

    public String getSugarFree() {
        return SugarFree;
    }

    public void setSugarFree(String sugarFree) {
        SugarFree = sugarFree;
    }

    public String getSuggestedUse() {
        return SuggestedUse;
    }

    public void setSuggestedUse(String suggestedUse) {
        SuggestedUse = suggestedUse;
    }

    public String getTaxable() {
        return Taxable;
    }

    public void setTaxable(String taxable) {
        Taxable = taxable;
    }

    public String getVariantID() {
        return VariantID;
    }

    public void setVariantID(String variantID) {
        VariantID = variantID;
    }

    public String getVegetarian() {
        return Vegetarian;
    }

    public void setVegetarian(String vegetarian) {
        Vegetarian = vegetarian;
    }

    public String getWarnings() {
        return Warnings;
    }

    public void setWarnings(String warnings) {
        Warnings = warnings;
    }

    public String getWheatFree() {
        return WheatFree;
    }

    public void setWheatFree(String wheatFree) {
        WheatFree = wheatFree;
    }

    public String getHeight() {
        return Height;
    }

    public void setHeight(String height) {
        Height = height;
    }

    public String getLength() {
        return Length;
    }

    public void setLength(String length) {
        Length = length;
    }

    public String getWidth() {
        return Width;
    }

    public void setWidth(String width) {
        Width = width;
    }

    public String getSellerCost() {
        return SellerCost;
    }

    public void setSellerCost(String sellerCost) {
        SellerCost = sellerCost;
    }

    public String getWeight() {
        return Weight;
    }

    public void setWeight(String weight) {
        Weight = weight;
    }

    public String getIsParent() {
        return IsParent;
    }

    public void setIsParent(String isParent) {
        IsParent = isParent;
    }

    public String getVoyageOnePrice() {
        return VoyageOnePrice;
    }

    public void setVoyageOnePrice(String voyageOnePrice) {
        VoyageOnePrice = voyageOnePrice;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getVoyageOneMSRP() {
        return VoyageOneMSRP;
    }

    public void setVoyageOneMSRP(String voyageOneMSRP) {
        VoyageOneMSRP = voyageOneMSRP;
    }

    public String getMd5() {
        StringBuffer temp = new StringBuffer();
        temp.append(this.SKU);
        temp.append(this.UPC);
        temp.append(this.EAN);
        temp.append(this.MPN);
        temp.append(this.Description);
        temp.append(this.Manufacturer);
        temp.append(this.Brand1);
        temp.append(this.ShortTitle);
        temp.append(this.ProductMargin);
        temp.append(this.BuyItNowPrice);
        temp.append(this.RetailPrice);
        temp.append(this.RelationshipName);
        temp.append(this.VariationParentSKU);
        temp.append(this.Classification);
        temp.append(this.AlternativeGroups);
        temp.append(this.AmazonUSASIN);
        temp.append(this.BaseSize);
        temp.append(this.Brand2);
        temp.append(this.CasinFree);
        temp.append(this.CNMSRP);
        temp.append(this.CNPrice);
        temp.append(this.COLOUR);
        temp.append(this.CONDITION);
        temp.append(this.CountryofOrigin);
        temp.append(this.CountryISOCode);
        temp.append(this.DairyFree);
        temp.append(this.DosageSize);
        temp.append(this.DosageUnits);
        temp.append(this.Dropship);
        temp.append(this.EcoFriendly);
        temp.append(this.EggFree);
        temp.append(this.FullTitle);
        temp.append(this.GenericColor);
        temp.append(this.GenericFlavor);
        temp.append(this.GlutenFree);
        temp.append(this.HTMLMarketingDescription);
        temp.append(this.HypoAllergenic);
        temp.append(this.ImageList);
        temp.append(this.IngredientsText);
        temp.append(this.IsItemMAP);
        temp.append(this.ItemCategBuyerGroup);
        temp.append(this.ItemName);
        temp.append(this.Keywords);
        temp.append(this.Kosher);
        temp.append(this.LactoseFree);
        temp.append(this.LowCarb);
        temp.append(this.LowestAvailablePrice);
        temp.append(this.MAPPrice);
        temp.append(this.MarketingDescription);
        temp.append(this.MerchantPrimaryCategory);
        temp.append(this.MSRP);
        temp.append(this.NoAnimalTesting);
        temp.append(this.NormalSellingPrice);
        temp.append(this.NutFree);
        temp.append(this.Organic);
        temp.append(this.ParabenFree);
        temp.append(this.PotencySize);
        temp.append(this.PotencyUnits);
        temp.append(this.PrimaryImage);
        temp.append(this.ProductID);
        temp.append(this.ProductMarginPercentage);
        temp.append(this.ProductRating);
        temp.append(this.ProductURL);
        temp.append(this.Restricted);
        temp.append(this.SalePrice);
        temp.append(this.Scent);
        temp.append(this.SecondaryCategories);
        temp.append(this.SecondaryImages);
        temp.append(this.Servings);
        temp.append(this.ShippingSurcharge);
        temp.append(this.Size);
        temp.append(this.SizeUnits);
        temp.append(this.SoyFree);
        temp.append(this.SpecificColor);
        temp.append(this.SpecificFlavor);
        temp.append(this.SugarFree);
        temp.append(this.SuggestedUse);
        temp.append(this.Taxable);
        temp.append(this.VariantID);
        temp.append(this.Vegetarian);
        temp.append(this.Warnings);
        temp.append(this.WheatFree);
        temp.append(this.Height);
        temp.append(this.Length);
        temp.append(this.Width);
        temp.append(this.SellerCost);
        temp.append(this.Weight);
        temp.append(this.IsParent);
        temp.append(this.VoyageOnePrice);
        temp.append(this.VoyageOneMSRP);
        return MD5.getMD5(temp.toString());
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }
}
