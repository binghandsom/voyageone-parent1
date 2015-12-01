package com.voyageone.batch.cms.mongoDao;

import com.voyageone.base.dao.mongodb.BaseJomgoTemplate;
import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.batch.cms.mongoModel.CmsMtPlatformCategoryTreeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PlatformCategoryDao extends BaseMongoDao {

    @Autowired
    BaseJomgoTemplate mongoTemplate;

    public PlatformCategoryDao() {
        super.entityClass = CmsMtPlatformCategoryTreeModel.class;
    }

    public CmsMtPlatformCategoryTreeModel findOne(String strQuery) {
        return mongoTemplate.findOne(strQuery, CmsMtPlatformCategoryTreeModel.class, collectionName);
    }

    public void savePlatformCategories(List<CmsMtPlatformCategoryTreeModel> models){
        mongoTemplate.insert(models, collectionName);

    }

    public void deletePlatformCategories(Integer cartId){
        mongoTemplate.remove("{cartId:"+cartId+"}", collectionName);

    }

    public List<CmsMtPlatformCategoryTreeModel> selectPlatformCategoriesByCartId(Integer cartId){

        List<CmsMtPlatformCategoryTreeModel> categoryModels = mongoTemplate.find("{cartId:"+cartId+"}",CmsMtPlatformCategoryTreeModel.class,collectionName);

        return categoryModels;
    }

}
