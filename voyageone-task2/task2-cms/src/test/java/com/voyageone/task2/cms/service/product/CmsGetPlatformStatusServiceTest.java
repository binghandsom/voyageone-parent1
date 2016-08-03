package com.voyageone.task2.cms.service.product;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by jason.jiang on 2016/08/03
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsGetPlatformStatusServiceTest {

    @Autowired
    CmsGetPlatformStatusService targetService;

    @Test
    public void testOnStartup() {
        targetService.startup();
    }
}