package com.voyageone.service.dao.cms.mongo;

import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.service.model.cms.mongo.CmsBtCustomPropModel;
import com.voyageone.service.model.cms.mongo.CmsBtTranslateModel;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

/**
 *
 * Created by james on 2017/2/27.
 */
@Repository
public class CmsBtTranslateDao extends BaseMongoDao<CmsBtTranslateModel> {

    public CmsBtTranslateModel get(String channelId, Integer customPropType, String name){
        JongoQuery jongoQuery = new JongoQuery();
        jongoQuery.setQuery(new Criteria("channelId").is(channelId).and("type").is(customPropType).and("name").is(name));
        return selectOneWithQuery(jongoQuery);
    }
}
