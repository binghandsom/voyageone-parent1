package com.voyageone.task2.cms.service.feed;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by jonasvlag on 16/3/31.
 *
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class SEAnalysisServiceTest {

    @Autowired
    private SEAnalysisService seAnalysisService;

    @Test
    public void testStartup() {
        seAnalysisService.startup();
    }
}