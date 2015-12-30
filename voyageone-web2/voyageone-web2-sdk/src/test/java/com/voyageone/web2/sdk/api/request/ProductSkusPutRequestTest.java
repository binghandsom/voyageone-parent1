package com.voyageone.web2.sdk.api.request;

import com.voyageone.cms.service.model.CmsBtProductModel_Sku;
import com.voyageone.web2.sdk.api.VoApiDefaultClient;
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
    public void testProductUpdatePrice() {
        ProductSkusPutRequest requestModel = new ProductSkusPutRequest("300");
        //{ "skuCode" : "100001-1" , "barcode" : "1234567890147" , "priceMsrp" : 165.0 , "priceRetail" : 313.0 , "priceSale" : 872.0 , "skuCarts" : [ 21 , 23] , "size" : "OneSize" , "msrp" : 101.0 , "salePrice" : 103.0 , "retailPrice" : 102.0}

        List<CmsBtProductModel_Sku> skus = new ArrayList<>();
        CmsBtProductModel_Sku skuModel = new CmsBtProductModel_Sku();
        skuModel.setSkuCode("100001-1");
        skuModel.setBarcode("n234567890147");
        skuModel.setPriceMsrp(102d);
        skus.add(skuModel);

        //requestModel.addProductId(1l, skus);

        skus = new ArrayList<>();
        skuModel = new CmsBtProductModel_Sku();
        skuModel.setSkuCode("100002-1");
        skuModel.setBarcode("n234567890147");
        skuModel.setPriceMsrp(102d);
        //skus.add(skuModel);

        skuModel = new CmsBtProductModel_Sku();
        skuModel.setSkuCode("100002-11");
        skuModel.setBarcode("n234567890147");
        skuModel.setPriceMsrp(102d);
        skus.add(skuModel);

        requestModel.addProductId(2l, skus);

        //SDK取得Product 数据
        voApiClient.execute(requestModel);
    }
}
