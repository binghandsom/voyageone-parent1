package com.voyageone.service.model.cms.mongo.product;

import com.voyageone.base.dao.mongodb.model.ChannelPartitionModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private CmsBtProductModel_Field fields = new CmsBtProductModel_Field();
    private CmsBtProductGroupModel groups = new CmsBtProductGroupModel();
    private List<CmsBtProductModel_Sku> skus = new ArrayList<>();
    private List<String> tags = new ArrayList<>();
    private List<String> freeTags = new ArrayList<>();
    private CmsBtProductModel_BatchField batchField = new CmsBtProductModel_BatchField();
    private CmsBtProductModel_Feed feed = new CmsBtProductModel_Feed();
    private List<CmsBtProductModel_Carts> carts = new ArrayList<>();
    private Map sales = new HashMap<>();

    public String getOrgChannelId() {
        return orgChannelId == null ? this.channelId : orgChannelId;
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

    //code 产品code
    public List<CmsBtProductModel_Carts> getCarts() {
        return carts;
    }

    public void setCarts(List<CmsBtProductModel_Carts> productCarts) {
        this.carts = productCarts;
    }

    public Map getSales() {
        return sales;
    }

    public void setSales(Map sales) {
        this.sales = sales;
    }
}