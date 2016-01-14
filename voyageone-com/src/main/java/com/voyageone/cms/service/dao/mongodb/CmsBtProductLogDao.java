package com.voyageone.cms.service.dao.mongodb;

import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.cms.service.model.CmsBtFeedInfoModel;
import com.voyageone.cms.service.model.CmsBtProductLogModel;
import org.springframework.stereotype.Repository;

@Repository
public class CmsBtProductLogDao extends BaseMongoDao {

    @Override
    public Class getEntityClass() {
        return CmsBtProductLogModel.class;
    }
}
