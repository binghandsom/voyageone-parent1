package com.voyageone.web2.cms.wsdl.service;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.BulkWriteResult;
import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import com.voyageone.web2.cms.wsdl.BaseService;
import com.voyageone.service.dao.cms.CmsBtPriceLogDao;
import com.voyageone.web2.sdk.api.VoApiConstants;
import com.voyageone.service.model.cms.CmsBtPriceLogModel;
import com.voyageone.service.bean.cms.ProductPriceModel;
import com.voyageone.service.bean.cms.ProductSkuPriceModel;
import com.voyageone.web2.sdk.api.exception.ApiException;
import com.voyageone.web2.sdk.api.request.*;
import com.voyageone.web2.sdk.api.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 *  Product Group Service
 *
 * @author chuanyu.liang 15/12/9
 * @version 2.0.1
 * @since. 2.0.0
 */
@Service
public class ProductSkuService extends BaseService {

    @Autowired
    private ProductService productService;

    @Autowired
    private CmsBtProductDao cmsBtProductDao;

    @Autowired
    private CmsBtPriceLogDao cmsBtPriceLogDao;

    /**
     * select List
     * @param request ProductSkusGetRequest
     * @return ProductSkusGetResponse
     */
    public ProductSkusGetResponse selectList(ProductSkusGetRequest request) {
        ProductSkusGetResponse result = new ProductSkusGetResponse();
        List<CmsBtProductModel_Sku> productSkus = new ArrayList<>();

        ProductsGetRequest productsRequest = createProductsRequest(request);
        ProductsGetResponse productsResponse = productService.selectList(productsRequest);

        if (productsResponse != null && productsResponse.getProducts() != null) {
            for (CmsBtProductModel productModel : productsResponse.getProducts()) {
                if (productModel != null && productModel.getSkus() != null && productModel.getSkus().size()>0) {
                    productSkus.addAll(productModel.getSkus());
                }
            }
        }
        result.setProductSkus(productSkus);
        result.setTotalCount((long) productSkus.size());

        return result;
    }

    /**
     * ProductSkusGetRequest - > ProductsGetRequest
     * @param inputRequest ProductSkusGetRequest
     * @return ProductsGetRequest
     */
    private ProductsGetRequest createProductsRequest(ProductSkusGetRequest inputRequest) {
        if (inputRequest == null) {
            return null;
        }
        ProductsGetRequest result = new ProductsGetRequest();
        result.setFields(inputRequest.getFields());
        result.addField("skus");


        result.setSorts(inputRequest.getSorts());
        result.setPageNo(inputRequest.getPageNo());
        result.setPageSize(inputRequest.getPageSize());

        result.setChannelId(inputRequest.getChannelId());
        result.setProductIds(inputRequest.getProductIds());
        result.setProductCodes(inputRequest.getProductCodes());

        return result;
    }

