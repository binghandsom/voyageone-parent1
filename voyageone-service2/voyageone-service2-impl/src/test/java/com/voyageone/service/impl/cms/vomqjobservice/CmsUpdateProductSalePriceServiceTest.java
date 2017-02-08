package com.voyageone.service.impl.cms.vomqjobservice;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.impl.cms.vomq.vomessage.body.UpdateProductSalePriceMQMessageBody;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by james on 2017/2/8.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-service2.xml")
public class CmsUpdateProductSalePriceServiceTest {
    @Autowired
    CmsUpdateProductSalePriceService cmsUpdateProductSalePriceService;
    @Test
    public void process() throws Exception {
        String json = "{\"consumerRetryTimes\":0,\"mqId\":0,\"delaySecond\":0,\"sender\":\"james\",\"productCodes\":[\"10006255\"],\"cartId\":28,\"channelId\":\"928\",\"params\":{\"cartId\":29,\"_option\":\"saleprice\",\"productIds\":[\"10006255\"],\"isSelAll\":0,\"priceType\":\"priceSale\",\"optionType\":\"=\",\"priceValue\":null,\"roundType\":2,\"skuUpdType\":3},\"userId\":9}";
        UpdateProductSalePriceMQMessageBody model = JacksonUtil.json2Bean(json, UpdateProductSalePriceMQMessageBody.class);
        cmsUpdateProductSalePriceService.process(model);
    }

}