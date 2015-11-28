package com.voyageone.batch.cms.mongoDao;

import com.voyageone.base.dao.mongodb.BaseJomgoTemplate;
import com.voyageone.batch.cms.mongoModel.PlatformCategoryMongoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PlatformCategoryMongoDao {

    @Autowired
    BaseJomgoTemplate mongoTemplate;

    public PlatformCategoryMongoModel findOne(String strQuery) {
        return mongoTemplate.findOne(strQuery, PlatformCategoryMongoModel.class, PlatformCategoryMongoModel.getCollectionName());
    }

    public void savePlatformCategories(List<PlatformCategoryMongoModel> models){
        mongoTemplate.insert(models);

    }

    public void deletePlatformCategories(String cartId){
        mongoTemplate.remove("{cartId:"+cartId+"}", PlatformCategoryMongoModel.getCollectionName());

    }

    public void insertWithProduct(PlatformCategoryMongoModel entity) {
        mongoTemplate.save(entity, entity.getCollectionName());
    }

    public void saveWithProduct(PlatformCategoryMongoModel entity) {
        mongoTemplate.save(entity, entity.getCollectionName());
    }

    public void deleteWithProduct(PlatformCategoryMongoModel entity) {
        mongoTemplate.removeById(entity.get_id(), entity.getCollectionName());
    }


}
