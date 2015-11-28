package com.voyageone.cms.service.model;

import com.voyageone.base.dao.mongodb.model.BaseMongoModel;

import java.util.List;
import java.util.Map;

/**
 * Created by james.li on 2015/11/26.
 */
public class FeedCategoryModel extends BaseMongoModel {
    public static final String COLLECTION_NAME = "feed_category_info";

    private String channelId;
    private List<Map> categoryTree;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public List<Map> getCategoryTree() {
        return categoryTree;
    }

    public void setCategoryTree(List<Map> categoryTree) {
        this.categoryTree = categoryTree;
    }
}
