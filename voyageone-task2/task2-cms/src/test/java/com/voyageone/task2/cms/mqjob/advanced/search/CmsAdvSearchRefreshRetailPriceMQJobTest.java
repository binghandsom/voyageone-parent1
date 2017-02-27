package com.voyageone.task2.cms.mqjob.advanced.search;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.impl.cms.vomq.vomessage.body.AdvSearchRefreshRetailPriceMQMessageBody;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by dell on 2017/1/13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsAdvSearchRefreshRetailPriceMQJobTest {

    @Autowired
    private CmsAdvSearchRefreshRetailPriceMQJob cmsAdvSearchRefreshRetailPriceMQJob;

    @Test
    public void onStartup() throws Exception {
        String temp =  "{\"channelId\":\"010\",\"codeList\":[\"1FMA3324Y11-edward\"],\"cartId\":26,\"consumerRetryTimes\":0,\"mqId\":0,\"delaySecond\":0,\"sender\":\"edward\"}";
        AdvSearchRefreshRetailPriceMQMessageBody messageBody = JacksonUtil.json2Bean(temp, AdvSearchRefreshRetailPriceMQMessageBody.class);
//        MQConfigInitTestUtil.startMQ();
        cmsAdvSearchRefreshRetailPriceMQJob.onStartup(messageBody);
    }

}