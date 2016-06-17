package com.voyageone.web2.cms.openapi.control;

import com.voyageone.web2.cms.openapi.OpenAipBaseController;
import com.voyageone.web2.cms.openapi.service.OpenApiProductService;
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
 * @since 2.0.0
 */

@RestController
@RequestMapping(
        value  = "/rest/product",
        method = RequestMethod.POST
)
public class OpenApiProductController extends OpenAipBaseController {

    // TODO 好像不能只取到group的platforms数据
    private final String searchItems = "prodId;channelId;orgChannelId;fields;skus;groups";

    @Autowired
    private OpenApiProductService productService;

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
}