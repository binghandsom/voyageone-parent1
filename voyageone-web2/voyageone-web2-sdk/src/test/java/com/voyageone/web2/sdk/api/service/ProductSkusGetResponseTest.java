package com.voyageone.web2.sdk.api.service;

import com.voyageone.cms.service.model.CmsBtProductModel_Sku;
import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.request.ProductSkusGetRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by DELL on 2015/12/28.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class ProductSkusGetResponseTest {

    @Autowired
    protected VoApiDefaultClient voApiClient;

    @Test
    public void testGetProductSkuByProductId() {
        ProductSkusGetRequest requestModel = new ProductSkusGetRequest("300");
        requestModel.addProductId(1L);
        requestModel.addProductId(10L);
        //SDK取得Product 数据
        List<CmsBtProductModel_Sku> skus = voApiClient.execute(requestModel).getProductSkus();
        for (CmsBtProductModel_Sku sku : skus) {
            System.out.println(sku);
        }
    }


    @Test
    public void testGetProductSkuByProductCode() {
        ProductSkusGetRequest requestModel = new ProductSkusGetRequest("300");
        requestModel.addProductCode("100001");
        requestModel.addProductCode("100010");
        //SDK取得Product 数据
        List<CmsBtProductModel_Sku> skus = voApiClient.execute(requestModel).getProductSkus();
        for (CmsBtProductModel_Sku sku : skus) {
            System.out.println(sku);
        }
    }



}
