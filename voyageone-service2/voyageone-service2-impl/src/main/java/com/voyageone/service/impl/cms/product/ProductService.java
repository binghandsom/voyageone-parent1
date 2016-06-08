package com.voyageone.service.impl.cms.product;

import com.google.common.base.Joiner;
import com.mongodb.BasicDBObject;
import com.mongodb.BulkWriteResult;
import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.base.dao.mongodb.JomgoUpdate;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Enums.ChannelConfigEnums.Channel;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.util.BeanUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.MongoUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.product.*;
import com.voyageone.service.dao.cms.mongo.CmsBtFeedInfoDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductGroupDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductLogDao;
import com.voyageone.service.dao.wms.WmsBtInventoryCenterLogicDao;
import com.voyageone.service.daoext.cms.CmsBtPriceLogDaoExt;
import com.voyageone.service.daoext.cms.CmsBtSxWorkloadDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.ImageTemplateService;
import com.voyageone.service.impl.cms.MongoSequenceService;
import com.voyageone.service.impl.cms.feed.FeedMappingService;
import com.voyageone.service.model.cms.CmsBtPriceLogModel;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.product.*;
import com.voyageone.service.model.wms.WmsBtInventoryCenterLogicModel;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

/**
 * product Service
 *
 * @author chuanyu.liang 15/12/9
 * @version 2.0.1
 */
@Service
public class ProductService extends BaseService {

    @Autowired
    protected CmsBtProductLogDao cmsBtProductLogDao;

    @Autowired
    private CmsBtProductGroupDao cmsBtProductGroupDao;

    @Autowired
    private CmsBtProductDao cmsBtProductDao;

    @Autowired
    private CmsBtPriceLogDaoExt cmsBtPriceLogDaoExt;

    @Autowired
    private ProductSkuService productSkuService;

    @Autowired
    private CmsBtSxWorkloadDaoExt cmsBtSxWorkloadDaoExt;

    @Autowired
    private CmsBtFeedInfoDao cmsBtFeedInfoDao;

    @Autowired
    private WmsBtInventoryCenterLogicDao wmsBtInventoryCenterLogicDao;

    @Autowired
    private FeedMappingService feedMappingService;

    @Autowired
    private ImageTemplateService imageTemplateService;

    @Autowired
    private MongoSequenceService commSequenceMongoService;

    /**
     * 获取商品 根据ID获
     */
    public CmsBtProductModel getProductById(String channelId, long prodId) {
        String query = "{\"prodId\":" + prodId + "}";
        return cmsBtProductDao.selectOneWithQuery(query, channelId);
    }

    /**
     * 获取商品 根据Code
     */
    public CmsBtProductModel getProductByCode(String channelId, String code) {
        String query = "{\"fields.code\":\"" + code + "\"}";
        return cmsBtProductDao.selectOneWithQuery(query, channelId);
    }

    /**
     * 获取商品 根据Code
     */
    public CmsBtProductModel getProductSingleSku(String channelId, String code, String originalCode) {
        String query = "{\"fields.code\":\"" + code + "\", \"fields.originalCode\":\"" + originalCode + "\"}";
        return cmsBtProductDao.selectOneWithQuery(query, channelId);
    }

    /**
     * 获取商品 根据OriginalCode
     */
    public List<CmsBtProductModel> getProductByOriginalCode(String channelId, String code) {
        String query = "{\"fields.originalCode\":\"" + code + "\"}";
        return cmsBtProductDao.select(query, channelId);
    }

    /**
     * 获取商品 Sku
     */
    public CmsBtProductModel getProductBySku(String channelId, String sku) {
        String query = "{\"skus.skuCode\":\"" + sku + "\"}";
        return cmsBtProductDao.selectOneWithQuery(query, channelId);
    }

    /**
     * 获取商品 根据query
     */
    public CmsBtProductModel getProductByCondition(String channelId, JomgoQuery query) {
        return cmsBtProductDao.selectOneWithQuery(query, channelId);
    }

    /**
     * 根据多个groupsIds获取产品列表
     *
     * @param channelId String
     * @param groupId   Long
     * @param flag:     true:检索主商品以外的商品,false:检索所有的商品
     * @return List<CmsBtProductModel>
     */
    public List<CmsBtProductModel> getProductByGroupId(String channelId, Long groupId, Boolean flag) {
        JomgoQuery queryObject = new JomgoQuery();
        queryObject.setQuery(String.format("{ \"groupId\":%d}", groupId));
        CmsBtProductGroupModel grpObj = cmsBtProductGroupDao.selectOneWithQuery(queryObject, channelId);
        if (grpObj == null) {
            return null;
        }
        List<String> codeList = grpObj.getProductCodes();
        if (codeList == null || codeList.isEmpty()) {
            return null;
        }
        if (flag) {
            // 检索主商品以外的商品时，若该组仅有一个主商品，没有子商品，则返回空
            if (codeList.size() <= 1) {
                return null;
            } else {
                codeList.remove(0);
            }
        }

        String[] codeArr = new String[codeList.size()];
        codeArr = codeList.toArray(codeArr);
        queryObject.setQuery("{" + MongoUtils.splicingValue("fields.code", codeArr, "$in") + "}");

        List<CmsBtProductModel> rst = cmsBtProductDao.select(queryObject, channelId);
        rst.forEach(prodObj -> prodObj.setGroups(grpObj));
        return rst;
    }

    // 查询指定平台下各商品组中包含的商品code
    // 返回的map中，key是group id，value中是商品code列表
    public Map<String, List<String>> getProductGroupIdCodesMapByCart(String channelId, int cartId, String orgChannelId) {
        JomgoQuery queryObject = new JomgoQuery();
        queryObject.setQuery("{\"cartId\":" + cartId + "}");
        queryObject.setProjection("{'groupId':1,'productCodes':1,'_id':0}");
        List<CmsBtProductGroupModel> grpList = cmsBtProductGroupDao.select(queryObject, channelId);

        Map<String, List<String>> result = new LinkedHashMap<>();
        for (CmsBtProductGroupModel grpObj : grpList) {
            result.put(Long.toString(grpObj.getGroupId()), grpObj.getProductCodes());
        }
        return result;
    }

