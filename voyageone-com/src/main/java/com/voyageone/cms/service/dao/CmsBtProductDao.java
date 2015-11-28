package com.voyageone.cms.service.dao;

import com.voyageone.base.dao.mongodb.BaseJomgoTemplate;
import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.cms.service.model.FeedProductModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CmsBtProductDao {
    @Autowired
    BaseJomgoTemplate mongoTemplate;

    public CmsBtProductModel getProductByCode(String channelId, String code) {
        String query = "{\"code\":\"" + code + "\"}";
        return mongoTemplate.findOne(query, CmsBtProductModel.class, CmsBtProductModel.getCollectionName(channelId));
    }

    public void save(CmsBtProductModel product){
        mongoTemplate.save(product, product.getCollectionName());
    }

    public void update(CmsBtProductModel product){
        mongoTemplate.save(product, product.getCollectionName());
    }
}
