package com.voyageone.service.impl.cms.prices;

import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by james on 2017/2/7.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-service2.xml")
public class PriceServiceTest {

    @Autowired
    PriceService priceService;

    @Autowired
    ProductService productService;
    @Test
    public void unifySkuPriceSale() throws Exception {
        CmsBtProductModel cmsBtProductModel = productService.getProductByCode("928","028-ps6816332");
        Integer chg = priceService.setPrice(cmsBtProductModel,false);
//        priceService.unifySkuPriceSale(cmsBtProductModel, cmsBtProductModel.getPlatform(28).getSkus(),"928", 28);
        System.out.println(chg);
        return;
    }

}