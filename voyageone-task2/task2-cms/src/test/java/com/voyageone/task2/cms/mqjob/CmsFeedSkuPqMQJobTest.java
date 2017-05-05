package com.voyageone.task2.cms.mqjob;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsFeedSkuPqMQMessageBody;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by 123 on 2017/3/22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsFeedSkuPqMQJobTest {

    @Autowired
    CmsFeedSkuPqMQJob cmsFeedSkuPqMQJob;

    @Test
    public void onStartup() throws Exception {
        String msg1 = "{\"skuList\":[{\"clientSku\":\"TestSKU100014676\",\"qty\":20,\"isSale\":1,\"attribute\":{}},{\"clientSku\":\"TestSKU100014677\",\"qty\":30,\"isSale\":1,\"attribute\":{}}],\"consumerRetryTimes\":0,\"mqId\":0,\"delaySecond\":0,\"sender\":\"VmsGetCaClientInvJob\",\"channelId\":\"010\"}";

        CmsFeedSkuPqMQMessageBody message = JacksonUtil.json2Bean(msg1, CmsFeedSkuPqMQMessageBody.class);
        cmsFeedSkuPqMQJob.onStartup(message);
    }


}
