package com.voyageone.service.model.cms.mongo.product;

import com.voyageone.base.dao.mongodb.model.ChannelPartitionModel;
import com.voyageone.common.configs.Enums.CartEnums;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * {@link CmsBtProductModel} 的商品Model
 *
 * @author chuanyu.liang, 12/11/15
 * @version 2.0.0
 * @since 2.0.0
 */
public class OldCmsBtProductModel extends ChannelPartitionModel {

    private Long prodId;
    private String catId;
    private String catPath;
    private String orgChannelId;
    //商品lock
    private String lock = "";
    //商品注释
    private String comment = "";

    //fields
    private OldCmsBtProductModel_Field fields = new OldCmsBtProductModel_Field();
    //SKU
    private List<OldCmsBtProductModel_Sku> skus = new ArrayList<>();
    //标签
    private List<String> tags = new ArrayList<>();
    //品牌方标签
    private List<String> freeTags = new ArrayList<>();
    private CmsBtProductModel_BatchField batchField = new CmsBtProductModel_BatchField();
    //品牌方数据
    private CmsBtProductModel_Feed feed = new CmsBtProductModel_Feed();
    //店铺内类目
    private CmsBtProductModel_SellerCats sellerCats = new CmsBtProductModel_SellerCats();
    //共通属性
    private CmsBtProductModel_Common common = new CmsBtProductModel_Common();
    //平台属性Map
    private Map<String, CmsBtProductModel_Platform_Cart> platforms = new HashMap<>();
    //销售数据统计
    private OldCmsBtProductModel_Sales sales = new OldCmsBtProductModel_Sales();

    public OldCmsBtProductModel() {
    }

    public OldCmsBtProductModel(String channelId) {
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
    @Deprecated // fields属性现在应该从common里取得
    public OldCmsBtProductModel_Field getFields() {
        return fields;
    }
    @Deprecated
    public void setFields(OldCmsBtProductModel_Field fields) {
        this.fields = fields;
    }

    //skus
    @Deprecated // skus属性现在应该从common里取得
    public List<OldCmsBtProductModel_Sku> getSkus() {
        return skus;
    }
    @Deprecated
    public void setSkus(List<OldCmsBtProductModel_Sku> skus) {
        this.skus = skus;
    }
    @Deprecated
    public OldCmsBtProductModel_Sku getSku(String skuCode) {
        if (skuCode != null && this.skus != null) {
            for (OldCmsBtProductModel_Sku sku : skus) {
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

    /**
     * 返回非空CmsBtProductModel_Common对象，
     */
    public CmsBtProductModel_Common getCommonNotNull() {
        if (common == null) {
            return new CmsBtProductModel_Common();
        }
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
        if (platforms == null) {
            return null;
        }
        return platforms.get(PLATFORM_CART_PRE + cartId);
    }
    public void setPlatform(int cartId, CmsBtProductModel_Platform_Cart cart) {
        cart.setCartId(cartId);
        platforms.put(PLATFORM_CART_PRE + cartId, cart);
    }
    public CmsBtProductModel_Platform_Cart getPlatform(CartEnums.Cart cartType) {
        if (platforms == null) {
            return null;
        }
        return platforms.get(PLATFORM_CART_PRE + cartType.getId());
    }
    public void setPlatform(CartEnums.Cart cartType, CmsBtProductModel_Platform_Cart cart) {
        platforms.put(PLATFORM_CART_PRE + cartType.getId(), cart);
    }

    public void platformClear(){
        platforms = new HashMap<>();
    }

    public OldCmsBtProductModel_Sales getSales() {
        return sales;
    }

    public void setSales(OldCmsBtProductModel_Sales sales) {
        this.sales = sales;
    }

    /**
     * 取得本商品销售平台列表
     * 注意: 此方法为逻辑取得，不表示CmsBtProductModel含有"cartIdList"这样一个字段
     */
    public List<Integer> getCartIdList() {
        if (platforms == null || platforms.isEmpty()) {
            return new ArrayList<>(0);
        }
        return ((Set<String>) platforms.keySet()).stream().map(cartKey -> NumberUtils.toInt(cartKey.substring(1))).collect(Collectors.toList());
    }
}