package com.voyageone.service.dao.cms.mongo;

import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.BaseMongoChannelDao;
import com.voyageone.common.util.MongoUtils;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by james.li on 2015/11/27.
 */

@Repository
public class CmsBtFeedInfoDao extends BaseMongoChannelDao<CmsBtFeedInfoModel> {

    public CmsBtFeedInfoModel selectProductByCode(String channelId, String code) {
        String query = "{\"code\":\"" + code + "\"}";
        return selectOneWithQuery(query, channelId);
    }

    /**
     * 根据updFlg来获取商品列表(updFlg=0: 等待反映到主数据, updFlg=1: 已经反映到主数据)
     * @param channelId channel id
     * @param updFlg updFlg=0: 等待反映到主数据, updFlg=1: 已经反映到主数据
     * @return 商品列表
     */
    //jeff 2016/06 change start
    // public List<CmsBtFeedInfoModel> selectProductByUpdFlg(String channelId, int updFlg) {
    public List<CmsBtFeedInfoModel> selectProductByUpdFlg(String channelId, int[] updFlg) {
        // String query = String.format("{ channelId: '%s', updFlg: %s}", channelId, updFlg);
        String query = "{\"channelId\":\"" + channelId + "\",\"updFlg\":{$in:[";
        for (int item : updFlg) {
            query += String.valueOf(item) + ",";
        }
        query = query.substring(0, query.length() - 1) + "]}}";
        return select(query, channelId);
    }
    //jeff 2016/06 change end

    /**
     * updateFeedInfoUpdFlg
     */
    public int updateFeedInfoUpdFlg(String channelId,String[] modelCode){

        StringBuilder sbQuery = new StringBuilder();
        sbQuery.append("{");
        sbQuery.append(MongoUtils.splicingValue("channelId", channelId));
        sbQuery.append(",");
        sbQuery.append(MongoUtils.splicingValue("model", modelCode));
        sbQuery.append("}");

        String collectionName = getCollectionName(channelId);

        WriteResult updateRes = mongoTemplate.updateMulti(sbQuery.toString(),"{ $set: {updFlg: 0}}", collectionName);

        return updateRes.getN();
    }
}
