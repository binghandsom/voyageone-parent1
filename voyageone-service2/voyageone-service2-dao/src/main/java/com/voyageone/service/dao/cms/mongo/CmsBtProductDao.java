package com.voyageone.service.dao.cms.mongo;

import com.mongodb.*;
import com.voyageone.base.dao.mongodb.BaseMongoPartDao;
import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.base.dao.mongodb.model.BaseMongoModel;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.MongoUtils;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Group_Platform;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Repository;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Repository
public class CmsBtProductDao extends BaseMongoPartDao<CmsBtProductModel> {

    /**
     * 获取商品 根据ID获
     */
    public CmsBtProductModel selectProductById(String channelId, long prodId) {
        String query = "{\"prodId\":" + prodId + "}";
        return selectOneWithQuery(query, channelId);
    }

    /**
     * 获取商品 根据ID获
     */
    public JSONObject selectProductByIdWithJson(String channelId, long prodId) {
        String query = "{\"prodId\":" + prodId + "}";
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        String projection = "{catIdPath:1, channelId:1}";
        List<JSONObject>  result = mongoTemplate.find(query, projection, collectionName);
        return result.get(0);
    }

    /**
     * 获取商品 根据Code
     */
    public CmsBtProductModel selectProductByCode(String channelId, String code) {
        String query = "{\"fields.code\":\"" + code + "\"}";
        return selectOneWithQuery(query, channelId);
    }

    /**
     * 获取商品的group 根据modelCode, cartId
     */
    public CmsBtProductModel selectProductGroupByModelCodeAndCartId(String channelId, String modelCode, String cartId) {
        String query = String.format("{\"feed.orgAtts.modelCode\":\"%s\",\"groups.platforms.cartId\":%s}, {\"groups.platforms.cartId.$\":1}", modelCode, cartId);
        return selectOneWithQuery(query, channelId);
    }

    /**
     * 获取商品List 根据GroupId
     */
    public List<CmsBtProductModel> selectProductByGroupId(String channelId, long groupId) {
        String query = "{\"groups.platforms.groupId\":" + groupId + "}";
        return select(query, channelId);
    }

    /**
     * 获取商品List 根据TagId
     */
    public List<CmsBtProductModel> selectProductByTagId(String channelId, Integer tagId) {
        String query = "{\"tags\":{$regex:\".*-" + tagId + "-.*\"}}";
        return select(query, channelId);
    }


    /**
     * 获取商品List count 根据TagId
     */
    public long selectProductCountByTagId(String channelId, Integer tagId) {
        String query = "{\"tags\":{$regex:\".*-" + tagId + "-.*\"}}";
        return countByQuery(query, channelId);
    }

    /**
     * 获取商品Code 数量 根据CartId（含SKU）
     * @param channelId channel Id
     * @param cartId cart Id
     *
     */
    public long selectProductByCartIdRecCount(String channelId, String cartId) {
        String query = "{\"groups.platforms.cartId\":" + cartId + "}";

        return countByQuery(query, channelId);
    }

    /**
     * 获取商品Model 数量 根据CartId（含SKU）
     * @param channelId channel Id
     * @param cartId cart Id
     */
    public long selectGroupByCartIdRecCount(String channelId, String cartId) {
        String query = "{\"groups.platforms.cartId\":" + cartId + ",\"groups.platforms.isMain\":1}";
        return countByQuery(query, channelId);
    }

    /**
     * 获取SKUList 根据prodId
     */
    public List<CmsBtProductModel_Sku> selectSKUById(String channelId, long prodId) {
        JomgoQuery queryObject = new JomgoQuery();
        queryObject.setProjection("skus.skuCode");
        queryObject.setQuery("{\"prodId\":" + prodId + "}");
        CmsBtProductModel product = selectOneWithQuery(queryObject, channelId);
        if (product != null) {
            return product.getSkus();
        }
        return null;
    }

