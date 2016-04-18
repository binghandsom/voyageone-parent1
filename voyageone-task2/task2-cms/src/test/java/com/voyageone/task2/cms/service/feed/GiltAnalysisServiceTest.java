package com.voyageone.task2.cms.service.feed;

import com.voyageone.components.gilt.service.GiltSkuService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Jonas, 2/3/16.
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class GiltAnalysisServiceTest {

    @Autowired
    private GiltAnalysisService giltAnalysisService;

    @Autowired
    private GiltSkuService giltSkuService;

    @Test
    public void testOnStartup() throws Exception {
        giltAnalysisService.startup();
    }
}