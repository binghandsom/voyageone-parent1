package com.voyageone.service.impl.cms;


import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.impl.cms.product.ProductLogService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class CmsProductLogServiceTest {
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductLogService productLogService;

    @Test
    public void testInsertCmsBtProduct() throws Exception {
        CmsBtProductModel ret = productService.getProductByCode("001", "00341");
        productLogService.insertProductHistory(ret);
    }
}