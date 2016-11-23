package com.voyageone.service.impl.cms.jumei;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by james on 2016/10/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-service2.xml")
public class CmsBtJmImageTemplateServiceTest {
    @Autowired
    CmsBtJmImageTemplateService cmsBtJmImageTemplateService;
    @Test
    public void getUrl() throws Exception {
        cmsBtJmImageTemplateService.getUrl("dddd","appEntrance",111);
    }

}