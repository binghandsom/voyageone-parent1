package com.voyageone.cms.service.dao.mongodb;

import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.BaseJomgoTemplate;
import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.cms.service.model.CmsMtPlatformCategorySchemaModel;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by lewis on 15-12-1.
 */
@Repository
public class CmsMtPlatformCategorySchemaDao extends BaseMongoDao{

    @Autowired
    BaseJomgoTemplate mongoTemplate;

    @Override
    public Class getEntityClass() {
        return CmsMtPlatformCategorySchemaModel.class;
    }

    public WriteResult deletePlatformCategorySchemaByCartId(Integer cartId){
        String queryStr = "{cartId:"+cartId+"}";
        return  deleteWithQuery(queryStr);
    }

    public List<JSONObject> getAllSchemaKeys(){
        String columnResult="{_id:1}";
        return mongoTemplate.find(null,columnResult,collectionName);
    }

    public CmsMtPlatformCategorySchemaModel getPlatformCatSchemaModelById(String id){

        return super.selectById(id);
    }


}
