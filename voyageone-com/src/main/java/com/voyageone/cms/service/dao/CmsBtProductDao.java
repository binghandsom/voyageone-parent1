package com.voyageone.cms.service.dao;

import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.cms.service.model.CmsBtProductModel;
import org.springframework.stereotype.Repository;

@Repository
public class CmsBtProductDao extends BaseMongoDao {

    public CmsBtProductDao() {
        super.entityClass = CmsBtProductModel.class;
    }

    public CmsBtProductModel getProductByCode(String channelId, String code) {
        String query = "{\"code\":\"" + code + "\"}";
        String collectionName = mongoTemplate.getCollectionName(CmsBtProductModel.class, channelId);
        return mongoTemplate.findOne(query, CmsBtProductModel.class, collectionName);
    }

    public void save(CmsBtProductModel product){
        System.out.println(this.collectionName);
        String collectionName = mongoTemplate.getCollectionName(product);
//        String collectionName = product.getCollectionName();
        mongoTemplate.save(product, collectionName);
    }

    public void update(CmsBtProductModel product){
        String collectionName = mongoTemplate.getCollectionName(product);
        mongoTemplate.save(product, collectionName);
    }
}