    /**
     * 获取SKUList 根据prodId
     */
    public List<CmsBtProductModel_Sku> selectSKUByCode(String channelId, String productCode) {
        JomgoQuery queryObject = new JomgoQuery();
        queryObject.setProjection("skus.skuCode");
        queryObject.setQuery("{\"fields.code\":\"" + productCode + "\"}");
        CmsBtProductModel product = selectOneWithQuery(queryObject, channelId);
        if (product != null) {
            return product.getSkus();
        }
        return null;
    }

    /**
     * 获取Product List 根据catIdPath
     */
    public List<CmsBtProductModel> selectLeftLikeCatIdPath(String channelId, String catIdPath) {
        String queryTemp = "{catIdPath:{$regex:\"^%s\"}}";
        String queryStr  = String.format(queryTemp, catIdPath);
        return select(queryStr, channelId);
    }

    /**
     * 获取Product ALL MongoCursor
     */
    public Iterator<CmsBtProductModel> selectAllReturnCursor(String channelId) {
        return selectCursorAll(channelId);
    }

    /**
     *更新Platform
     */
    public WriteResult updateWithPlatform(String channelId, String code, CmsBtProductModel_Group_Platform platformMode) {
        if (channelId == null || code == null || platformMode == null || platformMode.getGroupId() == null) {
            throw new RuntimeException("channelId, code, platformMode, groupId not null");
        }
        String query = String.format("{\"groups.platforms.groupId\":%s}", platformMode.getGroupId());
        String update = String.format("{$set: %s }", platformMode.toUpdateString("groups.platforms.$."));
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.updateFirst(query, update, collectionName);
    }

    /**
     * 更新SKU
     */
    public WriteResult updateWithSku(String channelId, String code, CmsBtProductModel_Sku skuModel) {
        if (channelId == null || code == null || skuModel == null || skuModel.getSkuCode() == null) {
            throw new RuntimeException("channelId, code, skuModel, sku not null");
        }
        String query = String.format("{\"skus.sku\":\"%s\"}", skuModel.getSkuCode());
        String update = String.format("{$set: %s }", skuModel.toUpdateString("skus.$."));
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.updateFirst(query, update, collectionName);
    }

    /**
     * 批量更新Field记录
     * @param channelId 渠道ID
     * @param prodIdList id List
     * @param field  CmsBtProductModel_Field
     * @param modifier  更新者
     * @return 运行结果
     */
    public BulkWriteResult bulkUpdateFieldsByProdIds(String channelId, List<Long> prodIdList, CmsBtProductModel_Field field, String modifier) {
        BulkWriteResult result = null;
        if (prodIdList != null && prodIdList.size()>0 && field != null && field.size()>0) {
            int step = 100;
            DBCollection coll = getDBCollection(channelId);
            int index  = 0;
            BulkWriteOperation bwo = null;
            for(Long prodid : prodIdList) {
                if (bwo == null) {
                    bwo = coll.initializeOrderedBulkOperation();
                }

                BasicDBObject fieldUpdateObj = field.toUpdateBasicDBObject("fields.");
                fieldUpdateObj.append("modified", DateTimeUtil.getNowTimeStamp());
                if (modifier != null && !"".equals(modifier.trim())) {
                    fieldUpdateObj.append("modifier", modifier);
                }
                BasicDBObject updateObj = new BasicDBObject();
                updateObj.append("$set", fieldUpdateObj);

                BasicDBObject query = new BasicDBObject().append("prodId", prodid);
                bwo.find(query).update(updateObj);

                index++;
                if (index % step == 0) {
                    result = bwo.execute();
                    bwo = null;
                }
            }
            if (bwo != null) {
                result = bwo.execute();
            }
        }
        return result;
    }