    /**
     * getList
     */
    public List<CmsBtProductModel> getList(String channelId, Set<Long> pids, String[] projections) {
        JomgoQuery queryObject = new JomgoQuery();
        String pidsArrStr = Joiner.on(", ").skipNulls().join(pids);
        queryObject.setQuery(String.format("{ \"prodId\" : { $in : [ %s ] } }", pidsArrStr));
        queryObject.setProjectionExt(projections);
        return getList(channelId, queryObject);
    }

    /**
     * getList
     */
    public List<CmsBtProductModel> getList(String channelId, JomgoQuery queryObject) {
        return cmsBtProductDao.select(queryObject, channelId);
    }

    /**
     * getList
     */
    public List<CmsBtProductBean> getBeanList(String channelId, JomgoQuery queryObject) {
        return cmsBtProductDao.selectBean(queryObject, channelId);
    }

    // 查询产品信息(合并该产品的组信息)
    // queryObject中必须包含输出项:"fields.code"，否则将查询不到组信息
    public List<CmsBtProductBean> getListWithGroup(String channelId, int cartId, JomgoQuery queryObject) {
        List<CmsBtProductBean> prodList = cmsBtProductDao.selectBean(queryObject, channelId);
        if (prodList == null || prodList.isEmpty()) {
            return prodList;
        }

        for (CmsBtProductBean prodObj : prodList) {
            // 从group表合并platforms信息
            StringBuilder qurStr = new StringBuilder();
            qurStr.append(MongoUtils.splicingValue("cartId", cartId));
            qurStr.append(",");
            qurStr.append(MongoUtils.splicingValue("productCodes", prodObj.getFields().getCode()));

            // 在group表中过滤platforms相关信息
            JomgoQuery qrpQuy = new JomgoQuery();
            qrpQuy.setQuery("{" + qurStr.toString() + "}");
            List<CmsBtProductGroupModel> grpList = cmsBtProductGroupDao.select(qrpQuy, channelId);
            if (grpList == null || grpList.isEmpty()) {
                $warn("ProductService.getListWithGroup prodCode=" + prodObj.getFields().getCode());
            } else {
                CmsBtProductGroupModel groupModelMap = grpList.get(0);
                // 设置其group信息，用于画面显示
                long grpId = groupModelMap.getGroupId();
                CmsBtProductGroupModel platformModel = new CmsBtProductGroupModel();
                platformModel.setCartId(cartId);
                platformModel.setGroupId(grpId);
                platformModel.setNumIId(groupModelMap.getNumIId());
                platformModel.setInStockTime(groupModelMap.getInStockTime());
                platformModel.setOnSaleTime(groupModelMap.getOnSaleTime());
                platformModel.setPublishTime(groupModelMap.getPublishTime());
                platformModel.setQty(groupModelMap.getQty());
                platformModel.setPlatformStatus(groupModelMap.getPlatformStatus());
                platformModel.setPlatformActive(groupModelMap.getPlatformActive());
                prodObj.setGroups(platformModel);
                prodObj.setGroupBean(platformModel);
            }
        }
        return prodList;
    }

    /**
     * getCnt
     */
    public long getCnt(String channelId, String queryStr) {
        return cmsBtProductDao.countByQuery(queryStr, channelId);
    }

    /**
     * get prices log list
     */
    public List<CmsBtPriceLogModel> getPriceLog(String sku, String channelId, Map<String, Object> params) {
        params.put("channelId", channelId);
        params.put("sku", sku);
        return cmsBtPriceLogDaoExt.selectPriceLogByCode(params);
    }

    /**
     * get prices log list
     */
    public int getPriceLogCnt(String sku, String channelId, Map<String, Object> params) {
        params.put("channelId", channelId);
        params.put("sku", sku);
        return cmsBtPriceLogDaoExt.selectPriceLogByCodeCnt(params);
    }

    public boolean checkProductDataIsReady(String channelId, Long productId) {
        return cmsBtProductDao.checkProductDataIsReady(channelId, productId);
    }

    /**
     * 插入商品
     */
    public WriteResult insert(CmsBtProductModel model) {
        return cmsBtProductDao.insert(model);
    }

    /**
     * 插入商品
     */
    public WriteResult insert(Collection<CmsBtProductModel> models) {
        return cmsBtProductDao.insertWithList(models);
    }

    /**
     * add products
     */
    public void createProduct(String channelId, final CmsBtProductModel product, String modifier) {
        /**
         * check row exist
         */
        String prodIdQuery = String.format("{ \"prodId\" : %s }", product.getProdId());
        long count = cmsBtProductDao.countByQuery(prodIdQuery, channelId);
        if (count > 0) {
            throw new RuntimeException("prodId has existed, not add!");
        }

        String prodCodeQuery = String.format("{ \"fields.code\" : \"%s\" }", product.getFields().getCode());
        count = cmsBtProductDao.countByQuery(prodCodeQuery, channelId);
        if (count > 0) {
            throw new RuntimeException("fields.code has existed, not add!");
        }

        //update channel and modifier
        CmsBtProductGroupModel grp = product.getGroups();
        product.setGroups(null);
        product.setChannelId(channelId);
        product.setCreater(modifier);
        product.setModifier(modifier);

        //save
        cmsBtProductDao.insert(product);
        product.setGroups(grp);
    }

    public WriteResult updateProduct(String channelId, Map paraMap, Map updateMap) {
        return cmsBtProductDao.update(channelId, paraMap, updateMap);
    }

