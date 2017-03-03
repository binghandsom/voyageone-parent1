package com.voyageone.service.dao.cms.mongo;

import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.service.model.cms.mongo.CmsBtCustomPropModel;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

/**
 * Created by james on 2017/2/21.
 */
@Repository
public class CmsBtCustomPropDao extends BaseMongoDao<CmsBtCustomPropModel> {

    public CmsBtCustomPropModel getCustomPropByCatChannel(String channelId, String orgChannelId, String cat){
        JongoQuery jongoQuery = new JongoQuery();
        jongoQuery.setQuery(new Criteria("cat").is(cat).and("channelId").is(channelId).and("orgChannelId").is(orgChannelId));
        return selectOneWithQuery(jongoQuery);
    }

    public WriteResult update(CmsBtCustomPropModel cmsBtCustomPropModel){
        return mongoTemplate.save(cmsBtCustomPropModel);
    }
}
