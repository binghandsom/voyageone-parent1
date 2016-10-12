package com.voyageone.service.impl.cms.product;

import com.voyageone.service.model.cms.mongo.product.CmsBtProductConstants;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Calendar;

import static com.voyageone.service.model.cms.mongo.product.CmsBtProductConstants.Platform_SKU_COM.confPriceRetail;
import static com.voyageone.service.model.cms.mongo.product.CmsBtProductConstants.Platform_SKU_COM.priceSale;
import static org.junit.Assert.*;

/**
 * 进行价格比较，并创建日志。并提供查询
 * Created by jonas on 2016/10/12.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-service2.xml")
public class CmsBtPriceLogServiceTest {
    @Autowired
    private CmsBtPriceLogService priceLogService;

    @Autowired
    private ProductService productService;

    @Test
    public void addLogAndCallSyncPriceJob() throws Exception {

        CmsBtProductModel productModel = productService.getProductByCode("010", "SM4602FEAMSCSI");

        productModel.getPlatform(26).getSkus().get(0).setAttribute(priceSale.name(), 200d);

        long last = System.currentTimeMillis();
        priceLogService.addLogAndCallSyncPriceJob("010", productModel, "test", "jonas");
        System.out.println(System.currentTimeMillis() - last);
    }
}