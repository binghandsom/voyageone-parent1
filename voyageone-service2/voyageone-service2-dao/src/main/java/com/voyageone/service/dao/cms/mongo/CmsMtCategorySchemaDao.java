package com.voyageone.service.dao.cms.mongo;

import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.common.masterdate.schema.factory.SchemaJsonReader;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.service.model.cms.mongo.CmsMtCategorySchemaModel;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by lewis on 15-12-9.
 */
@Repository
public class CmsMtCategorySchemaDao extends BaseMongoDao<CmsMtCategorySchemaModel> {

    public List<JSONObject> getAllSchemaIds(){
        String columnResult="{_id:1}";
        return mongoTemplate.find(null, columnResult, collectionName);
    }

    public List<JSONObject> getSchemaList(String columnResult,String query, Integer skip, Integer limit){
        JongoQuery jongoQuery = new JongoQuery(columnResult,query,null,limit,skip);
        return mongoTemplate.find(jongoQuery, JSONObject.class, collectionName);
    }

    public Long getCategoryCount(String params){
        return mongoTemplate.count(params, collectionName);
    }

    public JSONObject selectByIdRetrunJson(String _id) {
        return mongoTemplate.findById(_id, collectionName);
    }

    /**
     * 根据category id 查询对应的Schema.
     * @param catId String
     * @return CmsMtCategorySchemaModel
     */
    public CmsMtCategorySchemaModel getMasterSchemaModelByCatId(String catId) {

        String queryStr = "{catId:\""+catId+"\"}";
        JSONObject schemaModel = mongoTemplate.findOne(queryStr,collectionName);
        CmsMtCategorySchemaModel masterSchemaModel = null;
        List<Map<String, Object>> skuList = new ArrayList<>();
        if (schemaModel != null){

            List fieldList = (List)schemaModel.get("fields");

            Map<String, Object> skuJson = (Map<String, Object>) schemaModel.get("sku");

            skuList.add(skuJson);

            List<Field> skuFields = SchemaJsonReader.readJsonForList(skuList);

            List<Field> fields = SchemaJsonReader.readJsonForList(fieldList);
            String catFullPath = (String)schemaModel.get("catFullPath");
            masterSchemaModel = new CmsMtCategorySchemaModel(catId,catFullPath,fields);
            masterSchemaModel.setModifier((String) schemaModel.get("modifier"));
            masterSchemaModel.setCreater((String) schemaModel.get("creater"));
            masterSchemaModel.setCreated((String) schemaModel.get("created"));
            masterSchemaModel.setModified(schemaModel.get("modified").toString());
            masterSchemaModel.set_id(schemaModel.get("_id").toString());
            masterSchemaModel.setSku(skuFields.get(0));
        }

        return masterSchemaModel;
    }

    public JSONObject getMasterSchemaJsonObjectByCatId(String catId) {

        String queryStr = "{catId:\""+catId+"\"}";

        return mongoTemplate.findOne(queryStr,collectionName);
    }

    public void updateScheam(Map<String,Object>scheam){
        mongoTemplate.save(scheam);
    }
}
