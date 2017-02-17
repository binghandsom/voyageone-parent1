package com.voyageone.service.impl.cms.product;

import com.voyageone.service.impl.cms.vomq.vomessage.body.AdvSearchRefreshRetailPriceMQMessageBody;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

import static org.junit.Assert.*;

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
        AdvSearchRefreshRetailPriceMQMessageBody messageBody = new AdvSearchRefreshRetailPriceMQMessageBody();
        messageBody.setChannelId("928");
        messageBody.setCartId(31);
        messageBody.setCodeList(Arrays.asList("1143593872"));
        messageBody.setSender("test");

        cmsProductPriceUpdateService.updateProductRetailPrice(messageBody);
    }

}