package com.voyageone.cms.service.dao.mongodb;

import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.cms.service.model.CmsBtFeedInfoModel;
import org.springframework.stereotype.Repository;

/**
 * Created by james.li on 2015/11/27.
 */

@Repository
public class CmsBtFeedInfoDao extends BaseMongoDao {

    public CmsBtFeedInfoDao() {
        super.entityClass = CmsBtFeedInfoModel.class;
    }

    public CmsBtFeedInfoModel selectProductByCode(String channelId, String code) {
        String query = "{\"code\":\"" + code + "\"}";
        String collectionName = mongoTemplate.getCollectionName(CmsBtFeedInfoModel.class, channelId);
        return mongoTemplate.findOne(query, CmsBtFeedInfoModel.class, collectionName);
    }

    public void updateProduct(CmsBtFeedInfoModel product){
        String collectionName = mongoTemplate.getCollectionName(product);
        mongoTemplate.save(product, collectionName);
    }
}
