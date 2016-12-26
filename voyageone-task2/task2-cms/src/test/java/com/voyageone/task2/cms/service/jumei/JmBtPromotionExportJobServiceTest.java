package com.voyageone.task2.cms.service.jumei;

import com.voyageone.common.configs.MQConfigInitTestUtil;
import com.voyageone.task2.cms.mqjob.CmsJmPromotionExportMQJob;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by dell on 2016/8/9.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class JmBtPromotionExportJobServiceTest {
    @Autowired
    CmsJmPromotionExportMQJob service;
    @Test
    public void testOnStartup() throws Exception {
        MQConfigInitTestUtil.startMQ(service);
    }
}
