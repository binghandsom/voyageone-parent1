package com.voyageone.task2.cms.mqjob.jm;

import com.voyageone.common.configs.MQConfigInitTestUtil;
import com.voyageone.task2.cms.mqjob.jm.CmsJmPromotionImportMQJob;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by dell on 2016/7/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsJmPromotionImportMQJobTest {
    @Autowired
    CmsJmPromotionImportMQJob serviceJob;

    //service.importFile(25, "/usr/web/contents/cms/jumei_sx/import");
    @Test
    public void testOnStartup() throws InterruptedException {
        MQConfigInitTestUtil.startMQ(serviceJob);
    }
}