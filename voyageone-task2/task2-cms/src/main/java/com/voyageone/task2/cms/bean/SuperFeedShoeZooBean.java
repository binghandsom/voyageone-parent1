package com.voyageone.task2.cms.bean;

import java.util.HashSet;
import java.util.Set;

public class SuperFeedShoeZooBean extends SuperFeedBean{
    private String variationParentSku;

    private String auctionTitle;

    private String shortDescription;

    private String pictureUrls;

    private String brandname;

    private String swatchimageurl;

    private String featureBullets;

    private String md5;

    private Integer updateflag;

    private String inventoryNumber;

    private String upc1;

    private String mpn;

    private String description;

    private String manufacturer;

    private String brand;

    private String condition;

    private String buyItNowPrice;

    private String retailPrice;

    private String taxproductcode;

    private String relationshipName;

    private String amzrepricerautoprice;

    private String categories;

    private String closuretype;

    private String colormap;

    private String colorname;

    private String departmentname;

    private String ebaymaincolor;

    private String ebaystorecategorytext;

    private String externalproductid;

    private String externalproductidtype;

    private String feedproducttype;

    private String gender;

    private String generickeywords;

    private String itemclassdisplaypath;

    private String itemname;

    private String itemtype;

    private String itemtypekeyword;

    private String listprice;

    private String materialtype;

    private String model;

    private String numberofitems;

    private String productdescription;

    private String recommendedbrowsenodes;

    private String relationshiptype;

    private String shoesizedisplay;

    private String shoesizeinfants;

    private String shoesizekids;

    private String shoesizemen;

    private String shoesizewomen;

    private String shoetypes;

    private String sizename;

    private String solematerial;

    private String standardprice;

    private String stylekeywords1;

    private String stylename;

    private String toestyle;

    private String upc2;

    private String variationtheme;

    private String waterresistancelevel;
    private String VoyageOnePurchasePrice;
    private String Country_of_Origin;
    private String Tmall_Weight;
    private String qty;

    public String getVariationParentSku() {
        return variationParentSku;
    }

    public void setVariationParentSku(String variationParentSku) {
        this.variationParentSku = variationParentSku == null ? null : variationParentSku.trim();
    }

    public String getAuctionTitle() {
        return auctionTitle;
    }

    public void setAuctionTitle(String auctionTitle) {
        this.auctionTitle = auctionTitle == null ? null : auctionTitle.trim();
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription == null ? null : shortDescription.trim();
    }

    public String getPictureUrls() {
        return pictureUrls;
    }

    public void setPictureUrls(String pictureUrls) {
        this.pictureUrls = pictureUrls == null ? null : pictureUrls.trim();
    }

    public String getBrandname() {
        return brandname;
    }

    public void setBrandname(String brandname) {
        this.brandname = brandname == null ? null : brandname.trim();
    }

    public String getSwatchimageurl() {
        return swatchimageurl;
    }

    public void setSwatchimageurl(String swatchimageurl) {
        this.swatchimageurl = swatchimageurl == null ? null : swatchimageurl.trim();
    }

    public String getFeatureBullets() {
        return featureBullets;
    }

