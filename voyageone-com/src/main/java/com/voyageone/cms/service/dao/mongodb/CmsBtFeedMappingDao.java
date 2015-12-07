package com.voyageone.cms.service.dao.mongodb;

import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.cms.service.model.CmsBtFeedMappingModel;
import org.springframework.stereotype.Repository;

/**
 * Created by zhujiaye on 15/12/7.
 */

@Repository
public class CmsBtFeedMappingDao extends BaseMongoDao {

    @Override
    public Class getEntityClass() {
        return CmsBtFeedMappingModel.class;
    }

    /**
     * 根据feedCategory,获取该feedCategory默认的对应关系
     * @param channelId channel id
     * @param feedCategory feed category
     * @return 类目和属性对应关系
     */
    public CmsBtFeedMappingModel selectByDefault(String channelId, String feedCategory) {
        // 除了传入的参数之外,还需要一个条件,就是default=1
        String query = String.format("{ scope.channelId: '%s', scope.feedCategory: %s, defaultMapping: 1}", channelId, feedCategory);

        return selectOneWithQuery(query, channelId);
    }

    /**
     * 根据feedCategory和mainCategoryId,获取key指定的唯一的一条对应关系
     * @param channelId channel id
     * @param feedCategory feed category
     * @param mainCategoryId main category id
     * @return 类目和属性对应关系
     */
    public CmsBtFeedMappingModel selectByKey(String channelId, String feedCategory, String mainCategoryId) {
        String query = String.format("{ scope.channelId: '%s', scope.feedCategory: '%s', scope.mainCategoryId: '%s'}", channelId, feedCategory, mainCategoryId);

        return selectOneWithQuery(query, channelId);
    }
}
