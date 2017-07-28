package com.voyageone.service.model.cms.mongo.product;
import com.voyageone.base.dao.mongodb.model.ChannelPartitionModel;

import java.util.List;

public class CmsBtProductTopModel extends ChannelPartitionModel {
    private String channelId;
    private String sellerCatId;//店铺内分类
    private Integer cartId;

    public String getSellerCatId() {
        return sellerCatId;
    }

    public void setSellerCatId(String sellerCatId) {
        this.sellerCatId = sellerCatId;
    }

    private Long productTopId;
    private List<String> productCodeList;//置顶列表
    private String sortColumnName;// 排序列名称
    private int sortType;//排序类型  1：升序  -1：降序

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public Long getProductTopId() {
        return productTopId;
    }

    public void setProductTopId(Long productTopId) {
        this.productTopId = productTopId;
    }

    public List<String> getProductCodeList() {
        return productCodeList;
    }

    public void setProductCodeList(List<String> productCodeList) {
        this.productCodeList = productCodeList;
    }

    public String getSortColumnName() {
        return sortColumnName;
    }

    public void setSortColumnName(String sortColumnName) {
        this.sortColumnName = sortColumnName;
    }

    public int getSortType() {
        return sortType;
    }

    public void setSortType(int sortType) {
        this.sortType = sortType;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }
}

