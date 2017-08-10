package com.voyageone.task2.cms.mqjob.usa;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.impl.cms.vomq.vomessage.body.usa.CmsBtProductUpdateListDelistStatusMQMessageBody;
import com.voyageone.service.impl.cms.vomq.vomessage.body.usa.CmsBtProductUpdatePriceMQMessageBody;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;

import static org.junit.Assert.*;

/**
 * Created by dell on 2017/7/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsBtProductUpdateListDelistStatusMQJobTest {
    @Autowired
    CmsBtProductUpdateListDelistStatusMQJob cmsBtProductUpdateListDelistStatusMQJob;

    @Test
    public void testOnStartup() throws Exception {

        CmsBtProductUpdateListDelistStatusMQMessageBody map = new CmsBtProductUpdateListDelistStatusMQMessageBody();

        map.setCartId(8);
        map.setChannelId("001");
        //下架
        map.setActiveStatus("list");
        map.setProductCodes(Collections.singletonList("609973-460"));
        map.setDays(5);

        String s = "{\"consumerRetryTimes\":0,\"mqId\":0,\"delaySecond\":0,\"sender\":\"test\",\"channelId\":\"001\",\"cartId\":8,\"activeStatus\":\"list\",\"productCodes\":[\"68220-gem\"],\"days\":0}";
        CmsBtProductUpdateListDelistStatusMQMessageBody body = JacksonUtil.json2Bean(s, CmsBtProductUpdateListDelistStatusMQMessageBody.class);

        cmsBtProductUpdateListDelistStatusMQJob.onStartup(body);

    }
}