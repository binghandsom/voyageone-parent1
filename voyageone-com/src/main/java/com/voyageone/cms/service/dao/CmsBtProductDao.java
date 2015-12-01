package com.voyageone.cms.service.dao;

import com.jayway.jsonpath.JsonPath;
import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.cms.service.model.CmsBtProductModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class CmsBtProductDao extends BaseMongoDao {

    public CmsBtProductDao() {
        super.entityClass = CmsBtProductModel.class;
    }

    /**
     * 获取商品 根据ID获
     * @param channelId
     * @param prodId
     * @return
     */
    public CmsBtProductModel selectProductById(String channelId, int prodId) {
        String query = "{\"prodId\":" + prodId + "}";
        String collectionName = mongoTemplate.getCollectionName(CmsBtProductModel.class, channelId);
        return mongoTemplate.findOne(query, CmsBtProductModel.class, collectionName);
    }

    /**
     * 获取商品 根据Code
     * @param channelId
     * @param code
     * @return
     */
    public CmsBtProductModel selectProductByCode(String channelId, String code) {
        String query = "{\"field.code\":\"" + code + "\"}";
        String collectionName = mongoTemplate.getCollectionName(CmsBtProductModel.class, channelId);
        return mongoTemplate.findOne(query, CmsBtProductModel.class, collectionName);
    }

    /**
     * 获取商品List 根据GroupId
     * @param channelId
     * @param groupId
     * @return
     */
    public List<CmsBtProductModel> selectProductByGroupId(String channelId, int groupId) {
        String query = "{\"group.platforms.groupId\":" + groupId + "}";
        String collectionName = mongoTemplate.getCollectionName(CmsBtProductModel.class, channelId);
        return mongoTemplate.find(query, CmsBtProductModel.class, collectionName);
    }

    /**
     * 获取SKUList 根据prodId
     * @param channelId
     * @param prodId
     * @return
     */
    public List<CmsBtProductModel> selectSKUById(String channelId, int prodId) {
        String query = "{\"prodId\":" + prodId + "}";
        String collectionName = mongoTemplate.getCollectionName(CmsBtProductModel.class, channelId);
        CmsBtProductModel product = mongoTemplate.findOne(query, CmsBtProductModel.class, collectionName);
        List<Map> authors = JsonPath.read(product, "$.skus.*");
        return null;
    }

    /**
     * 添加商品
     * @param product
     */
    public void insertProduct(CmsBtProductModel product){
        String collectionName = mongoTemplate.getCollectionName(product);
        mongoTemplate.save(product, collectionName);
    }

    /**
     * 更新商品
     * @param product
     */
    public void updateProduct(CmsBtProductModel product){
        String collectionName = mongoTemplate.getCollectionName(product);
        mongoTemplate.save(product, collectionName);
    }
}
