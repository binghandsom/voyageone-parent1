package com.voyageone.task2.cms.mqjob;

import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsResetProductTitleMQMessageBody;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by james on 2017/6/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsResetProductTitleMQJobTest {

    @Autowired
    CmsResetProductTitleMQJob cmsResetProductTitleMQJob;
    @Test
    public void onStartup() throws Exception {
        CmsResetProductTitleMQMessageBody cmsResetProductTitleMQMessageBody = new CmsResetProductTitleMQMessageBody();
        cmsResetProductTitleMQMessageBody.setChannelId("928");
        cmsResetProductTitleMQMessageBody.setCodes(Arrays.asList("1127642188","1127642221"));
        cmsResetProductTitleMQJob.onStartup(cmsResetProductTitleMQMessageBody);
    }

}