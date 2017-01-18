package com.voyageone.task2.cms.mqjob;

import com.voyageone.common.configs.MQConfigInitTestUtil;

import com.voyageone.task2.cms.mqjob.advanced.search.CmsAdvSearchProductApprovalMQJob;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by rex on 2017/1/13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmdAdvSearchProductApprovalMQJobTest {

    @Autowired
    private CmsAdvSearchProductApprovalMQJob cmsAdvSearchProductApprovalMQJob;

    @Test
    public void onStartup() throws Exception {
        MQConfigInitTestUtil.startMQ(cmsAdvSearchProductApprovalMQJob);
    }

}