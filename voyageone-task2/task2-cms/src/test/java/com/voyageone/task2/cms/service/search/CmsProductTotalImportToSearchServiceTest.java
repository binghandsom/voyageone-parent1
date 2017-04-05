package com.voyageone.task2.cms.service.search;

import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:context-cms-test.xml")
public class CmsProductTotalImportToSearchServiceTest {
    @Autowired
    ProductService productService;
    @Test
    public void onStartup() throws Exception {

    }

    @Autowired
    private CmsProductTotalImportToSearchService cmsProductTotalImportToSearchService;

    @Test
    public void testImportDataToSearchFromMongo() {
//        CmsBtProductModel cmsBtProductModel = productService.getProductByObjectId("018","5784e205b48b2a3c51e41aa6");

        String channelId = "001";
        cmsProductTotalImportToSearchService.importDataToSearchFromMongo(channelId);
    }

    @Test
    public void testOnStartup() throws Exception {
        cmsProductTotalImportToSearchService.onStartup(null);
    }
}
