package com.voyageone.service.impl.cms.product;

import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.MongoUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductGroupDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.MongoSequenceService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import org.springframework.beans.BeanUtils;
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

    @Autowired
    private MongoSequenceService commSequenceMongoService; // DAO: Sequence

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
            queryObject.setQuery("{'common.fields.code':{$in:#},'platforms.P" + model.getCartId() + ".pStatus':'WaitingPublish'}");
            queryObject.setParameters(model.getProductCodes());

            // 如果该产品已经上新过,则对应值为true,否则为false
            queryObject.setProjection("{\"common.fields.code\": 1}");
            List<CmsBtProductModel> products = cmsBtProductDao.select(queryObject, model.getChannelId());
            Map<String, Boolean> isPublishedProducts = new HashMap<>();
            for (CmsBtProductModel product : products) {
                isPublishedProducts.put(product.getCommon().getFields().getCode(), true);
            }

            // 批量更新产品的平台状态.
            List<BulkUpdateModel> bulkList2 = new ArrayList<>();
            for (String code : model.getProductCodes()) {

                if (!isPublishedProducts.containsKey(code)) {
                    continue;
                }

                // 设置批量更新条件
                HashMap<String, Object> bulkQueryMap2 = new HashMap<>();
                bulkQueryMap2.put("common.fields.code", code);

                // 设置更新值
                HashMap<String, Object> bulkUpdateMap2 = new HashMap<>();
                if (model.getPlatformStatus() != null) {
                    bulkUpdateMap2.put("platforms.P"+model.getCartId() + ".pStatus", model.getPlatformStatus().name());
                }
                if (!isPublishedProducts.get(code)) {
                    bulkUpdateMap2.put("platforms.P"+model.getCartId() + ".pPublishTime", model.getPublishTime());
                    bulkUpdateMap2.put("platforms.P"+model.getCartId() + ".pNumIId", model.getNumIId());
                    bulkUpdateMap2.put("platforms.P"+model.getCartId() + ".pProductId", model.getPlatformPid());
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

    /**
     * 获取聚美下面的所有group的codes大于1的数据,然后将其对应的group按一个code一个group做拆分,并删除以前的group.
     * @param channelId 渠道Id
     * @return
     */
    public String splitJmProductGroup (String channelId) {
        List<CmsBtProductGroupModel> allGroupList = cmsBtProductGroupDao.selectGroupInfoByMoreProductCodes(channelId, 27);


        $info("处理的channelId:" + channelId + ",取得group的总件数:" + allGroupList.size());
        for(CmsBtProductGroupModel groupInfo : allGroupList) {

            if (groupInfo.getProductCodes().size() > 0) {

                for(String code : groupInfo.getProductCodes()) {
                    CmsBtProductGroupModel newGroupInfo = new CmsBtProductGroupModel();
                    BeanUtils.copyProperties(groupInfo, newGroupInfo);
                    newGroupInfo.set_id(null);
                    newGroupInfo.setGroupId(commSequenceMongoService.getNextSequence(MongoSequenceService.CommSequenceName.CMS_BT_PRODUCT_GROUP_ID));
                    newGroupInfo.setMainProductCode(code);

                    List<String> productCodes = new ArrayList<>();
                    productCodes.add(code);
                    newGroupInfo.setProductCodes(productCodes);

                    if (!code.equals(groupInfo.getMainProductCode())) {
                        newGroupInfo.setPlatformStatus(CmsConstants.PlatformStatus.WaitingPublish);
                        newGroupInfo.setInStockTime("");
                        newGroupInfo.setNumIId("");
                        newGroupInfo.setPlatformPid("");
                        newGroupInfo.setPublishTime("");
                        newGroupInfo.setOnSaleTime("");
                    }

                    $info("插入新group数据内容" + JacksonUtil.bean2Json(newGroupInfo));

                    cmsBtProductGroupDao.insert(newGroupInfo);

                }

                $info("插入新数据成功,删除旧的group数据:" + JacksonUtil.bean2Json(groupInfo));
                cmsBtProductGroupDao.deleteById(groupInfo.get_id(), groupInfo.getChannelId());

            }else {
                break;
            }
        }

        return "成功处理group的总件数:" + allGroupList.size();
    }
}
