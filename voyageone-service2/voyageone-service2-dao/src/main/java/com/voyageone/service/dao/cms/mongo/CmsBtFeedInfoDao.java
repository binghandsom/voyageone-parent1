package com.voyageone.service.dao.cms.mongo;

import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.BaseMongoChannelDao;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.MongoUtils;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import org.springframework.data.mongodb.core.query.Criteria;
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

    public CmsBtFeedInfoModel selectProductBySku(String channelId, String sku) {
        String query = "{\"skus.sku\":\"" + sku + "\"}";
        return selectOneWithQuery(query, channelId);
    }

    public List<CmsBtFeedInfoModel> selectProductListBySku(String channelId, String sku) {
        String query = "{\"skus.sku\":\"" + sku + "\"}";
        return select(query, channelId);
    }

    /**
     * 根据clientSku获取feed product信息
     * @param channelId channel
     * @param clientSku client sku
     * @return feed product
     */
    public CmsBtFeedInfoModel selectProductByClientSku(String channelId, String clientSku) {
        String query = "{\"skus.clientSku\":\"" + clientSku + "\"}";
        return selectOneWithQuery(query, channelId);
    }

    public WriteResult updateFeedInfoSkuPrice(String channelId, String sku, Double price){
        JongoUpdate updObj = new JongoUpdate();
        updObj.setQuery("{\"skus.sku\":#}");
        updObj.setQueryParameters(sku);
        updObj.setUpdate("{$set:{\"skus.$.priceCurrent\":#,\"skus.$.priceNet\":#,\"skus.$.priceClientRetail\":#}}");
        updObj.setUpdateParameters(price,price,price);
        return updateMulti(updObj,channelId);
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

    public WriteResult updateAllUpdFlg(String channelId, String strQuery, int updFlg, String modifier){
        WriteResult updateRes = mongoTemplate.updateMulti(strQuery, String.format("{ $set: {updFlg: %d,modified:'%s',modifier:'%s'}}", updFlg,DateTimeUtil.getNowTimeStamp(),modifier),
                getCollectionName(channelId));
        return updateRes;
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

    public List<CmsBtFeedInfoModel> selectProductByModel(String channelId, String model) {
        JongoQuery jongoQuery = new JongoQuery();
        jongoQuery.setQuery(new Criteria("model").is(model));
        return select(jongoQuery, channelId);
    }
}