    /**
     * save Skus
     * @param request ProductSkusPutRequest
     * @return ProductSkusPutResponse
     */
    public ProductSkusPutResponse saveSkus(ProductSkusPutRequest request) {
        ProductSkusPutResponse result = new ProductSkusPutResponse();
        checkCommRequest(request);
        //ChannelId
        String channelId = request.getChannelId();
        checkRequestChannelId(channelId);

        List<BulkUpdateModel> bulkInsertList = new ArrayList<>();
        List<BulkUpdateModel> bulkUpdateList = new ArrayList<>();

        Long productId = request.getProductId();
        String productCode = request.getProductCode();
        List<CmsBtProductModel_Sku> skus = request.getSkus();
        if (productId != null) {
            saveSkusAddBlukUpdateModel(channelId, productId, null, skus, bulkInsertList, bulkUpdateList);
        } else if (!StringUtils.isEmpty(productCode)) {
            saveSkusAddBlukUpdateModel(channelId, null, productCode, skus, bulkInsertList, bulkUpdateList);
        } else {
            VoApiConstants.VoApiErrorCodeEnum codeEnum = VoApiConstants.VoApiErrorCodeEnum.ERROR_CODE_70007;
            throw new ApiException(codeEnum.getErrorCode(), "productIdSkuMap or productCodeSkuMap not found!");
        }

        if (bulkInsertList.size() > 0) {
            BasicDBObject queryObj = (BasicDBObject)bulkInsertList.get(0).getQueryMap();
            BasicDBList skusList = new BasicDBList();
            for (BulkUpdateModel bulkInsert : bulkInsertList) {
                skusList.add(bulkInsert.getUpdateMap());
            }
            BasicDBObject skusObj = new BasicDBObject().append("skus", skusList);
            BasicDBObject pushObj = new BasicDBObject().append("$pushAll", skusObj);
            WriteResult writeResult = cmsBtProductDao.getDBCollection(channelId).update(queryObj, pushObj);
            result.setInsertedCount(writeResult.getN());
        }

        if (bulkUpdateList.size() > 0) {
            BulkWriteResult bulkWriteResult = cmsBtProductDao.bulkUpdateWithMap(channelId, bulkUpdateList, null, "$set");
            setResultCount(result, bulkWriteResult);
        }

        return result;
    }

    /**
     * saveSkusAddBlukUpdateModel
     * @param channelId channel ID
     * @param productId product Id
     * @param productCode product Code
     * @param models CmsBtProductModel_Sku
     * @param bulkUpdateList List<BulkUpdateModel>
     * @param bulkInsertList List<BulkUpdateModel>
     */
    private void saveSkusAddBlukUpdateModel(String channelId, Long productId, String productCode, List<CmsBtProductModel_Sku> models, List<BulkUpdateModel> bulkInsertList, List<BulkUpdateModel> bulkUpdateList) {
        VoApiConstants.VoApiErrorCodeEnum codeEnum = VoApiConstants.VoApiErrorCodeEnum.ERROR_CODE_70007;
        if (models == null || models.size() == 0) {
            return;
        }

        if (productId == null && StringUtils.isEmpty(productCode)) {
            throw new ApiException(codeEnum.getErrorCode(), "productId or productCode not found!");
        }

        CmsBtProductModel findModel = null;
        JomgoQuery queryObject = new JomgoQuery();
        queryObject.setProjection("skus.skuCode");
        if (productId != null) {
            queryObject.setQuery("{\"prodId\":" + productId + "}");
            findModel = cmsBtProductDao.selectOneWithQuery(queryObject, channelId);
        } else if (StringUtils.isEmpty(productCode)) {
            queryObject.setQuery("{\"fields.code\":\"" + productCode + "\"}");
            findModel = cmsBtProductDao.selectOneWithQuery(queryObject, channelId);
        }

        if (findModel != null) {
            Set<String> findSkuSet = new HashSet<>();
            if (findModel.getSkus() != null) {
                for (CmsBtProductModel_Sku findSku : findModel.getSkus()) {
                    if (findSku != null && findSku.getSkuCode() != null) {
                        findSkuSet.add(findSku.getSkuCode());
                    }
                }
            }

            for (CmsBtProductModel_Sku model : models) {
                if (model == null) {
                    continue;
                }
                BasicDBObject queryMap = new BasicDBObject();
                if (productId != null) {
                    queryMap.append("prodId", productId);
                } else {
                    queryMap.append("fields.code", productCode);
                }
                if (StringUtils.isEmpty(model.getSkuCode())) {
                    throw new ApiException(codeEnum.getErrorCode(), "SkuCode not found!");
                }
                if (findSkuSet.contains(model.getSkuCode())) {
                    queryMap.put("skus.skuCode", model.getSkuCode());

                    BasicDBObject dbObject = model.toUpdateBasicDBObject("skus.$.");

                    if (dbObject.size() > 0) {
                        BulkUpdateModel skuUpdateModel = new BulkUpdateModel();
                        skuUpdateModel.setUpdateMap(dbObject);
                        skuUpdateModel.setQueryMap(queryMap);

                        bulkUpdateList.add(skuUpdateModel);
                    }
                } else {
                    //BasicDBObject skuCodeObject = new BasicDBObject();
                    //skuCodeObject.append("$ne", model.getSkuCode());
                    //queryMap.put("skus.skuCode", skuCodeObject);

                    BasicDBObject dbObject = model.toUpdateBasicDBObject("");
                    if (dbObject.size() > 0) {
                        BulkUpdateModel skuUpdateModel = new BulkUpdateModel();
                        skuUpdateModel.setUpdateMap(dbObject);
                        skuUpdateModel.setQueryMap(queryMap);
                        bulkInsertList.add(skuUpdateModel);
                    }
                }
            }
        }
    }

