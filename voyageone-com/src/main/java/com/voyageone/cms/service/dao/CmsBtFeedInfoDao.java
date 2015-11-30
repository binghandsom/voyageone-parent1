package com.voyageone.cms.service.dao;

import com.voyageone.base.dao.mongodb.BaseJomgoTemplate;
import com.voyageone.cms.service.model.CmsBtFeedInfoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by james.li on 2015/11/27.
 */
@Repository
public class CmsBtFeedInfoDao {
    @Autowired
    BaseJomgoTemplate mongoTemplate;

    public CmsBtFeedInfoModel selectProductByCode(String channelId, String code) {
        String query = "{\"code\":\"" + code + "\"}";
        return mongoTemplate.findOne(query, CmsBtFeedInfoModel.class, CmsBtFeedInfoModel.getCollectionName(channelId));
    }

    public void updateProduct(CmsBtFeedInfoModel product){
        mongoTemplate.save(product, product.getCollectionName());
    }
}
