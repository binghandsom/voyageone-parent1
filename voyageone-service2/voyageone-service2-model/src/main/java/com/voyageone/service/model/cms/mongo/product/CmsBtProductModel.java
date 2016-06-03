package com.voyageone.service.model.cms.mongo.product;

import com.voyageone.base.dao.mongodb.model.ChannelPartitionModel;
import com.voyageone.common.configs.Enums.CartEnums;

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
    //在售平台
    private List<CmsBtProductModel_Carts> carts = new ArrayList<>();
    //店铺内类目
    private CmsBtProductModel_SellerCats sellerCats = new CmsBtProductModel_SellerCats();
    //groups
    private CmsBtProductGroupModel groups = new CmsBtProductGroupModel();
    //共通属性
    private CmsBtProductModel_Common common = new CmsBtProductModel_Common();
    //平台属性Map
    private Map<String, CmsBtProductModel_Platform_Cart> platforms = new HashMap<>();

    public CmsBtProductModel() {
    }

    public CmsBtProductModel(String channelId) {
        super(channelId);
    }

    //prodId
    public Long getProdId() {
        return prodId;
    }
    public void setProdId(Long prodId) {
        this.prodId = prodId;
    }

    //catId
    public String getCatId() {
        return catId;
    }
    public void setCatId(String catId) {
        this.catId = catId;
    }

    //catPath
    public String getCatPath() {
        return catPath;
    }
    public void setCatPath(String catPath) {
        this.catPath = catPath;
    }

    //orgChannelId
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

    //comment
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }

    //fields
    public CmsBtProductModel_Field getFields() {
        return fields;
    }
    public void setFields(CmsBtProductModel_Field fields) {
        this.fields = fields;
    }

    //skus
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

    //tags
    public List<String> getTags() {
        return tags;
    }
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    //freeTags
    public List<String> getFreeTags() {
        return freeTags;
    }

    public void setFreeTags(List<String> freeTags) {
        this.freeTags = freeTags;
    }

    //batchField
    public CmsBtProductModel_BatchField getBatchField() {
        return batchField;
    }
    public void setBatchField(CmsBtProductModel_BatchField batchField) {
        this.batchField = batchField;
    }

    //feed
    public CmsBtProductModel_Feed getFeed() {
        return feed;
    }
    public void setFeed(CmsBtProductModel_Feed feed) {
        this.feed = feed;
    }

    //carts
    public List<CmsBtProductModel_Carts> getCarts() {
        return carts;
    }
    public void setCarts(List<CmsBtProductModel_Carts> productCarts) {
        this.carts = productCarts;
    }

    //sellerCats
    public CmsBtProductModel_SellerCats getSellerCats() {
        return sellerCats;
    }
    public void setSellerCats(CmsBtProductModel_SellerCats sellerCats) {
        this.sellerCats = sellerCats;
    }

    //common
    public CmsBtProductModel_Common getCommon() {
        return common;
    }
    public void setCommon(CmsBtProductModel_Common common) {
        this.common = common;
    }

    //platforms
    public Map<String, CmsBtProductModel_Platform_Cart> getPlatforms() {
        return platforms;
    }
    public void setPlatforms(Map<String, CmsBtProductModel_Platform_Cart> platforms) {
        this.platforms = platforms;
    }


    private final static String  PLATFORM_CART_PRE = "P";
    //platform
    public CmsBtProductModel_Platform_Cart getPlatform(int cartId) {
        return platforms.get(PLATFORM_CART_PRE + cartId);
    }
    public void setPlatform(int cartId, CmsBtProductModel_Platform_Cart cart) {
        platforms.put(PLATFORM_CART_PRE + cartId, cart);
    }
    public CmsBtProductModel_Platform_Cart getPlatform(CartEnums.Cart cartType) {
        return platforms.get(PLATFORM_CART_PRE + cartType.getId());
    }
    public void setPlatform(CartEnums.Cart cartType, CmsBtProductModel_Platform_Cart cart) {
        platforms.put(PLATFORM_CART_PRE + cartType.getId(), cart);
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