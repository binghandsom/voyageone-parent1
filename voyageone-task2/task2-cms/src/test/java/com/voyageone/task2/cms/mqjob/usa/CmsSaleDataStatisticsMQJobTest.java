package com.voyageone.task2.cms.mqjob.usa;

import com.voyageone.service.impl.cms.vomq.vomessage.body.usa.CmsSaleDataStatisticsMQMessageBody;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by james on 2017/7/20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsSaleDataStatisticsMQJobTest {
    @Autowired
    CmsSaleDataStatisticsMQJob cmsSaleDataStatisticsMQJob;

    @Test
    public void onStartup() throws Exception {
        CmsSaleDataStatisticsMQMessageBody cmsSaleDataStatisticsMQMessageBody = new CmsSaleDataStatisticsMQMessageBody();
        cmsSaleDataStatisticsMQMessageBody.setCartId(27);
        cmsSaleDataStatisticsMQMessageBody.setChannelId("001");
        cmsSaleDataStatisticsMQMessageBody.setStartDate("2017-01-01");
        cmsSaleDataStatisticsMQMessageBody.setEndDate("2017-08-01");
        cmsSaleDataStatisticsMQJob.onStartup(cmsSaleDataStatisticsMQMessageBody);

//        cmsSaleDataStatisticsMQMessageBody.setCartId(23);
//        cmsSaleDataStatisticsMQJob.onStartup(cmsSaleDataStatisticsMQMessageBody);
//
//        cmsSaleDataStatisticsMQMessageBody.setCartId(26);
//        cmsSaleDataStatisticsMQJob.onStartup(cmsSaleDataStatisticsMQMessageBody);

//        cmsSaleDataStatisticsMQMessageBody.setCartId(0);
//        cmsSaleDataStatisticsMQJob.onStartup(cmsSaleDataStatisticsMQMessageBody);
    }

}