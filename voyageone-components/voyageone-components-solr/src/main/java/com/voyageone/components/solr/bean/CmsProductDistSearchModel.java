package com.voyageone.components.solr.bean;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Cms Product distribution Search Model
 *
 * @author chuanyu.liang 2016/10/20
 * @version 2.0.0
 * @since 2.0.0
 */
public class CmsProductDistSearchModel {

    private String code;
    private String nameEn;
    private String nameCn;
    private String descEn;
    private String descCn;
    private String color;
    private Integer onDate;
    private String brandEn;
    private String brandCn;
    private Set<String> catEns;
    private Set<String> catCns;
    private Set<String> brandCats;
    private Double salePrice;
    private String ImageLink;
    private String channelId;
    private Integer pv;
    private Integer uv;
    private Integer saleCount;
    private Long lastVer;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    public String getDescEn() {
        return descEn;
    }

    public void setDescEn(String descEn) {
        this.descEn = descEn;
    }

    public String getDescCn() {
        return descCn;
    }

    public void setDescCn(String descCn) {
        this.descCn = descCn;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getOnDate() {
        return onDate;
    }

    public void setOnDate(Integer onDate) {
        this.onDate = onDate;
    }

    public String getBrandEn() {
        return brandEn;
    }

    public void setBrandEn(String brandEn) {
        this.brandEn = brandEn;
    }

    public String getBrandCn() {
        return brandCn;
    }

    public void setBrandCn(String brandCn) {
        this.brandCn = brandCn;
    }

    public void addCatEn(String catEn) {
        if (catEns == null) {
            catEns = new LinkedHashSet<>();
        }
        catEns.add(catEn);
    }

    public Set<String> getCatEns() {
        return catEns;
    }

    public void setCatEns(Set<String> catEns) {
        this.catEns = catEns;
    }

    public void addCatCn(String catCn) {
        if (catCns == null) {
            catCns = new LinkedHashSet<>();
        }
        catCns.add(catCn);
    }

    public Set<String> getCatCns() {
        return catCns;
    }

    public void setCatCns(Set<String> catCns) {
        this.catCns = catCns;
    }

    public void addBrandCats(String brandCat) {
        if (brandCats == null) {
            brandCats = new LinkedHashSet<>();
        }
        brandCats.add(brandCat);
    }

    public Set<String> getBrandCats() {
        return brandCats;
    }

    public void setBrandCats(Set<String> brandCats) {
        this.brandCats = brandCats;
    }

    public Double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }

    public String getImageLink() {
        return ImageLink;
    }

    public void setImageLink(String imageLink) {
        ImageLink = imageLink;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public Integer getPv() {
        return pv;
    }

    public void setPv(Integer pv) {
        this.pv = pv;
    }

    public Integer getUv() {
        return uv;
    }

    public void setUv(Integer uv) {
        this.uv = uv;
    }

    public Integer getSaleCount() {
        return saleCount;
    }

    public void setSaleCount(Integer saleCount) {
        this.saleCount = saleCount;
    }

    public Long getLastVer() {
        return lastVer;
    }

    public void setLastVer(Long lastVer) {
        this.lastVer = lastVer;
    }
}
