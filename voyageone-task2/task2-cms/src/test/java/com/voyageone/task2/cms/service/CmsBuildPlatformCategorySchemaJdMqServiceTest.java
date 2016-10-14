package com.voyageone.task2.cms.service;

import com.voyageone.common.configs.beans.ShopBean;
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
//        int cartId = 29;  // 京东国际Liking悦境店铺

        ShopBean shopProp = new ShopBean();
        shopProp.setApp_url("https://api.jd.com/routerjson");
        shopProp.setAppKey("");
        shopProp.setAppSecret("");
        shopProp.setSessionKey("");   // 28 匠心界
//        shopProp.setSessionKey("");   // 29 悦境
        // platformid一定要设成京东，否则默认为天猫（1）的话，expressionParser.parse里面会上传照片到天猫空间，出现异常
        shopProp.setPlatform_id("2");

        cmsBuildPlatformCategorySchemaJdMqService.doSetPlatformJdSchemaCommon(shopProp, cartId);
    }


}