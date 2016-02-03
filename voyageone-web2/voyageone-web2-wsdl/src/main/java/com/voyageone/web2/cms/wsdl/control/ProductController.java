package com.voyageone.web2.cms.wsdl.control;

import com.voyageone.web2.cms.wsdl.BaseController;
import com.voyageone.web2.cms.wsdl.service.ProductService;
import com.voyageone.web2.sdk.api.VoApiUpdateResponse;
import com.voyageone.web2.sdk.api.request.*;
import com.voyageone.web2.sdk.api.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * product Controller
 *
 * @author chuanyu.liang 15/12/9
 * @version 2.0.1
 * @since. 2.0.0
 */

@RestController
@RequestMapping(
        value  = "/rest/product",
        method = RequestMethod.POST
)
public class ProductController extends BaseController {

    @Autowired
    private ProductService productService;

    /**
     * selectOne
     *
     * @return ProductGetResponse
     */
    @RequestMapping("selectOne")
    public ProductGetResponse selectOne(@RequestBody ProductGetRequest request) {
        return productService.selectOne(request);
    }

    /**
     * selectList
     *
     * @return ProductsGetResponse
     */
    @RequestMapping("selectList")
    public ProductsGetResponse selectList(@RequestBody ProductsGetRequest request) {
        return productService.selectList(request);
    }

    /**
     * selectList
     *
     * @return ProductsCountResponse
     */
    @RequestMapping("selectCount")
    public ProductsCountResponse selectCount(@RequestBody ProductsCountRequest request) {
        return productService.selectCount(request);
    }

    /**
     * getPriceLog
     *
     * @return ProductPriceLogGetResponse
     */
    @RequestMapping("priceLog/get")
    public ProductPriceLogGetResponse getPriceLog(@RequestBody ProductPriceLogGetRequest request) {
        return productService.getPriceLog(request);
    }

    /**
     * add product
     *
     * @return ProductsAddResponse
     */
    @RequestMapping("add")
    public ProductsAddResponse addProduct(@RequestBody ProductsAddRequest request) {
        return productService.addProducts(request);
    }

    /**
     * update product
     *
     * @return ProductUpdateResponse
     */
    @RequestMapping("update")
    public ProductUpdateResponse updateProduct(@RequestBody ProductUpdateRequest request) {
        return productService.updateProduct(request);
    }

    /**
     * remove product
     *
     * @return ProductsDeleteResponse
     */
    @RequestMapping("delete")
    public ProductsDeleteResponse deleteProducts(@RequestBody ProductsDeleteRequest request) {
        return productService.deleteProducts(request);
    }



    /**
     * update Status Product
     *
     * @return ProductGroupsPutResponse
     */
    @RequestMapping("/status/update")
    public ProductGroupsPutResponse updateStatusProduct(@RequestBody ProductStatusPutRequest request) {
        return productService.updateStatusProduct(request);
    }

    /**
     * change product category
     * @param request
     * @return
     */
    @RequestMapping("/category/switch")
    public VoApiUpdateResponse changeProductCategory(@RequestBody ProductCategoryUpdateRequest request){

        return productService.changeProductCategory(request);
    }

    @RequestMapping("getWmsProductsInfo")
    public ProductForWmsGetResponse getWmsProductsInfo(@RequestBody ProductForWmsGetRequest request) {
//        ProductForWmsGetResponse response = new ProductForWmsGetResponse();
//
//        // check the request is not null
//        if (request == null) {
//            response.setResult("NG");
//            response.setMessage("param data is empty");
//        } else {
//            StringBuilder sbNeed = new StringBuilder();
//            // check the channelId is not empty
//            if (StringUtils.isEmpty(request.getChannelId())) {
//                sbNeed.append("channelId");
//            }
//
//            // check the code is not empty
//            if (StringUtils.isEmpty(request.getCode())) {
//                if (sbNeed.length() > 0) sbNeed.append(", code");
//            }
//
//            if (sbNeed.length() > 0) {
//                response.setResult("NG");
//                response.setMessage(sbNeed.append(" is empty.").toString());
//            }
//            // if check ok then get the product info
//            else {
//                response = productService.getWmsProductsInfo(request);
//            }
//        }
        return productService.getWmsProductsInfo(request);
    }

    @RequestMapping("getOmsProductsInfo")
    public ProductForOmsGetResponse getOmsProductsInfo(@RequestBody ProductForOmsGetRequest request) {
        return productService.getOmsProductsInfo(request);
    }
}