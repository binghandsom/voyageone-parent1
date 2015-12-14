package com.voyageone.cms.service.dao.mongodb;

import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.base.dao.mongodb.model.CmsMtCategorySchemaModel;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by lewis on 15-12-9.
 */
@Repository
public class CmsMtCategorySchemaDao extends BaseMongoDao {

    @Override
    public Class getEntityClass() {
        return CmsMtCategorySchemaModel.class;
    }

    public List<JSONObject> getAllSchemaIds(){
        String columnResult="{_id:1}";
        return mongoTemplate.find(null, columnResult, collectionName);
    }

    public JSONObject selectByIdRetrunJson(String id) {
        return mongoTemplate.findById(id, collectionName);
    }
}
