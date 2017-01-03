package com.voyageone.task2.cms.service.jumei;

/**
 * Created by dell on 2016/12/30.
 */

import com.voyageone.common.configs.MQConfigInitTestUtil;
import com.voyageone.task2.cms.mqjob.jm.CmsJMProductUpdateMQJob;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsJMProductUpdateMQJobTest {

    @Autowired
    CmsJMProductUpdateMQJob service;
    @Test
    public void testOnStartup() throws Exception {


        //service.onStartup(map2);
        MQConfigInitTestUtil.startMQ(service);
    }
}
