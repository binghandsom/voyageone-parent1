package com.voyageone.task2.cms.service.feed;

import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;

/**
 * @author james.li on 2016/5/4.
 * @version 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class OverStockAnalysisServiceTest {
    @Autowired
    OverStockAnalysisService overStockAnalysisService;

    @Test
    public void testOnStartup() throws Exception {
        overStockAnalysisService.onStartup(new ArrayList<TaskControlBean>());
    }

    @Test
    public void testOnStartup2() throws Exception {
        overStockAnalysisService.getEventProduct();
    }
}