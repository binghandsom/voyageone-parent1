package com.voyageone.cms.service.dao.mongodb;

import com.fasterxml.jackson.databind.JsonNode;
import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.cms.service.model.CmsMtFeedCategoryTreeModel;
import org.springframework.stereotype.Repository;

/**
 * (mongo) cms_mt_feed_category_tree 表
 * @author james.li, 2015/11/26.
 * @author Jonas, 2015-12-14.
 * @version 2.0.0
 * @since 2.0.0
 */
@Repository
public class CmsMtFeedCategoryTreeDao extends BaseMongoDao {

    @Override
    public Class getEntityClass() {
        return CmsMtFeedCategoryTreeModel.class;
    }

    public CmsMtFeedCategoryTreeModel selectFeedCategory(String channelId) {
        String query = "{\"channelId\":\"" + channelId + "\"}";
        return mongoTemplate.findOne(query, CmsMtFeedCategoryTreeModel.class, collectionName);
    }

    public JsonNode selectFeedCategoryNode(String channelId) {
        String query = "{\"channelId\":\"" + channelId + "\"}";
        return mongoTemplate.findOne(query, JsonNode.class, collectionName);
    }
}
