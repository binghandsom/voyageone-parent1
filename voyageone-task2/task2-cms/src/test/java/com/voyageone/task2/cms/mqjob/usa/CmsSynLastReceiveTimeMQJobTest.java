package com.voyageone.task2.cms.mqjob.usa;

import com.voyageone.service.impl.cms.vomq.vomessage.body.usa.CmsSynLastReceiveTimeMQMessageBody;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by james on 2017/7/12.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsSynLastReceiveTimeMQJobTest {

    @Autowired
    CmsSynLastReceiveTimeMQJob cmsSynLastReceiveTimeMQJob;
    @Test
    public void onStartup() throws Exception {
        CmsSynLastReceiveTimeMQMessageBody cmsSynLastReceiveTimeMQMessageBody = new CmsSynLastReceiveTimeMQMessageBody();
        cmsSynLastReceiveTimeMQMessageBody.setSender("aa");
        cmsSynLastReceiveTimeMQMessageBody.setCodes(Arrays.asList("68220-gem","68220-lag"));
        cmsSynLastReceiveTimeMQMessageBody.setReceiveTime(0L);
        cmsSynLastReceiveTimeMQJob.onStartup(cmsSynLastReceiveTimeMQMessageBody);
    }

}