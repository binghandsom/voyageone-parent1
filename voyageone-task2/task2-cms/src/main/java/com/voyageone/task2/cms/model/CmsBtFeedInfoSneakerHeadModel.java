package com.voyageone.task2.cms.model;

import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;

import java.util.Date;

/**
 * Created by gjl on 2016/11/15.
 */
public class CmsBtFeedInfoSneakerHeadModel extends CmsBtFeedInfoModel {
    private String relationshiptype;
    private String variationtheme;
    private String colormap;
    private String accessory;
    private String unisex;
    private String sizeoffset;
    private String blogurl;
    private String isrewardeligible;
    private String isdiscounteligible;
    private String orderlimitcount;
    private String approveddescriptions;
    private String urlkey;
    private String boximages;

    public String getBoximages() {
        return boximages;
    }

    public void setBoximages(String boximages) {
        this.boximages = boximages;
    }


    public String getRelationshiptype() {
        return relationshiptype;
    }

    public void setRelationshiptype(String relationshiptype) {
        this.relationshiptype = relationshiptype;
    }

    public String getVariationtheme() {
        return variationtheme;
    }

    public void setVariationtheme(String variationtheme) {
        this.variationtheme = variationtheme;
    }

    public String getColormap() {
        return colormap;
    }

    public void setColormap(String colormap) {
        this.colormap = colormap;
    }

    public String getAccessory() {
        return accessory;
    }

    public void setAccessory(String accessory) {
        this.accessory = accessory;
    }

    public String getUnisex() {
        return unisex;
    }

    public void setUnisex(String unisex) {
        this.unisex = unisex;
    }

    public String getSizeoffset() {
        return sizeoffset;
    }

    public void setSizeoffset(String sizeoffset) {
        this.sizeoffset = sizeoffset;
    }

    public String getBlogurl() {
        return blogurl;
    }

    public void setBlogurl(String blogurl) {
        this.blogurl = blogurl;
    }

    public String getIsrewardeligible() {
        return isrewardeligible;
    }

    public void setIsrewardeligible(String isrewardeligible) {
        this.isrewardeligible = isrewardeligible;
    }

    public String getIsdiscounteligible() {
        return isdiscounteligible;
    }

    public void setIsdiscounteligible(String isdiscounteligible) {
        this.isdiscounteligible = isdiscounteligible;
    }

    public String getOrderlimitcount() {
        return orderlimitcount;
    }

    public void setOrderlimitcount(String orderlimitcount) {
        this.orderlimitcount = orderlimitcount;
    }

    public String getApproveddescriptions() {
        return approveddescriptions;
    }

    public void setApproveddescriptions(String approveddescriptions) {
        this.approveddescriptions = approveddescriptions;
    }

    public String getUrlkey() {
        return urlkey;
    }

    public void setUrlkey(String urlkey) {
        this.urlkey = urlkey;
    }
}
