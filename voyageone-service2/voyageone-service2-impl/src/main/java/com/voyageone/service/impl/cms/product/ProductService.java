package com.voyageone.service.impl.cms.product;

import com.google.common.base.Joiner;
import com.mongodb.BasicDBObject;
import com.mongodb.BulkWriteResult;
import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.JongoAggregate;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.Enums.CartEnums.Cart;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.*;
import com.voyageone.components.jd.service.JdProductService;
import com.voyageone.components.tmall.service.TbProductService;
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
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.model.cms.CmsBtPriceLogModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.product.*;
import com.voyageone.service.model.wms.WmsBtInventoryCenterLogicModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang.math.NumberUtils;

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
    private ImageTemplateService imageTemplateService;

    @Autowired
    private MongoSequenceService commSequenceMongoService;

    @Autowired
    private FeedCustomPropService customPropService;

    @Autowired
    private CmsBtPriceLogService cmsBtPriceLogService;

    @Autowired
    private SxProductService sxProductService;

    @Autowired
    private TbProductService tbProductService;

    @Autowired
    private JdProductService jdProductService;

    @Autowired
    private ProductStatusHistoryService productStatusHistoryService;

    @Autowired
    private ProductLogService productLogService;


    /**
     * 获取商品 根据ID获
     */
    public CmsBtProductModel getProductById(String channelId, long prodId) {
        String query = "{\"prodId\":" + prodId + "}";
        return cmsBtProductDao.selectOneWithQuery(query, channelId);
    }

    /**
     * 获取商品 根据ID获
     */
    public List<CmsBtProductModel> getProductByNumIid(String channelId, String numIid, Integer cartId) {
        String temp = "platforms.P"+cartId+".pNumIId";
        String query = String.format("{\"%s\":\"%s\"}",temp,numIid);
        return cmsBtProductDao.select(query, channelId);
    }
    /**
     * 获取商品 根据Code
     */
    public CmsBtProductModel getProductByCode(String channelId, String code) {
        String query = "{\"common.fields.code\":\"" + code + "\"}";
        return cmsBtProductDao.selectOneWithQuery(query, channelId);
    }

    /**
     * 获取商品 根据Code&OriginalCode
     */
    public CmsBtProductModel getProductSingleSku(String channelId, String code, String originalCode) {
        String query = "{\"common.fields.code\":\"" + code + "\", \"common.fields.originalCode\":\"" + originalCode + "\"}";
        return cmsBtProductDao.selectOneWithQuery(query, channelId);
    }

    /**
     * 获取商品 根据OriginalCode
     */
    public List<CmsBtProductModel> getProductByOriginalCode(String channelId, String code) {
        String query = "{\"common.fields.originalCode\":\"" + code + "\"}";
        return cmsBtProductDao.select(query, channelId);
    }

    /**
     * 根据Id返回多条产品数据
     */
    public List<CmsBtProductModel> getListByIds(List<Long> ids, String channelId) {
        return cmsBtProductDao.selectProductByIds(ids, channelId);
    }

    /**
     * 根据codes返回多条产品数据
     */
    public List<CmsBtProductModel> getListByCodes(List<String> codes, String channelId) {
        return cmsBtProductDao.selectProductByCodes(codes, channelId);
    }

    /**
     * 获取商品 Sku
     */
    public CmsBtProductModel getProductBySku(String channelId, String sku) {
        String query = "{\"common.skus.skuCode\":\"" + sku + "\"}";
        return cmsBtProductDao.selectOneWithQuery(query, channelId);
    }

    /**
     * 获取商品 根据query
     */
    public CmsBtProductModel getProductByCondition(String channelId, JongoQuery query) {
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
        JongoQuery queryObject = new JongoQuery();
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
        queryObject.setQuery("{" + MongoUtils.splicingValue("common.fields.code", codeArr, "$in") + "}");

        List<CmsBtProductBean> rst = cmsBtProductDao.selectBean(queryObject, channelId);
        rst.forEach(prodObj -> prodObj.setGroupBean(grpObj));
        return rst;
    }

    // 查询指定平台下各商品组中包含的商品code
    // 返回的map中，key是group id，value中是商品code列表
    public Map<String, List<String>> getProductGroupIdCodesMapByCart(String channelId, int cartId, String orgChannelId) {
        JongoQuery queryObject = new JongoQuery();
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
    public List<CmsBtProductModel> getList(String channelId, JongoQuery queryObject) {
        return cmsBtProductDao.select(queryObject, channelId);
    }

    /**
     * getList
     * 注意：调用此方法时，返回值中的getGroupBean()为空，需要自行填值
     * 如需要groupBean,请使用getListWithGroup()
     */
    public List<CmsBtProductBean> getBeanList(String channelId, JongoQuery queryObject) {
        return cmsBtProductDao.selectBean(queryObject, channelId);
    }

    // 查询产品信息(合并该产品的组信息)
    // queryObject中必须包含输出项:"common.fields.code"，否则将查询不到组信息
    public List<CmsBtProductBean> getListWithGroup(String channelId, int cartId, JongoQuery queryObject) {
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
            JongoQuery qrpQuy = new JongoQuery();
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

        String prodCodeQuery = String.format("{ \"common.fields.code\" : \"%s\" }", product.getCommon().getFields().getCode());
        count = cmsBtProductDao.countByQuery(prodCodeQuery, channelId);
        if (count > 0) {
            throw new RuntimeException("common.fields.code has existed, not add!");
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

    public WriteResult updateFirstProduct(JongoUpdate updObj, String channelId) {
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
            queryStr = String.format("{\"common.fields.code\" : \"%s\" }", productCode);
            queryMap.put("common.fields.code", productCode);
        }

        if (StringUtils.isEmpty(queryStr)) {
            return;
        }

        JongoQuery queryObject = new JongoQuery();
        queryObject.setQuery(queryStr);
        queryObject.setProjectionExt("prodId", "modified"); // TODO--这里不应该再从fields取status

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
            updateMap.put("common.catId", catId);
        }
        String catPath = productModel.getCommon().getCatPath();
        if (catPath != null) {
            updateMap.put("common.catPath", catPath);
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
        if (fields != null && !fields.isEmpty()) {
            BasicDBObject fieldObj = fields.toUpdateBasicDBObject("common.fields.");
            updateMap.putAll(fieldObj);
        }

        /**
         * Feed
         */
        CmsBtProductModel_Feed feed = productModel.getFeed();
        if (feed != null) {
            if (!StringUtils.isEmpty(feed.getCatId())) {
                updateMap.put("feed.catId", feed.getCatId());
            }
            if (!StringUtils.isEmpty(feed.getCatPath())) {
                updateMap.put("feed.catPath", feed.getCatPath());
            }
            if (feed.getOrgAtts() != null && !feed.getOrgAtts().isEmpty()) {
                BasicDBObject orgAttsObj = feed.getOrgAtts().toUpdateBasicDBObject("feed.orgAtts.");
                updateMap.putAll(orgAttsObj);
            }
            if (feed.getCnAtts() != null && !feed.getCnAtts().isEmpty()) {
                BasicDBObject cnAttsObj = feed.getCnAtts().toUpdateBasicDBObject("feed.cnAtts.");
                updateMap.putAll(cnAttsObj);
            }
            if (feed.getCustomIds() != null && !feed.getCustomIds().isEmpty()) {
                updateMap.put("feed.customIds", feed.getCustomIds());
            }
            if (feed.getCustomIdsCn() != null && !feed.getCustomIdsCn().isEmpty()) {
                updateMap.put("feed.customIdsCn", feed.getCustomIdsCn());
            }
        }


        /**
         * set update model
         */
        if (!updateMap.isEmpty()) {
            BulkUpdateModel model = new BulkUpdateModel();
            model.setUpdateMap(updateMap);
            model.setQueryMap(queryMap);
            bulkList.add(model);
        }

        /**
         * execute update
         */
        if (!bulkList.isEmpty()) {


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
        if (skus != null && !skus.isEmpty()) {

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
            logModel = JacksonUtil.json2Bean(JacksonUtil.bean2Json(productModel), logModel.getClass());
            logModel.set_id(null);
            cmsBtProductLogDao.insert(logModel);
        }
    }

    /**
     * get the product info from wms's request
     */
    public ProductForWmsBean getWmsProductsInfo(String channelId, String productSku, String[] projection) {
        JongoQuery queryObject = new JongoQuery();
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
            if(product == null) {
                $error("该产品不存在:" + productSku  + "--" + channelId);
                throw new BusinessException("该产品不存在:" + productSku);
            }
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
            if (!product.getCommon().getFields().getImages1().isEmpty()) {
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
        JongoQuery queryObject = new JongoQuery();
        // set fields
//        if (projection != null && projection.length > 0) {
//            queryObject.setProjectionExt(projection);
//        }

        StringBuilder sbQuery = new StringBuilder();

        if("0".equalsIgnoreCase(cartId)){
            if (!StringUtils.isEmpty(skuIncludes)) {
                sbQuery.append(MongoUtils.splicingValue("common.skus.skuCode", skuIncludes, "$regex"));
                sbQuery.append(",");
            } else if (skuList != null && !skuList.isEmpty()) {
                sbQuery.append(MongoUtils.splicingValue("common.skus.skuCode", skuList.toArray(new String[skuList.size()])));
                sbQuery.append(",");
            }
        }else {
            if (!StringUtils.isEmpty(skuIncludes)) {
                sbQuery.append(MongoUtils.splicingValue("platforms.P" + cartId + ".skus.skuCode", skuIncludes, "$regex"));
                sbQuery.append(",");
            } else if (skuList != null && !skuList.isEmpty()) {
                sbQuery.append(MongoUtils.splicingValue("platforms.P" + cartId + ".skus.skuCode", skuList.toArray(new String[skuList.size()])));
                sbQuery.append(",");
            }
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
            List<BaseMongoMap<String, Object>> skus = new ArrayList<>();
            if("0".equalsIgnoreCase(cartId)){
                List<CmsBtProductModel_Sku> skus1 = product.getCommon().getSkus();
                for(CmsBtProductModel_Sku s:skus1) {
                    skus.add(s);
                }
            }else {
                if (!StringUtils.isEmpty(skuIncludes)) {
                    skus = product.getPlatform(Integer.parseInt(cartId)).getSkus().stream()
                            .filter(sku -> sku.getStringAttribute("skuCode").indexOf(skuIncludes) > -1).collect(Collectors.toList());
                } else {
                    skus = product.getPlatform(Integer.parseInt(cartId)).getSkus().stream()
                            .filter(sku -> skuList.contains(sku.getStringAttribute("skuCode"))).collect(Collectors.toList());
                }
            }
            if(skus == null || skus.size() == 0) return resultInfo;
            skus.forEach(skuInfo -> {
                ProductForOmsBean bean = new ProductForOmsBean();
                // 设置商品的原始channelId
                bean.setChannelId(product.getOrgChannelId());
                String skuCode = skuInfo.getStringAttribute("skuCode");
                bean.setSku(skuCode);
                bean.setProduct(product.getCommon().getFields().getProductNameEn());
                bean.setDescription(product.getCommon().getFields().getLongDesEn());
                Double priceSale;
                if("0".equalsIgnoreCase(cartId)){
                    priceSale = skuInfo.getDoubleAttribute("priceRetail");
                }else{
                    priceSale = skuInfo.getDoubleAttribute("priceSale");
                }
                bean.setPricePerUnit(String.valueOf(priceSale));
                // TODO 目前无法取得库存值
                Map<String, Object> param = new HashMap<>();
                param.put("channelId", product.getOrgChannelId());
                param.put("sku", skuCode);
                WmsBtInventoryCenterLogicModel skuInventory = wmsBtInventoryCenterLogicDao.selectItemDetailBySku(param);

                if(skuInventory != null) {
                    bean.setInventory(String.valueOf(skuInventory.getQtyChina()));
                }
                String imagePath = "";
                if (!product.getCommon().getFields().getImages1().isEmpty()) {
                    if (!StringUtils.isEmpty(product.getCommon().getFields().getImages1().get(0).getName()))
                        imagePath = imageTemplateService.getImageUrl(product.getCommon().getFields().getImages1().get(0).getName());
                }
                bean.setImgPath(imagePath);

                // 取得该商品的组信息
                CmsBtProductGroupModel grpObj = cmsBtProductGroupDao.selectOneWithQuery("{\"cartId\":" + cartId + ",\"productCodes\":\"" + product.getCommon().getFields().getCode() + "\"},{\"numIid\":1,\"_id\":0}", channelId);

                // TODO 目前写死,以后再想办法修改
                String numIid = "";
                Cart cartEnum = Cart.getValueByID(cartId);
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

        if (listLogicInventory != null && !listLogicInventory.isEmpty()) {
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
        return updateProductPlatform(channelId, prodId, platformModel, modifier, isModifiedChk, EnumProductOperationType.WebEdit, "页面编辑");
    }

    public String updateProductPlatform(String channelId, Long prodId, CmsBtProductModel_Platform_Cart platformModel, String modifier, Boolean isModifiedChk, EnumProductOperationType opeType, String comment) {
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
            sku.setAttribute("priceDiffFlg", productSkuService.getPriceDiffFlg(channelId, sku));
            Double msrp = sku.getDoubleAttribute("priceMsrp");
            Double priceRetail = sku.getDoubleAttribute("priceRetail");
            if( msrp.compareTo(priceRetail) > 0){
                sku.setAttribute("priceMsrpFlg","XD");
            }else if( msrp.compareTo(priceRetail) < 0){
                sku.setAttribute("priceMsrpFlg","XU");
            }else{
                sku.setAttribute("priceMsrpFlg","");
            }
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
            sxProductService.insertSxWorkLoad(channelId, new ArrayList<String>(Arrays.asList(oldProduct.getCommon().getFields().getCode())), platformModel.getCartId(), modifier);
        }
        insertProductHistory(channelId, prodId);

        List<String> skus = new ArrayList<>();
        platformModel.getSkus().forEach(sku -> skus.add(sku.getStringAttribute("skuCode")));
        cmsBtPriceLogService.addLogForSkuListAndCallSyncPriceJob(skus, channelId, platformModel.getCartId(), modifier, comment);
        productStatusHistoryService.insert(channelId, oldProduct.getCommon().getFields().getCode(), platformModel.getStatus(), platformModel.getCartId(), opeType, comment, modifier);

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
        if(!common.getFields().getHsCodeStatus().equalsIgnoreCase(oldProduct.getCommon().getCatId())){
            if(common.getFields().getHsCodeStatus().equalsIgnoreCase("1")){
                common.getFields().setHsCodeSetTime(DateTimeUtil.getNowTimeStamp());
                common.getFields().setHsCodeSetter(modifier);
            }
        }
        common.getFields().setCategoryStatus(StringUtil.isEmpty(common.getCatId()) ? "0" : "1");
        if(!common.getFields().getCategoryStatus().equalsIgnoreCase(oldProduct.getCommon().getCatId())){
            if(common.getFields().getCategoryStatus().equalsIgnoreCase("1")){
                common.getFields().setCategorySetTime(DateTimeUtil.getNowTimeStamp());
                common.getFields().setCategorySetter(modifier);
            }
        }
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
        sxProductService.insertSxWorkLoad(getProductById(channelId, prodId), modifier);

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

    public int updateProductFeedToMaster(String channelId, CmsBtProductModel cmsProduct, String modifier, String comment) {
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

            if (feed.getOrgAtts() != null && !feed.getOrgAtts().isEmpty()) {
                BasicDBObject orgAttsObj = feed.getOrgAtts().toUpdateBasicDBObject("feed.orgAtts.");
                updateMap.putAll(orgAttsObj);
            }
            if (feed.getCnAtts() != null && !feed.getCnAtts().isEmpty()) {
                BasicDBObject cnAttsObj = feed.getCnAtts().toUpdateBasicDBObject("feed.cnAtts.");
                updateMap.putAll(cnAttsObj);
            }
            if (feed.getCustomIds() != null && !feed.getCustomIds().isEmpty()) {
                updateMap.put("feed.customIds", feed.getCustomIds());
            }
            if (feed.getCustomIdsCn() != null && !feed.getCustomIdsCn().isEmpty()) {
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
        // 记录价格变更履历
        addPriceUpdateHistory(cmsProduct,modifier, comment);

        return result.getModifiedCount();
    }

    public WriteResult updateMulti(JongoUpdate updObj, String channelId) {
        return cmsBtProductDao.updateMulti(updObj, channelId);
    }

    public long countByQuery(final String strQuery, String channelId) {
        return cmsBtProductDao.countByQuery(strQuery, channelId);
    }

    public long countByQuery(final String strQuery, final Object[] parameters, String channelId) {
        return cmsBtProductDao.countByQuery(strQuery, parameters, channelId);
    }

    public List<Map<String, Object>> aggregateToMap(String channelId, List<JongoAggregate> aggregateList) {
        return cmsBtProductDao.aggregateToMap(channelId, aggregateList);
    }

    /**
     * 获取CustomProp
     */
    public List<CustomPropBean> getCustomProp(CmsBtProductModel product) {

        String channelId = product.getChannelId();
        CmsBtProductModel_Field fields = product.getCommon().getFields();

        CmsBtProductModel_Feed productFeed = product.getFeed();
        BaseMongoMap<String, Object> cnAttrs = productFeed.getCnAtts();

        List<CustomPropBean> props = new ArrayList<>();

        //读feed_info
        CmsBtFeedInfoModel feedInfo = cmsBtFeedInfoDao.selectProductByCode(product.getOrgChannelId(), fields.getOriginalCode());
        if (feedInfo == null) {
            $error("getCustomProp 无feedInfo channelid=%s, prodid=%d", channelId, product.getProdId());
            return props;
        }
        Map<String, List<String>> feedAttr = feedInfo.getAttribute();

        //读cms_mt_feed_custom_prop
        List<FeedCustomPropWithValueBean> feedCustomPropList = customPropService.getPropList(channelId, feedInfo.getCategory());

        //去除掉feedCustomPropList中的垃圾数据
        if (feedCustomPropList != null && !feedCustomPropList.isEmpty()) {
            feedCustomPropList = feedCustomPropList.stream().filter(w -> !StringUtils.isNullOrBlank2(w.getFeed_prop_translation()) &&
                    !StringUtils.isNullOrBlank2(w.getFeed_prop_original())).collect(Collectors.toList());
        } else {
            feedCustomPropList = new ArrayList<>();
        }

        List<String> customIds = product.getFeed().getCustomIds();

        customIds = customIds == null ? new ArrayList<>() : customIds;


        //合并feedAttr和feedCustomPropList
        for (Map.Entry<String, List<String>> entry : feedAttr.entrySet()) {
            String attrKey = entry.getKey();
            List<String> valueList = entry.getValue();
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

                if (customIds.stream().filter(w -> w.equals(attrKey)).count() > 0) {
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

                if (customIds.stream().filter(w -> w.equals(feedKey)).count() > 0) {
                    prop.setCustomPropActive(true);
                }
                props.add(prop);
            }

        }
        return props;
    }

    public String updateProductAtts(String channelId, Long prodId, List<CustomPropBean> cnProps, String modifier) {

        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("prodId", prodId);
        Map<String, Object> rsMap = new HashMap<>();
        String modified = DateTimeUtil.getNowTimeStamp();
        rsMap.put("modified", modified);
        rsMap.put("modifier", modifier);
        if (cnProps != null) {
            rsMap.put("feed.customIds", cnProps.stream().filter(CustomPropBean::isCustomPropActive).map(CustomPropBean::getFeedAttrEn).collect(Collectors.toList()));
            rsMap.put("feed.customIdsCn", cnProps.stream().filter(CustomPropBean::isCustomPropActive).map(CustomPropBean::getFeedAttrCn).collect(Collectors.toList()));
            rsMap.put("feed.orgAtts", cnProps.stream().filter(customPropBean -> !StringUtil.isEmpty(customPropBean.getFeedAttrCn())).collect(toMap(CustomPropBean::getFeedAttrEn, CustomPropBean::getFeedAttrValueEn)));
            rsMap.put("feed.cnAtts", cnProps.stream().filter(customPropBean -> !StringUtil.isEmpty(customPropBean.getFeedAttrCn())).collect(toMap(CustomPropBean::getFeedAttrEn, CustomPropBean::getFeedAttrValueCn)));
        }

        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("$set", rsMap);

        cmsBtProductDao.update(channelId, queryMap, updateMap);
        // 更新workLoad表
        sxProductService.insertSxWorkLoad(getProductById(channelId, prodId), modifier);
        insertProductHistory(channelId, prodId);
        return modified;
    }

    public void delPlatfromProduct(String channelId, Integer cartId, String numIid){
        if(StringUtil.isEmpty(numIid)) return;
        ShopBean shopBean = Shops.getShop(channelId, cartId);
        Cart cartEnum = Cart.getValueByID(cartId.toString());
        try {
            switch (cartEnum) {
                case TM:
                case TG:
                    tbProductService.delItem(shopBean, numIid);
                    break;
                case JD:
                case JG:
                case JGY:
                case JGJ:
                    jdProductService.delItem(shopBean,numIid);
                    break;
            }
        }catch (Exception e){
            throw new BusinessException("商品删除失败：" + e.getMessage());
        }
    }

    public void addPriceUpdateHistory(CmsBtProductModel cmsProduct, String modifier, String comment) {
        // 记录商品价格表动履历，并向Mq发送消息同步sku,code,group价格范围
        if (cmsProduct != null && cmsProduct.getPlatforms() != null && cmsProduct.getPlatforms().size() > 0) {
            cmsProduct.getPlatforms().forEach((cartId, platform) -> {
                if (ListUtils.notNull(platform.getSkus())) {
                    List<String> skuCodeList = new ArrayList<>();
                    platform.getSkus().forEach(sku -> skuCodeList.add(sku.getStringAttribute("skuCode")));
                    // 记录商品价格变动履历
                    cmsBtPriceLogService.addLogForSkuListAndCallSyncPriceJob(skuCodeList, cmsProduct.getChannelId(),
                            platform.getCartId(), modifier, comment);
                }
            });
        }
    }
}
