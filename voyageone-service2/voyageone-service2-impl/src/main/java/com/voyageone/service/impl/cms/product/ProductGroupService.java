package com.voyageone.service.impl.cms.product;

import com.mongodb.BulkWriteResult;
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
     * @param channelId
     * @param groupId
     * @return
     */
    public CmsBtProductGroupModel getProductGroupByGroupId(String channelId, Long groupId) {
        JomgoQuery query = new JomgoQuery();
        query.setQuery(String.format("{\"groupId\": %d }", groupId));
        return cmsBtProductGroupDao.selectOneWithQuery(query, channelId);
    }

    /**
     * 更新group数据
     * @param model
     * @return
     */
    public WriteResult update(CmsBtProductGroupModel model) {
        return cmsBtProductGroupDao.update(model);
    }

    /**
     * 插入新的group数据
     * @param model
     * @return
     */
    public WriteResult insert(CmsBtProductGroupModel model) {
        return cmsBtProductGroupDao.insert(model);
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
        // jeff 2016/04 change start
        // String query = String.format("{\"feed.orgAtts.modelCode\":\"%s\"}, {\"fields.code\":1}", modelCode);
        String query = String.format("{'feed.orgAtts.modelCode':'%s','fields.isMasterMain':1},{'fields.code':1}", modelCode);
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
        queryObject.setQuery("{\"productCodes\":" + prodList.get(0).getFields().getCode() + ",\"cartId\":" + cartId + "}");
        queryObject.setProjection("{'groupId':1,'_id':0}");
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
     * @param model(model中包含的productCodes,是这次平台上新处理的codes)
     * @return
     */
    public CmsBtProductGroupModel updateGroupsPlatformStatus (CmsBtProductGroupModel model) {

        // 更新cms_bt_product_groups表
        this.update(model);

        // 如果传入的groups包含code列表,则同时更新code的状态
        if (model.getProductCodes().size() > 0) {

            // 获取以前的产品carts信息,用于判断是否需要更新publishTime
            JomgoQuery queryObject = new JomgoQuery();
            StringBuffer sbQuery = new StringBuffer();
            sbQuery.append(MongoUtils.splicingValue("carts.cartId", model.getCartId()));
            sbQuery.append(",");
            sbQuery.append(MongoUtils.splicingValue("fields.code", model.getProductCodes().toArray(new String[model.getProductCodes().size()]), "$in"));
            queryObject.setQuery("{" + sbQuery.toString() + "}");

            // 如果该产品已经上新过,则对应值为true,否则为false
            queryObject.setProjection("{'fields.code': 1, 'carts.$': 1}");
            List<CmsBtProductModel> products = cmsBtProductDao.select(queryObject, model.getChannelId());
            Map<String, Boolean> isPublishedProducts = new HashMap<>();
            for(CmsBtProductModel product : products) {
                isPublishedProducts.put(product.getFields().getCode(),
                        product.getCarts().size() >0 && !StringUtils.isEmpty(product.getCarts().get(0).getPublishTime())
                                ? true : false);
            }

            // 批量更新产品的平台状态.
            List<BulkUpdateModel> bulkList = new ArrayList<>();
            for (String code : model.getProductCodes()) {

                // 设置批量更新条件
                HashMap<String, Object> bulkQueryMap = new HashMap<>();
                bulkQueryMap.put("fields.code", code);
                bulkQueryMap.put("carts.cartId", model.getCartId());

                // 设置更新值
                HashMap<String, Object> bulkUpdateMap = new HashMap<>();
                if (model.getPlatformStatus() != null) {
                    bulkUpdateMap.put("carts.$.platformStatus", model.getPlatformStatus().name());
                }
                if (!isPublishedProducts.get(code)) {
                    bulkUpdateMap.put("carts.$.publishTime", model.getPublishTime());
                }

                // 设定批量更新条件和值
                if (bulkUpdateMap.size() > 0) {
                    BulkUpdateModel bulkUpdateModel = new BulkUpdateModel();
                    bulkUpdateModel.setUpdateMap(bulkUpdateMap);
                    bulkUpdateModel.setQueryMap(bulkQueryMap);
                    bulkList.add(bulkUpdateModel);
                }
            }

            // 批量更新product表
            if (bulkList.size() > 0) {
                // TODO: 16/4/23 需要确认该批量操作,如果该条数据不存在是否新规插入
                cmsBtProductDao.bulkUpdateWithMap(model.getChannelId(), bulkList, null, "$set", true);
            }
        }

        return model;
    }
}
