package com.voyageone.cms.service.dao;

import com.voyageone.base.dao.mongodb.BaseJomgoTemplate;
import com.voyageone.cms.service.model.FeedProductModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by james.li on 2015/11/27.
 */
@Repository
public class FeedProductDao {
    @Autowired
    BaseJomgoTemplate mongoTemplate;

    public FeedProductModel getProductByCode(String channelId, String code) {
        String query = "{\"code\":\"" + code + "\"}";
        return mongoTemplate.findOne(query, FeedProductModel.class, FeedProductModel.getCollectionName(channelId));
    }

    public void updateProduct(FeedProductModel product){
        mongoTemplate.save(product, product.getCollectionName());
    }
}
