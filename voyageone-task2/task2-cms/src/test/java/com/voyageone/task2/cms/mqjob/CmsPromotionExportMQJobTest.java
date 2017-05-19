package com.voyageone.task2.cms.mqjob;

import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsPromotionExportMQMessageBody;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 活动导出Job测试
 *
 * @Author rex.wu
 * @Create 2017-05-18 16:24
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsPromotionExportMQJobTest {

    @Autowired
    private CmsPromotionExportMQJob cmsPromotionExportMQJob;

    @Test
    public void testExport() {
        CmsPromotionExportMQMessageBody messageBody = new CmsPromotionExportMQMessageBody();

        messageBody.setSender("edward");
        messageBody.setChannelId("001");
        messageBody.setCmsPromotionExportTaskId(4);
        try {
            cmsPromotionExportMQJob.onStartup(messageBody);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
