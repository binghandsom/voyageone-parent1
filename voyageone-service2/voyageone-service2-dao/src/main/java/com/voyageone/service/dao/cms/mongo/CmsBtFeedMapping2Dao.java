package com.voyageone.service.dao.cms.mongo;

import com.voyageone.base.dao.mongodb.BaseMongoChannelDao;
import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.service.model.cms.mongo.feed.mapping2.CmsBtFeedMapping2Model;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author jeff.duan, 16/6/6.
 * @version 2.1.0
 * @since 2.1.0
 */
@Repository
public class CmsBtFeedMapping2Dao extends BaseMongoChannelDao<CmsBtFeedMapping2Model> {

    /**
     * 根据feedCategory,获取该feedCategory默认的对应关系
     *
     * @param channelId    channel id
     * @param feedCategory feed category
     * @return 类目和属性对应关系
     */
    public CmsBtFeedMapping2Model findDefault(String channelId, String feedCategory) {

        JomgoQuery query = new JomgoQuery();

        // 为了防止categoryPath里有单引号, 这里外侧改为双引号
        query.setQuery(String.format("{ \"channelId\": \"%s\", \"feedCategoryPath\": \"%s\", defaultMapping: 1 }",
                channelId, feedCategory));

        return selectOneWithQuery(query,channelId);
    }

    /**
     * 根据feedCategory和mainCategoryId,获取key指定的唯一的一条对应关系
     *
     * @param channelId          channel id
     * @param feedCategory       feed category
     * @param mainCategoryIdPath main category id path
     * @return 类目和属性对应关系
     */
    public CmsBtFeedMapping2Model selectByKey(String channelId, String feedCategory, String mainCategoryIdPath) {
        String query = String.format("{ \"channelId\": \"%s\", \"feedCategoryPath\": \"%s\", \"mainCategoryPath\": \"%s\"}",
                channelId, feedCategory, mainCategoryIdPath);

        return selectOneWithQuery(query,channelId);
    }
}
