package com.voyageone.task2.cms.mqjob.advanced.search;

import com.voyageone.common.configs.MQConfigInitTestUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsPlatformCategoryUpdateMQMessageBody;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Edward
 * @version 2.0.0, 2017/2/8
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsPlatformCategoryUpdateMQJobTest {

    @Autowired
    private CmsPlatformCategoryUpdateMQJob cmsPlatformCategoryUpdateMQJob;

    @Test
    public void onStartup() throws Exception {
        String message = "{\"consumerRetryTimes\":0,\"mqId\":0,\"delaySecond\":0,\"sender\":\"james\",\"channelId\":\"010\",\"productCodes\":[\"00395\"],\"cartId\":27,\"pCatPath\":\"手表>智能腕表\",\"pCatId\":\"125026010\"}";
        CmsPlatformCategoryUpdateMQMessageBody body = JacksonUtil.json2Bean(message, CmsPlatformCategoryUpdateMQMessageBody.class);
        cmsPlatformCategoryUpdateMQJob.onStartup(body);
    }


}