package com.voyageone.task2.cms.bean;

import java.util.HashSet;
import java.util.Set;

public class SuperFeedYogaDemocracyBean extends SuperFeedBean {

    private String sku;

    private String parentid;

    private String price;

    private String msrp;

    private String quantity;

    private String category;

    private String md5;

    private Integer updateflag;

    private String relationshiptype;

    private String variationtheme;

    private String title;

    private String productid;

    private String images;

    private String description;

    private String shortdiscription;

    private String productorigin;

    private String weight;

    private String attributekey1;

    private String attributevalue1;

    private String attributekey2;

    private String attributevalue2;

    private String attributekey3;

    private String attributevalue3;

    private String attributekey4;

    private String attributevalue4;

    private String attributekey5;

    private String attributevalue5;

    private String attributekey6;

    private String attributevalue6;

    private String attributekey7;

    private String attributevalue7;

    private String attributekey8;

    private String attributevalue8;

    private String attributekey9;

    private String attributevalue9;

    private String attributekey10;

    private String attributevalue10;

    private String attributekey11;

    private String attributevalue11;

    private String attributekey12;

    private String attributevalue12;

    private String attributekey13;

    private String attributevalue13;

    private String attributekey14;

    private String attributevalue14;

    private String attributekey15;

    private String attributevalue15;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku == null ? null : sku.trim();
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid == null ? null : parentid.trim();
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price == null ? null : price.trim();
    }

    public String getMsrp() {
        return msrp;
    }

    public void setMsrp(String msrp) {
        this.msrp = msrp == null ? null : msrp.trim();
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

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid == null ? null : productid.trim();
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

    public String getShortdiscription() {
        return shortdiscription;
    }

    public void setShortdiscription(String shortdiscription) {
        this.shortdiscription = shortdiscription == null ? null : shortdiscription.trim();
    }



    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight == null ? null : weight.trim();
    }

    public String getAttributekey1() {
        return attributekey1;
    }

    public void setAttributekey1(String attributekey1) {
        this.attributekey1 = attributekey1 == null ? null : attributekey1.trim();
    }

    public String getAttributevalue1() {
        return attributevalue1;
    }

    public void setAttributevalue1(String attributevalue1) {
        this.attributevalue1 = attributevalue1 == null ? null : attributevalue1.trim();
    }

    public String getAttributekey2() {
        return attributekey2;
    }

    public void setAttributekey2(String attributekey2) {
        this.attributekey2 = attributekey2 == null ? null : attributekey2.trim();
    }

    public String getAttributevalue2() {
        return attributevalue2;
    }

    public void setAttributevalue2(String attributevalue2) {
        this.attributevalue2 = attributevalue2 == null ? null : attributevalue2.trim();
    }

    public String getAttributekey3() {
        return attributekey3;
    }

    public void setAttributekey3(String attributekey3) {
        this.attributekey3 = attributekey3 == null ? null : attributekey3.trim();
    }

    public String getAttributevalue3() {
        return attributevalue3;
    }

    public void setAttributevalue3(String attributevalue3) {
        this.attributevalue3 = attributevalue3 == null ? null : attributevalue3.trim();
    }

    public String getAttributekey4() {
        return attributekey4;
    }

    public void setAttributekey4(String attributekey4) {
        this.attributekey4 = attributekey4 == null ? null : attributekey4.trim();
    }

    public String getAttributevalue4() {
        return attributevalue4;
    }

    public void setAttributevalue4(String attributevalue4) {
        this.attributevalue4 = attributevalue4 == null ? null : attributevalue4.trim();
    }

    public String getAttributekey5() {
        return attributekey5;
    }

    public void setAttributekey5(String attributekey5) {
        this.attributekey5 = attributekey5 == null ? null : attributekey5.trim();
    }

    public String getAttributevalue5() {
        return attributevalue5;
    }

    public void setAttributevalue5(String attributevalue5) {
        this.attributevalue5 = attributevalue5 == null ? null : attributevalue5.trim();
    }

    public String getAttributekey6() {
        return attributekey6;
    }

    public void setAttributekey6(String attributekey6) {
        this.attributekey6 = attributekey6 == null ? null : attributekey6.trim();
    }

    public String getAttributevalue6() {
        return attributevalue6;
    }

    public void setAttributevalue6(String attributevalue6) {
        this.attributevalue6 = attributevalue6 == null ? null : attributevalue6.trim();
    }

    public String getAttributekey7() {
        return attributekey7;
    }

    public void setAttributekey7(String attributekey7) {
        this.attributekey7 = attributekey7 == null ? null : attributekey7.trim();
    }

    public String getAttributevalue7() {
        return attributevalue7;
    }

    public void setAttributevalue7(String attributevalue7) {
        this.attributevalue7 = attributevalue7 == null ? null : attributevalue7.trim();
    }

    public String getAttributekey8() {
        return attributekey8;
    }

    public void setAttributekey8(String attributekey8) {
        this.attributekey8 = attributekey8 == null ? null : attributekey8.trim();
    }

    public String getAttributevalue8() {
        return attributevalue8;
    }

    public void setAttributevalue8(String attributevalue8) {
        this.attributevalue8 = attributevalue8 == null ? null : attributevalue8.trim();
    }

    public String getAttributekey9() {
        return attributekey9;
    }

    public void setAttributekey9(String attributekey9) {
        this.attributekey9 = attributekey9 == null ? null : attributekey9.trim();
    }

    public String getAttributevalue9() {
        return attributevalue9;
    }

    public void setAttributevalue9(String attributevalue9) {
        this.attributevalue9 = attributevalue9 == null ? null : attributevalue9.trim();
    }

    public String getAttributekey10() {
        return attributekey10;
    }

    public void setAttributekey10(String attributekey10) {
        this.attributekey10 = attributekey10 == null ? null : attributekey10.trim();
    }

    public String getAttributevalue10() {
        return attributevalue10;
    }

    public void setAttributevalue10(String attributevalue10) {
        this.attributevalue10 = attributevalue10 == null ? null : attributevalue10.trim();
    }

    public String getAttributekey11() {
        return attributekey11;
    }

    public void setAttributekey11(String attributekey11) {
        this.attributekey11 = attributekey11 == null ? null : attributekey11.trim();
    }

    public String getAttributevalue11() {
        return attributevalue11;
    }

    public void setAttributevalue11(String attributevalue11) {
        this.attributevalue11 = attributevalue11 == null ? null : attributevalue11.trim();
    }

    public String getAttributekey12() {
        return attributekey12;
    }

    public void setAttributekey12(String attributekey12) {
        this.attributekey12 = attributekey12 == null ? null : attributekey12.trim();
    }

    public String getAttributevalue12() {
        return attributevalue12;
    }

    public void setAttributevalue12(String attributevalue12) {
        this.attributevalue12 = attributevalue12 == null ? null : attributevalue12.trim();
    }

    public String getAttributekey13() {
        return attributekey13;
    }

    public void setAttributekey13(String attributekey13) {
        this.attributekey13 = attributekey13 == null ? null : attributekey13.trim();
    }

    public String getAttributevalue13() {
        return attributevalue13;
    }

    public void setAttributevalue13(String attributevalue13) {
        this.attributevalue13 = attributevalue13 == null ? null : attributevalue13.trim();
    }

    public String getAttributekey14() {
        return attributekey14;
    }

    public void setAttributekey14(String attributekey14) {
        this.attributekey14 = attributekey14 == null ? null : attributekey14.trim();
    }

    public String getAttributevalue14() {
        return attributevalue14;
    }

    public void setAttributevalue14(String attributevalue14) {
        this.attributevalue14 = attributevalue14 == null ? null : attributevalue14.trim();
    }

    public String getAttributekey15() {
        return attributekey15;
    }

    public void setAttributekey15(String attributekey15) {
        this.attributekey15 = attributekey15 == null ? null : attributekey15.trim();
    }

    public String getAttributevalue15() {
        return attributevalue15;
    }

    public void setAttributevalue15(String attributevalue15) {
        this.attributevalue15 = attributevalue15 == null ? null : attributevalue15.trim();
    }

    public String getProductorigin() {
        return productorigin;
    }

    public void setProductorigin(String productorigin) {
        this.productorigin = productorigin;
    }
}