package com.voyageone.task2.cms.service.jumei;

import com.voyageone.common.configs.MQConfigInitTestUtil;
import com.voyageone.task2.cms.mqjob.jm.CmsJmPromotionPriceRefreshMQJob;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by dell on 2017/1/3.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsJmPromotionPriceRefreshMQJobTest {
    @Autowired
    CmsJmPromotionPriceRefreshMQJob serviceJob;

    @Test
    public void testOnStartup() throws InterruptedException {
        MQConfigInitTestUtil.startMQ(serviceJob);
    }
}