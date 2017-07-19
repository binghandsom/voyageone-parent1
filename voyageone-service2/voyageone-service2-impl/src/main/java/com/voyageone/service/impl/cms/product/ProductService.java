package com.voyageone.service.impl.cms.product;

import com.google.common.collect.Lists;
import com.mongodb.BasicDBObject;
import com.mongodb.BulkWriteResult;
import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.JongoAggregate;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.dao.mongodb.model.BulkJongoUpdateList;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Enums.CartEnums.Cart;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.*;
import com.voyageone.service.bean.cms.CustomPropBean;
import com.voyageone.service.bean.cms.businessmodel.CmsAddProductToPromotion.TagTreeNode;
import com.voyageone.service.bean.cms.product.*;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductGroupDao;
import com.voyageone.service.dao.wms.WmsBtInventoryCenterLogicDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.CmsBtCustomPropService;
import com.voyageone.service.impl.cms.CmsMtEtkHsCodeService;
import com.voyageone.service.impl.cms.ImageTemplateService;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.wms.InventoryCenterLogicService;
import com.voyageone.service.model.cms.CmsMtEtkHsCodeModel;
import com.voyageone.service.model.cms.mongo.CmsBtOperationLogModel_Msg;
import com.voyageone.service.model.cms.mongo.product.*;
import com.voyageone.service.model.wms.WmsBtInventoryCenterLogicModel;
import org.apache.commons.collections.CollectionUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
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
    private CmsBtProductGroupDao cmsBtProductGroupDao;

    @Autowired
    private CmsBtProductDao cmsBtProductDao;

    @Autowired
    private ProductSkuService productSkuService;

    @Autowired
    private WmsBtInventoryCenterLogicDao wmsBtInventoryCenterLogicDao;

    @Autowired
    private ImageTemplateService imageTemplateService;

    @Autowired
    private CmsBtPriceLogService cmsBtPriceLogService;

    @Autowired
    private SxProductService sxProductService;

    @Autowired
    private CmsMtEtkHsCodeService cmsMtEtkHsCodeService;

    @Autowired
    private InventoryCenterLogicService inventoryCenterLogicService;

    @Autowired
    private CmsBtCustomPropService cmsBtCustomPropService;

    @Autowired
    private ProductStatusHistoryService productStatusHistoryService;
    /**
     * 获取商品 根据ID获
     */
    public CmsBtProductModel getProductByObjectId(String channelId, String objectId) {
        JongoQuery jongoQuery = new JongoQuery();
        jongoQuery.setObjectId(new ObjectId(objectId));
        return cmsBtProductDao.selectOneWithQuery(jongoQuery, channelId);
    }

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
        String temp = "platforms.P" + cartId + ".pNumIId";
        String query = String.format("{\"%s\":\"%s\"}", temp, numIid);
        return cmsBtProductDao.select(query, channelId);
    }

    /**
     * 获取商品 根据Code
     */
    public CmsBtProductModel getProductByCode(String channelId, String code) {
        String query = "{\"common.fields.code\":\"" + code + "\"}";
        return cmsBtProductDao.selectOneWithQuery(query, channelId);
    }

    public CmsBtProductModel getProductByCode(String channelId, String orgCannelId, String code) {
        String query = "{\"orgChannelId\":\"" + orgCannelId + "\",\"common.fields.code\":\"" + code + "\"}";
        return cmsBtProductDao.selectOneWithQuery(query, channelId);
    }

    /**
     * 获取商品 根据Code&OriginalCode
     */
    public CmsBtProductModel getProductSingleSku(String channelId, String code, String originalCode) {
        String query = "{\"common.fields.code\":\"" + code + "\", \"common.fields.originalCode\":\"" + originalCode + "\"}";
        return cmsBtProductDao.selectOneWithQuery(query, channelId);
    }

    public CmsBtProductModel getProductSingleSku(String channelId, String orgCannelId, String code, String originalCode) {
        String query = "{\"orgChannelId\":\"" + orgCannelId + "\",\"common.fields.code\":\"" + code + "\", \"common.fields.originalCode\":\"" + originalCode + "\"}";
        return cmsBtProductDao.selectOneWithQuery(query, channelId);
    }

    /**
     * 获取商品 根据OriginalCode
     */
    public List<CmsBtProductModel> getProductByOriginalCode(String channelId, String code) {
        String query = "{\"common.fields.originalCode\":\"" + code + "\"}";
        return cmsBtProductDao.select(query, channelId);
    }

    public List<CmsBtProductModel> getProductByOriginalCode(String channelId, String orgChannelId, String code) {
        String query = "{\"orgChannelId\":\"" + orgChannelId + "\", \"common.fields.originalCode\":\"" + code + "\"}";
        return cmsBtProductDao.select(query, channelId);
    }

    /**
     * 获取拆分后的商品 根据OriginalCode，去掉code和original一致的拆分前商品
     */
    public List<CmsBtProductModel> getProductByOriginalCodeWithoutItself(String channelId, String code) {
        List<CmsBtProductModel> result = null;
        List<CmsBtProductModel> originalCodeProductList = this.getProductByOriginalCode(channelId, code);
        if (ListUtils.notNull(originalCodeProductList)) {
            result = originalCodeProductList.stream().filter(p -> !p.getCommonNotNull().getFieldsNotNull().getCode().equals(p.getCommonNotNull().getFieldsNotNull().getOriginalCode())).collect(Collectors.toList());
        }

        return result;
    }

    /**
     * 获取商品 根据SkuCode(一个SkuCode应该只在一个product中)
     */
    public List<CmsBtProductModel> getProductBySkuCode(String channelId, String skuCode) {
        String query = "{\"common.skus.skuCode\":\"" + skuCode + "\"}";
        return cmsBtProductDao.select(query, channelId);
    }

    /**
     * 获取商品 Sku
     */
    public CmsBtProductModel getProductBySku(String channelId, String sku) {
        String query = "{\"common.skus.skuCode\":\"" + sku + "\"}";
        return cmsBtProductDao.selectOneWithQuery(query, channelId);
    }

    /**
     * 获取商品 Sku
     */
    public CmsBtProductModel getProductBySku(String channelId, String orgChannelId, String sku) {
        String query = "{\"orgChannelId\":\"" + orgChannelId + "\", \"common.skus.skuCode\":\"" + sku + "\"}";
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

    /**
     * 查询产品信息(合并该产品的组信息)
     * queryObject中必须包含输出项:"common.fields.code"，否则将查询不到组信息
     *
     * @param channelId
     * @param cartId
     * @param queryObject
     * @return
     */
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
     * checkProductDataIsReady
     *
     * @param channelId
     * @param productId
     * @return
     */
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

        // 记录价格变更履历
        addPriceUpdateHistory(product, modifier, "New");
    }

    /**
     * updateProduct
     *
     * @param channelId
     * @param paraMap
     * @param updateMap
     * @return
     */
    public WriteResult updateProduct(String channelId, Map paraMap, Map updateMap) {
        return cmsBtProductDao.update(channelId, paraMap, updateMap);
    }

    /**
     * updateFirstProduct
     *
     * @param updObj
     * @param channelId
     * @return
     */
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
            if (product == null) {
                $error("该产品不存在:" + productSku + "--" + channelId);
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
            if(channelConfig != null) {
                resultInfo.setClientPriceUnit(channelConfig.getConfigValue1());
            }

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
                String[] hsCodePu = hsCodePrivate.split(",");
                resultInfo.setHsCodePuId(hsCodePrivate);
                resultInfo.setHsCodePu(hsCodePu[0]);
                resultInfo.setHsDescriptionPu(hsCodePu[1]);
                resultInfo.setUnitPu(hsCodePu[2]);

                CmsMtEtkHsCodeModel cmsMtEtkHsCodeModel = cmsMtEtkHsCodeService.getEdcHsCodeByHsCode(hsCodePrivate);
                if (cmsMtEtkHsCodeModel != null) {
                    resultInfo.setEtkHsCode(cmsMtEtkHsCodeModel.getEtkHsCode());
                    resultInfo.setEtkDescription(cmsMtEtkHsCodeModel.getEtkDescription());
                    resultInfo.setEtkUnit(cmsMtEtkHsCodeModel.getEtkUnit());
                } else {
                    resultInfo.setEtkHsCode(hsCodePu[0]);
                    resultInfo.setEtkDescription(hsCodePu[1]);
                    resultInfo.setEtkUnit(hsCodePu[2]);
                }
            }
