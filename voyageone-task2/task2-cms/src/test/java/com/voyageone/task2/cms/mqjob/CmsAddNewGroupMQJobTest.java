package com.voyageone.task2.cms.mqjob;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsAddNewGroupMQMessageBody;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Edward
 * @version 2.0.0, 2017/4/27
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsAddNewGroupMQJobTest {

    @Autowired
    CmsAddNewGroupMQJob service;

    @Test
    public void testOnStartup() throws Exception {

        String msg = "{\"code\":\"15113101-Green-4Piece\",\"cartId\":0, \"isSingle\": true, \"consumerRetryTimes\":0,\"mqId\":0,\"delaySecond\":0,\"sender\":\"edward\",\"channelId\":\"024\"}";
        CmsAddNewGroupMQMessageBody messageBody = JacksonUtil.json2Bean(msg, CmsAddNewGroupMQMessageBody.class);

        service.onStartup(messageBody);

    }

}