package com.voyageone.service.model.cms.mongo.product;

import com.voyageone.base.dao.mongodb.model.BaseMongoModel;
import com.voyageone.base.dao.mongodb.model.ChannelPartitionModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rex.wu on 2016/11/28.
 */
public class CmsBtCombinedProductModel extends BaseMongoModel {

    private String channelId;
    private Integer cartId; // 平台ID
    private String wuliubaoCode; // 物流宝后台商品编码
    private String numID; // 商品编码（numID）
    private String productName; // 组合套装商品名称
    private Integer status; // 套装商品状态
    private Integer platformStatus; // 套装商品平台状态
    private Integer active = 1; // 标记是否被删除，默认1表示未被删除，0表示被删除


    private List<CmsBtCombinedProductModel_Sku> skus;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public String getWuliubaoCode() {
        return wuliubaoCode;
    }

    public void setWuliubaoCode(String wuliubaoCode) {
        this.wuliubaoCode = wuliubaoCode;
    }

    public String getNumID() {
        return numID;
    }

    public void setNumID(String numID) {
        this.numID = numID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public List<CmsBtCombinedProductModel_Sku> getSkus() {
        if (skus == null) {
            skus = new ArrayList<CmsBtCombinedProductModel_Sku>();
        }
        return skus;
    }

    public void setSkus(List<CmsBtCombinedProductModel_Sku> skus) {
        this.skus = skus;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getPlatformStatus() {
        return platformStatus;
    }

    public void setPlatformStatus(Integer platformStatus) {
        this.platformStatus = platformStatus;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }
}
