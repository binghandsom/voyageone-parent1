package com.voyageone.service.dao.cms.mongo;

import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategoryTreeModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CmsMtPlatformCategoryDao extends BaseMongoDao<CmsMtPlatformCategoryTreeModel> {

    public WriteResult deletePlatformCategories(Integer cartId,String channelId){
        String queryStr = "{'cartId':"+cartId+",'channelId':'"+channelId+"'}";
        return deleteWithQuery(queryStr);
    }

    public List<CmsMtPlatformCategoryTreeModel> selectPlatformCategoriesByCartId(Integer cartId){
        String queryStr = "{cartId:"+cartId+"}";
        return select(queryStr);
    }

    public CmsMtPlatformCategoryTreeModel selectByChannel_CartId_CatId(String channelId, int cartId, String categoryId) {
        String queryStrTemp = "{" +
                "channelId:'%s'" +
                ",cartId:%s" +
                ",catId:'%s'" +
                "}";
        String queryStr = String.format(queryStrTemp, channelId, cartId, categoryId);
        return selectOneWithQuery(queryStr);
    }

    public List<CmsMtPlatformCategoryTreeModel> selectByChannel_CartId(String channelId, int cartId) {
        String queryStrTemp = "{" +
                "channelId:'%s'" +
                ",cartId:%s" +
                "}";
        String queryStr = String.format(queryStrTemp, channelId, cartId);
        return select(queryStr);
    }

}
