package com.voyageone.service.model.cms.mongo.search;

import com.voyageone.base.dao.mongodb.model.BaseMongoModel;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * CMS检索和下载项分析Model
 *
 * @Author rex.wu
 * @Create 2017-05-12 11:33
 */
public class CmsBtSearchItemModel extends BaseMongoModel {

    /**
     * 渠道
     */
    private String channelId;
    /**
     * 检索/下载条件项
     */
    private List<CmsBtSearchItemModel_SearchItem> searchItems;

    /**
     * 用,拼接的检索/下载条件项
     */
    private String searchItemVal;
    /**
     * 排序条件项
     */
    private List<String> sortItems;
    /**
     * 自定义列项
     */
    private Map<String, List<String>> custColumnItems;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public List<CmsBtSearchItemModel_SearchItem> getSearchItems() {
        return searchItems;
    }

    public void setSearchItems(List<CmsBtSearchItemModel_SearchItem> searchItems) {
        this.searchItems = searchItems;
    }

    public String getSearchItemVal() {
        return searchItemVal;
    }

    public void setSearchItemVal(String searchItemVal) {
        this.searchItemVal = searchItemVal;
    }

    public List<String> getSortItems() {
        return sortItems;
    }

    public void setSortItems(List<String> sortItems) {
        this.sortItems = sortItems;
    }

    public Map<String, List<String>> getCustColumnItems() {
        return custColumnItems;
    }

    public void setCustColumnItems(Map<String, List<String>> custColumnItems) {
        this.custColumnItems = custColumnItems;
    }
}
