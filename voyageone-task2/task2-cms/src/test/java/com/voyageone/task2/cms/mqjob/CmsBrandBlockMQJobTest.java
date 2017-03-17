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

        String msg ="{\n" +
                "\t\"data\": {\n" +
                "\t\t\"channelId\": \"010\",\n" +
                "\t\t\"cartId\": 27,\n" +
                "\t\t\"type\": 2,\n" +
                "\t\t\"brand\": \"10507\",\n" +
                "\t\t\"id\": 68,\n" +
                "\t\t\"creater\": \"edward\",\n" +
                "\t\t\"modifier\": \"edward\"\n" +
                "\t},\n" +
                "\t\"blocking\": true,\n" +
                "\t\"consumerRetryTimes\": 0,\n" +
                "\t\"mqId\": 0,\n" +
                "\t\"delaySecond\": 0,\n" +
                "\t\"sender\": \"edward\"\n" +
                "}";
        CmsBrandBlockMQMessageBody messageBody = JacksonUtil.json2Bean(msg, CmsBrandBlockMQMessageBody.class);

        service.onStartup(messageBody);

    }
}
