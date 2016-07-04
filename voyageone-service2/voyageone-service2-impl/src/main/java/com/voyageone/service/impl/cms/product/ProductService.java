package com.voyageone.service.impl.cms.product;

import com.google.common.base.Joiner;
import com.mongodb.BasicDBObject;
import com.mongodb.BulkWriteResult;
import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.JomgoAggregate;
import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.base.dao.mongodb.JomgoUpdate;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Enums.ChannelConfigEnums.Channel;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.MongoUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.CustomPropBean;
import com.voyageone.service.bean.cms.feed.FeedCustomPropWithValueBean;
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
import com.voyageone.service.impl.cms.feed.FeedCustomPropService;
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

    @Autowired
    private FeedCustomPropService customPropService;


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
    public List<CmsBtProductBean> getProductByGroupId(String channelId, Long groupId, Boolean flag) {
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

        List<CmsBtProductBean> rst = cmsBtProductDao.selectBean(queryObject, channelId);
        rst.forEach(prodObj -> prodObj.setGroupBean(grpObj));
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
    public List<CmsBtProductModel> getList(String channelId, JomgoQuery queryObject) {
        return cmsBtProductDao.select(queryObject, channelId);
    }

    /**
     * getList
     * 注意：调用此方法时，返回值中的getGroupBean()为空，需要自行填值
     * 如需要groupBean,请使用getListWithGroup()
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
            qurStr.append(MongoUtils.splicingValue("productCodes", prodObj.getCommon().getFields().getCode()));

            // 在group表中过滤platforms相关信息
            JomgoQuery qrpQuy = new JomgoQuery();
            qrpQuy.setQuery("{" + qurStr.toString() + "}");
            List<CmsBtProductGroupModel> grpList = cmsBtProductGroupDao.select(qrpQuy, channelId);
            if (grpList == null || grpList.isEmpty()) {
                $warn("ProductService.getListWithGroup prodCode=" + prodObj.getCommon().getFields().getCode());
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

        String prodCodeQuery = String.format("{ \"fields.code\" : \"%s\" }", product.getCommon().getFields().getCode());
        count = cmsBtProductDao.countByQuery(prodCodeQuery, channelId);
        if (count > 0) {
            throw new RuntimeException("fields.code has existed, not add!");
        }

        //update channel and modifier
        product.setChannelId(channelId);
        product.setCreater(modifier);
        product.setModifier(modifier);

        //save
        cmsBtProductDao.insert(product);
    }

    public WriteResult updateProduct(String channelId, Map paraMap, Map updateMap) {
        return cmsBtProductDao.update(channelId, paraMap, updateMap);
    }

    public WriteResult updateFirstProduct(JomgoUpdate updObj, String channelId) {
        return cmsBtProductDao.updateFirst(updObj, channelId);
    }

    /**
     * update product
     */
    public void updateProduct(String channelId, ProductUpdateBean request) {
        CmsBtProductModel productModel = request.getProductModel();
        Long prodId = productModel.getProdId();
        String productCode = null;
        if (productModel.getCommon().getFields() != null) {
            productCode = productModel.getCommon().getFields().getCode();
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
        queryObject.setProjectionExt("prodId", "modified", "fields.status"); // TODO--这里不应该再从fields取status

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
        String catId = productModel.getCommon().getCatId();
        if (catId != null) {
            updateMap.put("catId", catId);
        }
        String catPath = productModel.getCommon().getCatPath();
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
        CmsBtProductModel_Field fields = productModel.getCommon().getFields();
        if (fields != null && fields.size() > 0) {
            BasicDBObject fieldObj = fields.toUpdateBasicDBObject("fields.");
            updateMap.putAll(fieldObj);
        }

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


            findModel.getPlatforms().forEach((s, platformInfo) -> {
//                platformInfo.getStatus()
                //// TODO: 16/6/30 edward 批量插入上新表 ,再判断是否还需要
            });

            /**
             * 更新产品数据
             */
            cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, null, "$set");
        }

        /**
         * 更新产品sku数据
         */
        List<CmsBtProductModel_Sku> skus = productModel.getCommon().getSkus();
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

    }

    private void insertProductHistory(String channelId, Long productId) {
        if (productId != null) {
            CmsBtProductModel productModel = getProductById(channelId, productId);
            CmsBtProductLogModel logModel = new CmsBtProductLogModel();
            JacksonUtil.json2Bean(JacksonUtil.bean2Json(productModel), logModel.getClass());
            logModel.set_id(null);
            cmsBtProductLogDao.insert(logModel);
        }
    }

    // jeff 2016/04 change start
    public void insertSxWorkLoad(String channelId, CmsBtProductModel cmsProduct, String modifier) {
        List<Integer> carts = cmsProduct.getCartIdList();
        if (carts != null && carts.size() > 0) {
            // 根据商品code获取其所有group信息(所有平台)
            List<CmsBtProductGroupModel> groups = cmsBtProductGroupDao.select("{\"productCodes\": \"" + cmsProduct.getCommon().getFields().getCode() + "\"}", channelId);
            Map<Integer, Long> platformsMap = groups.stream().collect(toMap(CmsBtProductGroupModel::getCartId, CmsBtProductGroupModel::getGroupId));

            // 获取所有的可上新的平台group信息
            List<CmsBtSxWorkloadModel> models = new ArrayList<>();

            for (Integer cartInfo : carts) {
                // Add desmond 2016/07/01 start
                // 由于2016/07/08版本的最新Product中product.Fields.status移到分平台product.platforms.P23.status下面去了
                // 所以是否Approved的判断只能移到insertSxWorkLoad()方法里面去做，当一个商品的所有product都没有Approved，则不插入sx_workload表
                if (cmsProduct.getPlatform(cartInfo) != null
                        && !CmsConstants.ProductStatus.Approved.name().equals(cmsProduct.getPlatform(cartInfo).getStatus())) {
                    continue;
                }
                // Add desmond 2016/07/01 end

                CmsBtSxWorkloadModel model = new CmsBtSxWorkloadModel();
                model.setChannelId(channelId);
                if (platformsMap.get(cartInfo) != null) {
                    model.setGroupId(platformsMap.get(cartInfo));
                } else {
                    CmsBtProductGroupModel newGroup;
                    try {
                        newGroup = (CmsBtProductGroupModel) BeanUtils.cloneBean(groups.get(0));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    newGroup.set_id(null);
                    newGroup.setChannelId(channelId);
                    newGroup.setNumIId(null);
                    newGroup.setCartId(cartInfo);
                    newGroup.setGroupId(commSequenceMongoService.getNextSequence(MongoSequenceService.CommSequenceName.CMS_BT_PRODUCT_GROUP_ID));
                    cmsBtProductGroupDao.insert(newGroup);
                    model.setGroupId(newGroup.getGroupId());
                }
                model.setCartId(cartInfo);
                model.setPublishStatus(0);
                model.setCreater(modifier);
                model.setModifier(modifier);
                models.add(model);
            }

            // TODO: 16/5/13 如果sxworkload表已经同样的未上新的数据,是否就不需要再插入该条数据了
            if (models.size() > 0) {
                int rslt = cmsBtSxWorkloadDaoExt.insertSxWorkloadModels(models);
                $debug("insertSxWorkLoad 新增SxWorkload结果 " + rslt);
            }
        }
    }
    // jeff 2016/04 change end

    /**
     * 添加数据到SxWorkLoad，使用批处理方式，<br>
     * 先判断列表中产品状态是否Approved，不是则不处理该条产品数据，<br>
     * 然后判断group是否存在，不存在则追加，<br>
     * 最后批量添加数据
     */
    public void insertSxWorkLoad(String channelId, List<String> prodCodeList, List<Integer> cartIdList, String modifier) {
        if (prodCodeList.isEmpty() || cartIdList.isEmpty()) {
            $warn("insertSxWorkLoad: 参数为空");
            return;
        }
        List<CmsBtProductGroupModel> newGroupList = new ArrayList<>();
        // 获取所有的可上新的平台group信息
        List<CmsBtSxWorkloadModel> models = new ArrayList<>();

        for (String prodCode : prodCodeList) {
            // 根据商品code获取其所有group信息(所有平台)
            List<CmsBtProductGroupModel> groups = cmsBtProductGroupDao.select("{\"productCodes\": \"" + prodCode + "\"}", channelId);
            Map<Integer, Long> platformsMap = groups.stream().collect(toMap(CmsBtProductGroupModel::getCartId, CmsBtProductGroupModel::getGroupId));

            for (Integer cartId : cartIdList) {
                CmsBtSxWorkloadModel model = new CmsBtSxWorkloadModel();
                model.setChannelId(channelId);
                if (platformsMap.get(cartId) != null) {
                    model.setGroupId(platformsMap.get(cartId));
                } else {
                    CmsBtProductGroupModel newGroup = groups.get(0);
                    newGroup.set_id(null);
                    newGroup.setChannelId(channelId);
                    newGroup.setNumIId(null);
                    newGroup.setCartId(cartId);
                    newGroup.setGroupId(commSequenceMongoService.getNextSequence(MongoSequenceService.CommSequenceName.CMS_BT_PRODUCT_GROUP_ID));
                    newGroupList.add(newGroup);
                    model.setGroupId(newGroup.getGroupId());
                }
                model.setCartId(cartId);
                model.setPublishStatus(0);
                model.setCreater(modifier);
                model.setModifier(modifier);
                models.add(model);
            }
        }

        if (newGroupList.size() > 0) {
            WriteResult rs = cmsBtProductGroupDao.insertWithList(newGroupList);
            $debug("insertSxWorkLoad 新增group结果 " + rs.toString());
        }

        if (models.size() > 0) {
            int rslt = cmsBtSxWorkloadDaoExt.insertSxWorkloadModels(models);
            $debug("insertSxWorkLoad 新增SxWorkload结果 " + rslt);
        }
    }

    /**
     * confirm change category
     */
    public Map<String, Object> changeProductCategory(String channelId, String categoryId, String categoryPath, List<String> models, String modifier) {
        HashMap<String, Object> updateMap = new HashMap<>();
        updateMap.put("common.catId", categoryId);
        updateMap.put("common.catPath", categoryPath);
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
                .forEach(path -> feedMappingService.setMapping(path, categoryPath, Channel.valueOfId(channelId), false));

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
        CmsBtProductModel prodObj = cmsBtProductDao.selectOneWithQuery("{\"fields.code\":\"" + prodCode + "\"},{\"fields.model\":1,\"_id\":0}", channelId);
        String prodModel = prodObj.getCommon().getFields().getModel();

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
//        if (projection != null && projection.length > 0) {
//            queryObject.setProjectionExt(projection);
//        }

        if (!StringUtils.isEmpty(productSku)) {
            queryObject.setQuery(String.format("{\"common.skus.skuCode\" : \"%s\" }", productSku));
        }

        ProductForWmsBean resultInfo = null;
        if (queryObject.getQuery() != null) {
            resultInfo = new ProductForWmsBean();

            CmsBtProductModel product = cmsBtProductDao.selectOneWithQuery(queryObject, channelId);
            resultInfo.setChannelId(product.getChannelId());
            resultInfo.setCode(product.getCommon().getFields().getCode());
            resultInfo.setName(product.getCommon().getFields().getProductNameEn());
            resultInfo.setProductId(product.getProdId().toString());
            resultInfo.setShortDescription(product.getCommon().getFields().getShortDesEn());
            resultInfo.setLongDescription(product.getCommon().getFields().getLongDesEn());
            // TODO set productType(but now productType is not commen field)
            resultInfo.setDescription(product.getCommon().getFields().getProductType());
            resultInfo.setBrandName(product.getCommon().getFields().getBrand());
            resultInfo.setGender(product.getCommon().getFields().getSizeType());
            // TODO 无法提供,属于主数据的非共通属性
            resultInfo.setMaterialFabricName("");
            resultInfo.setCountryName(product.getCommon().getFields().getOrigin());

            // 设置人民币价格
            resultInfo.setMsrp(product.getCommon().getSku(productSku).getPriceMsrp() != null ? product.getCommon().getSku(productSku).getPriceMsrp().toString() : "0.00");
            resultInfo.setPrice(product.getCommon().getSku(productSku).getPriceRetail() != null ? product.getCommon().getSku(productSku).getPriceRetail().toString() : "0.00");
            resultInfo.setRetailPrice(product.getCommon().getSku(productSku).getPriceRetail() != null ? product.getCommon().getSku(productSku).getPriceRetail().toString() : "0.00");

            // 设置美元价格
            resultInfo.setClientMsrpPrice(String.valueOf(product.getCommon().getSku(productSku).getClientMsrpPrice()));
            resultInfo.setClientRetailPrice(String.valueOf(product.getCommon().getSku(productSku).getClientRetailPrice()));
            resultInfo.setClientNetPrice(String.valueOf(product.getCommon().getSku(productSku).getClientNetPrice()));

            // 设置原始价格单位

            CmsChannelConfigBean channelConfig = CmsChannelConfigs.getConfigBeanNoCode(product.getOrgChannelId()
                    , CmsConstants.ChannelConfig.CLIENT_PRICE_UNIT);
            resultInfo.setClientPriceUnit(channelConfig.getConfigValue1());

            // TODO 无法提供,属于主数据的非共通属性
            resultInfo.setWeightkg("");
            // TODO 无法提供,属于主数据的非共通属性
            resultInfo.setWeightlb("");
            resultInfo.setModelName(product.getCommon().getFields().getModel());
            // TODO 无法提供,属于主数据的非共通属性
            resultInfo.setUrlKey("");
            String imagePath = "";
            if (product.getCommon().getFields().getImages1().size() > 0) {
                if (!StringUtils.isEmpty(product.getCommon().getFields().getImages1().get(0).getName()))
                    imagePath = imageTemplateService.getImageUrl(product.getCommon().getFields().getImages1().get(0).getName());
            }
            resultInfo.setShowName(imagePath);
            resultInfo.setCnName(product.getCommon().getFields().getOriginalTitleCn());
            // 获取HsCodeCrop
            String hsCodeCrop = product.getCommon().getFields().getHsCodeCrop();
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
            String hsCodePrivate = product.getCommon().getFields().getHsCodePrivate();
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
//        if (projection != null && projection.length > 0) {
//            queryObject.setProjectionExt(projection);
//        }

        StringBuilder sbQuery = new StringBuilder();

        if (!StringUtils.isEmpty(skuIncludes)) {
            sbQuery.append(MongoUtils.splicingValue("platforms.P" + cartId + ".skus.skuCode", skuIncludes, "$regex"));
            sbQuery.append(",");
        } else if (skuList != null && skuList.size() > 0) {
            sbQuery.append(MongoUtils.splicingValue("platforms.P" + cartId + ".skus.skuCode", skuList.toArray(new String[skuList.size()])));
            sbQuery.append(",");
        }

        // 设定name的模糊查询
        if (!StringUtils.isEmpty(nameIncludes)) {
            sbQuery.append(MongoUtils.splicingValue("common.fields.productNameEn", nameIncludes, "$regex"));
            sbQuery.append(",");
        }

        // 设定description的模糊查询
        if (!StringUtils.isEmpty(descriptionIncludes)) {
            sbQuery.append(MongoUtils.splicingValue("common.fields.longDesEn", descriptionIncludes, "$regex"));
            sbQuery.append(",");
        }

        if (!StringUtils.isEmpty(sbQuery.toString())) {
            queryObject.setQuery("{" + sbQuery.toString().substring(0, sbQuery.toString().length() - 1) + "}");
        }

        queryObject.setLimit(50);
        List<CmsBtProductModel> products = cmsBtProductDao.select(queryObject, channelId);

        List<ProductForOmsBean> resultInfo = new ArrayList<>();
        for (CmsBtProductModel product : products) {
            product.getPlatform(Integer.valueOf(cartId)).getSkus().forEach(skuInfo -> {
                ProductForOmsBean bean = new ProductForOmsBean();
                // 设置商品的原始channelId
                bean.setChannelId(product.getOrgChannelId());
                String skuCode = skuInfo.getStringAttribute("skuCode");
                bean.setSku(skuCode);
                bean.setProduct(product.getCommon().getFields().getProductNameEn());
                bean.setDescription(product.getCommon().getFields().getLongDesEn());
                Double priceSale = skuInfo.getDoubleAttribute("priceSale");
                bean.setPricePerUnit(String.valueOf(priceSale));
                // TODO 目前无法取得库存值
                Map<String, Object> param = new HashMap<>();
                param.put("channelId", product.getOrgChannelId());
                param.put("sku", skuCode);
                WmsBtInventoryCenterLogicModel skuInventory = wmsBtInventoryCenterLogicDao.selectItemDetailBySku(param);
                bean.setInventory(String.valueOf(skuInventory.getQtyChina()));
                String imagePath = "";
                if (product.getCommon().getFields().getImages1().size() > 0) {
                    if (!StringUtils.isEmpty(product.getCommon().getFields().getImages1().get(0).getName()))
                        imagePath = imageTemplateService.getImageUrl(product.getCommon().getFields().getImages1().get(0).getName());
                }
                bean.setImgPath(imagePath);

                // 取得该商品的组信息
                CmsBtProductGroupModel grpObj = cmsBtProductGroupDao.selectOneWithQuery("{\"cartId\":" + cartId + ",\"productCodes\":\"" + product.getCommon().getFields().getCode() + "\"},{\"numIid\":1,\"_id\":0}", channelId);

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
            });
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

    public String updateProductPlatform(String channelId, Long prodId, CmsBtProductModel_Platform_Cart platformModel, String modifier) {
        return updateProductPlatform(channelId, prodId, platformModel, modifier, false);
    }

    public String updateProductPlatform(String channelId, Long prodId, CmsBtProductModel_Platform_Cart platformModel, String modifier, Boolean isModifiedChk) {
        CmsBtProductModel oldProduct = getProductById(channelId, prodId);
        if (isModifiedChk) {
            CmsBtProductModel_Platform_Cart cmsBtProductModel_platform_cart = oldProduct.getPlatform(platformModel.getCartId());
            String oldModified = null;
            if (cmsBtProductModel_platform_cart != null) {
                oldModified = cmsBtProductModel_platform_cart.getModified();
            }
            if (oldModified != null) {
                if (!oldModified.equalsIgnoreCase(platformModel.getModified())) {
                    throw new BusinessException("200011");
                }
            } else if (platformModel.getModified() != null) {
                throw new BusinessException("200011");
            }
        }
        platformModel.getSkus().forEach(sku -> {
            String diffFlg = "1";
            if(sku.getDoubleAttribute("priceSale") < sku.getDoubleAttribute("priceRetail")){
                diffFlg = "2";
            }else if(sku.getDoubleAttribute("priceSale") < sku.getDoubleAttribute("priceRetail")){
                diffFlg = "3";
            }
            sku.setAttribute("priceDiffFlg",diffFlg);
        });

        HashMap<String, Object> queryMap = new HashMap<>();
        queryMap.put("prodId", prodId);

        List<BulkUpdateModel> bulkList = new ArrayList<>();
        HashMap<String, Object> updateMap = new HashMap<>();
        platformModel.setModified(DateTimeUtil.getNowTimeStamp());
        updateMap.put("platforms.P" + platformModel.getCartId(), platformModel);
        BulkUpdateModel model = new BulkUpdateModel();
        model.setUpdateMap(updateMap);
        model.setQueryMap(queryMap);
        bulkList.add(model);
        cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, modifier, "$set");

        if (CmsConstants.ProductStatus.Approved.toString().equalsIgnoreCase(platformModel.getStatus())) {
            insertSxWorkLoad(channelId, new ArrayList<>(Arrays.asList(oldProduct.getCommon().getFields().getCode())), new ArrayList<>(Arrays.asList(platformModel.getCartId())), modifier);
        }
        insertProductHistory(channelId, prodId);

        return platformModel.getModified();
    }

    /**
     * 更新product的common属性
     *
     * @param channelId     渠道
     * @param prodId        产品ID
     * @param common        comm信息
     * @param modifier      更新者
     * @param isModifiedChk 是否检查最后更新时间
     * @return Map
     */
    public Map<String, Object> updateProductCommon(String channelId, Long prodId, CmsBtProductModel_Common common, String modifier, boolean isModifiedChk) {

        CmsBtProductModel oldProduct = getProductById(channelId, prodId);
        if (isModifiedChk) {
            String oldModified = oldProduct.getCommon().getModified() != null ? oldProduct.getCommon().getModified() : "";
            String newModified = common.getModified() != null ? common.getModified() : "";
            if (!oldModified.equalsIgnoreCase(newModified)) {
                throw new BusinessException("200011");
            }
        }

        common.setModified(DateTimeUtil.getNowTimeStamp());
        common.setModifier(modifier);
        common.getFields().setHsCodeStatus(StringUtil.isEmpty(common.getFields().getHsCodePrivate()) ? "0" : "1");
        HashMap<String, Object> queryMap = new HashMap<>();
        queryMap.put("prodId", prodId);

        List<BulkUpdateModel> bulkList = new ArrayList<>();
        HashMap<String, Object> updateMap = new HashMap<>();
        updateMap.put("common", common);
        BulkUpdateModel model = new BulkUpdateModel();
        model.setUpdateMap(updateMap);
        model.setQueryMap(queryMap);
        bulkList.add(model);
        cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, null, "$set");

        // 更新workLoad表
        CmsBtProductModel productModel = getProductById(channelId, prodId);
        insertSxWorkLoad(channelId, productModel, modifier);

        insertProductHistory(channelId, prodId);

        Map<String, Object> result = new HashMap<>();
        result.put("modified", common.getModified());
        result.put("translateStatus", common.getFields().getTranslateStatus());
        result.put("hsCodeStatus", common.getFields().getHsCodeStatus());
        return result;
    }

    public void updateProductLock(String channelId, Long prodId, String lock, String modifier) {
        HashMap<String, Object> queryMap = new HashMap<>();
        queryMap.put("prodId", prodId);
        List<BulkUpdateModel> bulkList = new ArrayList<>();
        HashMap<String, Object> updateMap = new HashMap<>();
        updateMap.put("lock", lock);
        updateMap.put("modifier", modifier);
        updateMap.put("modified", DateTimeUtil.getNowTimeStamp());
        BulkUpdateModel model = new BulkUpdateModel();
        model.setUpdateMap(updateMap);
        model.setQueryMap(queryMap);
        bulkList.add(model);
        cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, null, "$set");
        insertProductHistory(channelId, prodId);
    }

    public int updateProductFeedToMaster(String channelId, CmsBtProductModel cmsProduct, String modifier) {
        HashMap<String, Object> queryMap = new HashMap<>();
        queryMap.put("prodId", cmsProduct.getProdId());
        queryMap.put("modified", cmsProduct.getModified());
        List<BulkUpdateModel> bulkList = new ArrayList<>();
        HashMap<String, Object> updateMap = new HashMap<>();
//        if (cmsProduct.getCommon().getCatId() != null) {
//            updateMap.put("catId", cmsProduct.getCommon().getCatId());
//        }
//        if (cmsProduct.getCommon().getCatPath() != null) {
//            updateMap.put("catPath", cmsProduct.getCommon().getCatPath());
//        }

//        /**
//         * fields
//         */
//        if (cmsProduct.getCommon().getFields() != null) {
//            updateMap.put("fields", cmsProduct.getCommon().getFields());
//        }

//        /**
//         * skus
//         */
//        if (cmsProduct.getCommon().getSkus() != null) {
//            updateMap.put("skus", cmsProduct.getCommon().getSkus());
//        }

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

        insertProductHistory(channelId, cmsProduct.getProdId());
        return result.getModifiedCount();
    }

    public WriteResult updateMulti(JomgoUpdate updObj, String channelId) {
        return cmsBtProductDao.updateMulti(updObj, channelId);
    }

    public long countByQuery(final String strQuery, String channelId) {
        return cmsBtProductDao.countByQuery(strQuery, channelId);
    }

    public List<Map<String, Object>> aggregateToMap(String channelId, List<JomgoAggregate> aggregateList) {
        return cmsBtProductDao.aggregateToMap(channelId, aggregateList);
    }

    /**
     * 获取CustomProp
     *
     * @param product
     * @return
     */
    public List<CustomPropBean> getCustomProp(CmsBtProductModel product) {

        String channelId = product.getChannelId();
        CmsBtProductModel_Field fields = product.getCommon().getFields();

        CmsBtProductModel_Feed productFeed = product.getFeed();
        BaseMongoMap<String, Object> cnAttrs = productFeed.getCnAtts();

        List<CustomPropBean> props = new ArrayList<>();

        //读feed_info
        CmsBtFeedInfoModel feedInfo = cmsBtFeedInfoDao.selectProductByCode(channelId, fields.getCode());
        Map<String, List<String>> feedAttr = feedInfo.getAttribute();

        //读cms_mt_feed_custom_prop
        List<FeedCustomPropWithValueBean> feedCustomPropList = customPropService.getPropList(channelId, feedInfo.getCategory());

        //去除掉feedCustomPropList中的垃圾数据
        if (feedCustomPropList != null && feedCustomPropList.size() > 0) {
            feedCustomPropList = feedCustomPropList.stream().filter(w -> (!StringUtils.isNullOrBlank2(w.getFeed_prop_translation()) &&
                    !StringUtils.isNullOrBlank2(w.getFeed_prop_original()))).collect(Collectors.toList());
        } else {
            feedCustomPropList = new ArrayList<>();
        }

        List<String>  customIds = product.getFeed().getCustomIds();

        customIds = customIds == null ? new ArrayList<>() : customIds;


        //合并feedAttr和feedCustomPropList
        for (String attrKey : feedAttr.keySet()) {
            List<String> valueList = feedAttr.get(attrKey);
            CustomPropBean prop = new CustomPropBean();
            prop.setFeedAttrEn(attrKey);
            String attrValue = Joiner.on(",").skipNulls().join(valueList);
            prop.setFeedAttrValueEn(attrValue);
            prop.setFeedAttrCn("");
            prop.setFeedAttrValueCn("");
            prop.setFeedAttr(true);
            prop.setCustomPropActive(false);

            if (feedCustomPropList.stream().filter(w -> w.getFeed_prop_original().equals(attrKey)).count() > 0) {
                FeedCustomPropWithValueBean feedCustProp = feedCustomPropList.stream().filter(w -> w.getFeed_prop_original().equals(attrKey)).findFirst().get();
                prop.setFeedAttrCn(feedCustProp.getFeed_prop_translation());
                if (cnAttrs.keySet().stream().filter(w -> w.equals(attrKey)).count() > 0) {
                    //如果product已经保存过
                    String cnAttKey = cnAttrs.keySet().stream().filter(w -> w.equals(attrKey)).findFirst().get();
                    prop.setFeedAttrValueCn(cnAttrs.getStringAttribute(cnAttKey));
                } else {
                    //取默认值
                    Map<String, List<String>> defaultValueMap = feedCustProp.getMapPropValue();
                    List<String> vList = defaultValueMap.get(attrValue);
                    if (vList != null) {
                        if (vList.stream().filter(w -> !StringUtils.isNullOrBlank2(w)).count() > 0) {
                            String cnAttValue = vList.stream().filter(w -> !StringUtils.isNullOrBlank2(w)).findFirst().get();
                            prop.setFeedAttrValueCn(cnAttValue);
                        }
                    }

                }

                if(customIds.stream().filter(w->w.equals(attrKey)).count() >0)
                {
                    prop.setCustomPropActive(true);
                }
            }

            props.add(prop);
        }


        //仅存在于cms_mt_feed_custom_prop中，不存在于feed attributes中的项目
        for (FeedCustomPropWithValueBean custProp : feedCustomPropList) {
            String feedKey = custProp.getFeed_prop_original();
            if (feedAttr.keySet().stream().filter(w -> w.equals(feedKey)).count() == 0) {
                CustomPropBean prop = new CustomPropBean();
                prop.setFeedAttrEn(feedKey);
                prop.setFeedAttrValueEn("");
                prop.setFeedAttrCn(custProp.getFeed_prop_translation());
                prop.setFeedAttrValueCn("");
                prop.setFeedAttr(false);
                prop.setCustomPropActive(false);

                if (cnAttrs.keySet().stream().filter(w -> w.equals(feedKey)).count() > 0) {
                    String cnAttKey = cnAttrs.keySet().stream().filter(w -> w.equals(feedKey)).findFirst().get();
                    prop.setFeedAttrValueCn(cnAttrs.getStringAttribute(cnAttKey));
                }

                if(customIds.stream().filter(w->w.equals(feedKey)).count() >0)
                {
                    prop.setCustomPropActive(true);
                }
                props.add(prop);
            }

        }
        return props;
    }
}
