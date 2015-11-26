package com.voyageone.cms.service.bean;

import com.voyageone.base.dao.mongodb.model.BaseMongoModel;

import java.util.List;

/**
 * Created by james.li on 2015/11/26.
 */
public class FeedCategoryBean extends BaseMongoModel {
    private String channelId;
    private List<FeedCategoryChildBean> categoryTree;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public List<FeedCategoryChildBean> getCategoryTree() {
        return categoryTree;
    }

    public void setCategoryTree(List<FeedCategoryChildBean> categoryTree) {
        this.categoryTree = categoryTree;
    }
}
