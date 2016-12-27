package com.voyageone.service.impl.cms.product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-service2.xml")
public class PlatformForcedInStockProduct_AutoOnSaleServiceTest {
    @Autowired
    PlatformForcedInStockProduct_AutoOnSaleService service;

    @Test
    public  void  test()
    {
        service.onSaleByChannelId("010");
//        service.sumByChannelId("016");
//        service.sumByChannelId("017");
//        service.sumByChannelId("018");
//        service.sumByChannelId("019");
//        service.sumByChannelId("021");
//        service.sumByChannelId("023");
//        service.sumByChannelId("024");
    }
}
