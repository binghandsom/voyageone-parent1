package com.voyageone.web2.cms.openapi.service;

import com.voyageone.web2.sdk.api.request.ProductForOmsGetRequest;
import com.voyageone.web2.sdk.api.request.ProductForWmsGetRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.Array;
import java.util.Arrays;

import static org.junit.Assert.*;

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