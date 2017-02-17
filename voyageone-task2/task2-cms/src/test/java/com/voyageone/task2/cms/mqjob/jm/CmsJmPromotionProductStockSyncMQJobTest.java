package com.voyageone.task2.cms.mqjob.jm;

import com.voyageone.common.configs.MQConfigInitTestUtil;
import com.voyageone.service.impl.cms.vomq.vomessage.body.JmPromotionProductStockSyncMQMessageBody;
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

    @Test
    public void testOnStartup() throws InterruptedException {
        MQConfigInitTestUtil.startMQ(serviceJob);
        JmPromotionProductStockSyncMQMessageBody messageBody = new JmPromotionProductStockSyncMQMessageBody();
        // {\"consumerRetryTimes\":3,\"mqId\":0,\"delaySecond\":0,\"sender\":\"CmsSynInventoryToCmsJob\"}",
        messageBody.setConsumerRetryTimes(3);
        messageBody.setMqId(0);
        messageBody.setDelaySecond(0);
        messageBody.setSender("CmsSynInventoryToCmsJob");
        serviceJob.onStartup(messageBody);
    }
}