package com.voyageone.task2.cms.service;

import com.voyageone.common.util.CommonUtil;
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
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsBuildPlatformCategorySchemaJdMqServiceTest {

//    @Autowired
//    private MqSender sender;

    @Autowired
    private CmsBuildPlatformCategorySchemaJdMqService cmsBuildPlatformCategorySchemaJdMqService;

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
//        sender.sendMessage(MqRoutingKey.CMS_BATCH_PlatformCategorySchemaJdJob, message);
    }

    @Test
    public void testDoSetPlatformJdSchemaCommon() throws Exception {
        int cartId = 28;  // 京东国际Liking匠心界店铺

        cmsBuildPlatformCategorySchemaJdMqService.doSetPlatformJdSchemaCommon(cartId);
    }


}