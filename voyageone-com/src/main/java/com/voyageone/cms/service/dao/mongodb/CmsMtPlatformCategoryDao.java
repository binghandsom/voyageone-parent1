package com.voyageone.cms.service.dao.mongodb;

import com.voyageone.base.dao.mongodb.BaseJomgoTemplate;
import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.cms.service.model.CmsMtPlatformCategoryTreeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CmsMtPlatformCategoryDao extends BaseMongoDao {

    @Autowired
    BaseJomgoTemplate mongoTemplate;

    @Override
    public Class getEntityClass() {
        return CmsMtPlatformCategoryTreeModel.class;
    }

    public CmsMtPlatformCategoryTreeModel selectByCartIdCatId(int cartId, String categoryId) {
        String queryStr = "{" +
                "cartId: '" + cartId + "'" +
                ", categoryId: '" + categoryId + "'" +
                "}";
        return selectOneWithQuery(queryStr);
    }

    public void deletePlatformCategories(Integer cartId){
        String queryStr = "{cartId:"+cartId+"}";
        deleteWithQuery(queryStr);
    }

    public List<CmsMtPlatformCategoryTreeModel> selectPlatformCategoriesByCartId(Integer cartId){
        String queryStr = "{cartId:"+cartId+"}";
        List<CmsMtPlatformCategoryTreeModel> categoryModels = select(queryStr);
        return categoryModels;
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
