package com.voyageone.service.model.cms.mongo.product;

import com.voyageone.base.dao.mongodb.model.ChannelPartitionModel;
import com.voyageone.common.configs.Enums.CartEnums;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * {@link CmsBtProductModel} 的商品Model
 *
 * @author linanbin on 6/29/2016
 * @version 2.2.0
 * @author chuanyu.liang, 12/11/15
 * @version 2.0.0
 * @since 2.2.0
 */
public class CmsBtProductModel extends ChannelPartitionModel {

    /** 定义platforms的固定文字 **/
    private final static String  PLATFORM_CART_PRE = "P";

    // 原始channelId
    private String orgChannelId;
    // 产品Id
    private Long prodId;
    //商品lock
    private String lock = "";
    //商品注释
    private String comment = "";
    //共通属性
    private CmsBtProductModel_Common common = new CmsBtProductModel_Common();
    //平台属性Map
    private Map<String, CmsBtProductModel_Platform_Cart> platforms = new HashMap<>();
    //品牌方数据
    private CmsBtProductModel_Feed feed = new CmsBtProductModel_Feed();
    //销售数据统计
    private CmsBtProductModel_Sales sales = new CmsBtProductModel_Sales();
    //标签
    private List<String> tags = new ArrayList<>();
    //品牌方标签
    private List<String> freeTags = new ArrayList<>();

    public CmsBtProductModel() {
    }

    public CmsBtProductModel(String channelId) {
        super(channelId);
    }

    //orgChannelId
    public String getOrgChannelId() {
        return orgChannelId == null ? this.channelId : orgChannelId;
    }
    public void setOrgChannelId(String orgChannelId) {
        this.orgChannelId = orgChannelId;
    }

    //prodId
    public Long getProdId() {
        return prodId;
    }
    public void setProdId(Long prodId) {
        this.prodId = prodId;
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
    //platform
    /**
     * 根据cartId返回对应的platforms.Pxx数据
     * @param cartId 平台Id
     * @return CmsBtProductModel_Platform_Cart
     */
    public CmsBtProductModel_Platform_Cart getPlatform(Integer cartId) {
        if (platforms == null) {
            return null;
        }
        return platforms.get(PLATFORM_CART_PRE + cartId);
    }
    /**
     * 根据平台Id设置对应的平台数据信息
     * @param cartId 平台Id
     * @param cart CmsBtProductModel_Platform_Cart
     */
    public void setPlatform(Integer cartId, CmsBtProductModel_Platform_Cart cart) {
        cart.setCartId(cartId);
        platforms.put(PLATFORM_CART_PRE + cartId, cart);
    }
    /**
     * 根据cartType返回对应的platforms.Pxx数据
     * @param cartType 平台类型
     * @return CmsBtProductModel_Platform_Cart
     */
    public CmsBtProductModel_Platform_Cart getPlatform(CartEnums.Cart cartType) {
        if (platforms == null) {
            return null;
        }
        return platforms.get(PLATFORM_CART_PRE + cartType.getId());
    }
    /**
     * 根据平台类型设置对应的平台数据信息
     * @param cartType 平台类型
     * @param cart CmsBtProductModel_Platform_Cart
     */
    public void setPlatform(CartEnums.Cart cartType, CmsBtProductModel_Platform_Cart cart) {
        platforms.put(PLATFORM_CART_PRE + cartType.getId(), cart);
    }
    /**
     * 清空所有的平台信息
     */
    public void platformsClear(){
        this.platforms = new HashMap<>();
    }
    /**
     * 根据平台Id清空该平台的platforms数据
     * @param cartId 平台Id
     */
    public void platformClearByCartId (Integer cartId) {
        platforms.put(PLATFORM_CART_PRE + cartId, new CmsBtProductModel_Platform_Cart());
    }

    /**
     * 如果对应的平台数据存在则返回平台数据,否则返回一个空的平台数据对象
     * @param cartId 平台Id
     * @return
     */
    public CmsBtProductModel_Platform_Cart getPlatformNotNull(int cartId) {
        if (platforms == null) {
            return new CmsBtProductModel_Platform_Cart();
        }
        CmsBtProductModel_Platform_Cart pcObj = platforms.get(PLATFORM_CART_PRE + cartId);
        if (pcObj == null) {
            return new CmsBtProductModel_Platform_Cart();
        }
        return pcObj;
    }


    //feed
    public CmsBtProductModel_Feed getFeed() {
        return feed;
    }
    public void setFeed(CmsBtProductModel_Feed feed) {
        this.feed = feed;
    }

    // sales
    public CmsBtProductModel_Sales getSales() {
        return sales;
    }
    public void setSales(CmsBtProductModel_Sales sales) {
        this.sales = sales;
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

    /**
     * 取得本商品销售平台列表
     * 注意: 此方法为逻辑取得，不表示CmsBtProductModel含有"cartIdList"这样一个字段
     */
    public List<Integer> getCartIdList() {
        if (platforms == null || platforms.isEmpty()) {
            return new ArrayList<>(0);
        }
        return ((Set<String>) platforms.keySet()).stream().map(cartKey -> NumberUtils.toInt(cartKey.substring(1))).filter(cartKey -> cartKey != 0).collect(Collectors.toList());
    }
}