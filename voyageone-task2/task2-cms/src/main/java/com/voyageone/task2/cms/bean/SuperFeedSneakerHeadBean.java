package com.voyageone.task2.cms.bean;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by gjl on 2016/11/15.
 */
public class SuperFeedSneakerHeadBean extends SuperFeedBean{
    private String sku;

    private String code;

    private BigDecimal  price;

    private BigDecimal msrp;

    private BigDecimal cnRetailPrice;

    private BigDecimal cnMsrp;

    private String quantity;

    private String category;

    private String md5;

    private Date created;

    private Integer updateflag;

    private String relationshiptype;

    private String variationtheme;

    private String title;

    private String barcode;

    private String weight;

    private String images;

    private String description;

    private String shortdescription;

    private String productorigin;

    private String brand;

    private String materials;

    private String vendorproducturl;

    private String size;

    private String model;

    private String producttype;

    private String sizetype;

    private String color;

    private String boximages;

    private String colormap;

    private String abstractdescription;

    private String accessory;

    private String unisex;

    private String sizeoffset;

    private String blogurl;

    private String isrewardeligible;

    private String isdiscounteligible;

    private String orderlimitcount;

    private String approveddescriptions;

    private String urlkey;

    private Date lastReceivedOn;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku == null ? null : sku.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getMsrp() {
        return msrp;
    }

    public void setMsrp(BigDecimal msrp) {
        this.msrp = msrp;
    }

    public BigDecimal getCnRetailPrice() {
        return cnRetailPrice;
    }

    public void setCnRetailPrice(BigDecimal cnRetailPrice) {
        this.cnRetailPrice = cnRetailPrice;
    }

    public BigDecimal getCnMsrp() {
        return cnMsrp;
    }

    public void setCnMsrp(BigDecimal cnMsrp) {
        this.cnMsrp = cnMsrp;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity == null ? null : quantity.trim();
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category == null ? null : category.trim();
    }

    public String getMd5() {
        StringBuffer temp = new StringBuffer();
        Set<String> noMd5Fields = new HashSet<>();
        noMd5Fields.add("md5");
        noMd5Fields.add("updateflag");
        noMd5Fields.add("created");


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

    public String getRelationshiptype() {
        return relationshiptype;
    }

    public void setRelationshiptype(String relationshiptype) {
        this.relationshiptype = relationshiptype == null ? null : relationshiptype.trim();
    }

    public String getVariationtheme() {
        return variationtheme;
    }

    public void setVariationtheme(String variationtheme) {
        this.variationtheme = variationtheme == null ? null : variationtheme.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode == null ? null : barcode.trim();
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight == null ? null : weight.trim();
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images == null ? null : images.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getShortdescription() {
        return shortdescription;
    }

    public void setShortdescription(String shortdescription) {
        this.shortdescription = shortdescription == null ? null : shortdescription.trim();
    }

    public String getProductorigin() {
        return productorigin;
    }

    public void setProductorigin(String productorigin) {
        this.productorigin = productorigin == null ? null : productorigin.trim();
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand == null ? null : brand.trim();
    }

    public String getMaterials() {
        return materials;
    }

    public void setMaterials(String materials) {
        this.materials = materials == null ? null : materials.trim();
    }

    public String getVendorproducturl() {
        return vendorproducturl;
    }

    public void setVendorproducturl(String vendorproducturl) {
        this.vendorproducturl = vendorproducturl == null ? null : vendorproducturl.trim();
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size == null ? null : size.trim();
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model == null ? null : model.trim();
    }

    public String getProducttype() {
        return producttype;
    }

    public void setProducttype(String producttype) {
        this.producttype = producttype == null ? null : producttype.trim();
    }

    public String getSizetype() {
        return sizetype;
    }

    public void setSizetype(String sizetype) {
        this.sizetype = sizetype == null ? null : sizetype.trim();
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color == null ? null : color.trim();
    }

    public String getBoximages() {
        return boximages;
    }

    public void setBoximages(String boximages) {
        this.boximages = boximages == null ? null : boximages.trim();
    }

    public String getColormap() {
        return colormap;
    }

    public void setColormap(String colormap) {
        this.colormap = colormap == null ? null : colormap.trim();
    }

    public String getAbstractdescription() {
        return abstractdescription;
    }

    public void setAbstractdescription(String abstractdescription) {
        this.abstractdescription = abstractdescription == null ? null : abstractdescription.trim();
    }

    public String getAccessory() {
        return accessory;
    }

    public void setAccessory(String accessory) {
        this.accessory = accessory == null ? null : accessory.trim();
    }

    public String getUnisex() {
        return unisex;
    }

    public void setUnisex(String unisex) {
        this.unisex = unisex == null ? null : unisex.trim();
    }

    public String getSizeoffset() {
        return sizeoffset;
    }

    public void setSizeoffset(String sizeoffset) {
        this.sizeoffset = sizeoffset == null ? null : sizeoffset.trim();
    }

    public String getBlogurl() {
        return blogurl;
    }

    public void setBlogurl(String blogurl) {
        this.blogurl = blogurl == null ? null : blogurl.trim();
    }

    public String getIsrewardeligible() {
        return isrewardeligible;
    }

    public void setIsrewardeligible(String isrewardeligible) {
        this.isrewardeligible = isrewardeligible == null ? null : isrewardeligible.trim();
    }

    public String getIsdiscounteligible() {
        return isdiscounteligible;
    }

    public void setIsdiscounteligible(String isdiscounteligible) {
        this.isdiscounteligible = isdiscounteligible == null ? null : isdiscounteligible.trim();
    }

    public String getOrderlimitcount() {
        return orderlimitcount;
    }

    public void setOrderlimitcount(String orderlimitcount) {
        this.orderlimitcount = orderlimitcount == null ? null : orderlimitcount.trim();
    }

    public String getApproveddescriptions() {
        return approveddescriptions;
    }

    public void setApproveddescriptions(String approveddescriptions) {
        this.approveddescriptions = approveddescriptions == null ? null : approveddescriptions.trim();
    }

    public String getUrlkey() {
        return urlkey;
    }

    public void setUrlkey(String urlkey) {
        this.urlkey = urlkey == null ? null : urlkey.trim();
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getLastReceivedOn() {
        return lastReceivedOn;
    }

    public void setLastReceivedOn(Date lastReceivedOn) {
        this.lastReceivedOn = lastReceivedOn;
    }
}