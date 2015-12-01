package com.voyageone.cms.service.dao.mongodb;

import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.cms.service.model.CmsBtProductLogModel;
import org.springframework.stereotype.Repository;

@Repository
public class CmsBtProductLogDao extends BaseMongoDao {

    public CmsBtProductLogDao() {
        super.entityClass = CmsBtProductLogModel.class;
    }
}
