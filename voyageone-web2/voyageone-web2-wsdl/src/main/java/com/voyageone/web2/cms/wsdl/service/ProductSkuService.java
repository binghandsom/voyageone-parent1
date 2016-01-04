package com.voyageone.web2.cms.wsdl.service;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.BulkWriteResult;
import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.cms.service.dao.mongodb.CmsBtProductDao;
import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.cms.service.model.CmsBtProductModel_Sku;
import com.voyageone.common.util.StringUtils;
import com.voyageone.web2.cms.wsdl.BaseService;
import com.voyageone.web2.sdk.api.VoApiConstants;
import com.voyageone.web2.sdk.api.domain.ProductPriceModel;
import com.voyageone.web2.sdk.api.domain.ProductSkuPriceModel;
import com.voyageone.web2.sdk.api.exception.ApiException;
import com.voyageone.web2.sdk.api.request.*;
import com.voyageone.web2.sdk.api.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private ProductGetService productService;

    @Autowired
    private CmsBtProductDao cmsBtProductDao;

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

        int insertCount = 0;
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
            String query = "{\"prodId\":" + productId + "}";
            queryObject.setQuery(query);
            findModel = cmsBtProductDao.selectOneWithQuery(queryObject, channelId);
        } else if (StringUtils.isEmpty(productCode)) {
            String query = "{\"fields.code\":\"" + productCode + "\"}";
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

        //request.getProductPrices()
        if (request.getProductPrices() == null || request.getProductPrices().size() == 0) {
            VoApiConstants.VoApiErrorCodeEnum codeEnum = VoApiConstants.VoApiErrorCodeEnum.ERROR_CODE_70007;
            throw new ApiException(codeEnum.getErrorCode(), "ProductPrices not found!");
        }

        List<BulkUpdateModel> bulkList = new ArrayList<>();

        for (ProductPriceModel model : request.getProductPrices()) {
            VoApiConstants.VoApiErrorCodeEnum codeEnum = VoApiConstants.VoApiErrorCodeEnum.ERROR_CODE_70007;
            if (model == null) {
                throw new ApiException(codeEnum.getErrorCode(), "ProductPrices not found!");
            }
            if (model.getProductId() == null && model.getProductCode() == null) {
                throw new ApiException(codeEnum.getErrorCode(), "ProductPrices ProductId or ProductCode not found!");
            }
            updatePricesAddBlukUpdateModel(model, bulkList);
        }

        if (bulkList.size() > 0) {
            BulkWriteResult bulkWriteResult = cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, null, "$set");
            setResultCount(result, bulkWriteResult);
        }

        return result;
    }

    /**
     * 批量更新价格信息 根据CodeList
     */
    public void updatePricesAddBlukUpdateModel(ProductPriceModel model, List<BulkUpdateModel> bulkList) {
        VoApiConstants.VoApiErrorCodeEnum codeEnum = VoApiConstants.VoApiErrorCodeEnum.ERROR_CODE_70007;

        HashMap<String, Object> productQueryMap = new HashMap<>();
        if (model.getProductId() != null) {
            productQueryMap.put("prodId", model.getProductId());
        } else {
            productQueryMap.put("fields.code", model.getProductCode());
        }

        HashMap<String, Object> productUpdateMap = new HashMap<>();
        if (model.getMsrpStart() != null) {
            productUpdateMap.put("fields.msrpStart", model.getMsrpStart());
        }
        if (model.getMsrpEnd() != null) {
            productUpdateMap.put("fields.msrpEnd", model.getMsrpEnd());
        }
        if (model.getRetailPriceStart() != null) {
            productUpdateMap.put("fields.retailPriceStart", model.getRetailPriceStart());
        }
        if (model.getRetailPriceEnd() != null) {
            productUpdateMap.put("fields.retailPriceEnd", model.getRetailPriceEnd());
        }
        if (model.getSalePriceStart() != null) {
            productUpdateMap.put("fields.salePriceStart", model.getSalePriceStart());
        }
        if (model.getSalePriceEnd() != null) {
            productUpdateMap.put("fields.salePriceEnd", model.getSalePriceEnd());
        }
        if (model.getCurrentPriceStart() != null) {
            productUpdateMap.put("fields.currentPriceStart", model.getCurrentPriceStart());
        }
        if (model.getCurrentPriceEnd() != null) {
            productUpdateMap.put("fields.currentPriceEnd", model.getCurrentPriceEnd());
        }

        if (productUpdateMap.size() > 0) {
            BulkUpdateModel productUpdateModel = new BulkUpdateModel();
            productUpdateModel.setUpdateMap(productUpdateMap);
            productUpdateModel.setQueryMap(productQueryMap);

            bulkList.add(productUpdateModel);
        }

        if (model.getSkuPrices() != null && model.getSkuPrices().size()>0) {
            for (ProductSkuPriceModel skuMode : model.getSkuPrices()) {
                HashMap<String, Object> skuQueryMap = new HashMap<>();
                if (model.getProductId() != null) {
                    skuQueryMap.put("prodId", model.getProductId());
                } else {
                    skuQueryMap.put("fields.code", model.getProductCode());
                }
                if (StringUtils.isEmpty(skuMode.getSkuCode())) {
                    throw new ApiException(codeEnum.getErrorCode(), "SkuPrices.SkuCode not found!");
                }
                skuQueryMap.put("skus.skuCode", skuMode.getSkuCode());

                HashMap<String, Object> updateMap = new HashMap<>();
                if (skuMode.getPriceMsrp() != null) {
                    updateMap.put("skus.$.priceMsrp", skuMode.getPriceMsrp());
                }
                if (skuMode.getPriceRetail() != null) {
                    updateMap.put("skus.$.priceRetail", skuMode.getPriceRetail());
                }
                if (skuMode.getPriceSale() != null) {
                    updateMap.put("skus.$.priceSale", skuMode.getPriceSale());
                }

                if (updateMap.size() > 0) {
                    BulkUpdateModel skuUpdateModel = new BulkUpdateModel();
                    skuUpdateModel.setUpdateMap(updateMap);
                    skuUpdateModel.setQueryMap(skuQueryMap);

                    bulkList.add(skuUpdateModel);
                }
            }
        }
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

        VoApiConstants.VoApiErrorCodeEnum codeEnum = VoApiConstants.VoApiErrorCodeEnum.ERROR_CODE_70007;
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
     * 批量更新价格信息 根据CodeList
     */
    public void addDeleteSkusBulk(String channelId, Long productId, String productCode, Set<String> skuCodes, List<BulkUpdateModel> bulkList) {
        VoApiConstants.VoApiErrorCodeEnum codeEnum = VoApiConstants.VoApiErrorCodeEnum.ERROR_CODE_70007;

        if (skuCodes != null && skuCodes.size()>0) {

            CmsBtProductModel findModel = null;
            JomgoQuery queryObject = new JomgoQuery();
            queryObject.setProjection("skus");
            if (productId != null) {
                String query = "{\"prodId\":" + productId + "}";
                queryObject.setQuery(query);
                findModel = cmsBtProductDao.selectOneWithQuery(queryObject, channelId);
            } else if (StringUtils.isEmpty(productCode)) {
                String query = "{\"fields.code\":\"" + productCode + "\"}";
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
                    if (StringUtils.isEmpty(skuCode)) {
                        continue;
                    }
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
