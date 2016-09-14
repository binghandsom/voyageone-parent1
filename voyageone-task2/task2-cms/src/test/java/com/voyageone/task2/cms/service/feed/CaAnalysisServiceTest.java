package com.voyageone.task2.cms.service.feed;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author james.li on 2016/9/14.
 * @version 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CaAnalysisServiceTest {

    @Autowired
    CaAnalysisService caAnalysisService;
    @Test
    public void testOnStartup() throws Exception {
        Map<String,Object> message = new HashMap<>();
        message.put("channelId","996");
        message.put("sellerSKUs", Arrays.asList("REBEL X-WING","LIGHTSABER"));
        caAnalysisService.onStartup(message);
    }
}