package com.voyageone.service.dao.cms.mongo;

import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.BaseMongoChannelDao;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.MongoUtils;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import org.springframework.stereotype.Repository;

import java.util.List;

import static java.util.stream.Collectors.joining;

/**
 * Created by james.li on 2015/11/27.
 *
 * @author james.li
 */
@Repository
public class CmsBtFeedInfoDao extends BaseMongoChannelDao<CmsBtFeedInfoModel> {

    public CmsBtFeedInfoModel selectProductByCode(String channelId, String code) {
        String query = "{\"code\":\"" + code + "\"}";
        return selectOneWithQuery(query, channelId);
    }

    /**
     * 根据updFlg来获取商品列表(updFlg=0: 等待反映到主数据, updFlg=1: 已经反映到主数据)
     *
     * @param channelId channel id
     * @param updFlg    updFlg=0: 等待反映到主数据, updFlg=1: 已经反映到主数据
     * @return 商品列表
     */
    public List<CmsBtFeedInfoModel> selectProductByUpdFlg(String channelId, int updFlg) {
        String query = String.format("{ channelId: '%s', updFlg: %s}", channelId, updFlg);

        return select(query, channelId);
    }

    /**
     * updateFeedInfoUpdFlg
     */
    public int updateFeedInfoUpdFlg(String channelId, String[] modelCode) {

        String sbQuery = "{" +
                MongoUtils.splicingValue("channelId", channelId) +
                "," +
                MongoUtils.splicingValue("model", modelCode) +
                "}";

        String collectionName = getCollectionName(channelId);

        WriteResult updateRes = mongoTemplate.updateMulti(sbQuery, "{$set: {updFlg: 0}}", collectionName);

        return updateRes.getN();
    }

    /**
     * 更新所有FeedInfo的UpdFlg
     */
    public int updateAllUpdFlg(String channelId, int updFlg) {

        WriteResult updateRes = mongoTemplate.updateMulti("{updFlg: 1}", String.format("{ $set: {updFlg: %d}}", updFlg),
                getCollectionName(channelId));
        return updateRes.getN();
    }

    public int updateAllUpdFlg(String channelId, String strQuery, int updFlg, String modifier){
        WriteResult updateRes = mongoTemplate.updateMulti(strQuery, String.format("{ $set: {updFlg: %d,modified:'%s',modifier:'%s'}}", updFlg,DateTimeUtil.getNowTimeStamp(),modifier),
                getCollectionName(channelId));
        return updateRes.getN();
    }

    /**
     * 查询 feed category 的 id 和 path
     *
     * @param models    指定查询的 model
     * @param channelId 查询的渠道
     * @return 只包含 feed category 的 id 和 path 的数据模型
     */
    public List<CmsBtFeedInfoModel> selectCategoryByModel(List<String> models, String channelId) {

        // param => 51A0HC13E1-00LCNB0', '16189
        String param = models.stream().collect(joining("\",\""));

        return selectWithProjection("{model:{$in:[\"" + param + "\"]}}", "{category:1, catId:1}", channelId);
    }
}
