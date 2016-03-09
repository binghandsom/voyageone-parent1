package com.voyageone.web2.sdk.api.request;

import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.response.ProductSkusDeleteResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by DELL on 2015/12/29.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class ProductSkusDeleteRequestTest {

    @Autowired
    protected VoApiDefaultClient voApiClient;

    @Test
    public void testProductSkusDelete() {
        ProductSkusDeleteRequest requestModel = new ProductSkusDeleteRequest("300");
        //{ "skuCode" : "100001-1" , "barcode" : "1234567890147" , "priceMsrp" : 165.0 , "priceRetail" : 313.0 , "priceSale" : 872.0 , "skuCarts" : [ 21 , 23] , "size" : "OneSize" , "msrp" : 101.0 , "salePrice" : 103.0 , "retailPrice" : 102.0}
        Set<String> skuCodes = new HashSet<>();
        skuCodes.add("100006-2");
        skuCodes.add("100006-3");
        requestModel.addProductIdSkuCodeList(6l, skuCodes);

        //SDK取得Product 数据
        ProductSkusDeleteResponse response =  voApiClient.execute(requestModel);
        System.out.println(response);
    }
}
