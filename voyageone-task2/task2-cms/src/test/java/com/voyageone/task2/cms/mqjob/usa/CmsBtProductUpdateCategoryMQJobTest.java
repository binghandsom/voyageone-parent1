package com.voyageone.task2.cms.mqjob.usa;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.impl.cms.vomq.vomessage.body.usa.CmsBtProductUpdateCategoryMQMessageBody;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by dell on 2017/7/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsBtProductUpdateCategoryMQJobTest {

    @Autowired
    CmsBtProductUpdateCategoryMQJob cmsBtProductUpdateCategoryMQJob;
    @Test
    public void testOnStartup() throws Exception {
        String message = "{\"consumerRetryTimes\":0,\"mqId\":0,\"delaySecond\":0,\"sender\":\"james\",\"channelId\":\"001\",\"productCodes\":[\"68220-gem\"],\"cartId\":1,\"pCatPath\":\"22222\",\"pCatId\":\"22222\"}";
        CmsBtProductUpdateCategoryMQMessageBody body = JacksonUtil.json2Bean(message, CmsBtProductUpdateCategoryMQMessageBody.class);
        cmsBtProductUpdateCategoryMQJob.onStartup(body);

    }
}