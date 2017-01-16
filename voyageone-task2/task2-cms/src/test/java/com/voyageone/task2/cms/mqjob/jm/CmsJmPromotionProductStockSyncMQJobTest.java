package com.voyageone.task2.cms.mqjob.jm;

import com.voyageone.common.configs.MQConfigInitTestUtil;
import com.voyageone.service.impl.cms.jumei2.CmsBtJmPromotionProduct3Service;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by dell on 2017/1/3.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsJmPromotionProductStockSyncMQJobTest {
    @Autowired
    CmsJmPromotionProductStockSyncMQJob serviceJob;

    @Autowired
    CmsBtJmPromotionProduct3Service cmsBtJmPromotionProduct3Service;

    @Test
    public void testOnStartup() throws InterruptedException {
        MQConfigInitTestUtil.startMQ(serviceJob);
    }

    @Test
    public  void  testSender()
    {
        cmsBtJmPromotionProduct3Service.sendMessageJmPromotionProductStockSync("test");
    }
}