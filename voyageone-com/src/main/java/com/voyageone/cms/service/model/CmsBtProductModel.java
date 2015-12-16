package com.voyageone.cms.service.model;

import com.voyageone.base.dao.mongodb.model.ChannelPartitionModel;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link CmsBtProductModel} 的商品Model
 * @author chuanyu.liang, 12/11/15
 * @version 2.0.0
 * @since 2.0.0
 */
public class CmsBtProductModel extends ChannelPartitionModel {

    private long prodId;
    private String catId;
    private String catPath;

    private CmsBtProductModel_Field fields = new CmsBtProductModel_Field();
    private CmsBtProductModel_Group groups = new CmsBtProductModel_Group();
    private List<CmsBtProductModel_Sku> skus = new ArrayList<>();
    private List<CmsBtProductModel_Tag> tags = new ArrayList<>();
    private CmsBtProductModel_Feed feedAtts = new CmsBtProductModel_Feed();

    public CmsBtProductModel() {
    }

    public CmsBtProductModel(String channelId) {
        super(channelId);
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getCatPath() {
        return catPath;
    }

    public void setCatPath(String catIdPath) {
        this.catPath = catIdPath;
    }

    public long getProdId() {
        return prodId;
    }

    public void setProdId(long prodId) {
        this.prodId = prodId;
    }

    public CmsBtProductModel_Field getFields() {
        return fields;
    }

    public void setFields(CmsBtProductModel_Field fields) {
        this.fields = fields;
    }

    public CmsBtProductModel_Group getGroups() {
        return groups;
    }

    public void setGroups(CmsBtProductModel_Group groups) {
        this.groups = groups;
    }

    public List<CmsBtProductModel_Sku> getSkus() {
        return skus;
    }

    public void setSkus(List<CmsBtProductModel_Sku> skus) {
        this.skus = skus;
    }

    public List<CmsBtProductModel_Tag> getTags() {
        return tags;
    }

    public void setTags(List<CmsBtProductModel_Tag> tags) {
        this.tags = tags;
    }

    public CmsBtProductModel_Feed getFeedAtts() {
        return feedAtts;
    }

    public void setFeedAtts(CmsBtProductModel_Feed feedAtt) {
        this.feedAtts = feedAtt;
    }

}