package com.voyageone.task2.cms.service.promotion.stock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by jeff.duan on 16/3/31.
 *
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class StockIncrementSeparateServiceTest {

    @Autowired
    private StockIncrementService stockIncrementSeparateService;

    @Test
    public void testStartup() {
        stockIncrementSeparateService.startup();
    }
}