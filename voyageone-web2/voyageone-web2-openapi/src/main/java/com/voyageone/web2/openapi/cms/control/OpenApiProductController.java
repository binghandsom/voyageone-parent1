package com.voyageone.web2.openapi.cms.control;

import com.voyageone.service.impl.cms.product.CmsBtCombinedProductService;
import com.voyageone.web2.openapi.OpenApiBaseController;
import com.voyageone.web2.openapi.cms.service.OpenApiProductService;
import com.voyageone.web2.sdk.api.request.cms.ProductForOmsGetRequest;
import com.voyageone.web2.sdk.api.request.cms.ProductForWmsGetRequest;
import com.voyageone.web2.sdk.api.request.cms.SuitSkuInfoForOmsRequest;
import com.voyageone.web2.sdk.api.response.cms.ProductForOmsGetResponse;
import com.voyageone.web2.sdk.api.response.cms.ProductForWmsGetResponse;
import com.voyageone.web2.sdk.api.response.cms.SuitSkuInfoForOmsResponse;
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
 * @since 2.0.0
 */

@RestController
@RequestMapping(
        value  = "/rest/cms/product",
        method = RequestMethod.POST
)
public class OpenApiProductController extends OpenApiBaseController {

    // TODO 好像不能只取到group的platforms数据
    private final String searchItems = "prodId;channelId;orgChannelId;common.fields;common.skus;fields;skus;platforms";

    @Autowired
    private OpenApiProductService productService;
    @Autowired
    private CmsBtCombinedProductService cmsBtCombinedProductService;

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
        request.setFields(searchItems);
        return productService.getWmsProductsInfo(request);
    }

    @RequestMapping("getOmsProductsInfo")
    public ProductForOmsGetResponse getOmsProductsInfo(@RequestBody ProductForOmsGetRequest request) {
        request.setFields(searchItems);
        return productService.getOmsProductsInfo(request);
    }

    /**
     * 获取组合套装商品信息，给OMS调用
     * @return
     */
    @RequestMapping("getSuitSkuInfo")
    public SuitSkuInfoForOmsResponse getSuitSkuInfo(@RequestBody SuitSkuInfoForOmsRequest request) {
        SuitSkuInfoForOmsResponse response = new SuitSkuInfoForOmsResponse();
        response.setResultInfo(cmsBtCombinedProductService.getSuitSkuInfo());
        return response;
    }
}