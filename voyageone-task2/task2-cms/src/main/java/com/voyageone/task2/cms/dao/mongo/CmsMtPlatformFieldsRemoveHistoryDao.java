package com.voyageone.task2.cms.dao.mongo;

import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.task2.cms.model.mongo.CmsMtPlatformRemoveFieldsModel;
import org.springframework.stereotype.Repository;

/**
 * Created by lewis on 15-12-9.
 */
@Repository
public class CmsMtPlatformFieldsRemoveHistoryDao extends BaseMongoDao {

    @Override
    public Class getEntityClass() {
        return CmsMtPlatformRemoveFieldsModel.class;
    }


}
