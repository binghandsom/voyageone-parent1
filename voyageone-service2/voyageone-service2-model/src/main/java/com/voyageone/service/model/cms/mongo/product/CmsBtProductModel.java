package com.voyageone.service.model.cms.mongo.product;

import com.voyageone.base.dao.mongodb.model.ChannelPartitionModel;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link CmsBtProductModel} 的商品Model
 *
 * @author chuanyu.liang, 12/11/15
 * @version 2.0.0
 * @since 2.0.0
 */
public class CmsBtProductModel extends ChannelPartitionModel {

    private Long prodId;
    private String catId;
    private String catPath;
    private String orgChannelId;
    //商品lock
    private String lock = "";
    //商品注释
    private String comment = "";

    //fields
    private CmsBtProductModel_Field fields = new CmsBtProductModel_Field();
    //SKU
    private List<CmsBtProductModel_Sku> skus = new ArrayList<>();
    //标签
    private List<String> tags = new ArrayList<>();
    //品牌方标签
    private List<String> freeTags = new ArrayList<>();
    private CmsBtProductModel_BatchField batchField = new CmsBtProductModel_BatchField();
    //品牌方数据
    private CmsBtProductModel_Feed feed = new CmsBtProductModel_Feed();
    //销售平台
    private List<CmsBtProductModel_Carts> carts = new ArrayList<>();
    //店铺内类目
    private CmsBtProductModel_SellerCats sellerCats = new CmsBtProductModel_SellerCats();
    //groups
    private CmsBtProductGroupModel groups = new CmsBtProductGroupModel();
    //共通属性
    private CmsBtProductModel_Common common = new CmsBtProductModel_Common();
    //平台属性
    private CmsBtProductModel_Platform platform = new CmsBtProductModel_Platform();

    public CmsBtProductModel() {
    }

    public CmsBtProductModel(String channelId) {
        super(channelId);
    }

    public Long getProdId() {
        return prodId;
    }

    public void setProdId(Long prodId) {
        this.prodId = prodId;
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

    public void setCatPath(String catPath) {
        this.catPath = catPath;
    }

    public String getOrgChannelId() {
        return orgChannelId == null ? this.channelId : orgChannelId;
    }

    public void setOrgChannelId(String orgChannelId) {
        this.orgChannelId = orgChannelId;
    }

    //lock商品
    public String getLock() {
        return lock;
    }

    public void setLock(String lock) {
        this.lock = lock;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public CmsBtProductModel_Field getFields() {
        return fields;
    }

    public void setFields(CmsBtProductModel_Field fields) {
        this.fields = fields;
    }

    public List<CmsBtProductModel_Sku> getSkus() {
        return skus;
    }

    public void setSkus(List<CmsBtProductModel_Sku> skus) {
        this.skus = skus;
    }

    public CmsBtProductModel_Sku getSku(String skuCode) {
        if (skuCode != null && this.skus != null) {
            for (CmsBtProductModel_Sku sku : skus) {
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

    public List<String> getFreeTags() {
        return freeTags;
    }

    public void setFreeTags(List<String> freeTags) {
        this.freeTags = freeTags;
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

    public List<CmsBtProductModel_Carts> getCarts() {
        return carts;
    }

    public void setCarts(List<CmsBtProductModel_Carts> productCarts) {
        this.carts = productCarts;
    }

    public CmsBtProductModel_Platform getPlatform() {
        return platform;
    }

    public void setPlatform(CmsBtProductModel_Platform platform) {
        this.platform = platform;
    }

    public CmsBtProductModel_SellerCats getSellerCats() {
        return sellerCats;
    }

    public void setSellerCats(CmsBtProductModel_SellerCats sellerCats) {
        this.sellerCats = sellerCats;
    }

    public CmsBtProductModel_Common getCommon() {
        return common;
    }

    public void setCommon(CmsBtProductModel_Common common) {
        this.common = common;
    }

    /**
     * @see com.voyageone.service.bean.cms.product.CmsBtProductBean
     */
    @Deprecated
    public CmsBtProductGroupModel getGroups() {
        return groups;
    }

    /**
     * @see com.voyageone.service.bean.cms.product.CmsBtProductBean
     */
    @Deprecated
    public void setGroups(CmsBtProductGroupModel groups) {
        this.groups = groups;
    }
}