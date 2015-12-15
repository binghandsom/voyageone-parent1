package com.voyageone.cms.service.model;

import com.voyageone.base.dao.mongodb.model.BaseMongoModel;

import java.util.List;

/**
 * cms_mt_feed_category_tree (mongodb)
 * @author james.li, 2015/11/26.
 * @author Jonas, 2015-12-11 19:17:18
 * @version 2.0.0
 * @since 2.0.0
 */
public class CmsMtFeedCategoryTreeModel extends BaseMongoModel {

    private String channelId;

    private List<CmsFeedCategoryModel> categoryTree;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public List<CmsFeedCategoryModel> getCategoryTree() {
        return categoryTree;
    }

    public void setCategoryTree(List<CmsFeedCategoryModel> categoryTree) {
        this.categoryTree = categoryTree;
    }
}
