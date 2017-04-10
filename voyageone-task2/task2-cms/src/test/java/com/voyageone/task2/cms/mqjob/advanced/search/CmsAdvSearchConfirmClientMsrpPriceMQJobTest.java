package com.voyageone.task2.cms.mqjob.advanced.search;

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
        AdvSearchConfirmClientMsrpPriceMQMessageBody advSearchConfirmClientMsrpPriceMQMessageBody = new AdvSearchConfirmClientMsrpPriceMQMessageBody();
        advSearchConfirmClientMsrpPriceMQMessageBody.setCodeList(Arrays.asList("68220-gem"));
        advSearchConfirmClientMsrpPriceMQMessageBody.setChannelId("001");
        advSearchConfirmClientMsrpPriceMQMessageBody.setSender("james");
        cmsAdvSearchConfirmClientMsrpPriceMQJob.onStartup(advSearchConfirmClientMsrpPriceMQMessageBody);
    }

}