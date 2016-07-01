package com.voyageone.service.impl.cms.product;

import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.base.dao.mongodb.JomgoUpdate;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.ListUtils;
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
     */
    public CmsBtProductGroupModel selectProductGroupByCode(String channelId, String code, Integer cartId) {
        JomgoQuery query = new JomgoQuery();
        query.setQuery(String.format("{\"productCodes\": \"%s\", \"cartId\": %d}", code, cartId));
        return getProductGroupByQuery(channelId, query);
    }

    /**
     * 根据channelId和产品Code检索出是否主商品.
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
        queryObject.setQuery("{\"productCodes\":\"" + prodList.get(0).getCommon().getFields().getCode() + "\",\"cartId\":" + cartId + "}");
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
     * 上新成功时更新该model对应的所有和上新有关的状态信息
     * @param model (model中包含的productCodes,是这次平台上新处理的codes)
     * @return CmsBtProductGroupModel
     */
    public CmsBtProductGroupModel updateGroupsPlatformStatus(CmsBtProductGroupModel model) {

        // 更新cms_bt_product_groups表
        this.update(model);

        // 如果传入的groups包含code列表,则同时更新code的状态
        if (model.getProductCodes().size() > 0) {

            // 获取未上新过的产品code信息,用于判断是否需要更新publishTime
            List<String> unPublishedProducts = getUnPublishedProducts(model);

            // 批量更新产品的平台状态.
            List<BulkUpdateModel> bulkList = new ArrayList<>();
            for (String code : model.getProductCodes()) {
                // 设置批量更新条件
                HashMap<String, Object> bulkQueryMap = new HashMap<>();
                bulkQueryMap.put("common.fields.code", code);
                // 产品code就应该可以唯一确定一条记录了，不用再加下面这个条件，db中cartId应该是int，有些数据是string，会出现找不到插入的问题
//                bulkQueryMap.put("platforms.P"+model.getCartId() + ".cartId", model.getCartId());

                // 设置更新值
                HashMap<String, Object> bulkUpdateMap = new HashMap<>();
                if (model.getPlatformStatus() != null) {
                    bulkUpdateMap.put("platforms.P" + model.getCartId() + ".pStatus", model.getPlatformStatus().name());
                }
                // 设置第一次上新的时候需要更新的值
                if (unPublishedProducts.contains(code)) {
                    bulkUpdateMap.put("platforms.P" + model.getCartId() + ".pPublishTime", DateTimeUtil.getNowTimeStamp());
                    if (!StringUtils.isEmpty(model.getNumIId())) {
                        bulkUpdateMap.put("platforms.P" + model.getCartId() + ".pNumIId", model.getNumIId());
                    }
                    if (!StringUtils.isEmpty(model.getPlatformPid())) {
                        bulkUpdateMap.put("platforms.P" + model.getCartId() + ".pProductId", model.getPlatformPid());
                    }
                }
                // 设置pPublishError：如果上新成功则更新成功则清空，如果上新失败，设置固定值"Error"
                // 这个方法是用于上新成功时的回写，上新失败时的回写用另外一个方法
                bulkUpdateMap.put("platforms.P" + model.getCartId() + ".pPublishError", "");

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
                // 因为是回写产品状态，找不到产品时也不插入新错误的记录
                cmsBtProductDao.bulkUpdateWithMap(model.getChannelId(), bulkList, null, "$set", false);
            }
        }

        return model;
    }

    /**
     * 上新失败时更新该model对应的所有产品的pPublishError的值
     * @param model (model中包含的productCodes,是这次平台上新处理的codes)
     * @return boolean 更新结果状态
     */
    public boolean updateUploadErrorStatus(CmsBtProductGroupModel model) {

        // 如果传入的groups包含code列表,则同时更新code的状态
        if (model.getProductCodes().size() > 0) {

            // 批量更新产品的平台状态.
            List<BulkUpdateModel> bulkList = new ArrayList<>();
            for (String code : model.getProductCodes()) {
                // 设置批量更新条件
                HashMap<String, Object> bulkQueryMap = new HashMap<>();
                bulkQueryMap.put("common.fields.code", code);

                // 设置更新值
                HashMap<String, Object> bulkUpdateMap = new HashMap<>();
                // 设置pPublishError：如果上新失败，设置固定值"Error"
                // 这个方法是用于上新成功时的回写，上新失败时的回写用另外一个方法
                bulkUpdateMap.put("platforms.P" + model.getCartId() + ".pPublishError", "Error");

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
                // 因为是回写产品状态，找不到产品时也不插入新错误的记录
                cmsBtProductDao.bulkUpdateWithMap(model.getChannelId(), bulkList, null, "$set", false);
            }
        }

        return true;
    }

    /**
     * 获取指定product group下所有产品中，没有上新过的产品code列表
     * @param model (model中包含的productCodes,是这次平台上新处理的codes)
     * @return CmsBtProductGroupModel
     */
    public List<String> getUnPublishedProducts(CmsBtProductGroupModel model) {
        // 获取未上新过的产品信息,用于判断是否需要更新publishTime
        JomgoQuery queryObject = new JomgoQuery();
        queryObject.setQuery("{'common.fields.code':{$in:#}, 'platforms.P" + model.getCartId() + ".pStatus':{$in:[null, '', 'WaitingPublish']}}");
        queryObject.setParameters(model.getProductCodes());

        // 如果该产品已经上新过,则对应值为true,否则为false
        queryObject.setProjection("{\"common.fields.code\": 1}");
        List<CmsBtProductModel> products = cmsBtProductDao.select(queryObject, model.getChannelId());

        List<String> unPublishedProducts = new ArrayList<>();
        if (ListUtils.notNull(products)) {
            products.stream().map(p -> unPublishedProducts.add(p.getCommon().getFields().getCode()));
        }

        return unPublishedProducts;
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

    public WriteResult updateMulti(JomgoUpdate updObj, String channelId) {
        return cmsBtProductGroupDao.updateMulti(updObj, channelId);
    }

    public long countByQuery(final String strQuery, String channelId) {
        return cmsBtProductGroupDao.countByQuery(strQuery, channelId);
    }
}
