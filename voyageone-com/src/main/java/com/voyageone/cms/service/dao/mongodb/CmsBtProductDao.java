package com.voyageone.cms.service.dao.mongodb;

import com.mongodb.*;
import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.cms.service.model.CmsBtProductModel_Field;
import com.voyageone.cms.service.model.CmsBtProductModel_Group_Platform;
import com.voyageone.cms.service.model.CmsBtProductModel_Sku;
import com.voyageone.common.util.DateTimeUtil;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Repository
public class CmsBtProductDao extends BaseMongoDao {

    @Override
    public Class getEntityClass() {
        return CmsBtProductModel.class;
    }

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
     * 获取商品List 根据GroupId
     */
    public List<CmsBtProductModel> selectProductByGroupId(String channelId, long groupId) {
        String query = "{\"groups.platforms.groupId\":" + groupId + "}";
        return select(query, channelId);
    }

    /**
     * 获取SKUList 根据prodId
     */
    public List<CmsBtProductModel_Sku>  selectSKUById(String channelId, long prodId) {
        String query = "{\"prodId\":" + prodId + "}";
        CmsBtProductModel product = selectOneWithQuery(query, channelId);
        if (product != null) {
            return product.getSkus();
        }
        //Object jsonObj = JsonPath.parse(JacksonUtil.bean2Json(product)).json();
        //List<Map> authors = JsonPath.read(jsonObj, "$.skus.*");
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
     * 根据检索条件返回当前页的product列表
     * @param query
     * @param currPage
     * @param pageSize
     * @param channelId
     * @return
     */
    public List<CmsBtProductModel> selectProductByQuery(String query, Integer currPage, Integer pageSize, String channelId, String[] searchItems) {
        JomgoQuery jQuery = new JomgoQuery();
        jQuery.setQuery(query)
                .setSkip((currPage - 1) * pageSize)
                .setLimit(pageSize)
                .setProjection(searchItems);

        return select(jQuery, channelId);
    }

    /**
     * 根据检索条件返回当前页的group列表(只包含main product)
     * @param query
     * @param currPage
     * @param pageSize
     * @param channelId
     * @return
     */
    public List<CmsBtProductModel> selectGroupWithMainProductByQuery(String query, Integer currPage, Integer pageSize, String channelId, String[] searchItems) {
        JomgoQuery jQuery = new JomgoQuery();

        jQuery.setQuery(query)
                .setSkip((currPage - 1) * pageSize)
                .setLimit(pageSize)
                .setProjection(searchItems);

        return select(jQuery, channelId);
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
     * @param codeList  code List
     * @param field  CmsBtProductModel_Field
     * @param modifier  更新者
     * @return 运行结果
     */
    public BulkWriteResult bathUpdateWithField(String channelId, List<String> codeList, CmsBtProductModel_Field field, String modifier) {
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
     * @param codeFieldMap  code field map
     * @param modifier  更新者
     * @return 运行结果
     */
    public BulkWriteResult bathUpdateWithFields(String channelId, Map<String, CmsBtProductModel_Field> codeFieldMap, String modifier) {
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
        BulkWriteResult result = null;
        //获取集合名
        DBCollection coll = getDBCollection(channelId);
        BulkWriteOperation bwo = coll.initializeOrderedBulkOperation();

        //设置更新者和更新时间
        BasicDBObject modifierObj = new BasicDBObject();
        if (modifier != null) {
            modifierObj.append("modifier", modifier);
        }
        modifierObj.append("modified", DateTimeUtil.getNowTimeStamp());

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

            bwo.find(queryObj).update(updateObj);
        }
        //最终批量运行
        result = bwo.execute();

        return result;
    }

    /**
     * 根据 传入Map批量设置BasicDBObject
     * @param map 条件或者值得MAP
     * @return 处理好的结果
     */
    public BasicDBObject setDBObjectWithMap(HashMap<String, Object> map) {
        BasicDBObject result = new BasicDBObject();
        result.putAll(map);
        return result;
    }
}
