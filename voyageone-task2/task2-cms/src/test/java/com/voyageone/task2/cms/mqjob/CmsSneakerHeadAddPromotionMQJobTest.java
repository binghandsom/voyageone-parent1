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
    @Test
    public void onStartup1() throws Exception {

    }

    @Test
    public void addPromotionDetail() throws Exception {
//        cmsSneakerHeadAddPromotionMQJob.addPromotionDetail("010", 23, "00395", 807, "james");
    }

    @Autowired
    CmsSneakerHeadAddPromotionMQJob cmsSneakerHeadAddPromotionMQJob;
    @Test
    public void onStartup() throws Exception {
        CmsSneakerHeadAddPromotionMQMessageBody cmsSneakerHeadAddPromotionMQMessageBody = new CmsSneakerHeadAddPromotionMQMessageBody();
        cmsSneakerHeadAddPromotionMQMessageBody.setPromotionId(809);
        cmsSneakerHeadAddPromotionMQJob.onStartup(cmsSneakerHeadAddPromotionMQMessageBody);
    }
}