    /**
     * 批量更新Field记录
     * @param channelId 渠道ID
     * @param codeList  code List
     * @param field  CmsBtProductModel_Field
     * @param modifier  更新者
     * @return 运行结果
     */
    public BulkWriteResult bulkUpdateFieldsByCodes(String channelId, List<String> codeList, CmsBtProductModel_Field field, String modifier) {
        BulkWriteResult result = null;
        if (codeList != null && codeList.size()>0 && field != null && field.size()>0) {
            int step = 100;
            DBCollection coll = getDBCollection(channelId);
            int index  = 0;
            BulkWriteOperation bwo = null;
            for(String code : codeList) {
                if (bwo == null) {
                    bwo = coll.initializeOrderedBulkOperation();
                }

                BasicDBObject fieldUpdateObj = field.toUpdateBasicDBObject("fields.");
                fieldUpdateObj.append("modified", DateTimeUtil.getNowTimeStamp());
                if (modifier != null && !"".equals(modifier.trim())) {
                    fieldUpdateObj.append("modifier", modifier);
                }
                BasicDBObject updateObj = new BasicDBObject();
                updateObj.append("$set", fieldUpdateObj);

                BasicDBObject query = new BasicDBObject().append("fields.code", code);
                bwo.find(query).update(updateObj);

                index++;
                if (index % step == 0) {
                    result = bwo.execute();
                    bwo = null;
                }
            }
            if (bwo != null) {
                result = bwo.execute();
            }
        }
        return result;
    }

    /**
     * 批量更新Field记录
     * @param channelId 渠道ID
     * @param prodIdFieldMap  prodId field map
     * @param modifier  更新者
     * @return 运行结果
     */
    public BulkWriteResult bulkUpdateFieldsByProdIds(String channelId, Map<Long, CmsBtProductModel_Field> prodIdFieldMap, String modifier) {
        BulkWriteResult result = null;
        if (prodIdFieldMap != null && prodIdFieldMap.size()>0) {
            int step = 100;
            DBCollection coll = getDBCollection(channelId);
            int index  = 0;
            BulkWriteOperation bwo = null;
            for(Map.Entry<Long, CmsBtProductModel_Field> entry : prodIdFieldMap.entrySet()) {
                Long prodId = entry.getKey();
                CmsBtProductModel_Field field = entry.getValue();
                if (prodId == null || field == null || field.size() == 0) {
                    continue;
                }
                if (bwo == null) {
                    bwo = coll.initializeOrderedBulkOperation();
                }

                BasicDBObject fieldUpdateObj = field.toUpdateBasicDBObject("fields.");
                fieldUpdateObj.append("modified", DateTimeUtil.getNowTimeStamp());
                if (modifier != null && !"".equals(modifier.trim())) {
                    fieldUpdateObj.append("modifier", modifier);
                }
                BasicDBObject updateObj = new BasicDBObject();
                updateObj.append("$set", fieldUpdateObj);

                BasicDBObject query = new BasicDBObject().append("prodId", prodId);
                bwo.find(query).update(updateObj);

                index++;
                if (index % step == 0) {
                    result = bwo.execute();
                    bwo = null;
                }
            }
            if (bwo != null) {
                result = bwo.execute();
            }
        }
        return result;
    }

