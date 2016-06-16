package com.voyageone.task2.cms.service;

import com.voyageone.service.impl.com.mq.MqSender;
import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author james.li on 2016/6/16.
 * @version 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsBuildPaltformCategoryTreeJMMqServiceTest {

//    @Autowired
//    CmsBuildPaltformCategoryTreeJMMqService cmsBuildPaltformCategoryTreeJMMqService;

    @Autowired
    private MqSender sender;

    @Test
    public void testOnStartup() throws Exception {

        Map<String,Object> messageMap = new HashMap<>();
        messageMap.put("channelId","010");
        sender.sendMessage(MqRoutingKey.CMS_BATCH_PlatformCategorySchemaJMJob, messageMap);
//        cmsBuildPaltformCategoryTreeJMMqService.onStartup(messageMap);
    }
}