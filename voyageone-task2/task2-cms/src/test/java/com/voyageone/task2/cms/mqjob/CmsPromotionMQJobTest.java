package com.voyageone.task2.cms.mqjob;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsPromotionMQMessageBody;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by james on 2017/5/2.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsPromotionMQJobTest {

    @Autowired
    CmsPromotionMQJob cmsPromotionMQJob;
    @Test
    public void onStartup() throws Exception {

        String str = "{\"consumerRetryTimes\":0,\"mqId\":0,\"delaySecond\":0,\"sender\":\"james\",\"channelId\":\"010\",\"promotionId\":820,\"numIidList\":null,\"triggerTime\":null,\"subBeanName\":\"010\"}";
        CmsPromotionMQMessageBody body = JacksonUtil.json2Bean(str, CmsPromotionMQMessageBody.class);
        cmsPromotionMQJob.onStartup(body);
    }

}