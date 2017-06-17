package com.voyageone.task2.cms.mqjob.advanced.search;

import com.voyageone.common.configs.MQConfigInitTestUtil;
import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsBatchUpdateProductTitleMQMessageBody;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by rex.wu on 2017/5/23.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsBatchUpdateProductTitleMQJobTest {

    @Autowired
    private CmsBatchUpdateProductTitleMQJob cmsBatchUpdateProductTitleMQJob;

    @Test
    public void onStartup() throws Exception {

        CmsBatchUpdateProductTitleMQMessageBody mqMessageBody = new CmsBatchUpdateProductTitleMQMessageBody();
        mqMessageBody.setChannelId("928");
        mqMessageBody.setProductCodes(Arrays.asList("144196"));
        mqMessageBody.setTitle("-616");
        mqMessageBody.setTitlePlace("suffix");
        mqMessageBody.setSender("rex");
        cmsBatchUpdateProductTitleMQJob.onStartup(mqMessageBody);

//        MQConfigInitTestUtil.startMQ(cmsBatchUpdateProductTitleMQJob);
    }

}