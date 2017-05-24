package com.voyageone.service.impl.cms.product;

import com.google.common.base.Joiner;
import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
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
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
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

    @Autowired
    private ProductService productService;

    /**
     * getList
     */
    public List<CmsBtProductGroupModel> getList(String channelId, JongoQuery queryObject) {
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
        JongoQuery query = new JongoQuery();
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
     * @param query JongoQuery
     * @return CmsBtProductGroupModel
     */
    public CmsBtProductGroupModel getProductGroupByQuery(String channelId, JongoQuery query) {
        return cmsBtProductGroupDao.selectOneWithQuery(query, channelId);
    }

    /**
     * 根据channelId和产品Code,cartId检索出productGroup数据.
     */
    public CmsBtProductGroupModel selectProductGroupByCode(String channelId, String code, Integer cartId) {
        JongoQuery query = new JongoQuery();
        query.setQuery(String.format("{\"productCodes\": \"%s\", \"cartId\": %d}", code, cartId));
        return getProductGroupByQuery(channelId, query);
    }

    /**
     * 根据channelId和产品Code检索出productGroup数据列表.
     */
    public List<CmsBtProductGroupModel> selectProductGroupListByCode(String channelId, String code) {
        // 先去看看是否有存在的了
        JongoQuery queryObject = new JongoQuery();
        queryObject.setQuery("{\"productCodes\":\"" + code + "\"}");
        return getList(channelId, queryObject);
    }

    /**
     * 根据channelId和产品Code检索出是否主商品.
     */
    public CmsBtProductGroupModel selectMainProductGroupByCode(String channelId, String code, Integer cartId) {
        JongoQuery query = new JongoQuery();
        query.setQuery(String.format("{\"mainProductCode\": \"%s\", \"cartId\": %d}", code, cartId));
        return getProductGroupByQuery(channelId, query);
    }

    /**
     * 根据channelId和numIId检索
     */
    public CmsBtProductGroupModel selectProductGroupByNumIId(String channelId, Integer cartId, String numIId) {
        JongoQuery query = new JongoQuery();
        query.setQuery(String.format("{\"numIId\": \"%s\", \"cartId\": %d}", numIId, cartId));
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
    public WriteResult updateMainProduct(String channelId, String productCode, Long groupId, String modifier) {
        JongoUpdate query = new JongoUpdate();
        query.setQuery("{\"groupId\": #}");
        query.setQueryParameters(groupId);

        query.setUpdate("{$set: {\"mainProductCode\": #, \"modifier\": #}}");
        query.setUpdateParameters(productCode, modifier);

        //// TODO: 16/7/8 如果两个商品的平台类目不一致,并且该商品已经上新则不能切换主商品


        // 更新group下面的mainProductCode
        return cmsBtProductGroupDao.updateMulti(query, channelId);  //.update(channelId, queryMap, updateMap);
    }

    /**
     * 根据modelCode, cartId获取商品的group的Model
     */
    public CmsBtProductGroupModel selectProductGroupByModelCodeAndCartId(String channelId, String modelCode, String cartId) {
        return selectProductGroupByModelCodeAndCartId(channelId, modelCode, cartId, null);
    }

    /**
     * 根据modelCode, cartId获取商品的group的Model(有orgChannelId)
     */
    public CmsBtProductGroupModel selectProductGroupByModelCodeAndCartId(String channelId, String modelCode, String cartId, String orgChannelId) {
        // jeff 2016/04 change start
        // String query = String.format("{\"feed.orgAtts.modelCode\":\"%s\"}, {\"fields.code\":1}", modelCode);
        // desmond 2016/07/04 update start
        //String query = String.format("{\"feed.orgAtts.modelCode\":\"%s\",\"fields.isMasterMain\":1},{\"fields.code\":1}", modelCode);
        // 检索条件(feed.orgAtts.modelCode = modelCode && common.fields.isMasterMain = 1)
//        String query = String.format("{\"feed.orgAtts.modelCode\":\"%s\",\"common.fields.isMasterMain\":1},{\"common.fields.code\":1}", modelCode);
        JongoQuery query = new JongoQuery();
//        String query = String.format("{\"common.fields.model\":\"%s\"},{\"common.fields.code\":1}", modelCode);
        if (!StringUtils.isEmpty(orgChannelId)) {
            // 由于可能存在2个子店的Product.model相同的情况，如果不加orgChannelId只用model去查product的话，会导致查出来别的店铺的product对应的group
            query.setQuery(String.format("{\"common.fields.model\":\"%s\", orgChannelId:\"%s\"}", modelCode, orgChannelId));
            query.setProjectionExt("common.fields.code");
        } else {
            CmsChannelConfigBean  cmsChannelConfigBean= CmsChannelConfigs.getConfigBean(channelId, CmsConstants.ChannelConfig.SPLIT_QUARTER_BY_CODE, "0");
            if(cmsChannelConfigBean != null && channelId.equals(cmsChannelConfigBean.getChannelId())){
                query.setQuery(String.format("{\"common.fields.model\":\"%s\"}", modelCode));
                query.setProjectionExt("common.fields.code","created");
                query.setSort("{\"created\":-1}");
            }else {
                query.setQuery(String.format("{\"common.fields.model\":\"%s\"}", modelCode));
                query.setProjectionExt("common.fields.code");
            }
        }
        // desmond 2016/07/04 update end
        // jeff 2016/04 change end
        // 一个feed.model(下面有多个product)对应一个平台group，所以先根据feed.model找到同一个model下面的其他product，就可以找到同一个group了
        List<CmsBtProductModel> prodList = cmsBtProductDao.select(query, channelId);
        if (prodList == null || prodList.isEmpty()) {
            return null;
        }
        // jeff 2016/04 change start
        // List<String> codeList = new ArrayList<>(prodList.size());
        // prodList.forEach(cmsBtProductModel -> codeList.add(cmsBtProductModel.getFields().getCode()));
        JongoQuery queryObject = new JongoQuery();
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
     * @param listSxCode 上新了的code
     * @return CmsBtProductGroupModel
     */
    public CmsBtProductGroupModel updateGroupsPlatformStatus(CmsBtProductGroupModel model, List<String> listSxCode) {
        return updateGroupsPlatformStatus(model, listSxCode, null);
    }

    /**
     * 上新成功时更新该model对应的所有和上新有关的状态信息
     * @param model (model中包含的productCodes,是这次平台上新处理的codes)
     * @param listSxCode 上新了的code
     * @param pCatInfoMap 平台类目信息
     * @return CmsBtProductGroupModel
     */
    // modified by morse.lu 2016/08/08 start
    // 一个group下可能有些code不上新，就不要回写了
//    public CmsBtProductGroupModel updateGroupsPlatformStatus(CmsBtProductGroupModel model) {
    public CmsBtProductGroupModel updateGroupsPlatformStatus(CmsBtProductGroupModel model, List<String> listSxCode, Map<String, String> pCatInfoMap) {
        // modified by morse.lu 2016/08/08 end

        if (model == null) {
            $error("回写上新成功状态信息时失败! [GroupModel=null]");
            return model;
        }

        // 更新cms_bt_product_groups表
        model.setModified(DateTimeUtil.getNow());
        this.update(model);

        // 如果传入的groups包含code列表,则同时更新code的状态
        if (!model.getProductCodes().isEmpty()) {

            // 获取未上新过的产品code信息,用于判断是否需要更新publishTime
            List<String> unPublishedProducts = getUnPublishedProducts(model);

            // 批量更新产品的平台状态.
            List<BulkUpdateModel> bulkList = new ArrayList<>();
            // modified by morse.lu 2016/08/08 start
            // 一个group下可能有些code不上新，就不要回写了
//            for (String code : model.getProductCodes()) {
            if (ListUtils.isNull(listSxCode)) {
                $error("回写上新成功状态信息时失败!上新对象产品Code列表为空 [listSxCode=null]");
                return model;
            }

            for (String code : listSxCode) {
                // modified by morse.lu 2016/08/08 end
                // 设置批量更新条件
                HashMap<String, Object> bulkQueryMap = new HashMap<>();
                bulkQueryMap.put("common.fields.code", code);
                // 产品code就应该可以唯一确定一条记录了，不用再加下面这个条件，db中cartId应该是int，有些数据是string，会出现找不到插入的问题
//                bulkQueryMap.put("platforms.P"+model.getCartId() + ".cartId", model.getCartId());

                // 设置更新值
                HashMap<String, Object> bulkUpdateMap = new HashMap<>();
                if (model.getPlatformStatus() != null) {
                    // cms系统中的上下架状态
                    bulkUpdateMap.put("platforms.P" + model.getCartId() + ".pStatus", model.getPlatformStatus().name());
                    // 平台上真实的上下架状态，会有另外一个batch每天从平台上拉一次最新的商品上下架状态，保存到这里
                    bulkUpdateMap.put("platforms.P" + model.getCartId() + ".pReallyStatus", model.getPlatformStatus().name());
                }
                // 设置第一次上新的时候需要更新的值
                if (unPublishedProducts.contains(code)) {
                    bulkUpdateMap.put("platforms.P" + model.getCartId() + ".pPublishTime", DateTimeUtil.getNowTimeStamp());
                }

                // 有可能会变更numIId(比如天猫平台)，所以不能只在第一次上新的时候设置pNumIId,每次都要设置成跟group一样的值
                bulkUpdateMap.put("platforms.P" + model.getCartId() + ".pNumIId", model.getNumIId());
                bulkUpdateMap.put("platforms.P" + model.getCartId() + ".pProductId", model.getPlatformPid());

                // 设置pPublishError：如果上新成功则更新成功则清空，如果上新失败，设置固定值"Error"
                // 这个方法是用于上新成功时的回写，上新失败时的回写用另外一个方法
                bulkUpdateMap.put("platforms.P" + model.getCartId() + ".pPublishError", "");
                // 设置pPublishMessage(产品平台详情页显示用的错误信息)清空
                bulkUpdateMap.put("platforms.P" + model.getCartId() + ".pPublishMessage", "");

                // 上新成功时，把platforms.Pxx.isNewSku设为"0"(子店到Liking总店上新不算，feed->master导入时928cart也没有设置成"1")
                if (928 != model.getCartId()) {
                    bulkUpdateMap.put("platforms.P" + model.getCartId() + ".isNewSku", "0");
                }

                // 如果需要回写该平台的pCatId和pCatPath的时候
                if (pCatInfoMap != null && pCatInfoMap.size() > 0) {
                    if (!StringUtils.isEmpty(pCatInfoMap.get("pCatId")))
                        bulkUpdateMap.put("platforms.P" + model.getCartId() + ".pCatId", pCatInfoMap.get("pCatId"));

                    if (!StringUtils.isEmpty(pCatInfoMap.get("pCatPath")))
                        bulkUpdateMap.put("platforms.P" + model.getCartId() + ".pCatPath", pCatInfoMap.get("pCatPath"));

                    if (!StringUtils.isEmpty(pCatInfoMap.get("pCatId"))
                            || !StringUtils.isEmpty(pCatInfoMap.get("pCatPath")))
                        bulkUpdateMap.put("platforms.P" + model.getCartId() + ".pCatStatus", "1");
                }

                // 更新时间
                bulkUpdateMap.put("platforms.P" + model.getCartId() + ".modified", model.getModified());
                // 更新者
                bulkUpdateMap.put("platforms.P" + model.getCartId() + ".modifier", model.getModifier());

                // 设定批量更新条件和值
                if (!bulkUpdateMap.isEmpty()) {
                    BulkUpdateModel bulkUpdateModel = new BulkUpdateModel();
                    bulkUpdateModel.setUpdateMap(bulkUpdateMap);
                    bulkUpdateModel.setQueryMap(bulkQueryMap);
                    bulkList.add(bulkUpdateModel);
                }
            }

            // 批量更新product表
            if (!bulkList.isEmpty()) {
                // 因为是回写产品状态，找不到产品时也不插入新错误的记录
                cmsBtProductDao.bulkUpdateWithMap(model.getChannelId(), bulkList, null, "$set", false);
            }
        }

        $info("上新成功时回写group和产品表上新状态信息结束！[groupId:%s] [codes:%s]",
                model.getGroupId(), ListUtils.isNull(listSxCode) ? "空" : Joiner.on(",").join(listSxCode));

        return model;
    }

    /**
     * 上新失败时更新该model对应的所有产品的pPublishError和pPublishMessage的值
     * @param model CmsBtProductGroupModel model中包含的productCodes,是这次平台上新处理的codes
     * @param errMsg String sxData中的上新错误消息
     * @return boolean 更新结果状态
     */
    public boolean updateUploadErrorStatus(CmsBtProductGroupModel model, List<String> listSxCode, String errMsg) {

        if (model == null) {
            $error("回写上新错误信息时失败! [GroupModel=null]");
            return false;
        }

        // 如果传入的groups包含code列表,则同时更新code的状态
        if (ListUtils.isNull(listSxCode)) {
            $error("回写上新失败状态信息时失败!上新对象产品Code列表为空 [listSxCode=null]");
            return false;
        }

        // 批量更新产品的平台状态.
        List<BulkUpdateModel> bulkList = new ArrayList<>();
        for (String code : listSxCode) {
            // 设置批量更新条件
            HashMap<String, Object> bulkQueryMap = new HashMap<>();
            bulkQueryMap.put("common.fields.code", code);

            // 设置更新值
            HashMap<String, Object> bulkUpdateMap = new HashMap<>();
            // 设置pPublishError：如果上新失败，设置固定值"Error"
            // 这个方法是用于上新成功时的回写，上新失败时的回写用另外一个方法
            bulkUpdateMap.put("platforms.P" + model.getCartId() + ".pPublishError", "Error");
            // 设置pPublishMessage(产品平台详情页显示用的错误信息)
            bulkUpdateMap.put("platforms.P" + model.getCartId() + ".pPublishMessage", errMsg);

            // 更新时间
            bulkUpdateMap.put("platforms.P" + model.getCartId() + ".modified", model.getModified());
            // 更新者
            bulkUpdateMap.put("platforms.P" + model.getCartId() + ".modifier", model.getModifier());

            // 设定批量更新条件和值
            if (!bulkUpdateMap.isEmpty()) {
                BulkUpdateModel bulkUpdateModel = new BulkUpdateModel();
                bulkUpdateModel.setUpdateMap(bulkUpdateMap);
                bulkUpdateModel.setQueryMap(bulkQueryMap);
                bulkList.add(bulkUpdateModel);
            }
        }

        // 批量更新product表
        if (!bulkList.isEmpty()) {
            // 因为是回写产品状态，找不到产品时也不插入新错误的记录
            cmsBtProductDao.bulkUpdateWithMap(model.getChannelId(), bulkList, null, "$set", false);
        }

        $info("上新失败时回写group和产品表上新状态信息结束！[groupId:%s] [codes:%s] [errMsg:%s]",
                model.getGroupId(), ListUtils.isNull(listSxCode) ? "空" : Joiner.on(",").join(listSxCode), errMsg);

        return true;
    }

    /**
     * 获取指定product group下所有产品中，没有上新过的产品code列表
     * @param model (model中包含的productCodes,是这次平台上新处理的codes)
     * @return CmsBtProductGroupModel
     */
    public List<String> getUnPublishedProducts(CmsBtProductGroupModel model) {
        // 获取未上新过的产品信息,用于判断是否需要更新publishTime
        JongoQuery queryObject = new JongoQuery();
//        queryObject.setQuery("{'common.fields.code':{$in:#}, 'platforms.P" + model.getCartId() + ".pStatus':{$in:[null, '', 'WaitingPublish']}}");
        queryObject.setQuery("{'common.fields.code':{$in:#}, $or: [{'platforms.P" + model.getCartId() + ".pStatus':{$in:[null, '', 'WaitingPublish']}}, {'platforms.P" + model.getCartId() + ".pPublishTime':{$in:[null, '']}}]}");
        queryObject.setParameters(model.getProductCodes());

        // 如果该产品已经上新过,则对应值为true,否则为false
        queryObject.setProjection("{\"common.fields.code\": 1}");
        List<CmsBtProductModel> products = cmsBtProductDao.select(queryObject, model.getChannelId());

        List<String> unPublishedProducts = new ArrayList<>();
        if (ListUtils.notNull(products)) {
            products.forEach(p -> unPublishedProducts.add(p.getCommon().getFields().getCode()));
        }

        return unPublishedProducts;
    }

    /**
     * 获取聚美下面的所有group的codes大于1的数据,然后将其对应的group按一个code一个group做拆分,并删除以前的group.
     * @param channelId 渠道Id
     */
    public String splitJmProductGroup (String channelId) {
        List<CmsBtProductGroupModel> allGroupList = cmsBtProductGroupDao.selectGroupInfoByMoreProductCodes(channelId, 27);


        $info("处理的channelId:" + channelId + ",取得group的总件数:" + allGroupList.size());
        for(CmsBtProductGroupModel groupInfo : allGroupList) {

            if (!groupInfo.getProductCodes().isEmpty()) {

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
                        newGroupInfo.setPlatformStatus(null);
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

    public WriteResult updateFirst(JongoUpdate updObj, String channelId) {
        return cmsBtProductGroupDao.updateMulti(updObj, channelId);
    }

    public long countByQuery(final String strQuery, String channelId) {
        return cmsBtProductGroupDao.countByQuery(strQuery, channelId);
    }

    /**
     * 计算group的价格区间
     */
    public void calculatePriceRange (CmsBtProductGroupModel groupModel) {
        Integer cartId = groupModel.getCartId();
        Double priceSaleSt = null;
        Double priceSaleEd = null;
        Double priceRetailSt = null;
        Double priceRetailEd = null;
        Double priceMsrpSt = null;
        Double priceMsrpEd = null;
        for (String code : groupModel.getProductCodes()) {
            CmsBtProductModel productModel = productService.getProductByCode(groupModel.getChannelId(), code);
            if (productModel != null) {
                if (cartId < CmsConstants.ACTIVE_CARTID_MIN) {
                    priceSaleSt = 0.0;
                    priceSaleEd = 0.0;
                    for (CmsBtProductModel_Sku skuModel : productModel.getCommon().getSkus()) {
                        Double skuPriceRetail = skuModel.getPriceRetail();
                        if (priceRetailSt == null || (skuPriceRetail != null && skuPriceRetail < priceRetailSt)) {
                            priceRetailSt = skuPriceRetail;
                        }
                        if (priceRetailEd == null || (skuPriceRetail != null && skuPriceRetail > priceRetailEd)) {
                            priceRetailEd = skuPriceRetail;
                        }

                        Double skuPriceMsrp = skuModel.getPriceMsrp();
                        if (priceMsrpSt == null || (skuPriceMsrp != null && skuPriceMsrp < priceMsrpSt)) {
                            priceMsrpSt = skuPriceMsrp;
                        }
                        if (priceMsrpEd == null || (skuPriceMsrp != null && skuPriceMsrp > priceMsrpEd)) {
                            priceMsrpEd = skuPriceMsrp;
                        }
                    }
                } else {
                    for (Map.Entry<String, CmsBtProductModel_Platform_Cart> platform : productModel.getPlatforms().entrySet()) {
                        // 找到对应的平台信息
                        if (cartId.equals(platform.getValue().getCartId())) {
                            for (Map<String, Object> sku : platform.getValue().getSkus()) {
                                Object objSkuPriceSale = sku.get("priceSale");
                                Double skuPriceSale = null;
                                if (objSkuPriceSale != null) {
                                    skuPriceSale = new Double(String.valueOf(objSkuPriceSale));
                                }
                                if (priceSaleSt == null || (skuPriceSale != null && skuPriceSale < priceSaleSt)) {
                                    priceSaleSt = skuPriceSale;
                                }
                                if (priceSaleEd == null || (skuPriceSale != null && skuPriceSale > priceSaleEd)) {
                                    priceSaleEd = skuPriceSale;
                                }

                                Object objSkuPriceRetail = sku.get("priceRetail");
                                Double skuPriceRetail = null;
                                if (objSkuPriceRetail != null) {
                                    skuPriceRetail = new Double(String.valueOf(objSkuPriceRetail));
                                }
                                if (priceRetailSt == null || (skuPriceRetail != null && skuPriceRetail < priceRetailSt)) {
                                    priceRetailSt = skuPriceRetail;
                                }
                                if (priceRetailEd == null || (skuPriceRetail != null && skuPriceRetail > priceRetailEd)) {
                                    priceRetailEd = skuPriceRetail;
                                }

                                Object objSkuPriceMsrp = sku.get("priceMsrp");
                                Double skuPriceMsrp = null;
                                if (objSkuPriceMsrp != null) {
                                    skuPriceMsrp = new Double(String.valueOf(objSkuPriceMsrp));
                                }
                                if (priceMsrpSt == null || (skuPriceMsrp != null && skuPriceMsrp < priceMsrpSt)) {
                                    priceMsrpSt = skuPriceMsrp;
                                }
                                if (priceMsrpEd == null || (skuPriceMsrp != null && skuPriceMsrp > priceMsrpEd)) {
                                    priceMsrpEd = skuPriceMsrp;
                                }
                            }
                            break;
                        }
                    }
                }
            }
        }

        groupModel.setPriceSaleSt(priceSaleSt);
        groupModel.setPriceSaleEd(priceSaleEd);
        groupModel.setPriceRetailSt(priceRetailSt);
        groupModel.setPriceRetailEd(priceRetailEd);
        groupModel.setPriceMsrpSt(priceMsrpSt);
        groupModel.setPriceMsrpEd(priceMsrpEd);
    }

    /**
     * 新建一个新的Group
     * @param channelId
     * @param cartId
     * @param productCode
     * @param isCalculatePriceRange
     * @return
     */
    public CmsBtProductGroupModel createNewGroup(String channelId, Integer cartId, String productCode, Boolean isCalculatePriceRange) {

        CmsBtProductGroupModel group = new CmsBtProductGroupModel();

        // 渠道id
        group.setChannelId(channelId);

        // cart id
        group.setCartId(cartId);

        // 获取唯一编号
        group.setGroupId(commSequenceMongoService.getNextSequence(MongoSequenceService.CommSequenceName.CMS_BT_PRODUCT_GROUP_ID));

        // 主商品Code
        group.setMainProductCode(productCode);

        // platform status:发布状态: 未上新 // Synship.com_mt_type : id = 45
        group.setPlatformStatus(null);

        CmsChannelConfigBean cmsChannelConfigBean = CmsChannelConfigs.getConfigBean(channelId
                , CmsConstants.ChannelConfig.PLATFORM_ACTIVE
                , String.valueOf(group.getCartId()));
        if (cmsChannelConfigBean != null && !com.voyageone.common.util.StringUtils.isEmpty(cmsChannelConfigBean.getConfigValue1())) {
            if (CmsConstants.PlatformActive.ToOnSale.name().equals(cmsChannelConfigBean.getConfigValue1())) {
                group.setPlatformActive(CmsConstants.PlatformActive.ToOnSale);
            } else {
                // platform active:上新的动作: 暂时默认是放到:仓库中
                group.setPlatformActive(CmsConstants.PlatformActive.ToInStock);
            }
        } else {
            // platform active:上新的动作: 暂时默认是放到:仓库中
            group.setPlatformActive(CmsConstants.PlatformActive.ToInStock);
        }

        // ProductCodes
        List<String> codes = new ArrayList<>();
        codes.add(productCode);
        group.setProductCodes(codes);
        group.setCreater(getClass().getName());
        group.setModifier(getClass().getName());

        if(isCalculatePriceRange){
            // 计算group价格区间
            calculatePriceRange(group);
        }
        return group;
    }

    /**
     * 将一个code加入到对应已经存在group中
     * @param channelId
     * @param cartId
     * @param productCode
     * @param mainProductCode
     * @param isCalculatePriceRange
     * @return
     */
    public CmsBtProductGroupModel addCodeToGroup(String channelId, Integer cartId, String productCode, String mainProductCode,Boolean isCalculatePriceRange) {
        JongoQuery query = new JongoQuery();
        query.setQuery("{\"cartId\": #, \"mainProductCode\": #}");
        query.setParameters(cartId, mainProductCode);
        CmsBtProductGroupModel group = cmsBtProductGroupDao.selectOneWithQuery(query, channelId);

        // 如果group存在则将该产品加入到该group中
        if (group != null) {
            group.getProductCodes().add(productCode);
        } else {
            throw new BusinessException(String.format("对应的group不存在,请确认正确的group数据(channelId: %s, cartId: %d, mainProductCode: %s)", channelId, cartId, mainProductCode));
        }

        if(isCalculatePriceRange){
            // 计算group价格区间
            calculatePriceRange(group);
        }
        return group;

    }

    /**
     * 根据cartId来判断是新建group还是将现有productCode加入已经存在group中
     * @param channelId
     * @param cartId
     * @param productCode
     * @param mainProductCode
     * @param isCalculatePriceRange
     * @return
     */
    public CmsBtProductGroupModel creatOrUpdateGroup(String channelId, Integer cartId, String productCode, String mainProductCode,Boolean isCalculatePriceRange) {
        if (CartEnums.Cart.JM.getValue() == cartId
                || CartEnums.Cart.LCN.getValue() == cartId
                || CartEnums.Cart.CN.getValue() == cartId
                || CartEnums.Cart.DT.getValue() == cartId) {
            return createNewGroup(channelId, cartId, productCode, isCalculatePriceRange);
        } else {
            return addCodeToGroup(channelId, cartId, productCode, mainProductCode, isCalculatePriceRange);
        }
    }


    /**
     * 删除group
     */
    public void deleteGroup (CmsBtProductGroupModel groupModel) {
        cmsBtProductGroupDao.delete(groupModel);
    }
}
