package com.voyageone.task2.cms.service.feed;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;

/**
 * Created by gjl on 2017/1/4.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class WmfPriceAnalysisServiceTest {
    @Autowired
    WmfPriceAnalysisService wmfPriceAnalysisService;
    @Test
    public void testOnStartup() throws Exception {
        wmfPriceAnalysisService.onStartup(new ArrayList<>());
    }
}

