package com.voyageone.service.model.cms.mongo.feed;

import com.voyageone.base.dao.mongodb.model.BaseMongoModel;

import java.util.List;
import java.util.Map;

/**
 * cms_mt_feed_category_tree 的弱类型模型.
 * 如果需要强类型模型,请参看{@link CmsMtFeedCategoryTreeModelx}
 *
 * @author james.li, 2015/11/26.
 * @author Jonas, 2015-12-11 19:17:18
 * @version 2.0.0
 * @since 2.0.0
 */
public class CmsMtFeedCategoryTreeModel extends BaseMongoModel {

    private String channelId;

    private List<Map<String, Object>> categoryTree;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public List<Map<String, Object>> getCategoryTree() {
        return categoryTree;
    }

    public void setCategoryTree(List<Map<String, Object>> categoryTree) {
        this.categoryTree = categoryTree;
    }
}
