package com.voyageone.task2.cms.service;

import com.voyageone.task2.cms.service.CmsSetMainPropMongoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by zhujiaye on 15/12/21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsSetMainPropMongoServiceTest {

    @Autowired
    private CmsSetMainPropMongoService cmsSetMainPropMongoService;

    @Test
    public void testOnStartup() throws Exception {
        cmsSetMainPropMongoService.startup();
    }
}