package com.voyageone.task2.cms.service;

import com.voyageone.common.mq.MqSender;
import com.voyageone.common.mq.enums.MqRoutingKey;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by desmond on 2016/4/12.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class CmsBuildPlatformCategorySchemaJdMqServiceTest {

    @Autowired
    private MqSender sender;

    @Test
    public void testSendMessage() throws Exception {
        Map<String,Object> message=new HashMap<String,Object>();
        message.put("test","111");
        sender.sendMessage(MqRoutingKey.CMS_BATCH_PlatformCategorySchemaJdJob, message);
    }
}