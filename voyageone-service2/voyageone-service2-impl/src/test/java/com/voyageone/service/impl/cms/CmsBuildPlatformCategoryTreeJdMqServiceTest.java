package com.voyageone.service.impl.cms;

import com.voyageone.service.impl.com.mq.MqSender;
import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-service2.xml")
public class CmsBuildPlatformCategoryTreeJdMqServiceTest {

    @Autowired
    private MqSender sender;

    @Test
    public void testSendMessage() throws Exception {
        Map<String,Object> message=new HashMap<>();
        message.put("test","111");
        sender.sendMessage(MqRoutingKey.CMS_BATCH_PlatformCategoryTreeJdJob, message);
    }

}