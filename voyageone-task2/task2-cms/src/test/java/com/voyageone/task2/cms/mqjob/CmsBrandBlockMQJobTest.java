package com.voyageone.task2.cms.mqjob;

import com.voyageone.common.configs.MQConfigInitTestUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsBrandBlockMQJobTest {

    @Autowired
    CmsBrandBlockMQJob service;

    @Test
    public void testOnStartup() throws Exception {
        MQConfigInitTestUtil.startMQ(service);
    }
}
