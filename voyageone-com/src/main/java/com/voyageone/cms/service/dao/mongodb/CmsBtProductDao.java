package com.voyageone.cms.service.dao.mongodb;

import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.cms.service.model.CmsBtProductModel;
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
     * @param channelId
     * @param prodId
     * @return
     */
    public CmsBtProductModel selectProductById(String channelId, int prodId) {
        String query = "{\"prodId\":" + prodId + "}";
        return selectOneWithQuery(query, channelId);
    }

    /**
     * 获取商品 根据ID获
     * @param channelId
     * @param prodId
     * @return
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
     * @param channelId
     * @param code
     * @return
     */
    public CmsBtProductModel selectProductByCode(String channelId, String code) {
        String query = "{\"fields.code\":\"" + code + "\"}";
        return selectOneWithQuery(query, channelId);
    }

    /**
     * 获取商品List 根据GroupId
     * @param channelId
     * @param groupId
     * @return
     */
    public List<CmsBtProductModel> selectProductByGroupId(String channelId, int groupId) {
        String query = "{\"groups.platforms.groupId\":" + groupId + "}";
        return select(query, channelId);
    }

    /**
     * 获取SKUList 根据prodId
     * @param channelId
     * @param prodId
     * @return
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
     * @param channelId
     * @param catIdPath
     * @return
     * @throws IOException
     */
    public List<CmsBtProductModel> selectLeftLikeCatIdPath(String channelId, String catIdPath) {
        String queryTemp = "{catIdPath:{$regex:\"^%s\"}}";
        String queryStr  = String.format(queryTemp, catIdPath);
        return select(queryStr, channelId);
    }

    /**
     * 获取Product ALL MongoCursor
     * @param channelId
     * @return
     */
    public Iterator<CmsBtProductModel> selectAllReturnCursor(String channelId) {
        return selectCursorAll(channelId);
    }

}
