package com.voyageone.batch.cms.mongoDao;

import com.voyageone.base.dao.mongodb.BaseJomgoTemplate;
import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.batch.cms.mongoModel.PlatformCategoryMongoModel;
import com.voyageone.cms.service.model.CmsBtFeedInfoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PlatformCategoryMongoDao extends BaseMongoDao {

    public PlatformCategoryMongoDao() {
        super.entityClass = PlatformCategoryMongoModel.class;
    }

    public PlatformCategoryMongoModel findOne(String strQuery) {
        return mongoTemplate.findOne(strQuery, PlatformCategoryMongoModel.class, collectionName);
    }

    public void savePlatformCategories(List<PlatformCategoryMongoModel> models){
        mongoTemplate.insert(models);

    }

    public void deletePlatformCategories(String cartId){
        mongoTemplate.remove("{cartId:"+cartId+"}", collectionName);

    }

    public void insertWithProduct(PlatformCategoryMongoModel entity) {
        mongoTemplate.save(entity, collectionName);
    }

    public void saveWithProduct(PlatformCategoryMongoModel entity) {
        mongoTemplate.save(entity, collectionName);
    }

    public void deleteWithProduct(PlatformCategoryMongoModel entity) {
        mongoTemplate.removeById(entity.get_id(), collectionName);
    }


}
