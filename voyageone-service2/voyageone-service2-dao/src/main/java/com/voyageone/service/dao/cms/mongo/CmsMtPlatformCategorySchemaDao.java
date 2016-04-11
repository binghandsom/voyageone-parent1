package com.voyageone.service.dao.cms.mongo;

import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategorySchemaModel;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by lewis on 15-12-1.
 */
@Repository
public class CmsMtPlatformCategorySchemaDao extends BaseMongoDao<CmsMtPlatformCategorySchemaModel> {

    public WriteResult deletePlatformCategorySchemaByCartId(Integer cartId){
        String queryStr = "{cartId:"+cartId+"}";
        return  deleteWithQuery(queryStr);
    }

    public List<JSONObject> getAllSchemaKeys(){
        String columnResult="{_id:1}";
        return mongoTemplate.find(null, columnResult, collectionName);
    }

    public CmsMtPlatformCategorySchemaModel getPlatformCatSchemaModelById(String id){

        return super.selectById(id);
    }

    public CmsMtPlatformCategorySchemaModel getPlatformCatSchemaModel(String catId, int cartId){
        String queryStr = "{" +
                "cartId: " + cartId +
                ", catId: '" + catId + "'" +
                "}";
        return selectOneWithQuery(queryStr);
    }

}
