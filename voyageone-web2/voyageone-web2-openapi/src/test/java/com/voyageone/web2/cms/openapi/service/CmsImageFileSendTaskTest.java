package com.voyageone.web2.cms.openapi.service;

import com.voyageone.service.impl.com.mq.MqSender;
import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import com.voyageone.service.impl.com.mq.handler.MqAdminHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class CmsImageFileSendTaskTest {

    @Autowired
    private MqSender sender;

    @Test
    public void test() throws Exception {
//        for (int i=16219; i>0; i--) {
//            Map<String, Object> map = new HashMap<>();
//            map.put("id", i);
//            sender.sendMessage(MqRoutingKey.CMS_BATCH_CmsMtImageCreateTaskJob, map);
//        }

        Map<String, Object> map = new HashMap<>();
        map.put("id", 20478);
        sender.sendMessage(MqRoutingKey.CMS_BATCH_CmsMtImageCreateTaskJob, map);
    }

    @Autowired
    private MqAdminHandler adminHandler;

    @Test
    public void testGetCount() {
        adminHandler.getQueueCount(MqRoutingKey.CMS_BATCH_CmsMtImageCreateTaskJob);
    }
}
