package com.voyageone.task2.cms.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by james on 2017/6/13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsBuildPaltformCategoryTreeKLMqServiceTest {

    @Autowired
    CmsBuildPaltformCategoryTreeKLMqService cmsBuildPaltformCategoryTreeKLMqService;
    @Test
    public void onStartup() throws Exception {
        Map<String,Object> messageMap = new HashMap<>();
        messageMap.put("channelId","001");
        cmsBuildPaltformCategoryTreeKLMqService.onStartup(messageMap);
    }

}