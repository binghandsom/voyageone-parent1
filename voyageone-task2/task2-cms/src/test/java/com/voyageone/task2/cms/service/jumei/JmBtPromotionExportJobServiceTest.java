package com.voyageone.task2.cms.service.jumei;

import com.voyageone.common.configs.MQConfigInitTestUtil;
import com.voyageone.service.impl.cms.jumei2.CmsBtJmPromotionImportTask3Service;
import com.voyageone.task2.cms.mqjob.CmsJmPromotionExportMQJob;
import org.apache.commons.collections.map.HashedMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

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


    //service.onStartup(map2);
        MQConfigInitTestUtil.startMQ(service);
    }

    public static void main(String[] arg)
    {
       // Throwable ex = new Throwable();
       // logger.info("at com.voyageone.service.impl.cms.vomq.CmsMqSenderService.sendMessage(CmsMqSenderService.java:37)");
       // logger.info("at com.voyageone.service.impl.cms.vomq.CmsMqSenderService.sendMessage(CmsMqSenderService.java:31)");
       // logger.info("at com.voyageone.service.impl.cms.vomq.CmsMqSenderService.sendMessage(CmsMqSenderService.java:32)");

        System.out.println("at com.voyageone.service.impl.cms.vomq.CmsMqSenderService.sendMessage(CmsMqSenderService.java:30)");
        System.out.println("com.voyageone.task2.cms.service.jumei.JmBtPromotionExportJobServiceTest.main(JmBtPromotionExportJobServiceTest.java:41)");

        System.out.println("com.voyageone.task2.cms.service.jumei.JmBtPromotionExportJobServiceTest.main(JmBtPromotionExportJobServiceTest.java:40)");

        //StackTraceElement[] stackElements = ex.getStackTrace();

    }
}
