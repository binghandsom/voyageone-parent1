package com.voyageone.batch.cms.dao.mongo;

import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.batch.cms.model.mongo.CmsMtPlatformRemoveFieldsModel;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Repository;

import java.util.List;

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
