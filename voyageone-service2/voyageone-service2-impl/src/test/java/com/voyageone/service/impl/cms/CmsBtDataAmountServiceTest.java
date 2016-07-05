package com.voyageone.service.impl.cms;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class CmsBtDataAmountServiceTest {
    @Autowired
    CmsBtDataAmountService service;
    @Test
    public void testsumCmsBtFeedInfo() {
        service.sumCmsBtFeedInfo();
    }
    @Test
    public void testSumMaster() {
        service.sumMaster();
    }
    @Test
    public void testSumPrice()
    {
        service.sumPrice();;
    }
    @Test
    public  void  testSumPlatInfo()
    {
         service.sumPlatInfo();
    }
}