    /**
     * update product
     */
    public void updateProduct(String channelId, ProductUpdateBean request) {
        CmsBtProductModel productModel = request.getProductModel();
        Long prodId = productModel.getProdId();
        String productCode = null;
        if (productModel.getFields() != null) {
            productCode = productModel.getFields().getCode();
        }

        String queryStr = null;
        HashMap<String, Object> queryMap = new HashMap<>();
        if (prodId != null) {
            queryStr = String.format("{\"prodId\" : %s}", prodId);
            queryMap.put("prodId", prodId);
        } else if (!StringUtils.isEmpty(productCode)) {
            queryStr = String.format("{\"fields.code\" : \"%s\" }", productCode);
            queryMap.put("fields.code", productCode);
        }

        if (StringUtils.isEmpty(queryStr)) {
            return;
        }

        JomgoQuery queryObject = new JomgoQuery();
        queryObject.setQuery(queryStr);
        queryObject.setProjectionExt("prodId", "modified", "fields.status", "carts");

        CmsBtProductModel findModel = cmsBtProductDao.selectOneWithQuery(queryObject, channelId);
        if (findModel == null) {
            throw new RuntimeException("product not found!");
        }

        if (request.getIsCheckModifed()) {
            if (findModel.getModified() != null && !findModel.getModified().equals(productModel.getModified())) {
                throw new RuntimeException("product has been update, not update!");
            }
        }

        List<BulkUpdateModel> bulkList = new ArrayList<>();

        HashMap<String, Object> updateMap = new HashMap<>();

        /**
         * common attribute
         */
        String catId = productModel.getCatId();
        if (catId != null) {
            updateMap.put("catId", catId);
        }
        String catPath = productModel.getCatPath();
        if (catPath != null) {
            updateMap.put("catPath", catPath);
        }

        String modified = DateTimeUtil.getNowTimeStamp();
        if (request.getModified() != null) {
            modified = request.getModified();
        }
        updateMap.put("modified", modified);
        updateMap.put("modifier", request.getModifier());

        /**
         * Fields
         */
        CmsBtProductModel_Field fields = productModel.getFields();
        if (fields != null && fields.size() > 0) {
            BasicDBObject fieldObj = fields.toUpdateBasicDBObject("fields.");
            updateMap.putAll(fieldObj);
        }

        /**
         * platforms
         */
        Map<String, CmsBtProductModel_Platform_Cart> platforms = productModel.getPlatforms();
        updateMap.put("platforms", platforms);

        /**
         * Feed
         */
        CmsBtProductModel_Feed feed = productModel.getFeed();
        if (feed != null) {
            if (feed.getOrgAtts() != null && feed.getOrgAtts().size() > 0) {
                BasicDBObject orgAttsObj = feed.getOrgAtts().toUpdateBasicDBObject("feed.orgAtts.");
                updateMap.putAll(orgAttsObj);
            }
            if (feed.getCnAtts() != null && feed.getCnAtts().size() > 0) {
                BasicDBObject cnAttsObj = feed.getCnAtts().toUpdateBasicDBObject("feed.cnAtts.");
                updateMap.putAll(cnAttsObj);
            }
            if (feed.getCustomIds() != null && feed.getCustomIds().size() > 0) {
                updateMap.put("feed.customIds", feed.getCustomIds());
            }
            if (feed.getCustomIdsCn() != null && feed.getCustomIdsCn().size() > 0) {
                updateMap.put("feed.customIdsCn", feed.getCustomIdsCn());
            }
        }


        /**
         * set update model
         */
        if (updateMap.size() > 0) {
            BulkUpdateModel model = new BulkUpdateModel();
            model.setUpdateMap(updateMap);
            model.setQueryMap(queryMap);
            bulkList.add(model);
        }

        /**
         * execute update
         */
        if (bulkList.size() > 0) {

            if (findModel.getFields() != null && findModel.getFields().getStatus() != null
                    && productModel.getFields() != null && productModel.getFields().getStatus() != null) {
                //insert　ProductHistory
                CmsConstants.ProductStatus befStatus = CmsConstants.ProductStatus.valueOf(findModel.getFields().getStatus());
                CmsConstants.ProductStatus aftStatus = CmsConstants.ProductStatus.valueOf(productModel.getFields().getStatus());
                insertProductHistory(befStatus, aftStatus, channelId, findModel.getProdId());

                // jeff 2016/04 del start
                //insert　SxWorkLoad
//                String modifier = "0";
//                if (!StringUtils.isEmpty(request.getModifier())) {
//                    modifier = request.getModifier();
//                }
//                insertSxWorkLoad(befStatus, aftStatus, channelId, findModel.getGroups().getPlatforms(), modifier);
                // jeff 2016/04 del end
            }

            /**
             * 更新产品数据
             */
            cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, null, "$set");
        }

        /**
         * 更新产品sku数据
         */
        List<CmsBtProductModel_Sku> skus = productModel.getSkus();
        if (skus != null && skus.size() > 0) {

            // 如果sku价格发生变化更新product/model的price
            ProductPriceBean model = new ProductPriceBean();
            model.setProductId(findModel.getProdId());

            // 设置sku的价格.
            for (CmsBtProductModel_Sku sku : skus) {
                ProductSkuPriceBean skuPriceModel = new ProductSkuPriceBean();
                skuPriceModel.setSkuCode(sku.getSkuCode());
                skuPriceModel.setPriceMsrp(sku.getPriceMsrp());
                skuPriceModel.setPriceRetail(sku.getPriceRetail());
                skuPriceModel.setPriceSale(sku.getPriceSale());
                //vendor price update
                skuPriceModel.setClientNetPrice(sku.getClientNetPrice());
                skuPriceModel.setClientMsrpPrice(sku.getClientMsrpPrice());
                skuPriceModel.setClientRetailPrice(sku.getClientRetailPrice());
                model.addSkuPrice(skuPriceModel);
            }

            List<ProductPriceBean> productPriceBeanList = new ArrayList<>();

            productPriceBeanList.add(model);
            productSkuService.updatePrices(channelId, productPriceBeanList, request.getModifier());

            // 更新sku信息
            productSkuService.saveSkus(channelId, findModel.getProdId(), skus);
        }

