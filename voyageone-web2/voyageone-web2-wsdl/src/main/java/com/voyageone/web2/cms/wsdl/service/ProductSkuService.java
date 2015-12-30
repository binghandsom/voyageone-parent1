package com.voyageone.web2.cms.wsdl.service;

import com.mongodb.BasicDBObject;
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
import com.voyageone.web2.sdk.api.request.ProductSkusGetRequest;
import com.voyageone.web2.sdk.api.request.ProductSkusPutRequest;
import com.voyageone.web2.sdk.api.request.ProductUpdatePriceRequest;
import com.voyageone.web2.sdk.api.request.ProductsGetRequest;
import com.voyageone.web2.sdk.api.response.ProductSkusGetResponse;
import com.voyageone.web2.sdk.api.response.ProductSkusPutResponse;
import com.voyageone.web2.sdk.api.response.ProductsGetResponse;
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

        List<BulkUpdateModel> bulkList = new ArrayList<>();
        Map<Long, List<CmsBtProductModel_Sku>> productIdSkuMap = request.getProductIdSkuMap();
        Map<String, List<CmsBtProductModel_Sku>> productCodeSkuMap = request.getProductCodeSkuMap();
        if (productIdSkuMap != null && productIdSkuMap.size() > 0) {
            for (Map.Entry<Long, List<CmsBtProductModel_Sku>> entry : productIdSkuMap.entrySet()) {
                Long productId = entry.getKey();
                saveSkusAddBlukUpdateModel(channelId, productId, null, entry.getValue(), bulkList);
            }
        } else if (productCodeSkuMap != null && productCodeSkuMap.size() > 0) {
            for (Map.Entry<String, List<CmsBtProductModel_Sku>> entry : productCodeSkuMap.entrySet()) {
                String productCode = entry.getKey();
                saveSkusAddBlukUpdateModel(channelId, null, productCode, entry.getValue(), bulkList);
            }
        } else {
            VoApiConstants.VoApiErrorCodeEnum codeEnum = VoApiConstants.VoApiErrorCodeEnum.ERROR_CODE_70007;
            throw new ApiException(codeEnum.getErrorCode(), "productIdSkuMap or productCodeSkuMap not found!");
        }

        if (bulkList.size() > 0) {
            cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, null, "$push", true);
        }

        return result;
    }

    /**
     * saveSkusAddBlukUpdateModel
     * @param channelId channel ID
     * @param productId product Id
     * @param productCode product Code
     * @param models CmsBtProductModel_Sku
     * @param bulkList List<BulkUpdateModel>
     */
    private void saveSkusAddBlukUpdateModel(String channelId, Long productId, String productCode, List<CmsBtProductModel_Sku> models, List<BulkUpdateModel> bulkList) {
        VoApiConstants.VoApiErrorCodeEnum codeEnum = VoApiConstants.VoApiErrorCodeEnum.ERROR_CODE_70007;
        if (models == null || models.size() == 0) {
            return;
        }

        if (productId == null && StringUtils.isEmpty(productCode)) {
            throw new ApiException(codeEnum.getErrorCode(), "productId or productCode not found!");
        }

        long findCount = 0;
        if (productId != null) {
            String query = "{\"prodId\":" + productId + "}";
            findCount = cmsBtProductDao.countByQuery(query, channelId);
        } else if (StringUtils.isEmpty(productCode)) {
            String query = "{\"fields.code\":\"" + productCode + "\"}";
            findCount = cmsBtProductDao.countByQuery(query, channelId);
        }

        if (findCount > 0) {
            for (CmsBtProductModel_Sku model : models) {
                if (model == null) {
                    continue;
                }
                HashMap<String, Object> queryMap = new HashMap<>();
                if (productId != null) {
                    queryMap.put("prodId", productId);
                } else {
                    queryMap.put("fields.code", productCode);
                }
                if (StringUtils.isEmpty(model.getSkuCode())) {
                    throw new ApiException(codeEnum.getErrorCode(), "SkuCode not found!");
                }
                //queryMap.put("skus.skuCode", model.getSkuCode());

                BasicDBObject dbObject = model.toUpdateBasicDBObject("skus.");

                if (dbObject.size() > 0) {
                    BulkUpdateModel skuUpdateModel = new BulkUpdateModel();
                    skuUpdateModel.setUpdateMap(dbObject);
                    skuUpdateModel.setQueryMap(queryMap);

                    bulkList.add(skuUpdateModel);
                }
            }
        }
    }

    /**
     * update Prices
     * @param request ProductSkusGetRequest
     * @return ProductSkusGetResponse
     */
    public ProductUpdatePriceRequest updatePrices(ProductUpdatePriceRequest request) {
        ProductUpdatePriceRequest result = new ProductUpdatePriceRequest();

        List<CmsBtProductModel> products = null;
        long totalCount = 0L;

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
            cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, null, "$set");
        }

        return result;
    }

    /**
     * 批量更新上新结果 根据CodeList
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

}
