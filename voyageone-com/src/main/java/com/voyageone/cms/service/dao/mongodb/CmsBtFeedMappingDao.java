package com.voyageone.cms.service.dao.mongodb;

import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.cms.service.model.CmsBtFeedMappingModel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zhujiaye, 15/12/7.
 * @author Jonas, 2015-12-10 14:31:11
 * @version 2.0.0
 * @since 2.0.0
 */
@Repository
public class CmsBtFeedMappingDao extends BaseMongoDao {

    @Override
    public Class getEntityClass() {
        return CmsBtFeedMappingModel.class;
    }

    /**
     * 查询渠道的所有类目匹配关系
     *
     * @param channelId 渠道
     * @return 类目的 mapping 关系
     */
    public List<CmsBtFeedMappingModel> selectCategoryMappingByChannel(String channelId) {

        return selectWithProjection(
                String.format("{ scope.channelId: '%s' }", channelId),
                "{scope:1,defaultMapping:1,defaultMain:1,matchOver:1}");
    }

    /**
     * 根据feedCategory,获取该feedCategory默认的对应关系
     *
     * @param channelId    channel id
     * @param feedCategory feed category
     * @return 类目和属性对应关系
     */
    public CmsBtFeedMappingModel selectByDefault(String channelId, String feedCategory) {
        // 除了传入的参数之外,还需要一个条件,就是default=1
        String query = String.format("{ scope.channelId: '%s', scope.feedCategoryPath: %s, defaultMapping: 1}", channelId, feedCategory);

        return selectOneWithQuery(query, channelId);
    }

    /**
     * 根据feedCategory和mainCategoryId,获取key指定的唯一的一条对应关系
     *
     * @param channelId          channel id
     * @param feedCategory       feed category
     * @param mainCategoryIdPath main category id path
     * @return 类目和属性对应关系
     */
    public CmsBtFeedMappingModel selectByKey(String channelId, String feedCategory, String mainCategoryIdPath) {
        String query = String.format("{ scope.channelId: '%s', scope.feedCategoryPath: '%s', scope.mainCategoryPath: '%s'}",
                channelId, feedCategory, mainCategoryIdPath);

        return selectOneWithQuery(query);
    }
}
