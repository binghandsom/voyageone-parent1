package com.voyageone.task2.cms.mqjob.advanced.search;

import com.voyageone.common.configs.MQConfigInitTestUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Edward
 * @version 2.0.0, 2017/2/9
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsSaveChannelCategoryMQJobTest {

    @Autowired
    CmsSaveChannelCategoryMQJob cmsSaveChannelCategoryMQJob;

    @Test
    public void testOnStartup() throws Exception {
        MQConfigInitTestUtil.startMQ(cmsSaveChannelCategoryMQJob);
    }
}