//            if (!StringUtil.isEmpty(hsCodePrivate)) {
//                CmsMtEtkHsCodeModel cmsMtEtkHsCodeModel = cmsMtEtkHsCodeService.getEdcHsCodeByHsCode(hsCodePrivate);
//                if (cmsMtEtkHsCodeModel != null) {
//                    resultInfo.setEtkHsCode(cmsMtEtkHsCodeModel.getEtkHsCode());
//                    resultInfo.setEtkDescription(cmsMtEtkHsCodeModel.getEtkDescription());
//                    resultInfo.setEtkUnit(cmsMtEtkHsCodeModel.getEtkUnit());
//                }
//            }
//            for (Map.Entry<String, CmsBtProductModel_Platform_Cart> entry : product.getPlatforms().entrySet()) {
//                if(entry.getValue().getCartId() > 10 && entry.getValue().getCartId() < 900 && entry.getValue().getStatus().equalsIgnoreCase("Approved") && !StringUtil.isEmpty(entry.getValue().getpCatPath())){
//                    CmsMtEtkHsCodeModel cmsMtEtkHsCodeModel = cmsMtEtkHsCodeService.getEdcHsCodeByHsCode(entry.getValue().getCartId(),  entry.getValue().getpCatPath());
//                    if(cmsMtEtkHsCodeModel != null){
//                        resultInfo.setEtkHsCode(cmsMtEtkHsCodeModel.getEtkHsCode());
//                        resultInfo.setEtkDescription(cmsMtEtkHsCodeModel.getEtkDescription());
//                        resultInfo.setEtkUnit(cmsMtEtkHsCodeModel.getEtkUnit());
//                        break;
//                    }
//                }
//            }
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

        if ("0".equalsIgnoreCase(cartId)) {
            if (!StringUtils.isEmpty(skuIncludes)) {
                sbQuery.append(MongoUtils.splicingValue("common.skus.skuCode", skuIncludes, "$regex"));
                sbQuery.append(",");
            } else if (skuList != null && !skuList.isEmpty()) {
                sbQuery.append(MongoUtils.splicingValue("common.skus.skuCode", skuList.toArray(new String[skuList.size()])));
                sbQuery.append(",");
            }
        } else {
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
            if ("0".equalsIgnoreCase(cartId)) {
                List<CmsBtProductModel_Sku> skus1 = product.getCommon().getSkus();
                for (CmsBtProductModel_Sku s : skus1) {
                    skus.add(s);
                }
            } else {
                if (!StringUtils.isEmpty(skuIncludes)) {
                    skus = product.getPlatform(Integer.parseInt(cartId)).getSkus().stream()
                            .filter(sku -> sku.getStringAttribute("skuCode").indexOf(skuIncludes) > -1).collect(Collectors.toList());
                } else if (skuList != null && !skuList.isEmpty()) {
                    System.out.print(product.getCommon().getFields().getCode());
                    skus = product.getPlatform(Integer.parseInt(cartId)).getSkus().stream()
                            .filter(sku -> skuList.contains(sku.getStringAttribute("skuCode"))).collect(Collectors.toList());
                } else {
                    skus = product.getPlatform(Integer.parseInt(cartId)).getSkus();
                }
            }
            if (skus == null || skus.size() == 0) return resultInfo;
            skus.forEach(skuInfo -> {
                ProductForOmsBean bean = new ProductForOmsBean();
                // 设置商品的原始channelId
                bean.setChannelId(product.getOrgChannelId());
                String skuCode = skuInfo.getStringAttribute("skuCode");
                bean.setSku(skuCode);
                bean.setProduct(product.getCommon().getFields().getProductNameEn());
                bean.setDescription(product.getCommon().getFields().getLongDesEn());
                Double priceSale;
                if ("0".equalsIgnoreCase(cartId)) {
                    priceSale = skuInfo.getDoubleAttribute("priceRetail");
                } else {
                    priceSale = skuInfo.getDoubleAttribute("priceSale");
                }
                bean.setPricePerUnit(String.valueOf(priceSale));
                bean.setInventory(String.valueOf(skuInfo.getIntAttribute("qty")));
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
                        case TM:
                        case TB:
                        case TT:
                        case LTT:
                            numIid = grpObj != null && !StringUtils.isEmpty(grpObj.getNumIId())
                                    ? Constants.productForOtherSystemInfo.TMALL_NUM_IID + grpObj.getNumIId() : "";
                            break;
                        case JD:
                        case JG:
                        case JGJ:
                        case JGY:
                            numIid = !StringUtils.isEmpty(skuInfo.getStringAttribute("jdSkuId"))
                                    ? String.format(Constants.productForOtherSystemInfo.JINDONG_NUM_IID, grpObj.getNumIId()) : "";
                            break;
                        case JM:
                            numIid = grpObj != null && !StringUtils.isEmpty(grpObj.getPlatformMallId())
                                    ? String.format(Constants.productForOtherSystemInfo.JUMEI_NUM_IID, grpObj.getPlatformMallId()) : "";
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
     * updateTags
     *
     * @param channelId
     * @param prodId
     * @param Tags
     * @param modifier
     */
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
                String sku;
                if (ChannelConfigEnums.Channel.SN.getId().equals(channelId)) {
                    sku = logicInventory.getSku().toLowerCase();
                } else {
                    sku = logicInventory.getSku();
                }
                Integer logicQty = logicInventory.getQtyChina();
                skuLogicQty.merge(sku, logicQty, (val, newVal) -> val + newVal);
            }
        }

        return skuLogicQty;
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
        if (common.getFields().getHsCodeStatus().equalsIgnoreCase("1")) {
            if (!common.getFields().getHsCodePrivate().equalsIgnoreCase(oldProduct.getCommonNotNull().getFields().getHsCodePrivate())) {
                common.getFields().setHsCodeSetTime(DateTimeUtil.getNowTimeStamp());
                common.getFields().setHsCodeSetter(modifier);
            }
        }
        common.getFields().setCategoryStatus(StringUtil.isEmpty(common.getCatId()) ? "0" : "1");
        if (common.getFields().getCategoryStatus().equalsIgnoreCase("1")) {
            if (!common.getCatId().equalsIgnoreCase(oldProduct.getCommonNotNull().getCatId())) {
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


        Map<String, Object> result = new HashMap<>();
        result.put("modified", common.getModified());
        result.put("translateStatus", common.getFields().getTranslateStatus());
        result.put("hsCodeStatus", common.getFields().getHsCodeStatus());
        return result;
    }

    /**
     * 产品级lock同时更新各个平台的lock属性  added by piao
     *
     * @param channelId
     * @param prodId
     * @param lock
     * @param modifier
     */
    public void updateProductLock(String channelId, Long prodId, String lock, String modifier) {
        HashMap<String, Object> queryMap = new HashMap<>();
        queryMap.put("prodId", prodId);
        List<BulkUpdateModel> bulkList = new ArrayList<>();
        HashMap<String, Object> updateMap = new HashMap<>();
        updateMap.put("lock", lock);

        CmsBtProductModel _productInfo = getProductById(channelId, prodId);

        if (_productInfo != null) {
            // 处理各平台
            _productInfo.getPlatforms().forEach((s, platform) -> {
                Integer cartId = platform.getCartId();

                if (cartId < CmsConstants.ACTIVE_CARTID_MIN)
                    return;

                //插入商品修改历史
                productStatusHistoryService.insert(channelId,
                        _productInfo.getCommon().getFields().getCode(),
                        platform.getStatus(),
                        platform.getCartId(), EnumProductOperationType.UpdateCommonLock,
                        String.format("%s,触发%s操作", CartEnums.Cart.getValueByID(platform.getCartId().toString()), lock.equals("1") ? "锁" : "解锁"),
                        modifier);

                updateMap.put(String.format("platforms.P%s.lock", cartId), lock);

            });
        }

        updateMap.put("modifier", modifier);
        updateMap.put("modified", DateTimeUtil.getNowTimeStamp());

        BulkUpdateModel model = new BulkUpdateModel();
        model.setUpdateMap(updateMap);
        model.setQueryMap(queryMap);
        bulkList.add(model);
        cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, null, "$set");

    }

    /**
     * 批量lock产品
     *
     * @return 错误信息集合
     */
    public List<CmsBtOperationLogModel_Msg> batchLockProducts(Map<String, Object> messageMap) {

        List<CmsBtOperationLogModel_Msg> errorList = new ArrayList<>();
        String channelId = (String) messageMap.get("channelId"),
                creator = (String) messageMap.get("creator"),
                lock = (String)messageMap.get("lock");
        Integer cartId = (Integer) messageMap.get("cartId");

        Collection<String> codeList = (Collection) messageMap.get("codeList");

        // 按产品的Code检索出所有的商品
        JongoQuery queryObj = new JongoQuery();
        queryObj.setQuery("{\"common.fields.code\": {$in: #}}");
        queryObj.setParameters(codeList);
        List<CmsBtProductModel> products = getList(channelId, queryObj);

        if (CollectionUtils.isNotEmpty(products)) {
            codeList.clear();

            //批处理
            BulkJongoUpdateList productBulkList = new BulkJongoUpdateList(1000, cmsBtProductDao, channelId);

            String productCode;

            for (CmsBtProductModel product : products) {

                productCode = product.getCommon().getFields().getCode();

                StringBuffer updateStr = new StringBuffer("{$set:{")
                        .append("'platforms.P").append(cartId).append(".lock':").append("'"+lock+"'");

                updateStr.append(",'modified':#")
                        .append(",'modifier':#")
                        .append("}}");

                JongoUpdate updateObj = new JongoUpdate();
                updateObj.setQuery("{'common.fields.code':#}");
                updateObj.setQueryParameters(productCode);
                updateObj.setUpdate(updateStr.toString());
                updateObj.setUpdateParameters(DateTimeUtil.getNowTimeStamp(), creator);

                /**插入商品修改历史 added by piao*/
                CmsBtProductModel_Platform_Cart cartPlatform = product.getPlatform(cartId);
                productStatusHistoryService.insert(channelId,
                        product.getCommon().getFields().getCode(),
                        cartPlatform.getStatus(),
                        cartPlatform.getCartId(), EnumProductOperationType.BatchUpdatePlatformLock,
                        String.format("%s,触发%s操作", CartEnums.Cart.getValueByID(cartId.toString()), lock.equals("1") ? "锁" : "解锁"),
                        creator);

                // 添加批处理执行语句
                productBulkList.addBulkJongo(updateObj);

            }

            // 执行批处理
            BulkWriteResult rs = productBulkList.execute();

            if (rs != null) {
                $debug(String.format("批量lock平台 channelId=%s 执行结果=%s", channelId, rs.toString()));
            }
        } else {
            CmsBtOperationLogModel_Msg errorInfo = new CmsBtOperationLogModel_Msg();
            errorInfo.setSkuCode("All");
            errorInfo.setMsg("该组商品在产品表中检索不到任何一个商品");
            errorList.add(errorInfo);
        }

        return errorList;

    }

    public void updateProductAppSwitch(String channelId, Long prodId, String appSwitch, String modifier) {
        HashMap<String, Object> queryMap = new HashMap<>();
        queryMap.put("prodId", prodId);
        List<BulkUpdateModel> bulkList = new ArrayList<>();
        HashMap<String, Object> updateMap = new HashMap<>();
        updateMap.put("common.fields.appSwitch", appSwitch);
        updateMap.put("common.fields.modifier", modifier);
        updateMap.put("common.fields.modified", DateTimeUtil.getNowTimeStamp());
        BulkUpdateModel model = new BulkUpdateModel();
        model.setUpdateMap(updateMap);
        model.setQueryMap(queryMap);
        bulkList.add(model);
        cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, null, "$set");
    }

    public void updateProductTranslateStatus(String channelId, Long prodId, String translateStatus, String modifier) {
        HashMap<String, Object> queryMap = new HashMap<>();
        queryMap.put("prodId", prodId);
        List<BulkUpdateModel> bulkList = new ArrayList<>();
        HashMap<String, Object> updateMap = new HashMap<>();
        updateMap.put("common.fields.translateStatus", translateStatus);
        if ("1".equals(translateStatus)) {
            updateMap.put("common.fields.translateTime", modifier);
            updateMap.put("common.fields.translator", DateTimeUtil.getNowTimeStamp());
        } else {
            updateMap.put("common.fields.translateTime", "");
            updateMap.put("common.fields.translator", "");
        }

        BulkUpdateModel model = new BulkUpdateModel();
        model.setUpdateMap(updateMap);
        model.setQueryMap(queryMap);
        bulkList.add(model);
        cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, null, "$set");
    }

    /**
     * 用于更新 feed.subCategories
     *
     * @param channelId channelId
     * @param requests  key 为 code, value 为 subCategory的列表
     */
    public void updateProductFeedSubCategory(String channelId,
                                             Map<String, Collection<String>> requests,
                                             String modifier) {
        List<BulkUpdateModel> bulkList = requests.entrySet().stream()
                .map(entry -> {
                    String code = entry.getKey();
                    Collection<String> subCategoryPaths = entry.getValue();
                    HashMap<String, Object> queryMap = new HashMap<>();
                    queryMap.put("channelId", channelId);
                    queryMap.put("common.fields.code", code);
                    HashMap<String, Object> updateMap = new HashMap<>();
                    updateMap.put("feed.subCategories", entry.getValue());
                    BulkUpdateModel bulkUpdateModel = new BulkUpdateModel();
                    bulkUpdateModel.setUpdateMap(updateMap);
                    bulkUpdateModel.setQueryMap(queryMap);
                    return bulkUpdateModel;
                })
                .collect(Collectors.toList());

        List<List<BulkUpdateModel>> partedBulkList = Lists.partition(bulkList, 100);
        partedBulkList.parallelStream().forEach(subBulkList -> {
            Date start = new Date();
            cmsBtProductDao.bulkUpdateWithMap(channelId, subBulkList, modifier, "$set");
            $debug("更新了 " + subBulkList.size() + " 个 product 的subCategories 共耗时 " +
                    (new Date().getTime() - start.getTime()) + " 毫秒");
        });
    }

    public BulkWriteResult updateForCheckRight(String channelId, CmsBtProductModel cmsProduct, String modifier) {
        HashMap<String, Object> queryMap = new HashMap<>();
        queryMap.put("prodId", cmsProduct.getProdId());
        List<BulkUpdateModel> bulkList = new ArrayList<>();
        HashMap<String, Object> updateMap = new HashMap<>();

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

        updateMap.put("modifier", modifier);
        updateMap.put("modified", DateTimeUtil.getNowTimeStamp());
        BulkUpdateModel model = new BulkUpdateModel();
        model.setUpdateMap(updateMap);
        model.setQueryMap(queryMap);
        bulkList.add(model);

        BulkWriteResult result = cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, null, "$set");
        return result;
    }

    public int updateProductFeedToMaster(String channelId, CmsBtProductModel cmsProduct, String modifier, String comment) {
        HashMap<String, Object> queryMap = new HashMap<>();
        queryMap.put("prodId", cmsProduct.getProdId());
//        queryMap.put("modified", cmsProduct.getModified());
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

            updateMap.put("feed.brand", feed.getBrand());

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

        // 记录价格变更履历
        addPriceUpdateHistory(cmsProduct, modifier, comment);

        return result.getModifiedCount();
    }

    public WriteResult updateMulti(JongoUpdate updObj, String channelId) {
        return cmsBtProductDao.updateMulti(updObj, channelId);
    }

    public long countByQuery(final String strQuery, final Object[] parameters, String channelId) {
        return cmsBtProductDao.countByQuery(strQuery, parameters, channelId);
    }

    public List<Map<String, Object>> aggregateToMap(String channelId, List<JongoAggregate> aggregateList) {
        return cmsBtProductDao.aggregateToMap(channelId, aggregateList);
    }


    /**
     * 根据model返回产品列表
     *
     * @param channelId
     * @param model
     * @return
     */
    public List<CmsBtProductModel> getProductListByModel(String channelId, String model, String orgChannelId) {
        JongoQuery query = new JongoQuery();
        query.setQuery("{\"channelId\": #, \"orgChannelId\": #, \"common.fields.model\": #}");
        query.setParameters(channelId, orgChannelId, model);
        return getList(channelId, query);
    }

    public List<CustomPropBean> getCustomProp(CmsBtProductModel product) {
        List<CustomPropBean> props = new ArrayList<>();
        cmsBtCustomPropService.setProductCustomProp(product);

        Map<String, String> attsName = product.getFeed().getAttsName();

        product.getFeed().getOrgAtts().forEach((s, o) -> {
            CustomPropBean customPropBean = new CustomPropBean();
            customPropBean.setFeedAttrEn(s);
            customPropBean.setFeedAttrCn(attsName.get(s));
            customPropBean.setFeedAttrValueEn((String) o);
            customPropBean.setFeedAttrValueCn((String) product.getFeed().getCnAtts().get(s));
            customPropBean.setFeedAttr(true);
            customPropBean.setCustomPropActive(product.getFeed().getCustomIds().contains(s));
            props.add(customPropBean);
        });
        props.sort((o1, o2) -> {
            Integer b1 = product.getFeed().getCustomIds().indexOf(o1.getFeedAttrEn()) == -1 ? Integer.MAX_VALUE : product.getFeed().getCustomIds().indexOf(o1.getFeedAttrEn());
            Integer b2 = product.getFeed().getCustomIds().indexOf(o2.getFeedAttrEn()) == -1 ? Integer.MAX_VALUE : product.getFeed().getCustomIds().indexOf(o2.getFeedAttrEn());
            return b1.compareTo(b2);
        });

        return props;
    }

    public String updateProductAtts(String channelId, Long prodId, List<CustomPropBean> cnProps,Map<String, Boolean> productCustomIsDisp, String modifier) {

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
            rsMap.put("feed.productCustomIsDisp", productCustomIsDisp);
        }

        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("$set", rsMap);

        cmsBtProductDao.update(channelId, queryMap, updateMap);
        // 更新workLoad表
        sxProductService.insertSxWorkLoad(getProductById(channelId, prodId), modifier);
