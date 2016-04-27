package com.voyageone.service.dao.cms.mongo;

import com.voyageone.base.dao.mongodb.BaseMongoChannelDao;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;


@Repository
public class CmsBtProductGroupDao extends BaseMongoChannelDao<CmsBtProductGroupModel> {

    public List<CmsBtProductGroupModel> selectGroupIdsByProductCode(String channelId, List<String> productCodes) {

        String joinStr = productCodes.stream().map(code -> "'" + code + "'")
                .collect(Collectors.joining(","));

        String query = "{productCodes:{$in:["+joinStr+"]}}";
        return  select(query, channelId);



    }
}