    /**
     * update Prices
     * @param request ProductSkusGetRequest
     * @return ProductSkusGetResponse
     */
    public ProductUpdatePriceResponse updatePrices(ProductUpdatePriceRequest request) {
        ProductUpdatePriceResponse result = new ProductUpdatePriceResponse();

        checkCommRequest(request);
        //ChannelId
        String channelId = request.getChannelId();
        checkRequestChannelId(channelId);

        String modifier = request.getModifier();

        //request.getProductPrices()
        if (request.getProductPrices() == null || request.getProductPrices().size() == 0) {
            VoApiConstants.VoApiErrorCodeEnum codeEnum = VoApiConstants.VoApiErrorCodeEnum.ERROR_CODE_70007;
            throw new ApiException(codeEnum.getErrorCode(), "ProductPrices not found!");
        }

        List<BulkUpdateModel> bulkList = new ArrayList<>();
        List<CmsBtPriceLogModel> logList = new ArrayList<>();

        for (ProductPriceModel model : request.getProductPrices()) {
            VoApiConstants.VoApiErrorCodeEnum codeEnum = VoApiConstants.VoApiErrorCodeEnum.ERROR_CODE_70007;
            if (model == null) {
                throw new ApiException(codeEnum.getErrorCode(), "ProductPrices not found!");
            }
            if (model.getProductId() == null && model.getProductCode() == null) {
                throw new ApiException(codeEnum.getErrorCode(), "ProductPrices ProductId or ProductCode not found!");
            }
            updatePricesAddBlukUpdateModel(channelId, model, bulkList, logList, modifier);
        }

        // 插入log履历
        if (logList.size()>0) {
            cmsBtPriceLogDao.insertCmsBtPriceLogList(logList);
        }

        // 更新sku价格变更
        if (bulkList.size() > 0) {
            BulkWriteResult bulkWriteResult = cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, null, "$set");
            setResultCount(result, bulkWriteResult);
        }

