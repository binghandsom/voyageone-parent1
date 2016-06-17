package com.voyageone.components.overstock.service;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.components.overstock.bean.OverstockMultipleRequest;
import com.voyageone.components.overstock.bean.product.OverstockProductOneQueryRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author aooer 2016/6/8.
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class OverstockProductServiceTest {

    private static final Logger log= LoggerFactory.getLogger("test");

    @Autowired
    private OverstockProductService overstockProductService;

    @Test
    public void testQueryForMultipleProducts() throws Exception {
        OverstockMultipleRequest request=new OverstockMultipleRequest();
        request.setOffset(0);
        request.setLimit(20);
        log.info(JacksonUtil.bean2Json(overstockProductService.queryForMultipleProducts(request)));
    }

    @Test
    public void testQueryForOneProduct() throws Exception {
        OverstockProductOneQueryRequest request=new OverstockProductOneQueryRequest();
        request.setProductId("kcuNrHeO9HpzFa2xjj1I-A");
        log.info(JacksonUtil.bean2Json(overstockProductService.queryForOneProduct(request)));
    }
}