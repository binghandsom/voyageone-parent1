package com.voyageone.web2.cms.wsdl.service;

import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.cms.service.model.CmsBtProductModel_Sku;
import com.voyageone.web2.cms.wsdl.BaseService;
import com.voyageone.web2.sdk.api.request.ProductSkusGetRequest;
import com.voyageone.web2.sdk.api.request.ProductsGetRequest;
import com.voyageone.web2.sdk.api.response.ProductSkusGetResponse;
import com.voyageone.web2.sdk.api.response.ProductsGetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        result.setTotalCount((long)productSkus.size());

        return result;
    }

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

}
