package com.voyageone.task2.cms.bean;

import java.util.HashSet;
import java.util.Set;

public class SuperFeedOverStockBean extends SuperFeedBean{
    private String sku;

    private String category;

    private String inventoryavailable;

    private String md5;

    private Integer updateflag;

    private String retailerid;

    private String model;

    private String clientmodel;

    private String clientsku;

    private String title;

    private String brand;

    private String manufacturername;

    private String shortdescription;

    private String longdescription;

    private String leadtime;

    private String adultcontent;

    private String countryoforigin;

    private String productactivestatus;

    private String shippingsitesale;

    private String discountsitesale;

    private String condition;

    private String returnpolicy;

    private String description;

    private String sellingpriceAmount;

    private String sellingpriceCurrency;

    private String mappriceAmount;

    private String mappriceCurrency;

    private String msrppriceAmount;

    private String msrppriceCurrency;

    private String msrpexpirationdate;

    private String compareatpriceCurrency;

    private String compareatpriceAmount;

    private String compareatexpirationdate;

    private String previouslyadvertisedpriceAmount;

    private String previouslyadvertisedpriceCurrency;

    private String shippingwidth;

    private String shippingheight;

    private String shippinglength;

    private String shippingweight;

    private String upc;

    private String shipsvialtl;

    private String attribute1;
    private String attributeColor;
    private String attributeMetal;
    private String attributeSize;

    private String image;

    private String modelImage;

    private String modelRetailerid;

    private String modelTitle;

    private String modelBrand;

    private String modelManufacturername;

    private String modelShortdescription;

    private String modelLongdescription;

    private String modelLeadtime;

    private String modelAdultcontent;

    private String modelCountryoforigin;

    private String modelProductactivestatus;

    private String modelShippingsitesale;

    private String modelDiscountsitesale;

    private String modelCondition;

    private String modelReturnpolicy;

    private String salepoint;

    public String getSku() {
        return sku;
    }
    public void setSku(String sku) {
        this.sku = sku == null ? null : sku.trim();
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category == null ? null : category.trim();
    }

    public String getInventoryavailable() {
        return inventoryavailable;
    }

