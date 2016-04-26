package com.voyageone.service.dao.cms.mongo;

import com.voyageone.base.dao.mongodb.BaseMongoChannelDao;
import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedMappingModel;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zhujiaye, 15/12/7.
 * @author Jonas, 2015-12-10 14:31:11
 * @version 2.0.0
 * @since 2.0.0
 */
@Repository
public class CmsBtFeedMappingDao extends BaseMongoChannelDao<CmsBtFeedMappingModel> {

    /**
     * 根据feedCategory,获取该feedCategory默认的对应关系
     *
     * @param channelId    channel id
     * @param feedCategory feed category
     * @param withProps    是否输出属性信息
     * @return 类目和属性对应关系
     */
    public CmsBtFeedMappingModel findDefault(String channelId, String feedCategory, boolean withProps) {

        JomgoQuery query = new JomgoQuery();

        // 为了防止categoryPath里有单引号, 这里外侧改为双引号
        query.setQuery(String.format("{ \"channelId\": \"%s\", \"feedCategoryPath\": \"%s\", defaultMapping: 1 }",
                channelId, feedCategory));

        if (!withProps)
            query.setProjection("{'props':0}");

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
    public CmsBtFeedMappingModel selectByKey(String channelId, String feedCategory, String mainCategoryIdPath) {
        String query = String.format("{ \"channelId\": \"%s\", \"feedCategoryPath\": \"%s\", \"mainCategoryPath\": \"%s\"}",
                channelId, feedCategory, mainCategoryIdPath);

        return selectOneWithQuery(query,channelId);
    }

    /**
     * 查询 selChannelId 渠道下, 类目路径包含 topCategoryPath 的 Mapping
     *
     * @param selChannelId    渠道 ID
     * @return 一组不带有 Prop Mapping 信息的类目 Mapping
     */
    public List<CmsBtFeedMappingModel> findMappingByChannelId(String selChannelId) {

        String strQuery = String.format("{\"channelId\": \"%s\"}", selChannelId);

        String projection = "{\"props\": 0}";

        return selectWithProjection(strQuery, projection,selChannelId);
    }

    /**
     * 查询 selChannelId 渠道下的 Mapping
     *
     * @param selChannelId    渠道 ID
     * @return 一组不带有 Prop Mapping 信息的类目 Mapping
     */
    public List<CmsBtFeedMappingModel> findMappingWithoutProps(String selChannelId) {

        String strQuery = String.format("{\"channelId\": \"%s\"}", selChannelId);

        String projection = "{\"props\": 0}";

        return selectWithProjection(strQuery, projection,selChannelId);
    }

    /**
     * 查询主类目为 mainCategoryPath 的默认 Mapping
     *
     * @param channelId        渠道 ID
     * @param mainCategoryPath 主数据类目路径
     * @return Mapping 模型
     */
    public CmsBtFeedMappingModel findDefaultMainMapping(String channelId, String mainCategoryPath) {

        String query = String.format("{ 'channelId': '%s', 'mainCategoryPath': '%s', 'defaultMain': 1 }",
                channelId, mainCategoryPath);

        return selectOneWithQuery(query,channelId);
    }

    public CmsBtFeedMappingModel findOne(ObjectId objectId,String channelId) {

        JomgoQuery jomgoQuery = new JomgoQuery();

        jomgoQuery.setObjectId(objectId);

        return selectOneWithQuery(jomgoQuery,channelId);
    }

    public List<CmsBtFeedMappingModel> findMappingsWithoutProps(String feedCategoryPath, String selChannelId) {

        String strQuery = String.format("{\"channelId\":\"%s\",\"feedCategoryPath\":\"%s\"}",
                selChannelId, feedCategoryPath);

        String projection = "{\"props\": 0}";

        return selectWithProjection(strQuery, projection,selChannelId);
    }

    public CmsBtFeedMappingModel findOneWithoutProps(ObjectId mappingId,String channelId) {

        JomgoQuery jomgoQuery = new JomgoQuery();
        jomgoQuery.setObjectId(mappingId);
        jomgoQuery.setProjection("{\"props\": 0}");

        return selectOneWithQuery(jomgoQuery,channelId);
    }
}
