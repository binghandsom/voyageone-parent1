package com.voyageone.service.dao.cms.mongo;

import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.service.model.cms.mongo.feed.CmsMtFeedCategoryTreeModel;
import com.voyageone.service.model.cms.mongo.feed.CmsMtFeedCategoryTreeModelx;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

/**
 * (mongo) cms_mt_feed_category_tree 表
 *
 * @author james.li, 2015/11/26.
 * @author Jonas, 2015-12-14.
 * @version 2.0.0
 * @since 2.0.0
 */
@Repository
public class CmsMtFeedCategoryTreeDao extends BaseMongoDao<CmsMtFeedCategoryTreeModel> {

    /**
     * 查询 Feed 类目
     *
     * @param channelId 渠道
     * @return Feed 类目的弱类型模型
     */
    public CmsMtFeedCategoryTreeModel selectFeedCategory(String channelId) {
        String query = "{\"channelId\":\"" + channelId + "\"}";
        return mongoTemplate.findOne(query, CmsMtFeedCategoryTreeModel.class, collectionName);
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
    public CmsMtFeedCategoryTreeModelx selectTopCategories(String channelId) {
        String query = "{\"channelId\":\"" + channelId + "\"}";
        String projection = "{'categoryTree.child':0}";
        return mongoTemplate.findOne(query, projection, CmsMtFeedCategoryTreeModelx.class);
    }

    /**
     * 查询 channelId 下 cid 为 categoryId 的顶级类目
     *
     * @param channelId  渠道
     * @param categoryId 类目 ID
     * @return 只包含目标类目的强类型模型
     */
    public CmsMtFeedCategoryTreeModelx selectTopCategory(String channelId, String categoryId) {
        String query = String.format("{channelId: '%s', 'categoryTree.cid': '%s'}", channelId, categoryId);
        String projection = "{'categoryTree.$':1}";
        return mongoTemplate.findOne(query, projection, CmsMtFeedCategoryTreeModelx.class);
    }

    /**
     * 查询 channelId 下的顶级类目信息
     *
     * @param channelId 渠道ID
     * @param topCategoryPath TOP CategoryPath
     * @return TOP Category
     */
    public CmsMtFeedCategoryTreeModel selectHasTrueChild(String channelId, String topCategoryPath) {

        int count = StringUtils.countMatches(topCategoryPath, "-");

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < count; i++)
            builder.append(".child");

        String query = String.format("{\"channelId\":\"%s\", \"categoryTree%s\":{$elemMatch:{\"path\": \"%s\", \"isChild\": 1}}}",
                channelId, builder, topCategoryPath);

        JomgoQuery jomgoQuery = new JomgoQuery();

        jomgoQuery.setQuery(query);

        jomgoQuery.setProjection("_id");

        return selectOneWithQuery(jomgoQuery);
    }
}
