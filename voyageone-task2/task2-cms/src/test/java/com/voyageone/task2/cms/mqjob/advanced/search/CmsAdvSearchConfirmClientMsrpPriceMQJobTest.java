package com.voyageone.task2.cms.mqjob.advanced.search;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.impl.cms.vomq.vomessage.body.AdvSearchConfirmClientMsrpPriceMQMessageBody;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by james on 2017/4/10.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsAdvSearchConfirmClientMsrpPriceMQJobTest {

    @Autowired
    CmsAdvSearchConfirmClientMsrpPriceMQJob cmsAdvSearchConfirmClientMsrpPriceMQJob;

    @Test
    public void onStartup() throws Exception {
        String aa="{\"consumerRetryTimes\":0,\"mqId\":0,\"delaySecond\":0,\"sender\":\"james\",\"channelId\":\"001\",\"codeList\":[\"68220-lag\"]}";
        AdvSearchConfirmClientMsrpPriceMQMessageBody advSearchConfirmClientMsrpPriceMQMessageBody = new AdvSearchConfirmClientMsrpPriceMQMessageBody();
//        advSearchConfirmClientMsrpPriceMQMessageBody.setCodeList(Arrays.asList("68220-gem"));
//        advSearchConfirmClientMsrpPriceMQMessageBody.setChannelId("001");
//        advSearchConfirmClientMsrpPriceMQMessageBody.setSender("james");
        advSearchConfirmClientMsrpPriceMQMessageBody = JacksonUtil.json2Bean(aa, AdvSearchConfirmClientMsrpPriceMQMessageBody.class);
        cmsAdvSearchConfirmClientMsrpPriceMQJob.onStartup(advSearchConfirmClientMsrpPriceMQMessageBody);
    }

}