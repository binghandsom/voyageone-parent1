package com.voyageone.cms.service.dao.mongodb;

import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.cms.service.model.CmsMtCategorySchemaModel;
import com.voyageone.common.masterdate.schema.factory.SchemaJsonReader;
import com.voyageone.common.masterdate.schema.field.Field;
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

    public List<JSONObject> getSchemaList(String columnResult,String query, Integer skip, Integer limit){
        JomgoQuery jomgoQuery = new JomgoQuery(columnResult,query,null,limit,skip);
        return mongoTemplate.find(jomgoQuery,getEntityClass());
    }

    public Long getCategoryCount(){
        return mongoTemplate.count(collectionName);
    }

    public JSONObject selectByIdRetrunJson(String _id) {
        return mongoTemplate.findById(_id, collectionName);
    }

    /**
     * 根据category id 查询对应的Schema.
     * @param catId
     * @return
     */
    public CmsMtCategorySchemaModel getMasterSchemaModelByCatId(String catId) {

        String queryStr = "{catId:\""+catId+"\"}";
        JSONObject schemaModel = mongoTemplate.findOne(queryStr,collectionName);
        CmsMtCategorySchemaModel masterSchemaModel = null;
        if (schemaModel !=null){
            List<Field> fields = SchemaJsonReader.readJsonForList((List)schemaModel.get("fields"));
            String catFullPath = (String)schemaModel.get("catFullPath");
            masterSchemaModel = new CmsMtCategorySchemaModel(catId,catFullPath,fields);
            masterSchemaModel.setModifier(null);
            masterSchemaModel.setCreater(null);
            masterSchemaModel.setCreated(null);
            masterSchemaModel.setModified(schemaModel.get("modified").toString());
        }

        return masterSchemaModel;
    }

    public CmsMtCategorySchemaModel getMasterSchemaModelBytId(String id) {

        String queryStr = "{\"_id\":ObjectId(\""+id+"\")}";
        JSONObject schemaModel = mongoTemplate.findOne(queryStr,collectionName);
        CmsMtCategorySchemaModel masterSchemaModel = null;
        if (schemaModel !=null){
            List<Field> fields = SchemaJsonReader.readJsonForList((List)schemaModel.get("fields"));
            String catFullPath = (String)schemaModel.get("catFullPath");
            masterSchemaModel = new CmsMtCategorySchemaModel((String)schemaModel.get("catId"),catFullPath,fields);
            masterSchemaModel.setModifier(null);
            masterSchemaModel.setCreater(null);
            masterSchemaModel.setCreated(null);
            masterSchemaModel.setModified(schemaModel.get("modified").toString());
        }

        return masterSchemaModel;
    }
}