    public void setInventoryavailable(String inventoryavailable) {
        this.inventoryavailable = inventoryavailable == null ? null : inventoryavailable.trim();
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


    public String getRetailerid() {
        return retailerid;
    }

    public void setRetailerid(String retailerid) {
        this.retailerid = retailerid == null ? null : retailerid.trim();
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model == null ? null : model.trim();
    }

    public String getClientmodel() {
        return clientmodel;
    }

    public void setClientmodel(String clientmodel) {
        this.clientmodel = clientmodel == null ? null : clientmodel.trim();
    }

    public String getClientsku() {
        return clientsku;
    }

    public void setClientsku(String clientsku) {
        this.clientsku = clientsku == null ? null : clientsku.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand == null ? null : brand.trim();
    }

    public String getManufacturername() {
        return manufacturername;
    }

    public void setManufacturername(String manufacturername) {
        this.manufacturername = manufacturername == null ? null : manufacturername.trim();
    }

    public String getShortdescription() {
        return shortdescription;
    }

    public void setShortdescription(String shortdescription) {
        this.shortdescription = shortdescription == null ? null : shortdescription.trim();
    }

    public String getLongdescription() {
        return longdescription;
    }

    public void setLongdescription(String longdescription) {
        this.longdescription = longdescription == null ? null : longdescription.trim();
    }

    public String getLeadtime() {
        return leadtime;
    }

    public void setLeadtime(String leadtime) {
        this.leadtime = leadtime == null ? null : leadtime.trim();
    }

    public String getAdultcontent() {
        return adultcontent;
    }

    public void setAdultcontent(String adultcontent) {
        this.adultcontent = adultcontent == null ? null : adultcontent.trim();
    }

    public String getCountryoforigin() {
        return countryoforigin;
    }

    public void setCountryoforigin(String countryoforigin) {
        this.countryoforigin = countryoforigin == null ? null : countryoforigin.trim();
    }

    public String getProductactivestatus() {
        return productactivestatus;
    }

    public void setProductactivestatus(String productactivestatus) {
        this.productactivestatus = productactivestatus == null ? null : productactivestatus.trim();
    }

    public String getShippingsitesale() {
        return shippingsitesale;
    }

    public void setShippingsitesale(String shippingsitesale) {
        this.shippingsitesale = shippingsitesale == null ? null : shippingsitesale.trim();
    }

    public String getDiscountsitesale() {
        return discountsitesale;
    }

    public void setDiscountsitesale(String discountsitesale) {
        this.discountsitesale = discountsitesale == null ? null : discountsitesale.trim();
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition == null ? null : condition.trim();
    }

    public String getReturnpolicy() {
        return returnpolicy;
    }

    public void setReturnpolicy(String returnpolicy) {
        this.returnpolicy = returnpolicy == null ? null : returnpolicy.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getSellingpriceAmount() {
        return sellingpriceAmount;
    }

    public void setSellingpriceAmount(String sellingpriceAmount) {
        this.sellingpriceAmount = sellingpriceAmount == null ? null : sellingpriceAmount.trim();
    }

    public String getSellingpriceCurrency() {
        return sellingpriceCurrency;
    }

    public void setSellingpriceCurrency(String sellingpriceCurrency) {
        this.sellingpriceCurrency = sellingpriceCurrency == null ? null : sellingpriceCurrency.trim();
    }

    public String getMappriceAmount() {
        return mappriceAmount;
    }

    public void setMappriceAmount(String mappriceAmount) {
        this.mappriceAmount = mappriceAmount == null ? null : mappriceAmount.trim();
    }

    public String getMappriceCurrency() {
        return mappriceCurrency;
    }

    public void setMappriceCurrency(String mappriceCurrency) {
        this.mappriceCurrency = mappriceCurrency == null ? null : mappriceCurrency.trim();
    }

    public String getMsrppriceAmount() {
        return msrppriceAmount;
    }

    public void setMsrppriceAmount(String msrppriceAmount) {
        this.msrppriceAmount = msrppriceAmount == null ? null : msrppriceAmount.trim();
    }

    public String getMsrppriceCurrency() {
        return msrppriceCurrency;
    }

    public void setMsrppriceCurrency(String msrppriceCurrency) {
        this.msrppriceCurrency = msrppriceCurrency == null ? null : msrppriceCurrency.trim();
    }

    public String getMsrpexpirationdate() {
        return msrpexpirationdate;
    }

    public void setMsrpexpirationdate(String msrpexpirationdate) {
        this.msrpexpirationdate = msrpexpirationdate == null ? null : msrpexpirationdate.trim();
    }

    public String getCompareatpriceCurrency() {
        return compareatpriceCurrency;
    }

    public void setCompareatpriceCurrency(String compareatpriceCurrency) {
        this.compareatpriceCurrency = compareatpriceCurrency == null ? null : compareatpriceCurrency.trim();
    }

    public String getCompareatpriceAmount() {
        return compareatpriceAmount;
    }

    public void setCompareatpriceAmount(String compareatpriceAmount) {
        this.compareatpriceAmount = compareatpriceAmount == null ? null : compareatpriceAmount.trim();
    }

    public String getCompareatexpirationdate() {
        return compareatexpirationdate;
    }

    public void setCompareatexpirationdate(String compareatexpirationdate) {
        this.compareatexpirationdate = compareatexpirationdate == null ? null : compareatexpirationdate.trim();
    }

    public String getPreviouslyadvertisedpriceAmount() {
        return previouslyadvertisedpriceAmount;
    }

    public void setPreviouslyadvertisedpriceAmount(String previouslyadvertisedpriceAmount) {
        this.previouslyadvertisedpriceAmount = previouslyadvertisedpriceAmount == null ? null : previouslyadvertisedpriceAmount.trim();
    }

    public String getPreviouslyadvertisedpriceCurrency() {
        return previouslyadvertisedpriceCurrency;
    }

    public void setPreviouslyadvertisedpriceCurrency(String previouslyadvertisedpriceCurrency) {
        this.previouslyadvertisedpriceCurrency = previouslyadvertisedpriceCurrency == null ? null : previouslyadvertisedpriceCurrency.trim();
    }

    public String getShippingwidth() {
        return shippingwidth;
    }

    public void setShippingwidth(String shippingwidth) {
        this.shippingwidth = shippingwidth == null ? null : shippingwidth.trim();
    }

    public String getShippingheight() {
        return shippingheight;
    }

    public void setShippingheight(String shippingheight) {
        this.shippingheight = shippingheight == null ? null : shippingheight.trim();
    }

    public String getShippinglength() {
        return shippinglength;
    }

    public void setShippinglength(String shippinglength) {
        this.shippinglength = shippinglength == null ? null : shippinglength.trim();
    }

    public String getShippingweight() {
        return shippingweight;
    }

    public void setShippingweight(String shippingweight) {
        this.shippingweight = shippingweight == null ? null : shippingweight.trim();
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc == null ? null : upc.trim();
    }

    public String getShipsvialtl() {
        return shipsvialtl;
    }

    public void setShipsvialtl(String shipsvialtl) {
        this.shipsvialtl = shipsvialtl == null ? null : shipsvialtl.trim();
    }

    public String getAttribute1() {
        return attribute1;
    }

    public void setAttribute1(String attribute1) {
        this.attribute1 = attribute1;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }

    public String getModelImage() {
        return modelImage;
    }

    public void setModelImage(String modelImage) {
        this.modelImage = modelImage == null ? null : modelImage.trim();
    }

    public String getModelRetailerid() {
        return modelRetailerid;
    }

    public void setModelRetailerid(String modelRetailerid) {
        this.modelRetailerid = modelRetailerid == null ? null : modelRetailerid.trim();
    }

    public String getModelTitle() {
        return modelTitle;
    }

    public void setModelTitle(String modelTitle) {
        this.modelTitle = modelTitle == null ? null : modelTitle.trim();
    }

    public String getModelBrand() {
        return modelBrand;
    }

    public void setModelBrand(String modelBrand) {
        this.modelBrand = modelBrand == null ? null : modelBrand.trim();
    }

    public String getModelManufacturername() {
        return modelManufacturername;
    }

    public void setModelManufacturername(String modelManufacturername) {
        this.modelManufacturername = modelManufacturername == null ? null : modelManufacturername.trim();
    }

    public String getModelShortdescription() {
        return modelShortdescription;
    }

    public void setModelShortdescription(String modelShortdescription) {
        this.modelShortdescription = modelShortdescription == null ? null : modelShortdescription.trim();
    }

    public String getModelLongdescription() {
        return modelLongdescription;
    }

    public void setModelLongdescription(String modelLongdescription) {
        this.modelLongdescription = modelLongdescription == null ? null : modelLongdescription.trim();
    }

    public String getModelLeadtime() {
        return modelLeadtime;
    }

    public void setModelLeadtime(String modelLeadtime) {
        this.modelLeadtime = modelLeadtime == null ? null : modelLeadtime.trim();
    }

    public String getModelAdultcontent() {
        return modelAdultcontent;
    }

    public void setModelAdultcontent(String modelAdultcontent) {
        this.modelAdultcontent = modelAdultcontent == null ? null : modelAdultcontent.trim();
    }

    public String getModelCountryoforigin() {
        return modelCountryoforigin;
    }

    public void setModelCountryoforigin(String modelCountryoforigin) {
        this.modelCountryoforigin = modelCountryoforigin == null ? null : modelCountryoforigin.trim();
    }

    public String getModelProductactivestatus() {
        return modelProductactivestatus;
    }

    public void setModelProductactivestatus(String modelProductactivestatus) {
        this.modelProductactivestatus = modelProductactivestatus == null ? null : modelProductactivestatus.trim();
    }

    public String getModelShippingsitesale() {
        return modelShippingsitesale;
    }

    public void setModelShippingsitesale(String modelShippingsitesale) {
        this.modelShippingsitesale = modelShippingsitesale == null ? null : modelShippingsitesale.trim();
    }

    public String getModelDiscountsitesale() {
        return modelDiscountsitesale;
    }

    public void setModelDiscountsitesale(String modelDiscountsitesale) {
        this.modelDiscountsitesale = modelDiscountsitesale == null ? null : modelDiscountsitesale.trim();
    }

    public String getModelCondition() {
        return modelCondition;
    }

    public void setModelCondition(String modelCondition) {
        this.modelCondition = modelCondition == null ? null : modelCondition.trim();
    }

    public String getModelReturnpolicy() {
        return modelReturnpolicy;
    }

    public void setModelReturnpolicy(String modelReturnpolicy) {
        this.modelReturnpolicy = modelReturnpolicy == null ? null : modelReturnpolicy.trim();
    }

    public String getSalepoint() {
        return salepoint;
    }

    public void setSalepoint(String salepoint) {
        this.salepoint = salepoint == null ? null : salepoint.trim();
    }

    public String getAttributeColor() {
        return attributeColor;
    }

    public void setAttributeColor(String attributeColor) {
        this.attributeColor = attributeColor;
    }

    public String getAttributeSize() {
        return attributeSize;
    }

    public void setAttributeSize(String attributeSize) {
        this.attributeSize = attributeSize;
    }

    public String getAttributeMetal() {
        return attributeMetal;
    }

    public void setAttributeMetal(String attributeMetal) {
        this.attributeMetal = attributeMetal;
    }
}