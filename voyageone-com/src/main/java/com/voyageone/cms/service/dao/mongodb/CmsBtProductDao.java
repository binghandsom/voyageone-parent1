package com.voyageone.cms.service.dao.mongodb;

import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.cms.service.model.CmsBtProductModel_Group_Platform;
import com.voyageone.cms.service.model.CmsBtProductModel_Sku;
import net.minidev.json.JSONObject;
import org.jongo.MongoCursor;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

@Repository
public class CmsBtProductDao extends BaseMongoDao {

    @Override
    public Class getEntityClass() {
        return CmsBtProductModel.class;
    }

    /**
     * 获取商品 根据ID获
     */
    public CmsBtProductModel selectProductById(String channelId, int prodId) {
        String query = "{\"prodId\":" + prodId + "}";
        return selectOneWithQuery(query, channelId);
    }

    /**
     * 获取商品 根据ID获
     */
    public JSONObject selectProductByIdWithJson(String channelId, int prodId) {
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
    public List<CmsBtProductModel> selectProductByGroupId(String channelId, int groupId) {
        String query = "{\"groups.platforms.groupId\":" + groupId + "}";
        return select(query, channelId);
    }

    /**
     * 获取SKUList 根据prodId
     */
    public List<CmsBtProductModel_Sku>  selectSKUById(String channelId, int prodId) {
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
        if (channelId == null || code == null || skuModel == null || skuModel.getSku() == null) {
            throw new RuntimeException("channelId, code, skuModel, sku not null");
        }
        String query = String.format("{\"skus.sku\":\"%s\"}", skuModel.getSku());
        String update = String.format("{$set: %s }", skuModel.toUpdateString("skus.$."));
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.updateFirst(query, update, collectionName);
    }

}
