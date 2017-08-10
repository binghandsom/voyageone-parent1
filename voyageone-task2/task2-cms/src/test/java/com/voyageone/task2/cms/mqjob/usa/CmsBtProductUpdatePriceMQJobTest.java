package com.voyageone.task2.cms.mqjob.usa;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.impl.cms.vomq.vomessage.body.usa.CmsBtProductUpdatePriceMQMessageBody;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by dell on 2017/7/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsBtProductUpdatePriceMQJobTest {

    @Autowired
    CmsBtProductUpdatePriceMQJob cmsBtProductUpdatePriceMQJob;
    @Test
    public void testOnStartup() throws Exception {
        CmsBtProductUpdatePriceMQMessageBody map = new CmsBtProductUpdatePriceMQMessageBody();
        map.setChannelId("001");
        map.setCartId(5);
        map.setProductCodes(Collections.singletonList("8125115S.RED"));
        Map<String, Object> paraMap = new HashMap<>();
        paraMap.put("changedPriceType","clientRetailPrice");
        paraMap.put("basePriceType","clientRetailPrice");
        paraMap.put("optionType","/");
        paraMap.put("value","100");
        paraMap.put("flag","1");
        map.setParams(paraMap);

        String s = "{\"consumerRetryTimes\":0,\"mqId\":0,\"delaySecond\":0,\"sender\":\"test\",\"channelId\":\"001\",\"productCodes\":[\"68220-gem\"],\"cartId\":0,\"params\":{\"optionType\":\"*\",\"flag\":\"1\",\"changedPriceType\":\"clientRetailPrice\",\"value\":\"10\",\"basePriceType\":\"clientRetailPrice\"}}";
        CmsBtProductUpdatePriceMQMessageBody body = JacksonUtil.json2Bean(s, CmsBtProductUpdatePriceMQMessageBody.class);

        cmsBtProductUpdatePriceMQJob.onStartup(body);

    }
}