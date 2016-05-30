package com.voyageone.task2.cms.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by lewis on 15-12-24.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class GetPlatformCategoryTreesServiceTest {

    @Autowired
    private GetPlatformCategoryTreesService getPlatformCategoryTreesService;

    @Test
    public void testOnStartup() throws Exception {

        getPlatformCategoryTreesService.startup();

    }
}