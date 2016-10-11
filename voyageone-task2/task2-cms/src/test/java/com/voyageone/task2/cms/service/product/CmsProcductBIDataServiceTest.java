package com.voyageone.task2.cms.service.product;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jason.jiang on 2016/08/30
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsProcductBIDataServiceTest {

    @Autowired
    CmsProcductBIDataService targetService;

    @Test
    public void testTMPlatform() {
        Map<String, Object> sqlParams = new HashMap<>(2);
        sqlParams.put("channelId", "929");
        sqlParams.put("cartId", 29);

        try {
            targetService.onStartup(sqlParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}