    /**
     * 批量更新Field记录
     * @param channelId 渠道ID
     * @param codeFieldMap code field map
     * @param modifier  更新者
     * @return 运行结果
     */
    public BulkWriteResult bulkUpdateFieldsByCodes(String channelId, Map<String, CmsBtProductModel_Field> codeFieldMap, String modifier) {
        BulkWriteResult result = null;
        if (codeFieldMap != null && codeFieldMap.size()>0) {
            int step = 100;
            DBCollection coll = getDBCollection(channelId);
            int index  = 0;
            BulkWriteOperation bwo = null;
            for(Map.Entry<String, CmsBtProductModel_Field> entry : codeFieldMap.entrySet()) {
                String code = entry.getKey();
                CmsBtProductModel_Field field = entry.getValue();
                if (code == null || field == null || field.size() == 0) {
                    continue;
                }
                if (bwo == null) {
                    bwo = coll.initializeOrderedBulkOperation();
                }

                BasicDBObject fieldUpdateObj = field.toUpdateBasicDBObject("fields.");
                fieldUpdateObj.append("modified", DateTimeUtil.getNowTimeStamp());
                if (modifier != null && !"".equals(modifier.trim())) {
                    fieldUpdateObj.append("modifier", modifier);
                }
                BasicDBObject updateObj = new BasicDBObject();
                updateObj.append("$set", fieldUpdateObj);

                BasicDBObject query = new BasicDBObject().append("fields.code", code);
                bwo.find(query).update(updateObj);

                index++;
                if (index % step == 0) {
                    result = bwo.execute();
                    bwo = null;
                }
            }
            if (bwo != null) {
                result = bwo.execute();
            }
        }
        return result;
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
     * 批量删除记录
     * @param channelId 渠道ID
     * @param bulkList  更新条件
     * @return 运行结果
     */
    public BulkWriteResult bulkRemoveWithMap(String channelId, List<Map<String, Object>> bulkList) {
        //获取集合名
        DBCollection coll = getDBCollection(channelId);
        BulkWriteOperation bwo = coll.initializeOrderedBulkOperation();

        //设置更新者和更新时间
        BasicDBObject modifierObj = new BasicDBObject();
        for (Map<String, Object> queryMap: bulkList){
            //生成查询对象
            BasicDBObject queryObj = setDBObjectWithMap(queryMap);
            bwo.find(queryObj).remove();
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


//    public List<CmsBtProductModel> getProductCodesByCart(String channelId, int cartId) {
//        String queryTemp = " { $match :{\"groups.platforms.cartId\":21}  }, \n" +
//                " { $unwind : \"$groups.platforms\"}, \n" +
//                " { $match :{\"groups.platforms.cartId\":21}  },\n" +
//                " { $project : {\"fields.code\":1, \"groups.platforms.groupId\":1 }}";
//
//        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
//        Aggregate aggregate = mongoTemplate.aggregate(queryTemp, collectionName);
//        Aggregate.ResultsIterator<CmsBtProductModel> aa = aggregate.as(getEntityClass());
//        //IteratorUtils.toList(
//        while(aa.hasNext()) {
//            CmsBtProductModel product = aa.next();
//            System.out.println(product.getFields().getCode());
//            System.out.println(product.getGroups().getPlatforms().get(0).getGroupId());
//        }
//        return null;
//    }

    /**
     * change main product.
     * @param groupId Long
     * @param channelId String
     * @param productId Long
     * @return WriteResult
     */
    public WriteResult updateMainProduct(Long groupId, String channelId, Long productId){

        //1.将老的主产品更新成一般产品.

        //2.将传入的product设置成主商品.

        return null;
    }

//    /**
//     * 取得该产品所有的group信息
//     * @param channelId String
//     * @param productIds Object[]
//     * @return List<CmsBtProductModel>
//     */
//    public List<CmsBtProductModel> getModelCode(String channelId, Object[] productIds) {
//
//        JomgoQuery jomgoQuery = new JomgoQuery();
//        jomgoQuery.setQuery(MongoUtils.splicingValue("prodId", productIds));
//        jomgoQuery.setProjection("groups");
//
//        return select(jomgoQuery, channelId);
//    }

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

    // TODO 删除edward:好像没有被用到
//    /**
//     * 查询某个group下已经在平台上新的产品列表.
//     * @param channelId
//     * @param modelCode
//     * @return
//     */
//    public List<CmsBtProductModel> getOnSaleProducts(String channelId,String modelCode){
//
//        String conditionQuery = String.format("{ 'feed.orgAtts.modelCode' : '%s', 'groups.platforms': {$elemMatch: {numIId: {'$nin':[null,''], '$exists':true} }} }",modelCode);
//
//        String projection = "{'prodId':1,'fields.code':1,'groups.platforms.$':1}";
//
//        List<CmsBtProductModel> cmsBtProductModels = selectWithProjection(conditionQuery,projection,channelId);
//
//        return cmsBtProductModels;
//
//    }


}