        /**
         * 更新carts
         */
        if (productModel.getCarts().size() > 0) {

            // 设置批量更新条件
//            Map<String, Object> bulkQueryMap = new HashMap<>();
//            bulkQueryMap.put("fields.code", productModel.getFields().getCode());

            BasicDBObject queryObj = new BasicDBObject();
            queryObj.put("fields.code", productModel.getFields().getCode());

            BasicDBObject cartsObj = new BasicDBObject().append("carts", productModel.getCarts());
            BasicDBObject pushObj = new BasicDBObject().append("$pushAll", cartsObj);
            cmsBtProductDao.getDBCollection(channelId).update(queryObj, pushObj);
        }

//        if (bulkUpdateList.size() > 0) {
//            cmsBtProductDao.bulkUpdateWithMap(channelId, bulkUpdateList, null, "$set");
//        }




//        for (CmsBtProductModel_Carts cartInfo : ) {
//
//            // 设置批量更新条件
//            HashMap<String, Object> bulkQueryMap = new HashMap<>();
//            bulkQueryMap.put("fields.code", productModel.getFields().getCode());
//            bulkQueryMap.put("carts.cartId", cartInfo.getCartId());
//
//            HashMap<String, Object> bulkUpdateMap = new HashMap<>();
//            bulkUpdateMap.put("carts.$.platformStatus", cartInfo.getPlatformStatus().name());
//            bulkUpdateMap.put("carts.$.publishTime", cartInfo.getPublishTime());
//
//            BasicDBObject skusObj = new BasicDBObject().append("skus", skusList);
//            BasicDBObject pushObj = new BasicDBObject().append("$pushAll", skusObj);
//            cmsBtProductDao.getDBCollection(channelId).update(queryObj, pushObj);
//
//            // 设定批量更新条件和值
//            BulkUpdateModel bulkUpdateModel = new BulkUpdateModel();
//            bulkUpdateModel.setUpdateMap(bulkUpdateMap);
//            bulkUpdateModel.setQueryMap(bulkQueryMap);
//            bulkCartsList.add(bulkUpdateModel);
//        }
//
//        // 批量更新product表
//        if (bulkCartsList.size() > 0) {
//            cmsBtProductDao.bulkUpdateWithMap(channelId, bulkCartsList, null, "$set", true);
//        }

    }

    private void insertProductHistory(CmsConstants.ProductStatus befStatus,
                                      CmsConstants.ProductStatus aftStatus,
                                      String channelId, Long productId) {
        if (befStatus != null && aftStatus != null && !befStatus.equals(aftStatus)) {
            if (productId != null) {
                CmsBtProductModel productModel = getProductById(channelId, productId);
                CmsBtProductLogModel logModel = new CmsBtProductLogModel();
                BeanUtil.copy(productModel, logModel);
                logModel.set_id(null);
                cmsBtProductLogDao.insert(logModel);
            }
        }
    }

    // jeff 2016/04 change start
