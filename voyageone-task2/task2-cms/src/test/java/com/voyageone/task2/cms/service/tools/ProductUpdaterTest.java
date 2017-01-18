package com.voyageone.task2.cms.service.tools;

import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.task2.cms.mqjob.advanced.search.CmsRefreshProductsMQJob;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * 默认属性下的商品属性强制重刷任务，商品更新器的单元测试类
 * Created by jonas on 2016/11/4.
 *
 * @since 2.9.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class ProductUpdaterTest {
    @Autowired
    private CmsRefreshProductsMQJob cmsRefreshProductsMQJob;

    @Autowired
    private ProductService productService;

    @Test
    public void update() throws Exception {

        String channelId = "010";
        Integer cartId = 23;
        long productId = 9315;
        String fieldId = "prop_8560225";
        Object expected = "Hello World";
        Object actual;

        CmsBtProductModel cmsBtProductModel = productService.getProductById(channelId, productId);

        Map<String, Object> valueMap = new HashMap<>();

        valueMap.put(fieldId, expected);

        cmsRefreshProductsMQJob.new ProductUpdater(cmsBtProductModel, valueMap, cartId, channelId)
                .update();

        cmsBtProductModel = productService.getProductById(channelId, productId);

        actual = cmsBtProductModel.getPlatform(cartId).getFields().get(fieldId);

        assertEquals(expected, actual);
    }
}