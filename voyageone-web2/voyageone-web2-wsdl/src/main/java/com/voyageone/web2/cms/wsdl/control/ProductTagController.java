package com.voyageone.web2.cms.wsdl.control;

import com.voyageone.web2.cms.wsdl.BaseController;
import com.voyageone.web2.cms.wsdl.service.ProductTagService;
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
        value  = "/rest/product/tag",
        method = RequestMethod.POST
)
public class ProductTagController extends BaseController {

    @Autowired
    private ProductTagService productTagService;

    /**
     * selectList
     * @return ProductSkusPutResponse
     */
    @RequestMapping("put")
    public ProductsTagPutResponse put(@RequestBody ProductsTagPutRequest request) {
        return productTagService.saveTagProducts(request);
    }

    /**
     * delete
     * @return ProductsTagDeleteResponse
     */
    @RequestMapping("delete")
    public ProductsTagDeleteResponse delete(@RequestBody ProductsTagDeleteRequest  request) {
        return productTagService.delete(request);
    }
}
