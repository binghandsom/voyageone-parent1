package com.voyageone.service.dao.cms.mongo;

import com.voyageone.base.dao.mongodb.BaseMongoChannelDao;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.service.model.cms.mongo.product.CmsBtTempCategoryModel;
import org.springframework.stereotype.Repository;


/**
 * @author Ethan Shi
 * @version 2.1.0
 */
@Repository
public class CmsBtTempCategoryDao extends BaseMongoChannelDao<CmsBtTempCategoryModel> {

    public CmsBtTempCategoryModel selectTempCategoryModelById(String channelId, Integer id) {

        JongoQuery query = new JongoQuery();
        query.setQuery("{\"id\": #}");
        query.setParameters(id);
        return selectOneWithQuery(query, channelId);
    }
}
