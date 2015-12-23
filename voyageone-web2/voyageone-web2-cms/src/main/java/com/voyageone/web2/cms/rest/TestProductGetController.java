package com.voyageone.web2.cms.rest;

import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.web2.cms.CmsRestController;
import com.voyageone.web2.sdk.api.request.ProductsGetRequest;
import com.voyageone.web2.sdk.api.request.ProductGetRequest;
import com.voyageone.web2.sdk.api.response.ProductsGetResponse;
import com.voyageone.web2.sdk.api.response.ProductGetResponse;
import com.voyageone.web2.sdk.api.service.ProductGetClient;
import com.voyageone.web2.sdk.api.service.ProductsGetClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * product Controller
 *
 * @author chuanyu.liang 15/12/9
 * @version 2.0.1
 * @since. 2.0.0
 */

@RestController
@RequestMapping(
        value  = "/rest/puroduct",
        method = RequestMethod.POST
)
public class TestProductGetController extends CmsRestController {

    @Autowired
    protected ProductGetClient productGetClient;

    @Autowired
    protected ProductsGetClient productsGetClient;


    /**
     * 返回selectOne
     * @return
     */
    @RequestMapping("testSelectOne")
    public ProductGetResponse testSelectOne(@RequestBody ProductGetRequest responseMode) {

        //SDK取得Product 数据
        CmsBtProductModel model = productGetClient.getMainProductByGroupId("300", 134);


        ProductGetResponse result = new ProductGetResponse();
        result.setProduct(model);

        // 返回用户信息
        return result;
    }

    /**
     * 返回selectOne
     * @return
     */
    @RequestMapping("testSelectList")
    public ProductsGetResponse testSelectList(@RequestBody ProductsGetRequest responseMode) {

        //SDK取得Product 数据
        Set<Long> productIds = new LinkedHashSet<>();
        productIds.add(1L);
        productIds.add(2L);

        Set<String> fields = new LinkedHashSet<>();
        fields.add("fields.code");
        fields.add("fields.brand");
        fields.add("fields.productName");
        fields.add("fields.middleTitle");


        List<CmsBtProductModel> products = productsGetClient.getProductByIds("300", productIds, fields);




        ProductsGetResponse result = new ProductsGetResponse();
        result.setProducts(products);

        // 返回用户信息
        return result;
    }

}
