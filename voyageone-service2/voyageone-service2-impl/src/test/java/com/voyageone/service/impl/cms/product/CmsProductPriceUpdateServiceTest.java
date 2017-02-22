package com.voyageone.service.impl.cms.product;

import com.voyageone.service.impl.cms.vomq.vomessage.body.ProductPriceUpdateMQMessageBody;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by james on 2017/2/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-service2.xml")
public class CmsProductPriceUpdateServiceTest {

    @Autowired
    CmsProductPriceUpdateService cmsProductPriceUpdateService;
    @Test
    public void updateProductRetailPrice() throws Exception {
        ProductPriceUpdateMQMessageBody messageBody = new ProductPriceUpdateMQMessageBody();
        messageBody.setChannelId("928");
        messageBody.setCartId(31);
        messageBody.setProdId(3032063L);
        messageBody.setSender("test");

        cmsProductPriceUpdateService.updateProductAndGroupPrice(messageBody);
    }

}