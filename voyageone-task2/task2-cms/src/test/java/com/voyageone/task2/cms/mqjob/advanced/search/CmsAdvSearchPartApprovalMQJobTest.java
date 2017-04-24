package com.voyageone.task2.cms.mqjob.advanced.search;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.impl.cms.vomq.vomessage.body.AdvSearchPartApprovalMQMessageBody;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by james on 2017/4/21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsAdvSearchPartApprovalMQJobTest {

    @Autowired
    CmsAdvSearchPartApprovalMQJob cmsAdvSearchPartApprovalMQJob;

    @Test
    public void onStartup() throws Exception {
        String json ="{\"consumerRetryTimes\":0,\"mqId\":0,\"delaySecond\":0,\"sender\":\"james\",\"channelId\":\"018\",\"codeList\":[\"14009927\",\"14009928\",\"14009929-PURPLE\"],\"cartId\":23,\"platformWorkloadAttributes\":[\"description\",\"title\",\"item_images\"],\"subBeanName\":\"018\"}";
        cmsAdvSearchPartApprovalMQJob.onStartup(JacksonUtil.json2Bean(json, AdvSearchPartApprovalMQMessageBody.class));
    }

}