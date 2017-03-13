package com.voyageone.web2.openapi.cms.service;

import com.voyageone.web2.sdk.api.request.cms.ProductForOmsGetRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

/**
 * @author james.li on 2016/6/23.
 * @version 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class OpenApiProductServiceTest {

    @Autowired
    OpenApiProductService openApiProductService;
    @Test
    public void testGetWmsProductsInfo() throws Exception {
        ProductForOmsGetRequest productForWmsGetRequest = new ProductForOmsGetRequest();
        productForWmsGetRequest.setChannelId("010");
//        productForWmsGetRequest.setSkuIncludes("DMC015700");
        productForWmsGetRequest.setSkuList(Arrays.asList("DMC015700"));
        productForWmsGetRequest.setCartId("0");
        openApiProductService.getOmsProductsInfo(productForWmsGetRequest);
    }
}