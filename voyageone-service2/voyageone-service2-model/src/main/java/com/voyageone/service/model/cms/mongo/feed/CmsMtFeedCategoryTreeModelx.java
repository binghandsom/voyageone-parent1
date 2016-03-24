package com.voyageone.service.model.cms.mongo.feed;

import com.voyageone.base.dao.mongodb.model.BaseMongoModel;
import com.voyageone.base.dao.mongodb.support.MongoCollection;

import java.util.List;

/**
 * cms_mt_feed_category_tree 的强类型模型.
 * 如果需要弱类型模型,请参看{@link CmsMtFeedCategoryTreeModel}
 *
 * @author Jonas, 12/25/15.
 * @version 2.0.0
 * @since 2.0.0
 */
@MongoCollection("cms_mt_feed_category_tree")
public class CmsMtFeedCategoryTreeModelx extends BaseMongoModel {

    private String channelId;

    private List<CmsMtFeedCategoryModel> categoryTree;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public List<CmsMtFeedCategoryModel> getCategoryTree() {
        return categoryTree;
    }

    public void setCategoryTree(List<CmsMtFeedCategoryModel> categoryTree) {
        this.categoryTree = categoryTree;
    }
}