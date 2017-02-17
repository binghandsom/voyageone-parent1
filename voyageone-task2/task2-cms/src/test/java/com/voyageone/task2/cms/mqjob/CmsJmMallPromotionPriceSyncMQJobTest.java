package com.voyageone.task2.cms.mqjob;

import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsJmMallPromotionPriceSyncMQMessageBody;
import com.voyageone.task2.cms.mqjob.jm.CmsJmMallPromotionPriceSyncMQJob;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by james on 2017/1/24.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsJmMallPromotionPriceSyncMQJobTest {

    @Autowired
    CmsJmMallPromotionPriceSyncMQJob cmsJmMallPromotionPriceSyncMQJobTest;

    @Test
    public void onStartup() throws Exception {
        CmsJmMallPromotionPriceSyncMQMessageBody  cmsJmMallPromotionPriceSyncMQMessageBody = new CmsJmMallPromotionPriceSyncMQMessageBody();
        cmsJmMallPromotionPriceSyncMQJobTest.onStartup(cmsJmMallPromotionPriceSyncMQMessageBody);
    }

}