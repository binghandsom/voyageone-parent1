package com.voyageone.batch.ims.dao;

import com.voyageone.base.dao.mongodb.BaseJomgoTemplate;
import com.voyageone.batch.ims.modelbean.PlatformCategoryMongoBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PlatformCategoryMongoDao {

    @Autowired
    BaseJomgoTemplate mongoTemplate;

    public PlatformCategoryMongoBean findOne(String channelId, String strQuery) {
        return mongoTemplate.findOne(strQuery, PlatformCategoryMongoBean.class, PlatformCategoryMongoBean.getCollectionName(channelId));
    }

    public void insertWithProduct(PlatformCategoryMongoBean entity) {
        mongoTemplate.save(entity, entity.getCollectionName());
    }

    public void saveWithProduct(PlatformCategoryMongoBean entity) {
        mongoTemplate.save(entity, entity.getCollectionName());
    }

    public void deleteWithProduct(PlatformCategoryMongoBean entity) {
        mongoTemplate.removeById(entity.get_id(), entity.getCollectionName());
    }


}
