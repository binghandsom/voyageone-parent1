package com.voyageone.service.dao.cms.mongo;

import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductLogModel;
import org.springframework.stereotype.Repository;

@Repository
public class CmsBtProductLogDao extends BaseMongoDao {

    @Override
    public Class getEntityClass() {
        return CmsBtProductLogModel.class;
    }
}