    public void setFeatureBullets(String featureBullets) {
        this.featureBullets = featureBullets == null ? null : featureBullets.trim();
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

    public String getInventoryNumber() {
        return inventoryNumber;
    }

    public void setInventoryNumber(String inventoryNumber) {
        this.inventoryNumber = inventoryNumber == null ? null : inventoryNumber.trim();
    }

    public String getUpc1() {
        return upc1;
    }

    public void setUpc1(String upc1) {
        this.upc1 = upc1 == null ? null : upc1.trim();
    }

    public String getMpn() {
        return mpn;
    }

    public void setMpn(String mpn) {
        this.mpn = mpn == null ? null : mpn.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer == null ? null : manufacturer.trim();
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand == null ? null : brand.trim();
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition == null ? null : condition.trim();
    }

    public String getBuyItNowPrice() {
        return buyItNowPrice;
    }

    public void setBuyItNowPrice(String buyItNowPrice) {
        this.buyItNowPrice = buyItNowPrice == null ? null : buyItNowPrice.trim();
    }

    public String getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(String retailPrice) {
        this.retailPrice = retailPrice == null ? null : retailPrice.trim();
    }

    public String getTaxproductcode() {
        return taxproductcode;
    }

    public void setTaxproductcode(String taxproductcode) {
        this.taxproductcode = taxproductcode == null ? null : taxproductcode.trim();
    }

    public String getRelationshipName() {
        return relationshipName;
    }

    public void setRelationshipName(String relationshipName) {
        this.relationshipName = relationshipName == null ? null : relationshipName.trim();
    }

    public String getAmzrepricerautoprice() {
        return amzrepricerautoprice;
    }

    public void setAmzrepricerautoprice(String amzrepricerautoprice) {
        this.amzrepricerautoprice = amzrepricerautoprice == null ? null : amzrepricerautoprice.trim();
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories == null ? null : categories.trim();
    }

    public String getClosuretype() {
        return closuretype;
    }

    public void setClosuretype(String closuretype) {
        this.closuretype = closuretype == null ? null : closuretype.trim();
    }

    public String getColormap() {
        return colormap;
    }

    public void setColormap(String colormap) {
        this.colormap = colormap == null ? null : colormap.trim();
    }

    public String getColorname() {
        return colorname;
    }

    public void setColorname(String colorname) {
        this.colorname = colorname == null ? null : colorname.trim();
    }

    public String getDepartmentname() {
        return departmentname;
    }

    public void setDepartmentname(String departmentname) {
        this.departmentname = departmentname == null ? null : departmentname.trim();
    }

    public String getEbaymaincolor() {
        return ebaymaincolor;
    }

    public void setEbaymaincolor(String ebaymaincolor) {
        this.ebaymaincolor = ebaymaincolor == null ? null : ebaymaincolor.trim();
    }

    public String getEbaystorecategorytext() {
        return ebaystorecategorytext;
    }

    public void setEbaystorecategorytext(String ebaystorecategorytext) {
        this.ebaystorecategorytext = ebaystorecategorytext == null ? null : ebaystorecategorytext.trim();
    }

    public String getExternalproductid() {
        return externalproductid;
    }

    public void setExternalproductid(String externalproductid) {
        this.externalproductid = externalproductid == null ? null : externalproductid.trim();
    }

    public String getExternalproductidtype() {
        return externalproductidtype;
    }

    public void setExternalproductidtype(String externalproductidtype) {
        this.externalproductidtype = externalproductidtype == null ? null : externalproductidtype.trim();
    }

    public String getFeedproducttype() {
        return feedproducttype;
    }

    public void setFeedproducttype(String feedproducttype) {
        this.feedproducttype = feedproducttype == null ? null : feedproducttype.trim();
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender == null ? null : gender.trim();
    }

    public String getGenerickeywords() {
        return generickeywords;
    }

    public void setGenerickeywords(String generickeywords) {
        this.generickeywords = generickeywords == null ? null : generickeywords.trim();
    }

    public String getItemclassdisplaypath() {
        return itemclassdisplaypath;
    }

    public void setItemclassdisplaypath(String itemclassdisplaypath) {
        this.itemclassdisplaypath = itemclassdisplaypath == null ? null : itemclassdisplaypath.trim();
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname == null ? null : itemname.trim();
    }

    public String getItemtype() {
        return itemtype;
    }

    public void setItemtype(String itemtype) {
        this.itemtype = itemtype == null ? null : itemtype.trim();
    }

    public String getItemtypekeyword() {
        return itemtypekeyword;
    }

    public void setItemtypekeyword(String itemtypekeyword) {
        this.itemtypekeyword = itemtypekeyword == null ? null : itemtypekeyword.trim();
    }

    public String getListprice() {
        return listprice;
    }

    public void setListprice(String listprice) {
        this.listprice = listprice == null ? null : listprice.trim();
    }

    public String getMaterialtype() {
        return materialtype;
    }

    public void setMaterialtype(String materialtype) {
        this.materialtype = materialtype == null ? null : materialtype.trim();
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model == null ? null : model.trim();
    }

    public String getNumberofitems() {
        return numberofitems;
    }

    public void setNumberofitems(String numberofitems) {
        this.numberofitems = numberofitems == null ? null : numberofitems.trim();
    }

    public String getProductdescription() {
        return productdescription;
    }

    public void setProductdescription(String productdescription) {
        this.productdescription = productdescription == null ? null : productdescription.trim();
    }

    public String getRecommendedbrowsenodes() {
        return recommendedbrowsenodes;
    }

    public void setRecommendedbrowsenodes(String recommendedbrowsenodes) {
        this.recommendedbrowsenodes = recommendedbrowsenodes == null ? null : recommendedbrowsenodes.trim();
    }

    public String getRelationshiptype() {
        return relationshiptype;
    }

    public void setRelationshiptype(String relationshiptype) {
        this.relationshiptype = relationshiptype == null ? null : relationshiptype.trim();
    }

    public String getShoesizedisplay() {
        return shoesizedisplay;
    }

    public void setShoesizedisplay(String shoesizedisplay) {
        this.shoesizedisplay = shoesizedisplay == null ? null : shoesizedisplay.trim();
    }

    public String getShoesizeinfants() {
        return shoesizeinfants;
    }

    public void setShoesizeinfants(String shoesizeinfants) {
        this.shoesizeinfants = shoesizeinfants == null ? null : shoesizeinfants.trim();
    }

    public String getShoesizekids() {
        return shoesizekids;
    }

    public void setShoesizekids(String shoesizekids) {
        this.shoesizekids = shoesizekids == null ? null : shoesizekids.trim();
    }

    public String getShoesizemen() {
        return shoesizemen;
    }

    public void setShoesizemen(String shoesizemen) {
        this.shoesizemen = shoesizemen == null ? null : shoesizemen.trim();
    }

    public String getShoesizewomen() {
        return shoesizewomen;
    }

    public void setShoesizewomen(String shoesizewomen) {
        this.shoesizewomen = shoesizewomen == null ? null : shoesizewomen.trim();
    }

    public String getShoetypes() {
        return shoetypes;
    }

    public void setShoetypes(String shoetypes) {
        this.shoetypes = shoetypes == null ? null : shoetypes.trim();
    }

    public String getSizename() {
        return sizename;
    }

    public void setSizename(String sizename) {
        this.sizename = sizename == null ? null : sizename.trim();
    }

    public String getSolematerial() {
        return solematerial;
    }

    public void setSolematerial(String solematerial) {
        this.solematerial = solematerial == null ? null : solematerial.trim();
    }

    public String getStandardprice() {
        return standardprice;
    }

    public void setStandardprice(String standardprice) {
        this.standardprice = standardprice == null ? null : standardprice.trim();
    }

    public String getStylekeywords1() {
        return stylekeywords1;
    }

    public void setStylekeywords1(String stylekeywords1) {
        this.stylekeywords1 = stylekeywords1 == null ? null : stylekeywords1.trim();
    }

    public String getStylename() {
        return stylename;
    }

    public void setStylename(String stylename) {
        this.stylename = stylename == null ? null : stylename.trim();
    }

    public String getToestyle() {
        return toestyle;
    }

    public void setToestyle(String toestyle) {
        this.toestyle = toestyle == null ? null : toestyle.trim();
    }

    public String getUpc2() {
        return upc2;
    }

    public void setUpc2(String upc2) {
        this.upc2 = upc2 == null ? null : upc2.trim();
    }

    public String getVariationtheme() {
        return variationtheme;
    }

    public void setVariationtheme(String variationtheme) {
        this.variationtheme = variationtheme == null ? null : variationtheme.trim();
    }

    public String getWaterresistancelevel() {
        return waterresistancelevel;
    }

    public void setWaterresistancelevel(String waterresistancelevel) {
        this.waterresistancelevel = waterresistancelevel == null ? null : waterresistancelevel.trim();
    }

    public String getVoyageOnePurchasePrice() {
        return VoyageOnePurchasePrice;
    }

    public void setVoyageOnePurchasePrice(String voyageOnePurchasePrice) {
        VoyageOnePurchasePrice = voyageOnePurchasePrice;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getTmall_Weight() {
        return Tmall_Weight;
    }

    public void setTmall_Weight(String tmall_Weight) {
        Tmall_Weight = tmall_Weight;
    }

    public String getCountry_of_Origin() {
        return Country_of_Origin;
    }

    public void setCountry_of_Origin(String country_of_Origin) {
        Country_of_Origin = country_of_Origin;
    }
}