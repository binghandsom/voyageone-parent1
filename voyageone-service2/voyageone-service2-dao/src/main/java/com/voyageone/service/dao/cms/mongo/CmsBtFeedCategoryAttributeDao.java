package com.voyageone.service.dao.cms.mongo;

import com.voyageone.base.dao.mongodb.BaseMongoChannelDao;
import com.voyageone.service.model.cms.mongo.feed.CmsMtFeedAttributesModel;
import org.springframework.stereotype.Repository;

/**
 * @author james.li on 2016/4/18.
 * @version 2.0.0
 */
@Repository
public class CmsBtFeedCategoryAttributeDao extends BaseMongoChannelDao<CmsMtFeedAttributesModel> {
    public CmsMtFeedAttributesModel selectCategoryAttributeByCatId(String channelId, String catId){
        String query = "{\"catId\":\"" + catId + "\"}";
        return selectOneWithQuery(query, channelId);
    }
}
