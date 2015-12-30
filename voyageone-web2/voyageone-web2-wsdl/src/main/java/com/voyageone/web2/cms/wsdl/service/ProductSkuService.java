package com.voyageone.web2.cms.wsdl.service;

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
import com.voyageone.web2.sdk.api.request.ProductUpdatePriceRequest;
import com.voyageone.web2.sdk.api.request.ProductsGetRequest;
import com.voyageone.web2.sdk.api.response.ProductSkusGetResponse;
import com.voyageone.web2.sdk.api.response.ProductsGetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
     * update Prices
     * @param request ProductSkusGetRequest
     * @return ProductSkusGetResponse
     */
    public ProductUpdatePriceRequest updatePrices(ProductUpdatePriceRequest request) {
        ProductUpdatePriceRequest result = new ProductUpdatePriceRequest();

        List<CmsBtProductModel> products = null;
        long totalCount = 0L;

        if (request == null) {
            VoApiConstants.VoApiErrorCodeEnum codeEnum = VoApiConstants.VoApiErrorCodeEnum.ERROR_CODE_70001;
            throw new ApiException(codeEnum.getErrorCode(), codeEnum.getErrorMsg());
        }

        //ChannelId
        String channelId = request.getChannelId();
        if (StringUtils.isEmpty(channelId)) {
            VoApiConstants.VoApiErrorCodeEnum codeEnum = VoApiConstants.VoApiErrorCodeEnum.ERROR_CODE_70003;
            throw new ApiException(codeEnum.getErrorCode(), codeEnum.getErrorMsg());
        }

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
            addBlukUpdateModel(model, bulkList);
        }

        if (bulkList.size() > 0) {
            cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, null, "$set");
        }

        return result;
    }

    /**
     * 批量更新上新结果 根据CodeList
     */
    public void addBlukUpdateModel(ProductPriceModel model, List<BulkUpdateModel> bulkList) {
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
                if (skuMode.getMsrp() != null) {
                    updateMap.put("skus.$.msrp", skuMode.getMsrp());
                }
                if (skuMode.getRetailPrice() != null) {
                    updateMap.put("skus.$.retailPrice", skuMode.getRetailPrice());
                }
                if (skuMode.getSalePrice() != null) {
                    updateMap.put("skus.$.salePrice", skuMode.getSalePrice());
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