//        insertProductHistory(channelId, prodId);
        return modified;
    }

    public void addPriceUpdateHistory(CmsBtProductModel cmsProduct, String modifier, String comment) {

        cmsBtPriceLogService.addLogAndCallSyncPriceJob(cmsProduct.getChannelId(), cmsProduct, comment, modifier);
//        // 记录商品价格表动履历，并向Mq发送消息同步sku,code,group价格范围
//        if (cmsProduct != null && cmsProduct.getPlatforms() != null && cmsProduct.getPlatforms().size() > 0) {
//            cmsProduct.getPlatforms().forEach((cartId, platform) -> {
//                if (ListUtils.notNull(platform.getSkus())) {
//                    List<String> skuCodeList = new ArrayList<>();
//                    platform.getSkus().forEach(sku -> skuCodeList.add(sku.getStringAttribute("skuCode")));
//                    // 记录商品价格变动履历
//                    cmsBtPriceLogService.addLogForSkuListAndCallSyncPriceJob(skuCodeList, cmsProduct.getChannelId(),
//                            platform.getCartId(), modifier, comment);
//                }
//            });
//        }
    }

    public void updateProductForMove(String channelId, CmsBtProductModel productMode, String modifier) {
        //更新mongo数据
        HashMap<String, Object> queryMap = new HashMap<>();
        queryMap.put("common.fields.code", productMode.getCommon().getFields().getCode());

        List<BulkUpdateModel> bulkList = new ArrayList<>();
        HashMap<String, Object> updateMap = new HashMap<>();
//        updateMap.put("common.fields.priceMsrpSt", productMode.getCommon().getFields().getPriceMsrpSt());
//        updateMap.put("common.fields.priceMsrpEd", productMode.getCommon().getFields().getPriceMsrpEd());
//        updateMap.put("common.fields.priceRetailSt", productMode.getCommon().getFields().getPriceRetailSt());
//        updateMap.put("common.fields.priceRetailEd", productMode.getCommon().getFields().getPriceRetailEd());
//        updateMap.put("common.modifier", productMode.getCommon().getModifier());
//        updateMap.put("common.modified", productMode.getCommon().getModified());
//        updateMap.put("common.skus", productMode.getCommon().getSkus());
        updateMap.put("common", productMode.getCommon());
        updateMap.put("platforms", productMode.getPlatforms());
        updateMap.put("sales", productMode.getSales());
        BulkUpdateModel model = new BulkUpdateModel();
        model.setUpdateMap(updateMap);
        model.setQueryMap(queryMap);
        bulkList.add(model);
        cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, modifier, "$set");
    }

    /**
     * 计算group的价格区间
     */
    public void calculatePriceRange(CmsBtProductModel productModel) {
        // Common.fields下的价格区间
        Double commonPriceRetailSt = null;
        Double commonPriceRetailEd = null;
        Double commonPriceMsrpSt = null;
        Double commonPriceMsrpEd = null;
        for (CmsBtProductModel_Sku skuModel : productModel.getCommon().getSkus()) {
            Double skuPriceRetail = skuModel.getPriceRetail();
            if (commonPriceRetailSt == null || (skuPriceRetail != null && skuPriceRetail < commonPriceRetailSt)) {
                commonPriceRetailSt = skuPriceRetail;
            }
            if (commonPriceRetailEd == null || (skuPriceRetail != null && skuPriceRetail > commonPriceRetailEd)) {
                commonPriceRetailEd = skuPriceRetail;
            }

            Double skuPriceMsrp = skuModel.getPriceMsrp();
            if (commonPriceMsrpSt == null || (skuPriceMsrp != null && skuPriceMsrp < commonPriceMsrpSt)) {
                commonPriceMsrpSt = skuPriceMsrp;
            }
            if (commonPriceMsrpEd == null || (skuPriceMsrp != null && skuPriceMsrp > commonPriceMsrpEd)) {
                commonPriceMsrpEd = skuPriceMsrp;
            }
        }
        productModel.getCommon().getFields().setPriceRetailSt(commonPriceRetailSt);
        productModel.getCommon().getFields().setPriceRetailEd(commonPriceRetailEd);
        productModel.getCommon().getFields().setPriceMsrpSt(commonPriceMsrpSt);
        productModel.getCommon().getFields().setPriceMsrpEd(commonPriceMsrpEd);

        // Platforms下的价格区间
        for (Map.Entry<String, CmsBtProductModel_Platform_Cart> platform : productModel.getPlatforms().entrySet()) {
            // 跳过P0（主数据）
            if (platform.getValue().getCartId().equals(0)) {
                continue;
            }
            Double priceSaleSt = null;
            Double priceSaleEd = null;
            Double priceRetailSt = null;
            Double priceRetailEd = null;
            Double priceMsrpSt = null;
            Double priceMsrpEd = null;
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
            platform.getValue().setpPriceSaleSt(priceSaleSt);
            platform.getValue().setpPriceSaleEd(priceSaleEd);
            platform.getValue().setpPriceRetailSt(priceRetailSt);
            platform.getValue().setpPriceRetailEd(priceRetailEd);
            platform.getValue().setpPriceMsrpSt(priceMsrpSt);
            platform.getValue().setpPriceMsrpEd(priceMsrpEd);
        }
    }

    public void removeTagByCodes(String channelId, List<String> codes, int tagId) {
        cmsBtProductDao.removeTagByCodes(channelId, codes, tagId);
    }

    //更新mongo product  tag
    public void updateCmsBtProductTags(String channelId, CmsBtProductModel productModel, int refTagId, List<TagTreeNode> tagList, String modifier) {
        int cnt = 0;
        if (tagList == null || tagList.size() == 0) return;
        //更新商品Tags  sunpt
        if (productModel != null) {
            List<String> tags = productModel.getTags();
            tagList.forEach(tagInfo -> {
                if (tagInfo.getChecked() == 0) {
                    //删除
                    tags.remove(String.format("-%s-%s-", refTagId, tagInfo.getId()));
                } else if (tagInfo.getChecked() == 2) {

                    //添加
                    String tag = String.format("-%s-%s-", refTagId, tagInfo.getId());
                    if (!tags.contains(tag)) {
                        tags.add(String.format("-%s-%s-", refTagId, tagInfo.getId()));
                        tags.add(String.format("-%s-", refTagId));
                    }

                }
            });
            //去掉重复项目
            List<String> newTag = tags.stream().distinct().collect(Collectors.toList());
            //如果只存在单个父级
            for (String tag : newTag)
                if (tag.split("-")[1].equals(String.valueOf(refTagId))) {
                    cnt++;
                }
            if (cnt == 1) {
                newTag.remove(String.format("-%s-", refTagId));
            }
            productModel.setTags(newTag);
            //3.更新
            updateTags(channelId, productModel.getProdId(), newTag, modifier);
        }
    }

    // 删除mongo product  tag
    public void deleteCmsBtProductTags(String channelId, String code, int refTagId, List<TagTreeNode> tagList, String modifier) {
        CmsBtProductModel productModel = this.getProductByCode(channelId, code);
        List<String> tags = null;
        if (productModel == null || CollectionUtils.isEmpty(tags = productModel.getTags()) || CollectionUtils.isEmpty(tagList)) {
            return;
        }

        for (TagTreeNode tagNode : tagList) {
            String tag = String.format("-%s-%s-", refTagId, tagNode.getId());
            if (tags.contains(tag)) {
                tags.remove(String.format("-%s-%s-", refTagId, tagNode.getId()));
            }
        }

        int cnt = 0;
        //去掉重复项目
        List<String> newTag = tags.stream().distinct().collect(Collectors.toList());
        //如果只存在单个父级
        for (String tag : newTag)
            if (tag.split("-")[1].equals(String.valueOf(refTagId))) {
                cnt++;
            }
        if (cnt == 1) {
            newTag.remove(String.format("-%s-", refTagId));
        }
        //3.更新
        updateTags(channelId, productModel.getProdId(), newTag, modifier);
    }

    public Long getProductIdByCode(String code, String channelId) {
        Criteria criteria = new Criteria("common.fields.code").is(code);
        JongoQuery queryObj = new JongoQuery();
        queryObj.setQuery(criteria);
        queryObj.setProjectionExt("");
        CmsBtProductModel prodObj = this.getProductByCondition(channelId, queryObj);
        prodObj.getCommon().getFields().getCode();
        return prodObj.getProdId();
    }

    public BulkWriteResult bulkUpdateWithMap(String channelId, List<BulkUpdateModel> bulkList, String modifier, String key) {
        return cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, modifier, key);
    }

    /**
     * 重置product和group的platformPid
     *
     * @param channelId 渠道Id
     * @param cartId    平台Id
     * @param code      产品Code
     * @return WriteResult
     */
    public WriteResult resetProductAndGroupPlatformPid(String channelId, Integer cartId, String code) {

        JongoUpdate query = new JongoUpdate();
        query.setQuery("{\"common.fields.code\": #}");
        query.setQueryParameters(code);

        query.setUpdate("{$set: {\"platforms.P" + cartId + ".pProductId\": \"\"}}");

        WriteResult rs = cmsBtProductDao.updateMulti(query, channelId);

        if (rs != null) {

            JongoUpdate groupQuery = new JongoUpdate();
            groupQuery.setQuery("{\"cartId\": #, \"productCodes\": #}");
            groupQuery.setQueryParameters(cartId, code);

            groupQuery.setUpdate("{$set: {\"platformPid\": \"\"}}");

            rs = cmsBtProductGroupDao.updateMulti(groupQuery, channelId);
        }

        return rs;
    }

    /**
     * 判断两税号是否一样
     *
     * @param hsCode1
     * @param hsCode2
     * @return
     */
    public Boolean compareHsCode(String hsCode1, String hsCode2) {
        String hs1 = "";
        String hs2 = "";
        if (hsCode1 != null) {
            String[] temp = hsCode1.split(",");
            if (temp.length > 1) hs1 = temp[0];
        }

        if (hsCode2 != null) {
            String[] temp = hsCode2.split(",");
            if (temp.length > 1) hs2 = temp[0];
        }
        return hs1.equalsIgnoreCase(hs2);
    }


    public BulkJongoUpdateList updateCmsBtProductInfo(String channelId) {
        BulkJongoUpdateList bulkList = new BulkJongoUpdateList(10, cmsBtProductDao, channelId);
        return bulkList;
    }

    public BulkJongoUpdateList updateCmsBtProductGroupInfo(String channelId) {
        BulkJongoUpdateList bulkList = new BulkJongoUpdateList(10, cmsBtProductGroupDao, channelId);
        return bulkList;
    }

    /**
     * 取得商品对应的平台状态
     */
    public String getPlatformStatus(String prodCode, String channelId, int cartId) {
        JongoQuery queryObj = new JongoQuery();
        queryObj.setQuery("{'common.fields.code':#}");
        queryObj.setParameters(prodCode);
        queryObj.setProjectionExt("platforms.P" + cartId + ".pStatus");
        CmsBtProductModel prodObj = cmsBtProductDao.selectOneWithQuery(queryObj, channelId);
        if (prodObj == null) {
            $warn("CmsPlatformActiveLogService 找不到商品 channelId=%s，code=%s", channelId, prodCode);
            return null;
        }
        CmsConstants.PlatformStatus pStatus = prodObj.getPlatformNotNull(cartId).getpStatus();
        if (pStatus != null) {
            return pStatus.name();
        }
        return null;
    }
}