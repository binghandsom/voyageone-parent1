package com.voyageone.service.dao.cms.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.BaseMongoChannelDao;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Repository
public class CmsBtProductGroupDao extends BaseMongoChannelDao<CmsBtProductGroupModel> {

    public List<CmsBtProductGroupModel> selectGroupIdsByProductCode(String channelId, List<String> productCodes) {

        String joinStr = productCodes.stream().map(code -> "'" + code + "'")
                .collect(Collectors.joining(","));

        String query = "{productCodes:{$in:[" + joinStr + "]}}";
        return select(query, channelId);
    }

    /**
     * 根据条件更新指定值
     * @param channelId
     * @param paraMap
     * @param rsMap
     * @return
     */
    public WriteResult update(String channelId, Map paraMap, Map rsMap) {
        //获取集合名
        DBCollection coll = getDBCollection(channelId);
        BasicDBObject params = new BasicDBObject();
        params.putAll(paraMap);
        BasicDBObject result = new BasicDBObject();
        result.putAll(rsMap);
        return coll.update(params, new BasicDBObject("$set", result), false, true);
    }

    /**
     * 根据渠道和平台,取得所对应的group下面产品code数大于1的所有group列表
     * @param channelId 渠道Id
     * @param cartId 平台Id
     * @return 该渠道及对应的平台group列表
     */
    public List<CmsBtProductGroupModel> selectGroupInfoByMoreProductCodes(String channelId, Integer cartId) {
        String query = "{\"cartId\": "+cartId+", \"productCodes.1\": {$exists:true}}";
        return select(query, channelId);
    }

    /**
     * 根据主商品code返回对应的cartId的group
     * @param channelId
     * @param cartId
     * @param mainProductCode
     * @return
     */
    public CmsBtProductGroupModel selectGroupInfoByMainProductCode(String channelId, Integer cartId, String mainProductCode) {
        JongoQuery query = new JongoQuery();
        query.setQuery("{\"channelId\": #, \"cartId\": #, \"mainProductCode\": #}");
        query.setParameters(channelId, cartId, mainProductCode);
        return selectOneWithQuery(query, channelId);
    }
}
