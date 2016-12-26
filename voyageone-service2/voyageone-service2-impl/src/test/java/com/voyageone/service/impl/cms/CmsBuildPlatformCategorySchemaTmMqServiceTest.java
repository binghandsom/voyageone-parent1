package com.voyageone.service.impl.cms;

import com.voyageone.common.util.CommonUtil;
import com.voyageone.service.impl.com.mq.MqSender;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-service2.xml")
public class CmsBuildPlatformCategorySchemaTmMqServiceTest {

    @Autowired
    private MqSender sender;

    @Test
    public void testSendMessage() throws Exception {
        List<Integer> list=new ArrayList<>();
        for (int i=0;i<40;i++)
        {
            list.add(i);
        }
       List<List<Integer>> pageList= CommonUtil.splitList(list,20);
        Map<String,Object> message=new HashMap<>();
        message.put("test","111");
        sender.sendMessage(CmsMqRoutingKey.CMS_BATCH_PlatformCategorySchemaTmJob, message);
    }

}