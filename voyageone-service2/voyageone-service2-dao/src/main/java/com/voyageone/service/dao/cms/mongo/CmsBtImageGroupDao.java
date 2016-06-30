package com.voyageone.service.dao.cms.mongo;

import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.service.model.cms.mongo.channel.CmsBtImageGroupModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CmsBtImageGroupDao extends BaseMongoDao<CmsBtImageGroupModel> {

    public final static String VALUE_ALL = "All";

    public List<CmsBtImageGroupModel> selectListByKeysWithAll(String channelId, int cartId, int imageType, int viewType, String brandName, String productType, String sizeType, int active) {
        String query = "{$and:[" +
                "{'channelId':'%s'}, {'cartId':%d}, {'imageType':%d}, {'viewType':%d}, " +
                "{$or:[{'brandName': '" + VALUE_ALL + "'}, {'brandName': \"%s\"}]}, " +
                "{$or:[{'productType': '" + VALUE_ALL + "'}, {'productType': \"%s\"}]}, " +
                "{$or:[{'sizeType': '" + VALUE_ALL + "'}, {'sizeType': \"%s\"}]}, " +
                "{'active':%d}" +
                "]}";

        query = String.format(query, channelId, cartId, imageType, viewType, brandName, productType, sizeType, active);

        return select(query);
    }
}
