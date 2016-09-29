package com.voyageone.service.impl.cms.feed;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-service2.xml")
public class FeedSaleServiceTest {

    @Autowired
    FeedSaleService saleService;

    @Test
    public void notSale( ) {

        String channelId="010";
         String clientSku="ESH96163FFEI";
        saleService.notSale(channelId, clientSku);
    }
    @Test
    public void sale( ) {

        String channelId="010";
        String clientSku="ESH96163FFEI";
        saleService.sale(channelId, clientSku,10);
    }
}
