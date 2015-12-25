package com.voyageone.cms.service.model;

import com.voyageone.base.dao.mongodb.model.BaseMongoModel;

import java.util.List;

/**
 * cms_mt_feed_category_tree 的强类型模型.
 * 如果需要弱类型模型,请参看{@link CmsMtFeedCategoryTreeModel}
 *
 * @author Jonas, 12/25/15.
 * @version 2.0.0
 * @since 2.0.0
 */
public class CmsMtFeedCategoryTreeModelx extends BaseMongoModel {

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