package com.voyageone.service.model.cms.mongo.product;

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

    private Long prodId;
    private String catId;
    private String catPath;
    private String orgChannelId;

    private CmsBtProductModel_Field fields = new CmsBtProductModel_Field();
    private CmsBtProductModel_Group groups = new CmsBtProductModel_Group();
    private List<CmsBtProductModel_Sku> skus = new ArrayList<>();
    private List<String> tags = new ArrayList<>();
    private CmsBtProductModel_BatchField batchField = new CmsBtProductModel_BatchField();
    private CmsBtProductModel_Feed feed = new CmsBtProductModel_Feed();

    public String getOrgChannelId() {

        return orgChannelId;
    }

    public void setOrgChannelId(String orgChannelId) {
        this.orgChannelId = orgChannelId;
    }

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

    public Long getProdId() {
        return prodId;
    }

    public void setProdId(Long prodId) {
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

    public CmsBtProductModel_Sku getSku(String skuCode) {
        if (skuCode != null && this.skus != null) {
            for(CmsBtProductModel_Sku sku : skus) {
                if (skuCode.equals(sku.getSkuCode())) {
                    return sku;
                }
            }
        }
        return null;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public CmsBtProductModel_BatchField getBatchField() {
        return batchField;
    }

    public void setBatchField(CmsBtProductModel_BatchField batchField) {
        this.batchField = batchField;
    }

    public CmsBtProductModel_Feed getFeed() {
        return feed;
    }

    public void setFeed(CmsBtProductModel_Feed feed) {
        this.feed = feed;
    }
}