//    private void insertSxWorkLoad(CmsConstants.ProductStatus befStatus,
//                                  CmsConstants.ProductStatus aftStatus,
//                                  String channelId, List<CmsBtProductModel_Group_Platform> platforms, String modifier) {
    public void insertSxWorkLoad(String channelId, CmsBtProductModel cmsProduct, String modifier){
//        if (befStatus != null && aftStatus != null) {
//            boolean isNeed = false;
//            // 从其他状态转为Pending
//            if (befStatus != CmsConstants.ProductStatus.Approved && aftStatus == CmsConstants.ProductStatus.Approved) {
//                isNeed = true;
//                // 从Pending转为其他状态
//                // 在Pending下变更了
//            } else if (befStatus == CmsConstants.ProductStatus.Approved) {
//                isNeed = true;
//            }

//            if (isNeed) {
//                CmsBtSxWorkloadModel model = new CmsBtSxWorkloadModel();
//                model.setChannelId(channelId);
//                model.setGroupId(groupId);
//                model.setPublishStatus(0);
//                model.setCreater(modifier);
//                model.setModifier(modifier);
//                cmsBtSxWorkloadDao.insertSxWorkloadModel(model);

        List<CmsBtProductModel_Carts> carts = cmsProduct.getCarts();

        // 获得该店铺的上新平台列表
//        List<Integer> carts = new ArrayList<>();
//        for(TypeChannelBean typeChannelBean : TypeChannels.getTypeListSkuCarts(channelId, Constants.comMtTypeChannel.SKU_CARTS_53_A, "en")){
//            carts.add(Integer.valueOf(typeChannelBean.getValue()));
//        }

        if (carts != null && carts.size() > 0) {
            // 根据商品code获取其所有group信息(所有平台)
            List<CmsBtProductGroupModel> groups = cmsBtProductGroupDao.select("{\"productCodes\": \"" + cmsProduct.getFields().getCode() + "\"}", channelId);
            Map<Integer, Long> platformsMap = groups.stream().collect(toMap(CmsBtProductGroupModel::getCartId, CmsBtProductGroupModel::getGroupId));

            // 获取所有的可上新的平台group信息
            List<CmsBtSxWorkloadModel> models = new ArrayList<>();

            for (CmsBtProductModel_Carts cartInfo : carts) {
                CmsBtSxWorkloadModel model = new CmsBtSxWorkloadModel();
                model.setChannelId(channelId);
                if(platformsMap.get(cartInfo.getCartId()) != null){
                    model.setGroupId(platformsMap.get(cartInfo.getCartId()));
                }else{
                    CmsBtProductGroupModel newGroup;
                    try {
                        newGroup = (CmsBtProductGroupModel) BeanUtils.cloneBean(groups.get(0));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    newGroup.set_id(null);
                    newGroup.setChannelId(channelId);
                    newGroup.setNumIId(null);
                    newGroup.setCartId(cartInfo.getCartId());
                    newGroup.setGroupId(commSequenceMongoService.getNextSequence(MongoSequenceService.CommSequenceName.CMS_BT_PRODUCT_GROUP_ID));
                    cmsBtProductGroupDao.insert(newGroup);
                    model.setGroupId(newGroup.getGroupId());
                }
                model.setCartId(cartInfo.getCartId());
                model.setPublishStatus(0);
                model.setCreater(modifier);
                model.setModifier(modifier);
                models.add(model);
            }

            // TODO: 16/5/13 如果sxworkload表已经同样的未上新的数据,是否就不需要再插入该条数据了
            if (models.size() > 0) {
                addtSxWorkloadModels(models);
            }
        }
//        }
    }
    // jeff 2016/04 change end

    /**
     * confirm change category
     */
    public Map<String, Object> changeProductCategory(String channelId, String categoryId, String categoryPath, List<String> models, String modifier) {

        HashMap<String, Object> updateMap = new HashMap<>();
        updateMap.put("catId", categoryId);
        updateMap.put("catPath", categoryPath);
        // bug CMS-30修正 edward 2016-05-24
        if (!Channel.VOYAGEONE.getId().equals(channelId))
            updateMap.put("batchField.switchCategory", 1);

        List<BulkUpdateModel> bulkList = new ArrayList<>();

        for (String modelCode : models) {
            HashMap<String, Object> queryMap = new HashMap<>();
            queryMap.put("feed.orgAtts.modelCode", modelCode);
            BulkUpdateModel model = new BulkUpdateModel();
            model.setUpdateMap(updateMap);
            model.setQueryMap(queryMap);
            bulkList.add(model);
        }

        // 批量更新product表
        BulkWriteResult result = null;
        if (bulkList.size() > 0) {
            result = cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, modifier, "$set");
        }

        // 批量更新feed表
        int updateFeedInfoCount = cmsBtFeedInfoDao.updateFeedInfoUpdFlg(channelId, models.toArray(new String[models.size()]));

        // <更新 feed mapping 信息>

        // 先获取所有 model 所在类目
        List<CmsBtFeedInfoModel> feedInfoModels = cmsBtFeedInfoDao.selectCategoryByModel(models, channelId);

        // 先去除类目重复
        // 之后挨个对类目进行 mapping 更新
        feedInfoModels
                .stream()
                .map(CmsBtFeedInfoModel::getCategory)
                .distinct()
                .forEach(path ->feedMappingService.setMapping(path, categoryPath, Channel.valueOfId(channelId), false));

        // </更新 feed mapping 信息>

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("updFeedInfoCount", updateFeedInfoCount);
        if (result != null) {
            resultMap.put("updProductCount", result.getModifiedCount());
            resultMap.put("modifiedCount", result.getModifiedCount() + updateFeedInfoCount);
        } else {
            resultMap.put("updProductCount", 0);
            resultMap.put("modifiedCount", 0);
        }

        return resultMap;
    }

    /**
     * 根据groupId批量更新产品的信息
     *
     * @param channelId String
     * @param prodCode  String
     * @param updateMap Map
     * @param modifier  String
     */
    public void updateTranslation(String channelId, String prodCode, Map<String, Object> updateMap, String modifier) {
        // 先根据产品code找到其model
        CmsBtProductModel prodObj = cmsBtProductDao.selectOneWithQuery("{'fields.code':'" + prodCode + "'},{'fields.model':1,'_id':0}", channelId);
        String prodModel = prodObj.getFields().getModel();

        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("fields.model", prodModel);
        HashMap<String, Object> optMap = new HashMap<>();
        optMap.put("$set", updateMap);

        cmsBtProductDao.update(channelId, queryMap, optMap);
    }

    /**
     * get the product info from wms's request
     */
    public ProductForWmsBean getWmsProductsInfo(String channelId, String productSku, String[] projection) {
        JomgoQuery queryObject = new JomgoQuery();
        // set fields
        if (projection != null && projection.length > 0) {
            queryObject.setProjectionExt(projection);
        }

        if (!StringUtils.isEmpty(productSku)) {
            queryObject.setQuery(String.format("{\"skus.skuCode\" : \"%s\" }", productSku));
        }

        ProductForWmsBean resultInfo = null;
        if (queryObject.getQuery() != null) {
            resultInfo = new ProductForWmsBean();

            CmsBtProductModel product = cmsBtProductDao.selectOneWithQuery(queryObject, channelId);
            resultInfo.setChannelId(product.getChannelId());
            resultInfo.setCode(product.getFields().getCode());
            resultInfo.setName(product.getFields().getProductNameEn());
            resultInfo.setProductId(product.getProdId().toString());
            resultInfo.setShortDescription(product.getFields().getShortDesEn());
            resultInfo.setLongDescription(product.getFields().getLongDesEn());
            // TODO set productType(but now productType is not commen field)
            resultInfo.setDescription(product.getFields().getProductType());
            resultInfo.setBrandName(product.getFields().getBrand());
            resultInfo.setGender(product.getFields().getSizeType());
            // TODO 无法提供,属于主数据的非共通属性
            resultInfo.setMaterialFabricName("");
            resultInfo.setCountryName(product.getFields().getOrigin());

            // 设置人民币价格
            resultInfo.setMsrp(product.getSku(productSku).getPriceMsrp() != null ? product.getSku(productSku).getPriceMsrp().toString() : "0.00");
            resultInfo.setPrice(product.getSku(productSku).getPriceSale() != null ? product.getSku(productSku).getPriceSale().toString() : "0.00");
            resultInfo.setRetailPrice(product.getSku(productSku).getPriceRetail() != null ? product.getSku(productSku).getPriceRetail().toString() : "0.00");

            // 设置美元价格
            resultInfo.setClientMsrpPrice(String.valueOf(product.getSku(productSku).getClientMsrpPrice()));
            resultInfo.setClientRetailPrice(String.valueOf(product.getSku(productSku).getClientRetailPrice()));
            resultInfo.setClientNetPrice(String.valueOf(product.getSku(productSku).getClientNetPrice()));

            // 设置原始价格单位

            CmsChannelConfigBean channelConfig = CmsChannelConfigs.getConfigBeanNoCode(channelId
                    , CmsConstants.ChannelConfig.CLIENT_PRICE_UNIT);
            resultInfo.setClientPriceUnit(channelConfig.getConfigValue1());

            // TODO 无法提供,属于主数据的非共通属性
            resultInfo.setWeightkg("");
            // TODO 无法提供,属于主数据的非共通属性
            resultInfo.setWeightlb("");
            resultInfo.setModelName(product.getFields().getModel());
            // TODO 无法提供,属于主数据的非共通属性
            resultInfo.setUrlKey("");
            String imagePath = "";
            if (product.getFields().getImages1().size() > 0) {
                if (!StringUtils.isEmpty(product.getFields().getImages1().get(0).getName()))
                    imagePath = imageTemplateService.getImageUrl(product.getFields().getImages1().get(0).getName());
            }
            resultInfo.setShowName(imagePath);
            resultInfo.setCnName(product.getFields().getLongTitle());
            // 获取HsCodeCrop
            String hsCodeCrop = product.getFields().getHsCodeCrop();
            if (!StringUtils.isEmpty(hsCodeCrop)) {
//                TypeChannelBean bean = TypeChannels.getTypeChannelByCode(Constants.productForOtherSystemInfo.HS_CODE_CROP, channelId, hsCodeCrop);
//                if (!StringUtils.isEmpty(hsCodeCrop)) {
                // TODO 暂时不需要填写hsCode
//                String[] hsCode = hsCodeCrop.split(",");
//                resultInfo.setHsCodeId(hsCodeCrop);
//                resultInfo.setHsCode(hsCode[0]);
//                resultInfo.setHsDescription(hsCode[1]);
//                resultInfo.setUnit(hsCode[2]);
//                }
            }
            // 获取HsCodePrivate
            String hsCodePrivate = product.getFields().getHsCodePrivate();
            if (!StringUtils.isEmpty(hsCodePrivate)) {
//                TypeChannelBean bean = TypeChannels.getTypeChannelByCode(Constants.productForOtherSystemInfo.HS_CODE_PRIVATE, channelId, hsCodePrivate);
//                if (!StringUtils.isEmpty(hsCodePrivate)) {
                String[] hsCodePu = hsCodePrivate.split(",");
                resultInfo.setHsCodePuId(hsCodePrivate);
                resultInfo.setHsCodePu(hsCodePu[0]);
                resultInfo.setHsDescriptionPu(hsCodePu[1]);
                resultInfo.setUnitPu(hsCodePu[2]);
//                }
            }
        }
        return resultInfo;
    }

    /**
     * get the product list from oms's request
     */
    public List<ProductForOmsBean> getOmsProductsInfo(String channelId,
                                                      String skuIncludes, List<String> skuList,
                                                      String nameIncludes,
                                                      String descriptionIncludes,
                                                      String cartId,
                                                      String[] projection) {
        JomgoQuery queryObject = new JomgoQuery();
        // set fields
        if (projection != null && projection.length > 0) {
            queryObject.setProjectionExt(projection);
        }

        StringBuilder sbQuery = new StringBuilder();

        if (!StringUtils.isEmpty(skuIncludes)) {
            sbQuery.append(MongoUtils.splicingValue("skus.skuCode", skuIncludes, "$regex"));
            sbQuery.append(",");
        } else if (skuList != null && skuList.size() > 0) {
            sbQuery.append(MongoUtils.splicingValue("skus.skuCode", skuList.toArray(new String[skuList.size()])));
            sbQuery.append(",");
        }

        // 设定name的模糊查询
        if (!StringUtils.isEmpty(nameIncludes)) {
            sbQuery.append(MongoUtils.splicingValue("fields.productNameEn", nameIncludes, "$regex"));
            sbQuery.append(",");
        }

        // 设定description的模糊查询
        if (!StringUtils.isEmpty(descriptionIncludes)) {
            sbQuery.append(MongoUtils.splicingValue("fields.longDesEn", descriptionIncludes, "$regex"));
            sbQuery.append(",");
        }

        if (!StringUtils.isEmpty(cartId) && !"1".equals(cartId)) {
            sbQuery.append(MongoUtils.splicingValue("carts.cartId", Integer.valueOf(cartId)));
            sbQuery.append(",");
        }

        if (!StringUtils.isEmpty(sbQuery.toString())) {
            queryObject.setQuery("{" + sbQuery.toString().substring(0, sbQuery.toString().length() - 1) + "}");
        }

        queryObject.setLimit(50);
        List<CmsBtProductModel> products = cmsBtProductDao.select(queryObject, channelId);

        List<ProductForOmsBean> resultInfo = new ArrayList<>();
        for (CmsBtProductModel product : products) {
            for (CmsBtProductModel_Sku sku : product.getSkus()) {
                ProductForOmsBean bean = new ProductForOmsBean();
                // 设置商品的原始channelId
                bean.setChannelId(product.getOrgChannelId());
                bean.setSku(sku.getSkuCode());
                bean.setProduct(product.getFields().getProductNameEn());
                bean.setDescription(product.getFields().getLongDesEn());
                bean.setPricePerUnit(sku.getPriceSale() != null ? sku.getPriceSale().toString() : "0.00");
                // TODO 目前无法取得库存值
                Map<String, Object> param = new HashMap<>();
                param.put("channelId", channelId);
                param.put("sku", sku.getSkuCode());
                WmsBtInventoryCenterLogicModel skuInfo = wmsBtInventoryCenterLogicDao.selectItemDetailBySku(param);
                bean.setInventory(String.valueOf(skuInfo.getQtyChina()));
                String imagePath = "";
                if (product.getFields().getImages1().size() > 0) {
                    if (!StringUtils.isEmpty(product.getFields().getImages1().get(0).getName()))
                        imagePath = imageTemplateService.getImageUrl(product.getFields().getImages1().get(0).getName());
                }
                bean.setImgPath(imagePath);

                // 取得该商品的组信息
                CmsBtProductGroupModel grpObj = cmsBtProductGroupDao.selectOneWithQuery("{'cartId':" + cartId + ",'productCodes':'" + product.getFields().getCode() + "'},{'numIid':1,'_id':0}", channelId);

                // TODO 目前写死,以后再想办法修改
                String numIid = "";
                CartEnums.Cart cartEnum = CartEnums.Cart.getValueByID(cartId);
                if (cartEnum != null) {
                    switch (cartEnum) {
                        case TG:
                            numIid = grpObj != null && !StringUtils.isEmpty(grpObj.getNumIId())
                                    ? Constants.productForOtherSystemInfo.TMALL_NUM_IID + grpObj.getNumIId() : "";
                            break;
                    }
                }
                bean.setSkuTmallUrl(numIid);

                resultInfo.add(bean);
            }
        }

        return resultInfo;
    }


    /**
     * distributeTranslation 分配翻译商品
     */
    public List<CmsBtProductModel> translateDistribute(String channelId, ProductTransDistrBean param) {
        /**
         * lock data
         */
        String nowStr = DateTimeUtil.getNow();
        int getCount = param.getLimit();
        String translator = param.getTranslator();
        int translateTimeHDiff = param.getTranslateTimeHDiff();
        int distributeRule = param.getDistributeRule();

        String queryStrTmp;
        switch (distributeRule) {
            case 0:
                // add translateTime condition
                queryStrTmp = "{\"$or\":" +
                        "[{\"fields.status\":{\"$nin\":[\"New\"]},\"fields.translateStatus\":{\"$in\":[null,\"\", \"0\"]},\"fields.translator\":{\"$in\":[null,\"\"]}}," +
                        "{\"fields.status\":{\"$nin\":[\"New\"]},\"fields.translator\":{\"$nin\":[null,\"\"]},\"fields.translateTime\":{\"$lt\":\"%s\"}}]}";
                break;
            case 1:
                queryStrTmp = "{\"$or\":" +
                        "[{\"fields.status\":{\"$nin\":[\"New\"]},\"fields.translateStatus\":{\"$in\":[null,\"\",\"0\"]},\"fields.translator\":{\"$in\":[null,\"\"]},\"fields.isMasterMain\":1}," +
                        "{\"fields.status\":{\"$nin\":[\"New\"]},\"fields.translator\":{\"$nin\":[null,\"\"]},\"fields.translateTime\":{\"$lt\":\"%s\"},\"fields.isMasterMain\":1}]}";
                break;
            default:
                // add translateTime condition
                queryStrTmp = "{\"$or\":" +
                        "[{\"fields.status\":{\"$nin\":[\"New\"]},\"fields.translateStatus\":{\"$in\":[null,\"\", \"0\"]},\"fields.translator\":{\"$in\":[null,\"\"]}}," +
                        "{\"fields.status\":{\"$nin\":[\"New\"]},\"fields.translator\":{\"$nin\":[null,\"\"]},\"fields.translateTime\":{\"$lt\":\"%s\"}}]}";
                break;
        }

        Date date = DateTimeUtil.addHours(DateTimeUtil.getDate(), -translateTimeHDiff);
        String translateTimeStr = DateTimeUtil.format(date, null);

        JomgoUpdate updateObject = new JomgoUpdate();
        // create query string
        String queryStr = String.format(queryStrTmp, translateTimeStr);
        updateObject.setQuery(queryStr);

        // create Projection
        updateObject.setProjection(param.getProjectionArr());

        // create sort String
        updateObject.setSort(param.getSortStr());

        // create Update string
        String strUpdateTmp = "{\"$set\":{\"fields.translateStatus\":\"0\", \"fields.translator\":\"%s\", \"fields.translateTime\":\"%s\"}}";
        String updateStr = String.format(strUpdateTmp, translator, nowStr);
        updateObject.setUpdate(updateStr);

        List<CmsBtProductModel> products = new ArrayList<>();
        //update translator translateTime
        for (int i = 0; i < getCount; i++) {
            CmsBtProductModel productModel = cmsBtProductDao.findAndModify(updateObject, channelId);
            if (productModel != null) {
                products.add(productModel);
            } else {
                break;
            }
        }

        return products;
    }

    public void updateTranslateStatus(String channelId, String prodCode, String translateStatus, String modifier) {
        Map<String, String> paraMap = new HashMap<>(1);
        paraMap.put("fields.code", prodCode);

        Map<String, String> rsMap = new HashMap<>(3);
        rsMap.put("fields.translateStatus", translateStatus);
        rsMap.put("modifier", modifier);
        rsMap.put("modified", DateTimeUtil.getNowTimeStamp());
        HashMap<String, Object> updateMap = new HashMap<>();
        updateMap.put("$set", rsMap);

        cmsBtProductDao.update(channelId, paraMap, updateMap);
    }

    public void updateTags(String channelId, Long prodId, List<String> Tags, String modifier) {
        Map<String, Object> paraMap = new HashMap<>(1);
        paraMap.put("channelId", channelId);
        paraMap.put("prodId", prodId);

        Map<String, Object> rsMap = new HashMap<>(3);
        rsMap.put("tags", Tags);
        rsMap.put("modifier", modifier);
        rsMap.put("modified", DateTimeUtil.getNowTimeStamp());
        HashMap<String, Object> updateMap = new HashMap<>();
        updateMap.put("$set", rsMap);

        cmsBtProductDao.update(channelId, paraMap, updateMap);
    }

    /**
     * 获取Sku的库存信息
     */
    public Map<String, Integer> getProductSkuQty(String channelId, String productCode) {
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("channelId", channelId);
        queryMap.put("code", productCode);

        List<WmsBtInventoryCenterLogicModel> inventoryList = wmsBtInventoryCenterLogicDao.selectItemDetailByCode(queryMap);

        Map<String, Integer> result = new HashMap<>();
        for (WmsBtInventoryCenterLogicModel inventory : inventoryList) {
            result.put(inventory.getSku(), inventory.getQtyChina());
        }
        return result;
    }

    /**
     * 取得逻辑库存
     *
     * @param channelId 渠道id
     * @param skuList   待取得逻辑库存的sku对象
     * @return 逻辑库存Map sku:logicQty
     */
    public Map<String, Integer> getLogicQty(String channelId, List<String> skuList) {
        // 逻辑库存Map
        Map<String, Integer> skuLogicQty = new HashMap<>();
        skuList.forEach(sku -> skuLogicQty.put(sku, 0)); // 初始化

        List<WmsBtInventoryCenterLogicModel> listLogicInventory = wmsBtInventoryCenterLogicDao.selectItemDetailBySkuList(channelId, skuList);

        if (listLogicInventory != null && listLogicInventory.size() > 0) {
            for (WmsBtInventoryCenterLogicModel logicInventory : listLogicInventory) {
                String sku = logicInventory.getSku();
                Integer logicQty = logicInventory.getQtyChina();
                skuLogicQty.merge(sku, logicQty, (val, newVal) -> val + newVal);
            }
        }

        return skuLogicQty;
    }

    /**
     * 返回已经存在的及新生成的carts数据
     *
     * @param skus  产品SKU列表
     * @param carts 寄存Cart列表
     * @return List<CmsBtProductModel_Carts>
     */
    public List<CmsBtProductModel_Carts> getCarts(List<CmsBtProductModel_Sku> skus, List<CmsBtProductModel_Carts> carts) {

        List<CmsBtProductModel_Carts> newCarts = skus
                .stream()
                .map(CmsBtProductModel_Sku::getSkuCarts)
                .flatMap(List::stream)
                .distinct()
                .filter(byCartId(carts))
                .map(this::toProductModelCart)
                .collect(Collectors.toList());

        return newCarts;
    }

    /**
     * 返回在不在既存carts中的新cart过滤器
     */
    private Predicate<Integer> byCartId(List<CmsBtProductModel_Carts> carts) {
        return i -> {
            for (CmsBtProductModel_Carts cart : carts) {
                if (Objects.equals(i, cart.getCartId()))
                    return false;
            }
            return true;
        };
    }

    /**
     * 返回新的cart信息
     */
    private CmsBtProductModel_Carts toProductModelCart(Integer i) {
        CmsBtProductModel_Carts newCart = new CmsBtProductModel_Carts();
        newCart.setCartId(i);
        newCart.setPlatformStatus(CmsConstants.PlatformStatus.WaitingPublish);
        return newCart;
    }

    /**
     * 更新cms_bt_product表的SellerCat字段
     */
    public Map<String, Object> updateSellerCat(List<String> cIdsList, List<String> cNamesList, List<String> fullCNamesList, List<String> fullCatCIdList, List<String> codeList,int cartId,String userName,String channelId){
        HashMap<String, Object> updateMap = new HashMap<>();
        updateMap.put("sellerCats.cartId", cartId);
        updateMap.put("sellerCats.cIds", cIdsList);
        updateMap.put("sellerCats.cNames",cNamesList);
        updateMap.put("sellerCats.fullCNames",fullCNamesList);
        updateMap.put("sellerCats.fullCIds",fullCatCIdList);
        List<BulkUpdateModel> bulkList = new ArrayList<>();
        for (String code : codeList) {
            HashMap<String, Object> queryMap = new HashMap<>();
            queryMap.put("fields.code", code);
            BulkUpdateModel model = new BulkUpdateModel();
            model.setUpdateMap(updateMap);
            model.setQueryMap(queryMap);
            bulkList.add(model);
        }
        // 批量更新product表
        BulkWriteResult result = null;
        if (bulkList.size() > 0) {
            result = cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, userName, "$set");
        }
        Map<String, Object> resultMap = new HashMap<>();
        if (result != null) {
            resultMap.put("updProductCount", result.getModifiedCount());
            resultMap.put("modifiedCount", result.getModifiedCount());
        } else {
            resultMap.put("updProductCount", 0);
            resultMap.put("modifiedCount", 0);
        }
        return resultMap;
    }

    /**
     * insertSxWorkloadModels
     */
    public void addtSxWorkloadModels(List<CmsBtSxWorkloadModel> models) {
        cmsBtSxWorkloadDaoExt.insertSxWorkloadModels(models);
    }

    public void updateProductPlatform(String channelId, Long prodId, CmsBtProductModel_Platform_Cart platformModel){

        HashMap<String, Object> queryMap = new HashMap<>();
        queryMap.put("prodId", prodId);

        List<BulkUpdateModel> bulkList = new ArrayList<>();
        HashMap<String, Object> updateMap = new HashMap<>();
        updateMap.put("platforms.P"+platformModel.getCartId(), platformModel);
        BulkUpdateModel model = new BulkUpdateModel();
        model.setUpdateMap(updateMap);
        model.setQueryMap(queryMap);
        bulkList.add(model);
        cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, null, "$set");

    }

    public int updateProductFeedToMaster(String channelId,CmsBtProductModel cmsProduct, String modifier){
        HashMap<String, Object> queryMap = new HashMap<>();
        queryMap.put("prodId", cmsProduct.getProdId());
        queryMap.put("modified", cmsProduct.getModified());
        List<BulkUpdateModel> bulkList = new ArrayList<>();
        HashMap<String, Object> updateMap = new HashMap<>();
        if (cmsProduct.getCatId() != null) {
            updateMap.put("catId", cmsProduct.getCatId());
        }
        if (cmsProduct.getCatPath() != null) {
            updateMap.put("catPath", cmsProduct.getCatPath());
        }

        /**
         * fields
         */
        if (cmsProduct.getFields() != null) {
            updateMap.put("fields", cmsProduct.getFields());
        }

        /**
         * skus
         */
        if (cmsProduct.getSkus() != null) {
            updateMap.put("skus", cmsProduct.getSkus());
        }

        /**
         * common
         */
        if (cmsProduct.getCommon() != null) {
            updateMap.put("common", cmsProduct.getCommon());
        }

        /**
         * platforms
         */
        if (cmsProduct.getPlatforms() != null) {
            updateMap.put("platforms", cmsProduct.getPlatforms());
        }

        /**
         * feed
         */
        CmsBtProductModel_Feed feed = cmsProduct.getFeed();
        if (feed != null) {
            if (feed.getCatId() != null) {
                updateMap.put("feed.catId", feed.getCatId());
            }

            if (feed.getCatId() != null) {
                updateMap.put("feed.catPath", feed.getCatPath());
            }

            if (feed.getOrgAtts() != null && feed.getOrgAtts().size() > 0) {
                BasicDBObject orgAttsObj = feed.getOrgAtts().toUpdateBasicDBObject("feed.orgAtts.");
                updateMap.putAll(orgAttsObj);
            }
            if (feed.getCnAtts() != null && feed.getCnAtts().size() > 0) {
                BasicDBObject cnAttsObj = feed.getCnAtts().toUpdateBasicDBObject("feed.cnAtts.");
                updateMap.putAll(cnAttsObj);
            }
            if (feed.getCustomIds() != null && feed.getCustomIds().size() > 0) {
                updateMap.put("feed.customIds", feed.getCustomIds());
            }
            if (feed.getCustomIdsCn() != null && feed.getCustomIdsCn().size() > 0) {
                updateMap.put("feed.customIdsCn", feed.getCustomIdsCn());
            }
        }

        updateMap.put("modifier", modifier);
        updateMap.put("modified", DateTimeUtil.getNowTimeStamp());
        BulkUpdateModel model = new BulkUpdateModel();
        model.setUpdateMap(updateMap);
        model.setQueryMap(queryMap);
        bulkList.add(model);
        BulkWriteResult result = cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, null, "$set");
        return result.getModifiedCount();
    }
}
