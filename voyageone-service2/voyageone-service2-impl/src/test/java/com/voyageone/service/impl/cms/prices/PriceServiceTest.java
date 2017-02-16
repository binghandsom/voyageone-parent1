package com.voyageone.service.impl.cms.prices;

import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by james on 2017/2/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-service2.xml")
public class PriceServiceTest {

    @Autowired
    PriceService priceService;
    @Autowired
    ProductService productService;
    @Test
    public void setPrice() throws Exception {

        CmsBtProductModel cmsBtProductModel = productService.getProductByCode("928","028-ps3833884");
        priceService.setPrice(cmsBtProductModel, 32, false);


    }

}