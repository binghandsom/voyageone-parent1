package com.voyageone.task2.cms.service;

import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by dell on 2016/10/25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsPlatformTitleTranslateMqServiceTest {

    @Autowired
    private CmsPlatformTitleTranslateMqService cmsPlatformTitleTranslateMqService;
    @Autowired
    private SxProductService sxProductService;

    @Test
    public void testOnStartup() throws Exception {

        String channelId = "928";
        String cartId = "28";
        String code = "ML565BL_D";

        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("runType", "1");
        messageMap.put("channelId", channelId);
        messageMap.put("cartId", cartId);
        messageMap.put("code", code);
        cmsPlatformTitleTranslateMqService.onStartup(messageMap);

//        SxData sxData = sxProductService.getSxDataByCodeWithoutCheck(channelId, Integer.valueOf(cartId), code, false);
//        ExpressionParser expressionParser = new ExpressionParser(sxProductService, sxData);

    }
}