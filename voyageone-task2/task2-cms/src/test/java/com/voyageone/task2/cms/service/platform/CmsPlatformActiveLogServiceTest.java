package com.voyageone.task2.cms.service.platform;

import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.com.mq.MqSender;
import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jason.jiang on 2016/07/24.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsPlatformActiveLogServiceTest {

    @Autowired
    private MqSender sender;

    @Test
    public void testPlatformActiveLog() {
        // 发送请求到MQ,插入操作历史记录
        Map<String, Object> logParams = new HashMap<>(6);
        logParams.put("channelId", "010");
        logParams.put("cartIdList", new int[]{23});
        logParams.put("activeStatus", "ToOnSale");
        logParams.put("creater", "will2");
        logParams.put("comment", "高级检索 批量上下架");
        logParams.put("codeList", new String[]{"15596:SZ65","15596:SZ75"});
        sender.sendMessage(MqRoutingKey.CMS_TASK_PlatformActiveLogJob, logParams);
    }
}