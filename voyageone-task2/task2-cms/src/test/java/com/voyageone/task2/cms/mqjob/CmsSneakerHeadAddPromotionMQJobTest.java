package com.voyageone.task2.cms.mqjob;

import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsSneakerHeadAddPromotionMQMessageBody;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by james on 2017/3/28.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsSneakerHeadAddPromotionMQJobTest {
    @Autowired
    CmsSneakerHeadAddPromotionMQJob cmsSneakerHeadAddPromotionMQJob;
    @Test
    public void onStartup() throws Exception {
        CmsSneakerHeadAddPromotionMQMessageBody cmsSneakerHeadAddPromotionMQMessageBody = new CmsSneakerHeadAddPromotionMQMessageBody();
        cmsSneakerHeadAddPromotionMQMessageBody.setPromotionId(9);
        cmsSneakerHeadAddPromotionMQJob.onStartup(cmsSneakerHeadAddPromotionMQMessageBody);
    }
}