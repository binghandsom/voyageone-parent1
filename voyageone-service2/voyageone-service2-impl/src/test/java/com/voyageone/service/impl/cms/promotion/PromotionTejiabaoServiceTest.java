package com.voyageone.service.impl.cms.promotion;

import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;

import static org.junit.Assert.*;

/**
 * Created by james on 2017/2/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-service2.xml")
public class PromotionTejiabaoServiceTest {
    @Autowired
    PromotionTejiabaoService promotionTejiabaoService;
    @Autowired
    ProductService productService;
    @Test
    public void updateTejiabaoPrice() throws Exception {
        CmsBtProductModel cmsBtProductModel = productService.getProductByCode("010", "ESW8601G3FBT");
        promotionTejiabaoService.updateTejiabaoPrice(cmsBtProductModel.getChannelId(),23,cmsBtProductModel.getCommon().getFields().getCode(), cmsBtProductModel.getPlatform(23).getSkus(),"test");
    }

}