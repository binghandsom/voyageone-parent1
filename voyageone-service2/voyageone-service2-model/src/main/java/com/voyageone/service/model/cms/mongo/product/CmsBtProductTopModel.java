package com.voyageone.service.model.cms.mongo.product;
import com.voyageone.base.dao.mongodb.model.ChannelPartitionModel;

import java.util.List;

public class CmsBtProductTopModel extends ChannelPartitionModel {
    private String channelId;
    private String catId;//分类
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

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
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
}

