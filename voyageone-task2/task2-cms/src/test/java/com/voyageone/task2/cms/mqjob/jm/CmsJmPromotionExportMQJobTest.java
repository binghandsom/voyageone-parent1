package com.voyageone.task2.cms.mqjob.jm;
import com.voyageone.common.configs.MQConfigInitTestUtil;
import com.voyageone.task2.cms.mqjob.jm.CmsJmPromotionExportMQJob;
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
public class CmsJmPromotionExportMQJobTest {


    @Autowired
    CmsJmPromotionExportMQJob service;
    @Test
    public void testOnStartup() throws Exception {
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
