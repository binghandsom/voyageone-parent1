package com.voyageone.task2.cms.mqjob;

import com.voyageone.common.configs.MQConfigInitTestUtil;
import com.voyageone.service.impl.cms.vomq.vomessage.body.AdvSearchExportMQMessageBody;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rex on 2017/1/5.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsAdvSearchExportMQJobTest {

    @Autowired
    private CmsAdvSearchExportMQJob cmsAdvSearchExportMQJob;

    @Test
    public void onStartup() throws Exception {

        MQConfigInitTestUtil.startMQ(cmsAdvSearchExportMQJob);

    }

}