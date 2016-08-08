package com.voyageone.service.impl.cms;

import com.voyageone.service.impl.cms.prices.PriceService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * Created by Ethan Shi on 2016/7/14.
 *
 * @author Ethan Shi
 * @author jonas
 * @version 2.4.0
 * @since 2.4.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class PriceServiceTest {

    @Autowired
    private PriceService priceService;

    @Autowired
    private ProductService productService;

    @Test
    public void setRetailPrice() throws Exception {

        CmsBtProductModel product = productService.getProductById("010", 9303);

        priceService.setRetailPrice(product, 23);

        System.out.println("\n\n");

        product.getPlatform(23).getSkus();

        System.out.println("\n\n");
    }
}