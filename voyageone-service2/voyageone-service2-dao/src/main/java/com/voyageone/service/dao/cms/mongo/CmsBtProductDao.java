package com.voyageone.service.dao.cms.mongo;

import com.mongodb.*;
import com.voyageone.base.dao.mongodb.BaseMongoChannelDao;
import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.base.dao.mongodb.model.BaseMongoModel;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

//import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Group_Platform;

@Repository
public class CmsBtProductDao extends BaseMongoChannelDao<CmsBtProductModel> {

    /**
     * 更新SKU
     */
    public WriteResult updateWithSku(String channelId, String code, CmsBtProductModel_Sku skuModel) {
        if (channelId == null || code == null || skuModel == null || skuModel.getSkuCode() == null) {
            throw new RuntimeException("channelId, code, skuModel, sku not null");
        }
        String query = String.format("{\"skus.sku\":\"%s\"}", skuModel.getSkuCode());
        String update = String.format("{$set: %s }", skuModel.toUpdateString("skus.$."));
        String collectionName = getCollectionName(channelId);
        return mongoTemplate.updateFirst(query, update, collectionName);
    }

    /**
     * 批量更新记录
     * @param channelId 渠道ID
     * @param bulkList  更新条件
     * @param modifier  更新者
     * @param key       MongoKey pop,set,push,addToSet
     * @return 运行结果
     */
    public BulkWriteResult bulkUpdateWithMap(String channelId, List<BulkUpdateModel> bulkList, String modifier, String key) {
        return bulkUpdateWithMap(channelId, bulkList, modifier, key, false);
    }

    /**
     * 批量更新记录
     * @param channelId 渠道ID
     * @param bulkList  更新条件
     * @param modifier  更新者
     * @param key       MongoKey pop,set,push,addToSet
     * @return 运行结果
     */
    public BulkWriteResult bulkUpdateWithMap(String channelId, List<BulkUpdateModel> bulkList, String modifier, String key, boolean isUpsert) {
        //获取集合名
        DBCollection coll = getDBCollection(channelId);
        BulkWriteOperation bwo = coll.initializeOrderedBulkOperation();

        //设置更新者和更新时间
        BasicDBObject modifierObj = new BasicDBObject();
        if (modifier != null) {
            modifierObj.append("modifier", modifier);
        }
        //modifierObj.append("modified", DateTimeUtil.getNowTimeStamp());

        for (BulkUpdateModel model: bulkList){

            //生成更新对象
            BasicDBObject updateObj = new BasicDBObject();
            BasicDBObject updateContent = setDBObjectWithMap(model.getUpdateMap());

            //设置更新者和更新时间
            if ("$set".equals(key)) {
                updateContent.putAll(modifierObj.toMap());
            } else {
                updateObj.append("$set", modifierObj);
            }
            updateObj.append(key, updateContent);

            //生成查询对象
            BasicDBObject queryObj = setDBObjectWithMap(model.getQueryMap());

            if (isUpsert) {
                bwo.find(queryObj).upsert().update(updateObj);
            } else {
                bwo.find(queryObj).update(updateObj);
            }
        }
        //最终批量运行
        return bwo.execute();
    }

    /**
     * 根据 传入Map批量设置BasicDBObject
     * @param map 条件或者值得MAP
     * @return 处理好的结果
     */
    public BasicDBObject setDBObjectWithMap(Map<String, Object> map) {
        BasicDBObject result = new BasicDBObject();
        result.putAll(map);
        return result;
    }

    @Override
    public WriteResult update(BaseMongoModel model) {
        throw new BusinessException("not suppert");
    }

    // 根据条件更新指定值
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
     * 检查该产品的数据是否已经准备完成.
     * @param channelId String
     * @param productId Long
     * @return boolean
     */
    public boolean checkProductDataIsReady(String channelId, Long productId){

        JomgoQuery jomgoQuery = new JomgoQuery();
        jomgoQuery.setQuery(String.format("{prodId: %s, batchField.switchCategory: 1}",productId));
        jomgoQuery.setProjection("prodId");

        List<CmsBtProductModel> result = select(jomgoQuery, channelId);

        return result.size() <= 0;
    }

}
