package com.voyageone.task2.cms.bean;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by gjl on 2016/7/5.
 */
public class SuperFeedLightHouseBean extends SuperFeedBean {
    private String sku;

    private String msrp;

    private String category;

    private String upc;

    private String md5;

    private Integer updateflag;

    private String title;

    private String brand;

    private String manufacturer;

    private String description;

    private String bulletpoint1;

    private String bulletpoint2;

    private String bulletpoint3;

    private String bulletpoint4;

    private String bulletpoint5;

    private String mainimage;

    private String image2;

    private String image3;

    private String image4;

    private String unitcount;

    private String color;

    private String directions;

    private String hairtype;

    private String indications;

    private String ingredients;

    private String skintone;

    private String skintype;

    private String targetgender;

    private String voyageoneprice;

    private String countryoforigin;

    private String weight;

    private String njtotalinventory ="0";

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku == null ? null : sku.trim();
    }

    public String getMsrp() {
        return msrp;
    }

    public void setMsrp(String msrp) {
        this.msrp = msrp == null ? null : msrp.trim();
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category == null ? null : category.trim();
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

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer == null ? null : manufacturer.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getBulletpoint1() {
        return bulletpoint1;
    }

    public void setBulletpoint1(String bulletpoint1) {
        this.bulletpoint1 = bulletpoint1 == null ? null : bulletpoint1.trim();
    }

    public String getBulletpoint2() {
        return bulletpoint2;
    }

    public void setBulletpoint2(String bulletpoint2) {
        this.bulletpoint2 = bulletpoint2 == null ? null : bulletpoint2.trim();
    }

    public String getBulletpoint3() {
        return bulletpoint3;
    }

    public void setBulletpoint3(String bulletpoint3) {
        this.bulletpoint3 = bulletpoint3 == null ? null : bulletpoint3.trim();
    }

    public String getBulletpoint4() {
        return bulletpoint4;
    }

    public void setBulletpoint4(String bulletpoint4) {
        this.bulletpoint4 = bulletpoint4 == null ? null : bulletpoint4.trim();
    }

    public String getBulletpoint5() {
        return bulletpoint5;
    }

    public void setBulletpoint5(String bulletpoint5) {
        this.bulletpoint5 = bulletpoint5 == null ? null : bulletpoint5.trim();
    }

    public String getMainimage() {
        return mainimage;
    }

    public void setMainimage(String mainimage) {
        this.mainimage = mainimage == null ? null : mainimage.trim();
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2 == null ? null : image2.trim();
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3 == null ? null : image3.trim();
    }

    public String getImage4() {
        return image4;
    }

    public void setImage4(String image4) {
        this.image4 = image4 == null ? null : image4.trim();
    }

    public String getUnitcount() {
        return unitcount;
    }

    public void setUnitcount(String unitcount) {
        this.unitcount = unitcount == null ? null : unitcount.trim();
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color == null ? null : color.trim();
    }

    public String getDirections() {
        return directions;
    }

    public void setDirections(String directions) {
        this.directions = directions == null ? null : directions.trim();
    }

    public String getHairtype() {
        return hairtype;
    }

    public void setHairtype(String hairtype) {
        this.hairtype = hairtype == null ? null : hairtype.trim();
    }

    public String getIndications() {
        return indications;
    }

    public void setIndications(String indications) {
        this.indications = indications == null ? null : indications.trim();
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients == null ? null : ingredients.trim();
    }

    public String getSkintone() {
        return skintone;
    }

    public void setSkintone(String skintone) {
        this.skintone = skintone == null ? null : skintone.trim();
    }

    public String getSkintype() {
        return skintype;
    }

    public void setSkintype(String skintype) {
        this.skintype = skintype == null ? null : skintype.trim();
    }

    public String getTargetgender() {
        return targetgender;
    }

    public void setTargetgender(String targetgender) {
        this.targetgender = targetgender == null ? null : targetgender.trim();
    }

    public String getVoyageoneprice() {
        return voyageoneprice;
    }

    public void setVoyageoneprice(String voyageoneprice) {
        this.voyageoneprice = voyageoneprice == null ? null : voyageoneprice.trim();
    }

    public String getCountryoforigin() {
        return countryoforigin;
    }

    public void setCountryoforigin(String countryoforigin) {
        this.countryoforigin = countryoforigin == null ? null : countryoforigin.trim();
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight == null ? null : weight.trim();
    }

    public String getNjtotalinventory() {
        return njtotalinventory;
    }

    public void setNjtotalinventory(String njtotalinventory) {
        this.njtotalinventory = njtotalinventory == null ? null : njtotalinventory.trim();
    }

}
