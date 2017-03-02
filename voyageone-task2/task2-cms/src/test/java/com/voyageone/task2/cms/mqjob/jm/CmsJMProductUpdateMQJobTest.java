package com.voyageone.task2.cms.mqjob.jm;

/**
 * Created by dell on 2016/12/30.
 */

import com.voyageone.service.impl.cms.vomq.vomessage.body.JMProductUpdateMQMessageBody;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:voyageone-config.xml")
public class CmsJMProductUpdateMQJobTest {

    @Autowired
    CmsJMProductUpdateMQJob service;

    @Test
    public void testOnStartup() throws Exception {

        // {\"cmsBtJmPromotionId\":1060,\"consumerRetryTimes\":0,\"mqId\":0,\"delaySecond\":0,\"sender\":\"edward\"}
        JMProductUpdateMQMessageBody messageBody = new JMProductUpdateMQMessageBody();
        messageBody.setCmsBtJmPromotionId(1308);
        messageBody.setConsumerRetryTimes(0);
        messageBody.setMqId(0);
        messageBody.setDelaySecond(0);
        messageBody.setSender("edward");

        //service.onStartup(map2);
//        MQConfigInitTestUtil.startMQ(service);
        service.onStartup(messageBody);
    }
}
