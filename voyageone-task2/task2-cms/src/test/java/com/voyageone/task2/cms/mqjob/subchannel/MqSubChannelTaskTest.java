package com.voyageone.task2.cms.mqjob.subchannel;

import com.voyageone.components.rabbitmq.service.MqSenderService;
import com.voyageone.service.model.cms.CmsBtBrandBlockModel;
//import com.voyageone.task2.cms.mqjob.test.MqSubChannelMQMessageBody;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class MqSubChannelTaskTest {

    @Autowired
    MqSenderService mqSenderService;

//    @Test
//    public void testSendMessage() throws InterruptedException {
//        for (int i = 0; i < 100; i++) {
//            mqSenderService.sendMessage(createBody("001"));
//
//            Thread.sleep(1000);
//        }
//    }
//
//
//    @Test
//    public void testSendMessage1() throws InterruptedException {
//        for (int i = 0; i < 10; i++) {
//            mqSenderService.sendMessage(createBody("002"));
//            Thread.sleep(1000);
//        }
//    }
//
//    private MqSubChannelMQMessageBody createBody(String channelId) {
//        CmsBtBrandBlockModel data = new CmsBtBrandBlockModel();
//        data.setChannelId(channelId);
//
//        MqSubChannelMQMessageBody body = new MqSubChannelMQMessageBody();
//        body.setSender("chuanyu.liang");
//        body.setData(data);
//        return body;
//    }
}
