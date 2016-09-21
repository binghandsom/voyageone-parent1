package com.voyageone.service.dao.cms.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.BaseMongoChannelDao;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.service.model.cms.mongo.CmsBtSxCnInfoModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Repository
public class CmsBtSxCnInfoDao extends BaseMongoChannelDao<CmsBtSxCnInfoModel> {

    public List<CmsBtSxCnInfoModel> selectWaitingPublishData(String channelId, int limit) {
        JongoQuery query = new JongoQuery();
        query.setQuery("{\"publishFlg\":#}");
        query.setParameters(0);
        query.setLimit(limit);
        query.setSort("{\"created\":1}");

        return select(query, channelId);
    }

    public CmsBtSxCnInfoModel selectInfoByGroupId(String channelId, Long groupId, int publishFlg) {
        JongoQuery query = new JongoQuery();
        query.setQuery("{\"groupId\":#, \"publishFlg\":#}");
        query.setParameters(groupId, publishFlg);
        return selectOneWithQuery(query, channelId);
    }

    public WriteResult updatePublishFlg(String channelId, List<Long> listGroupId, int publishFlg, String modifier) {
        JongoUpdate updateQuery = new JongoUpdate();
        updateQuery.setQuery("{\"groupId\":{$in:#}}");
        updateQuery.setQueryParameters(listGroupId);
        updateQuery.setUpdate("{$set:{\"publishFlg\": #, \"modifier\": #, \"modified\": #}}");
        updateQuery.setUpdateParameters(publishFlg, modifier, DateTimeUtil.getNowTimeStamp());

        return updateMulti(updateQuery, channelId);
    }

}
