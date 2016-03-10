package com.voyageone.web2.sdk.api.request;

import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.response.ProductSkusPutResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 2015/12/29.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class ProductSkusPutRequestTest {

    @Autowired
    protected VoApiDefaultClient voApiClient;

    @Test
    public void testProductSkusPut() {
        ProductSkusPutRequest requestModel = new ProductSkusPutRequest("300");
        //{ "skuCode" : "100001-1" , "barcode" : "1234567890147" , "priceMsrp" : 165.0 , "priceRetail" : 313.0 , "priceSale" : 872.0 , "skuCarts" : [ 21 , 23] , "size" : "OneSize" , "msrp" : 101.0 , "salePrice" : 103.0 , "retailPrice" : 102.0}

        CmsBtProductModel_Sku skuModel = new CmsBtProductModel_Sku();
        skuModel.setSkuCode("100002-1");
        skuModel.setBarcode("ab234567890147");
        skuModel.setPriceMsrp(102d);
        requestModel.addSkus(skuModel);

        skuModel = new CmsBtProductModel_Sku();
        skuModel.setSkuCode("100002-12");
        skuModel.setBarcode("ac234567890147");
        skuModel.setPriceMsrp(102d);
        requestModel.addSkus(skuModel);

        requestModel.setProductId(2l);

        //SDK取得Product 数据
        ProductSkusPutResponse response = voApiClient.execute(requestModel);
        System.out.println(response);
    }
}
