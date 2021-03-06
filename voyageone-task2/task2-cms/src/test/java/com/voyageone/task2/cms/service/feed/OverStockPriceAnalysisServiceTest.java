package com.voyageone.task2.cms.service.feed;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * @author james.li on 2016/8/8.
 * @version 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class OverStockPriceAnalysisServiceTest {

    @Autowired
    OverStockPriceAnalysisService overStockPriceAnalysisService;
    @Test
    public void testOnStartup() throws Exception {
        overStockPriceAnalysisService.onStartup(new ArrayList<>());
    }
}