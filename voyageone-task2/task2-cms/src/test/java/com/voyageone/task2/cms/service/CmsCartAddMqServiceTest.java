package com.voyageone.task2.cms.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by james on 2016/12/9.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsCartAddMqServiceTest {

    @Autowired
    CmsCartAddMqService cmsCartAddMqService;
    @Test
    public void onStartup(){
        Map<String,Object> map = new HashMap<>();
        map.put("channelId","010");
        map.put("cartId",928);

        try {
            cmsCartAddMqService.onStartup(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}