package com.voyageone.service.impl.cms.product;

import com.mongodb.BulkWriteResult;
import com.mongodb.util.JSON;
import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.common.util.MongoUtils;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductGroupDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *  Product Group Service
 *
 * @author chuanyu.liang 15/12/9
 * @version 2.0.1
 * @since 2.0.0
 */
@Service
public class ProductGroupService extends BaseService {

    @Autowired
    private CmsBtProductDao cmsBtProductDao;
    @Autowired
    private CmsBtProductGroupDao cmsBtProductGroupDao;

    /**
     * getList
     */
    public List<CmsBtProductGroupModel> getList(String channelId, JomgoQuery queryObject) {
        return cmsBtProductGroupDao.select(queryObject, channelId);
    }

    /**
     * save Groups
     * @param channelId
     * @param prodCode
     * @param cartId
     * @param platform
     */
    public void saveGroups(String channelId, String prodCode, int cartId, Map platform) {
        Map queryParam = new HashMap();
        queryParam.put("cartId", cartId);
        Map codeParam = new HashMap();
        codeParam.put("$in", new String[]{prodCode});
        queryParam.put("productCodes", codeParam);
        Map updateObj = new HashMap();
        updateObj.put("$set", platform);
        cmsBtProductGroupDao.updateFirst(JSON.serialize(queryParam), JSON.serialize(updateObj), channelId);
    }

    /**
     * change main product.
     */
    public int updateMainProduct(String channelId, Long productId, Long groupId, String modifier){

        int updateCount = 0;

        updateCount = updateCount + resetMainProduct(groupId, channelId, modifier);

        updateCount = updateCount + setMainProduct(groupId, channelId, productId, modifier);

        return updateCount;
    }

    /**
     * 将主商品设置为非主商品.
     */
    private int resetMainProduct(Long groupId, String channelId, String modifier) {
        List<BulkUpdateModel> bulkList = new ArrayList<>();

        HashMap<String, Object> updateMap = new HashMap<>();
        updateMap.put("groups.platforms.$.isMain", 0);

        HashMap<String, Object> queryMap = new HashMap<>();
        queryMap.put("groups.platforms.isMain", 1);
        queryMap.put("groups.platforms.groupId", groupId);

        BulkUpdateModel model = new BulkUpdateModel();
        model.setUpdateMap(updateMap);
        model.setQueryMap(queryMap);
        bulkList.add(model);
        BulkWriteResult result = cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, modifier, "$set");

        return result.getModifiedCount();
    }

    /**
     * 设置主产品.
     */
    private int setMainProduct(Long groupId, String channelId, Long productId, String modifier) {
        List<BulkUpdateModel> bulkList = new ArrayList<>();

        HashMap<String, Object> updateMap = new HashMap<>();
        updateMap.put("groups.platforms.$.isMain", 1);

        HashMap<String, Object> queryMap = new HashMap<>();

        queryMap.put("prodId", productId);

        queryMap.put("groups.platforms.groupId", groupId);

        BulkUpdateModel model = new BulkUpdateModel();
        model.setUpdateMap(updateMap);
        model.setQueryMap(queryMap);
        bulkList.add(model);

        BulkWriteResult result = cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, modifier, "$set");

        return result.getModifiedCount();
    }

    /**
     * 根据modelCode, cartId获取商品的group id
     */
    public CmsBtProductGroupModel selectProductGroupByModelCodeAndCartId(String channelId, String modelCode, String cartId) {
        String query = String.format("{\"feed.orgAtts.modelCode\":\"%s\"}, {\"fields.code\":1}", modelCode);
        List<CmsBtProductModel> prodList = cmsBtProductDao.select(query, channelId);
        if (prodList == null || prodList.isEmpty()) {
            return null;
        }
        List<String> codeList = new ArrayList<>(prodList.size());
        prodList.forEach(cmsBtProductModel -> codeList.add(cmsBtProductModel.getFields().getCode()));
        JomgoQuery queryObject = new JomgoQuery();
        String[] codeArr = new String[codeList.size()];
        codeArr = codeList.toArray(codeArr);
        queryObject.setQuery("{" + MongoUtils.splicingValue("productCodes", codeArr, "$in") + ",'cartId':" + cartId + "}");
        queryObject.setProjection("{'groupId':1,'_id':0}");

        List<CmsBtProductGroupModel> grpList = cmsBtProductGroupDao.select(queryObject, channelId);
        if (grpList == null || grpList.isEmpty()) {
            return null;
        } else {
            return grpList.get(0);
        }
    }
}
