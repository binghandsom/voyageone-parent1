package com.voyageone.task2.cms.mqjob;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsBrandBlockMQMessageBody;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsBrandBlockMQJobTest {

    @Autowired
    CmsBrandBlockMQJob service;

    @Test
    public void testOnStartup() throws Exception {

        String msg ="{\"consumerRetryTimes\":0,\"mqId\":0,\"delaySecond\":0,\"sender\":\"james\",\"channelId\":\"025\",\"data\":{\"id\":71,\"created\":null,\"creater\":\"james\",\"modified\":null,\"modifier\":\"james\",\"channelId\":\"025\",\"cartId\":1,\"type\":0,\"brand\":\"Nino Cerruti\"},\"blocking\":true}";
        CmsBrandBlockMQMessageBody messageBody = JacksonUtil.json2Bean(msg, CmsBrandBlockMQMessageBody.class);

        service.onStartup(messageBody);

    }
}
