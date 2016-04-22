package com.voyageone.service.dao.cms.mongo;

import com.voyageone.base.dao.mongodb.BaseMongoChannelDao;
import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.service.model.cms.mongo.feed.CmsMtFeedCategoryTreeModel;
import com.voyageone.service.model.cms.mongo.feed.CmsMtFeedCategoryTreeModelx;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * (mongo) cms_mt_feed_category_tree 表
 *
 * @author james.li, 2015/11/26.
 * @author Jonas, 2015-12-14.
 * @version 2.0.0
 * @since 2.0.0
 */
@Repository
public class CmsMtFeedCategoryTreeDao extends BaseMongoChannelDao<CmsMtFeedCategoryTreeModel> {

    /**
     * 查询 Feed 类目
     *
     * @param channelId 渠道
     * @return Feed 类目的弱类型模型
     */
    public CmsMtFeedCategoryTreeModel selectFeedCategoryByCategory(String channelId, String category) {
        String query = "{\"catName\":\"" + category + "\"}";
        return selectOneWithQuery(query, channelId);
    }
    /**
     * 查询 Feed 类目
     *
     * @param channelId 渠道
     * @return Feed 类目的弱类型模型
     */
    public CmsMtFeedCategoryTreeModel selectFeedCategoryByCategoryId(String channelId, String categoryId) {
        String query = "{\"catId\":\"" + categoryId + "\"}";
        return selectOneWithQuery(query, channelId);
    }

    public List<CmsMtFeedCategoryTreeModel> selectFeedAllCategory(String channelId) {
        String query = "{}";
        return select(query, channelId);
    }

    /**
     * 查询 Feed 类目
     *
     * @param channelId 渠道
     * @return Feed 类目的强类型模型
     */
    public CmsMtFeedCategoryTreeModelx selectFeedCategoryx(String channelId) {
        String query = "{\"channelId\":\"" + channelId + "\"}";
        return mongoTemplate.findOne(query, CmsMtFeedCategoryTreeModelx.class);
    }

    /**
     * 查询 channelId 下的顶级类目信息
     *
     * @param channelId 渠道
     * @return Feed 类目的强类型模型
     */
    public List<CmsMtFeedCategoryTreeModel> selectTopCategories(String channelId) {
        String query = "{\"channelId\":\"" + channelId + "\"}";
        String projection = "{'children':0}";
        return selectWithProjection(query, projection, channelId);
    }

}
