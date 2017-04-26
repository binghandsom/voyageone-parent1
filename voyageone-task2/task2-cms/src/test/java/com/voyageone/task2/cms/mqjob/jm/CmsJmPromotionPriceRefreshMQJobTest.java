package com.voyageone.task2.cms.mqjob.jm;

import com.voyageone.common.util.JsonUtil;
import com.voyageone.service.impl.cms.vomq.vomessage.body.JMRefreshPriceMQMessageBody;
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
public class CmsJmPromotionPriceRefreshMQJobTest {
    @Autowired
    CmsJmPromotionPriceRefreshMQJob serviceJob;

    @Test
    public void testOnStartup() throws InterruptedException {
//        MQConfigInitTestUtil.startMQ(serviceJob);
        String msg = "{\"cmsBtJmPromotionId\":1407,\"consumerRetryTimes\":0,\"mqId\":0,\"delaySecond\":0,\"sender\":\"edward\"}";
        JMRefreshPriceMQMessageBody messageBody = JsonUtil.jsonToBean(msg, JMRefreshPriceMQMessageBody.class);
        serviceJob.onStartup(messageBody);
    }
}