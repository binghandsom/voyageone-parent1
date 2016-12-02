package com.voyageone.service.dao.cms.mongo;

import com.voyageone.base.dao.mongodb.BaseMongoChannelDao;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductTopModel;
import org.springframework.stereotype.Repository;

@Repository
public class CmsBtProductTopDao extends BaseMongoChannelDao<CmsBtProductTopModel> {

    /**
     * 根据catId返回数据
     */
    public CmsBtProductTopModel selectBySellerCatId(String SellerCatId, String channelId) {
        String query = "{\"sellerCatId\":\"" + SellerCatId + "\"}";
        return selectOneWithQuery(query, channelId);
    }

}