        return result;
    }

    /**
     * 批量更新价格信息 根据CodeList
     */
    public void updatePricesAddBlukUpdateModel(String channelId, ProductPriceModel model,
                                               List<BulkUpdateModel> bulkList, List<CmsBtPriceLogModel> logList,
                                               String modifier) {
        VoApiConstants.VoApiErrorCodeEnum codeEnum = VoApiConstants.VoApiErrorCodeEnum.ERROR_CODE_70007;

        // 取得更新前的sku数据
        HashMap<String, Object> productQueryMap = new HashMap<>();
        CmsBtProductModel findModel;
        JomgoQuery queryObject = new JomgoQuery();
        queryObject.setProjection("prodId", "fields", "skus");
        if (model.getProductId() != null) {
            productQueryMap.put("prodId", model.getProductId());
            queryObject.setQuery("{\"prodId\":" + model.getProductId() + "}");
            findModel = cmsBtProductDao.selectOneWithQuery(queryObject, channelId);
        } else {
            productQueryMap.put("fields.code", model.getProductCode());
            queryObject.setQuery("{\"fields.code\":\"" + model.getProductCode() + "\"}");
            findModel = cmsBtProductDao.selectOneWithQuery(queryObject, channelId);
        }

        if (findModel == null || findModel.getSkus() == null) {
            return;
        }
        List<CmsBtProductModel_Sku> findSkuList = findModel.getSkus();

        //Sku price set
        List<Double> msrpPriceList = new ArrayList<>();
        List<Double> retailPriceList = new ArrayList<>();
        List<Double> salePriceList = new ArrayList<>();

        if (model.getSkuPrices() != null && model.getSkuPrices().size()>0) {

            // 循环原始sku列表
            for (CmsBtProductModel_Sku skuModelBefore : findSkuList) {
                boolean isFindSku = false;

                // 循环变更sku列表
                for (ProductSkuPriceModel skuModel : model.getSkuPrices()) {
                    if (skuModel.getSkuCode().equals(skuModelBefore.getSkuCode())) {
                        // 判断价格是否发生变化
                        if (isPriceChanged(skuModelBefore, skuModel)) {
                            CmsBtPriceLogModel cmsBtPriceLogModel = createPriceLogModel(channelId, findModel, skuModel, modifier);
                            logList.add(cmsBtPriceLogModel);

                            HashMap<String, Object> skuQueryMap = new HashMap<>();
                            if (model.getProductId() != null) {
                                skuQueryMap.put("prodId", model.getProductId());
                            } else {
                                skuQueryMap.put("fields.code", model.getProductCode());
                            }
                            if (StringUtils.isEmpty(skuModel.getSkuCode())) {
                                throw new ApiException(codeEnum.getErrorCode(), "SkuPrices.SkuCode not found!");
                            }
                            skuQueryMap.put("skus.skuCode", skuModel.getSkuCode());

                            HashMap<String, Object> updateMap = new HashMap<>();
                            if (skuModel.getPriceMsrp() != null) {
                                updateMap.put("skus.$.priceMsrp", skuModel.getPriceMsrp());
                                msrpPriceList.add(skuModel.getPriceMsrp());
                            }
                            if (skuModel.getPriceRetail() != null) {
                                updateMap.put("skus.$.priceRetail", skuModel.getPriceRetail());
                                retailPriceList.add(skuModel.getPriceRetail());
                            }
                            if (skuModel.getPriceSale() != null) {
                                updateMap.put("skus.$.priceSale", skuModel.getPriceSale());
                                salePriceList.add(skuModel.getPriceSale());
                            }

                            if (updateMap.size() > 0) {
                                BulkUpdateModel skuUpdateModel = new BulkUpdateModel();
                                skuUpdateModel.setUpdateMap(updateMap);
                                skuUpdateModel.setQueryMap(skuQueryMap);

                                bulkList.add(skuUpdateModel);
                            }
                        }
                        // 如果价格未发生变化,则设置isFindSku标示为true
                        else {
                            isFindSku = true;
                        }
                        break;
                    }
                }

                // 如果价格没有没有发生变化则插入当前未变化sku的原始价格
                if (isFindSku) {
                    if (skuModelBefore.getPriceMsrp() != null) {
                        msrpPriceList.add(skuModelBefore.getPriceMsrp());
                    }
                    if (skuModelBefore.getPriceRetail() != null) {
                        retailPriceList.add(skuModelBefore.getPriceRetail());
                    }
                    if (skuModelBefore.getPriceSale() != null) {
                        salePriceList.add(skuModelBefore.getPriceSale());
                    }
                }
            }
        }

        //get price price
        Map<String, Object> resultField = getNewPriceWithFields(findModel.getFields(), msrpPriceList, retailPriceList, salePriceList);

        HashMap<String, Object> productUpdateMap = new HashMap<>();
        if (model.getPriceChange() != null) {
            productUpdateMap.put("fields.priceChange", model.getPriceChange());
        }

        //update product price
//        if ((boolean)resultField.get("isChanged")) {
        if (bulkList.size() > 0) {

            if (resultField.get("priceMsrpSt") != null) {
                productUpdateMap.put("fields.priceMsrpSt", resultField.get("priceMsrpSt"));
            }
            if (resultField.get("priceMsrpEd") != null) {
                productUpdateMap.put("fields.priceMsrpEd", resultField.get("priceMsrpEd"));
            }
            if (resultField.get("priceRetailSt") != null) {
                productUpdateMap.put("fields.priceRetailSt", resultField.get("priceRetailSt"));
            }
            if (resultField.get("priceRetailEd") != null) {
                productUpdateMap.put("fields.priceRetailEd", resultField.get("priceRetailEd"));
            }
            if (resultField.get("priceSaleSt") != null) {
                productUpdateMap.put("fields.priceSaleSt", resultField.get("priceSaleSt"));
            }
            if (resultField.get("priceSaleEd") != null) {
                productUpdateMap.put("fields.priceSaleEd", resultField.get("priceSaleEd"));
            }

            CmsBtPriceLogModel cmsBtPriceLogModel = new CmsBtPriceLogModel();
            cmsBtPriceLogModel.setChannelId(channelId);
            cmsBtPriceLogModel.setProductId(findModel.getProdId().intValue());
            cmsBtPriceLogModel.setCode(findModel.getFields().getCode());
            cmsBtPriceLogModel.setSku("0");
            cmsBtPriceLogModel.setMsrpPrice(resultField.get("priceMsrpSt") + "-" + resultField.get("priceMsrpEd"));
            cmsBtPriceLogModel.setRetailPrice(resultField.get("priceRetailSt") + "-" + resultField.get("priceRetailEd"));
            cmsBtPriceLogModel.setSalePrice(resultField.get("priceSaleSt") + "-" + resultField.get("priceSaleEd"));
            cmsBtPriceLogModel.setComment("价格更新");
            cmsBtPriceLogModel.setCreater(modifier);

            logList.add(cmsBtPriceLogModel);
        }


        if (productUpdateMap.size() > 0) {
            BulkUpdateModel productUpdateModel = new BulkUpdateModel();
            productUpdateModel.setUpdateMap(productUpdateMap);
            productUpdateModel.setQueryMap(productQueryMap);

            bulkList.add(productUpdateModel);
        }
    }

    /**
     * 判断价格是否变更
     */
    private boolean isPriceChanged(CmsBtProductModel_Sku model1, ProductSkuPriceModel model2) {
        BigDecimal msrp1 = new BigDecimal(0);
        if (model1.getPriceMsrp() != null) {
            msrp1 = new BigDecimal(model1.getPriceMsrp());
        }
        BigDecimal retail1 = new BigDecimal(0);
        if (model1.getPriceRetail() != null) {
            retail1 = new BigDecimal(model1.getPriceRetail());
        }
        BigDecimal sale1 = new BigDecimal(0);
        if (model1.getPriceSale() != null) {
            sale1 = new BigDecimal(model1.getPriceSale());
        }

        BigDecimal msrp2 = new BigDecimal(0);
        if (model2.getPriceMsrp() != null) {
            msrp2 = new BigDecimal(model2.getPriceMsrp());
        }
        BigDecimal retail2 = new BigDecimal(0);
        if (model2.getPriceRetail() != null) {
            retail2 = new BigDecimal(model2.getPriceRetail());
        }
        BigDecimal sale2 = new BigDecimal(0);
        if (model2.getPriceSale() != null) {
            sale2 = new BigDecimal(model2.getPriceSale());
        }

        return !(msrp1.compareTo(msrp2) == 0 && retail1.compareTo(retail2) == 0 && sale1.compareTo(sale2) == 0);
    }

    /**
     * 创建 SKU PriceLog Model
     */
    private CmsBtPriceLogModel createPriceLogModel(String channelId, CmsBtProductModel productModel, ProductSkuPriceModel skuModel, String modifier) {
        CmsBtPriceLogModel cmsBtPriceLogModel = new CmsBtPriceLogModel();
        cmsBtPriceLogModel.setChannelId(channelId);
        cmsBtPriceLogModel.setProductId(productModel.getProdId().intValue());
        cmsBtPriceLogModel.setCode(productModel.getFields().getCode());
        cmsBtPriceLogModel.setSku(skuModel.getSkuCode());

        Double priceMsrp  = 0d;
        if (skuModel.getPriceMsrp() != null && !"null-null".equals(skuModel.getPriceMsrp())) {
            priceMsrp = skuModel.getPriceMsrp();
        }
        cmsBtPriceLogModel.setMsrpPrice(priceMsrp.toString());

        Double priceRetail  = 0d;
        if (skuModel.getPriceRetail() != null && !"null-null".equals(skuModel.getPriceRetail())) {
            priceRetail = skuModel.getPriceRetail();
        }
        cmsBtPriceLogModel.setRetailPrice(priceRetail.toString());

        Double priceSale  = 0d;
        if (skuModel.getPriceSale() != null && !"null-null".equals(skuModel.getPriceSale())) {
            priceSale = skuModel.getPriceSale();
        }
        cmsBtPriceLogModel.setSalePrice(priceSale.toString());
        cmsBtPriceLogModel.setComment("价格更新");
        cmsBtPriceLogModel.setCreater(modifier);
        return cmsBtPriceLogModel;
    }

    /**
     * 更新field价格
     */
    private Map<String, Object> getNewPriceWithFields(
            CmsBtProductModel_Field field,
            List<Double> msrpPriceList,
            List<Double> retailPriceList,
            List<Double> salePriceList) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> msrpMap = getNewPriceScope(msrpPriceList, field.getPriceMsrpSt(), field.getPriceMsrpEd());
        Map<String, Object> retailMap = getNewPriceScope(retailPriceList, field.getPriceRetailSt(), field.getPriceRetailEd());
        Map<String, Object> saleMap = getNewPriceScope(salePriceList, field.getPriceSaleSt(), field.getPriceSaleEd());
        if ((boolean) msrpMap.get("isChanged") || (boolean) retailMap.get("isChanged") || (boolean) saleMap.get("isChanged")) {
            result.put("priceMsrpSt", msrpMap.get("start"));
            result.put("priceMsrpEd", msrpMap.get("end"));
            result.put("priceRetailSt", retailMap.get("start"));
            result.put("priceRetailEd", retailMap.get("end"));
            result.put("priceSaleSt", saleMap.get("start"));
            result.put("priceSaleEd", saleMap.get("end"));
            result.put("isChanged", true);
        } else {
            result.put("isChanged", false);
        }
        result.put("field", field);
        return result;
    }

    /**
     * 获取价格范围
     */
    private Map<String, Object> getNewPriceScope(List<Double> priceList, Double priceStart, Double priceEnd) {
        Map<String, Object> result = new HashMap<>();
        Double start = priceStart;
        Double end = priceEnd;
        Boolean isChanged = false;
        if (priceList.size() > 0) {
            priceList.sort((o1, o2) -> o2.compareTo(o1));
            start = priceList.get(priceList.size() - 1);
            end = priceList.get(0);
            if (priceStart == null || priceEnd == null
                || start.compareTo(priceStart) != 0
                || end.compareTo(priceEnd) != 0) {
                isChanged = true;
            }
        }
        result.put("start", start);
        result.put("end", end);
        result.put("isChanged", isChanged);
        return result;
    }

    /**
     * sku deletes
     * @param request ProductSkusDeleteRequest
     * @return ProductSkusDeleteResponse
     */
    public ProductSkusDeleteResponse deletes(ProductSkusDeleteRequest request) {
        ProductSkusDeleteResponse result = new ProductSkusDeleteResponse();

        checkCommRequest(request);
        //ChannelId
        String channelId = request.getChannelId();
        checkRequestChannelId(channelId);

        //request.getProductPrices()
        if ((request.getProductIdSkuCodeListMap() == null || request.getProductIdSkuCodeListMap().size() == 0)
                && (request.getProductCodeSkuCodeListMap() == null || request.getProductCodeSkuCodeListMap().size() == 0)) {
            VoApiConstants.VoApiErrorCodeEnum codeEnum = VoApiConstants.VoApiErrorCodeEnum.ERROR_CODE_70007;
            throw new ApiException(codeEnum.getErrorCode(), "ProductIdSkuCodeListMap or ProductCodeSkuCodeListMap is not empty!");
        }

        List<BulkUpdateModel> bulkList = new ArrayList<>();
        if (request.getProductIdSkuCodeListMap() != null && request.getProductIdSkuCodeListMap().size() > 0) {
            for (Map.Entry<Long, Set<String>> product : request.getProductIdSkuCodeListMap().entrySet()) {
                addDeleteSkusBulk(channelId, product.getKey(), null, product.getValue(), bulkList);
            }
        } else {
            for (Map.Entry<String, Set<String>> product : request.getProductCodeSkuCodeListMap().entrySet()) {
                addDeleteSkusBulk(channelId, null, product.getKey(), product.getValue(), bulkList);
            }
        }

        if (bulkList.size() > 0) {
            BulkWriteResult bulkWriteResult = cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, null, "$pull");
            setResultCount(result, bulkWriteResult);
        }

        return result;
    }

    /**
     * addDeleteSkusBulk
     */
    public void addDeleteSkusBulk(String channelId, Long productId, String productCode, Set<String> skuCodes, List<BulkUpdateModel> bulkList) {
        if (skuCodes != null && skuCodes.size()>0) {
            CmsBtProductModel findModel = null;
            JomgoQuery queryObject = new JomgoQuery();
            queryObject.setProjection("skus");
            if (productId != null) {
                queryObject.setQuery("{\"prodId\":" + productId + "}");
                findModel = cmsBtProductDao.selectOneWithQuery(queryObject, channelId);
            } else if (StringUtils.isEmpty(productCode)) {
                queryObject.setQuery("{\"fields.code\":\"" + productCode + "\"}");
                findModel = cmsBtProductDao.selectOneWithQuery(queryObject, channelId);
            }

            if (findModel != null) {
                Map<String, CmsBtProductModel_Sku> findSkuMap = new HashMap<>();
                if (findModel.getSkus() != null) {
                    for (CmsBtProductModel_Sku findSku : findModel.getSkus()) {
                        if (findSku != null && findSku.getSkuCode() != null) {
                            findSkuMap.put(findSku.getSkuCode(), findSku);
                        }
                    }
                }

                for (String skuCode : skuCodes) {
                    if (!StringUtils.isEmpty(skuCode)) {
                        HashMap<String, Object> skuQueryMap = new HashMap<>();
                        if (productId != null) {
                            skuQueryMap.put("prodId", productId);
                        } else {
                            skuQueryMap.put("fields.code", productCode);
                        }
                        skuQueryMap.put("skus.skuCode", skuCode);

                        CmsBtProductModel_Sku findSku = findSkuMap.get(skuCode);
                        if (findSku != null) {
                            BasicDBObject skuObj = findSku.toUpdateBasicDBObject("");

                            HashMap<String, Object> updateMap = new HashMap<>();
                            updateMap.put("skus", skuObj);

                            BulkUpdateModel skuUpdateModel = new BulkUpdateModel();
                            skuUpdateModel.setUpdateMap(updateMap);
                            skuUpdateModel.setQueryMap(skuQueryMap);
                            bulkList.add(skuUpdateModel);
                        }
                    }
                }
            }
        }
    }

}
