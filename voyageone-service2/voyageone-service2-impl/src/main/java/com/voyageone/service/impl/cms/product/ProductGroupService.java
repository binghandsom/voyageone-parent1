package com.voyageone.service.impl.cms.product;

import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.common.util.MongoUtils;
import com.voyageone.common.util.StringUtils;
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

//    /**
//     * 新建一个group
//     * @param channelId
//     * @param prodCode
//     * @param cartId
//     * @param platform
//     */
//    public void saveGroups(String channelId, String prodCode, int cartId, Map platform) {
//        Map queryParam = new HashMap();
//        queryParam.put("cartId", cartId);
//        Map codeParam = new HashMap();
//        codeParam.put("$in", new String[]{prodCode});
//        queryParam.put("productCodes", codeParam);
//        Map updateObj = new HashMap();
//        updateObj.put("$set", platform);
//        cmsBtProductGroupDao.updateFirst(JSON.serialize(queryParam), JSON.serialize(updateObj), channelId);
//    }

    /**
     * 根据channelId和groupId取得单个group数据
     */
    public CmsBtProductGroupModel getProductGroupByGroupId(String channelId, Long groupId) {
        JomgoQuery query = new JomgoQuery();
        query.setQuery(String.format("{\"groupId\": %d }", groupId));
        return cmsBtProductGroupDao.selectOneWithQuery(query, channelId);
    }

    /**
     * 根据条件获取group数据
     * @param channelId String
     * @param query String
     * @return CmsBtProductGroupModel
     */
    public CmsBtProductGroupModel getProductGroupByQuery(String channelId, String query) {
        return cmsBtProductGroupDao.selectOneWithQuery(query, channelId);
    }

    /**
     * 根据条件获取group数据
     * @param channelId String
     * @param query JomgoQuery
     * @return CmsBtProductGroupModel
     */
    public CmsBtProductGroupModel getProductGroupByQuery(String channelId, JomgoQuery query) {
        return cmsBtProductGroupDao.selectOneWithQuery(query, channelId);
    }

    /**
     * 根据channelId和产品Code检索出productGroup数据.
     * @param channelId
     * @param code
     * @return
     */
    public CmsBtProductGroupModel selectProductGroupByCode(String channelId, String code, Integer cartId) {
        JomgoQuery query = new JomgoQuery();
        query.setQuery(String.format("{\"productCodes\": \"%s\", \"cartId\": %d}", code, cartId));
        return getProductGroupByQuery(channelId, query);
    }

    /**
     * 根据channelId和产品Code检索出是否主商品.
     * @param channelId
     * @param code
     * @return
     */
    public CmsBtProductGroupModel selectMainProductGroupByCode(String channelId, String code, Integer cartId) {
        JomgoQuery query = new JomgoQuery();
        query.setQuery(String.format("{\"mainProductCode\": \"%s\", \"cartId\": %d}", code, cartId));
        return getProductGroupByQuery(channelId, query);
    }

    /**
     * 更新group数据
     */
    public WriteResult update(CmsBtProductGroupModel model) {
        return cmsBtProductGroupDao.update(model);
    }

    /**
     * 插入新的group数据
     */
    public WriteResult insert(CmsBtProductGroupModel model) {
        return cmsBtProductGroupDao.insert(model);
    }

    /**
     * change main product.
     */
    public WriteResult updateMainProduct(String channelId, String productCode, Long groupId, String modifier){

        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("groupId", groupId);

        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("mainProductCode", productCode);
        updateMap.put("modifier", modifier);

        return cmsBtProductGroupDao.update(channelId, queryMap, updateMap);
    }

    /**
     * 根据modelCode, cartId获取商品的group的Model
     */
    public CmsBtProductGroupModel selectProductGroupByModelCodeAndCartId(String channelId, String modelCode, String cartId) {
        // jeff 2016/04 change start
        // String query = String.format("{\"feed.orgAtts.modelCode\":\"%s\"}, {\"fields.code\":1}", modelCode);
        String query = String.format("{\"feed.orgAtts.modelCode\":\"%s\",\"fields.isMasterMain\":1},{\"fields.code\":1}", modelCode);
        // jeff 2016/04 change end
        List<CmsBtProductModel> prodList = cmsBtProductDao.select(query, channelId);
        if (prodList == null || prodList.isEmpty()) {
            return null;
        }
        // jeff 2016/04 change start
        // List<String> codeList = new ArrayList<>(prodList.size());
        // prodList.forEach(cmsBtProductModel -> codeList.add(cmsBtProductModel.getFields().getCode()));
        JomgoQuery queryObject = new JomgoQuery();
        // String[] codeArr = new String[codeList.size()];
        // codeArr = codeList.toArray(codeArr);
        // queryObject.setQuery("{" + MongoUtils.splicingValue("productCodes", codeArr, "$in") + ",'cartId':" + cartId + "}");
        queryObject.setQuery("{\"productCodes\":\"" + prodList.get(0).getFields().getCode() + "\",\"cartId\":" + cartId + "}");
        // queryObject.setProjection("{'groupId':1,'_id':0}");
        // jeff 2016/04 change end
        List<CmsBtProductGroupModel> grpList = cmsBtProductGroupDao.select(queryObject, channelId);
        if (grpList == null || grpList.isEmpty()) {
            return null;
        } else {
            return grpList.get(0);
        }
    }

    /**
     * 更新该model对应的所有和上新有关的状态信息
     * @param model (model中包含的productCodes,是这次平台上新处理的codes)
     * @return CmsBtProductGroupModel
     */
    public CmsBtProductGroupModel updateGroupsPlatformStatus (CmsBtProductGroupModel model) {

        // 更新cms_bt_product_groups表
        this.update(model);

        // 如果传入的groups包含code列表,则同时更新code的状态
        if (model.getProductCodes().size() > 0) {

            // 获取以前的产品carts信息,用于判断是否需要更新publishTime
            JomgoQuery queryObject = new JomgoQuery();
            StringBuilder sbQuery = new StringBuilder();
            sbQuery.append(MongoUtils.splicingValue("carts.cartId", model.getCartId()));
            sbQuery.append(",");
            sbQuery.append(MongoUtils.splicingValue("common.fields.code", model.getProductCodes().toArray(new String[model.getProductCodes().size()]), "$in"));
            queryObject.setQuery("{" + sbQuery.toString() + "}");

            // 如果该产品已经上新过,则对应值为true,否则为false
            queryObject.setProjection("{\"common.fields.code\": 1, \"carts.$\": 1}");
            List<CmsBtProductModel> products = cmsBtProductDao.select(queryObject, model.getChannelId());
            Map<String, Boolean> isPublishedProducts = new HashMap<>();
            for(CmsBtProductModel product : products) {
                isPublishedProducts.put(product.getCommon().getFields().getCode(),
                        product.getCarts().size() > 0 && !StringUtils.isEmpty(product.getCarts().get(0).getPublishTime()));
            }

            // 批量更新产品的平台状态.
            List<BulkUpdateModel> bulkList = new ArrayList<>();
            List<BulkUpdateModel> bulkList2 = new ArrayList<>();
            for (String code : model.getProductCodes()) {

                if (!isPublishedProducts.containsKey(code)) {
                    continue;
                }

                // 设置批量更新条件
                HashMap<String, Object> bulkQueryMap = new HashMap<>();
                bulkQueryMap.put("common.fields.code", code);
                bulkQueryMap.put("carts.cartId", model.getCartId());

                // 设置批量更新条件
                HashMap<String, Object> bulkQueryMap2 = new HashMap<>();
                bulkQueryMap2.put("common.fields.code", code);

                // 设置更新值
                HashMap<String, Object> bulkUpdateMap = new HashMap<>();
                HashMap<String, Object> bulkUpdateMap2 = new HashMap<>();
                if (model.getPlatformStatus() != null) {
                    bulkUpdateMap.put("carts.$.platformStatus", model.getPlatformStatus().name());
                    bulkUpdateMap2.put("platforms.P"+model.getCartId() + ".pStatus", model.getPlatformStatus().name());
                }
                if (!isPublishedProducts.get(code)) {
                    bulkUpdateMap.put("carts.$.publishTime", model.getPublishTime());
                    bulkUpdateMap.put("carts.$.numIId", model.getNumIId());

                    bulkUpdateMap2.put("platforms.P"+model.getCartId() + ".pPublishTime", model.getPublishTime());
                    bulkUpdateMap2.put("platforms.P"+model.getCartId() + ".pNumIId", model.getNumIId());
                    bulkUpdateMap2.put("platforms.P"+model.getCartId() + ".pProductId", model.getPlatformPid());
                }

                // 设定批量更新条件和值
                if (bulkUpdateMap.size() > 0) {
                    BulkUpdateModel bulkUpdateModel = new BulkUpdateModel();
                    bulkUpdateModel.setUpdateMap(bulkUpdateMap);
                    bulkUpdateModel.setQueryMap(bulkQueryMap);
                    bulkList.add(bulkUpdateModel);
                }

                // 设定批量更新条件和值
                if (bulkUpdateMap2.size() > 0) {
                    BulkUpdateModel bulkUpdateModel2 = new BulkUpdateModel();
                    bulkUpdateModel2.setUpdateMap(bulkUpdateMap2);
                    bulkUpdateModel2.setQueryMap(bulkQueryMap2);
                    bulkList2.add(bulkUpdateModel2);
                }
            }

            // 批量更新product表
            if (bulkList.size() > 0) {
                cmsBtProductDao.bulkUpdateWithMap(model.getChannelId(), bulkList, null, "$set", true);
            }

            // 批量更新product表
            if (bulkList2.size() > 0) {
                cmsBtProductDao.bulkUpdateWithMap(model.getChannelId(), bulkList2, null, "$set", true);
            }
        }

        return model;
    }

    /**
     * 更新group的platformActive
     */
    public WriteResult updateGroupsPlatformActiveBympCode (CmsBtProductGroupModel model) {

        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("mainProductCode", model.getMainProductCode());

        if(model.getCartId() != null)
            queryMap.put("cartId", model.getCartId());

        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("platformActive", model.getPlatformActive().name());
        updateMap.put("modifier", model.getModifier());

        return cmsBtProductGroupDao.update(model.getChannelId(), queryMap, updateMap);
    }
}
