package com.voyageone.components.overstock.service;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.components.overstock.bean.variation.OverstockVariationMultipleQueryRequest;
import com.voyageone.components.overstock.bean.variation.OverstockVariationMultipleSkuQueryRequest;
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
public class OverstockVariationServiceTest {

    private static final Logger log= LoggerFactory.getLogger("test");

    @Autowired
    private OverstockVariationService overstockVariationService;

    @Test
    public void testQueryingForMultipleVariations() throws Exception {
        OverstockVariationMultipleQueryRequest request=new OverstockVariationMultipleQueryRequest();
        request.setOffset(0);
        request.setLimit(20);
        log.info(JacksonUtil.bean2Json(overstockVariationService.queryingForMultipleVariations(request)));
    }

    @Test
    public void testQueryingForMultipleSkuVariations() throws Exception {
        OverstockVariationMultipleSkuQueryRequest request=new OverstockVariationMultipleSkuQueryRequest();
        request.setSku("mnT5YM3RhA9aSt17SseU3Q");
        log.info(JacksonUtil.bean2Json(overstockVariationService.queryingForMultipleSkuVariations(request)));
    }
}