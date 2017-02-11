package com.voyageone.task2.cms.mqjob;

import com.voyageone.common.configs.MQConfigInitTestUtil;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by rex on 2017/1/5.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsFeedExportMQJobTest {

    @Autowired
    private CmsFeedExportMQJob cmsFeedExportMQJob;

    @Test
    public void onStartup() throws Exception {
        MQConfigInitTestUtil.startMQ(cmsFeedExportMQJob);
    }

}