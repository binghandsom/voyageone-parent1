package com.voyageone.service.impl.cms;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-service2.xml")
public class CmsBtDataAmountServiceTest {
    @Test
    public void sumByChannelId() throws Exception {

    }

    @Autowired
    CmsBtDataAmountService service;

    @Test
    public  void  test()
    {
        service.sumByChannelId("928");
//        service.sumByChannelId("016");
//        service.sumByChannelId("017");
//        service.sumByChannelId("018");
//        service.sumByChannelId("019");
//        service.sumByChannelId("021");
//        service.sumByChannelId("023");
//        service.sumByChannelId("024");
    }
}
