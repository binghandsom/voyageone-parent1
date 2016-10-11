package com.voyageone.service.dao.cms.mongo;

import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.BaseMongoCartDao;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategorySchemaModel;
import net.minidev.json.JSONObject;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by lewis on 15-12-1.
 */
@Repository
public class CmsMtPlatformCategorySchemaDao extends BaseMongoCartDao<CmsMtPlatformCategorySchemaModel> {

    public WriteResult deletePlatformCategorySchemaByCartId(Integer cartId){
        String queryStr = "{cartId:"+cartId+"}";
        return deleteWithQuery(queryStr, cartId);
    }

    public List<JSONObject> selectAllSchemaKeys(int cartId){
        String columnResult="{_id:1}";
        return mongoTemplate.find(null, columnResult, getCollectionName(cartId));
    }

    public CmsMtPlatformCategorySchemaModel selectPlatformCatSchemaModelById(String id, int cartId){
        return selectById(id, cartId);
    }

    public CmsMtPlatformCategorySchemaModel selectPlatformCatSchemaModel(String catId, int cartId){
        String queryStr = "{" +
                "cartId: " + cartId +
                ", catId: '" + catId + "'" +
                "}";
        return selectOneWithQuery(queryStr, cartId);
    }

    public CmsMtPlatformCategorySchemaModel selectByCategoryPath(String categoryPath, int cartId) {
        return selectOneWithQuery(new JongoQuery(
                new Criteria("catFullPath").is(categoryPath).and("cartId").is(cartId)), cartId);
    }

    /**
     * 删除参数指定的schema
     */
    public WriteResult deletePlatformCategorySchemaByCategory(Integer cartId, String categoryId){
        String queryStr = String.format("{catId:'%s'}", categoryId);
        return deleteWithQuery(queryStr, cartId);
    }
}
