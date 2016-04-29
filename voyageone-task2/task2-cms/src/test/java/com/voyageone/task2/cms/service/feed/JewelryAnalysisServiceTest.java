package com.voyageone.task2.cms.service.feed;

import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.cms.service.feed.JewelryAnalysisService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;

/**
 * @author james.li on 2016/1/15.
 * @version 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class JewelryAnalysisServiceTest {

    @Autowired
    private JewelryAnalysisService jewelryAnalysisService;

    @Autowired
    JewelryAnalysis2Service jewelryAnalysis2Service;
    @Test
    public void testOnStartup() throws Exception {
        jewelryAnalysisService.onStartup(new ArrayList<TaskControlBean>());
    }

    @Test
    public void testOnStartup2() throws Exception {
        jewelryAnalysis2Service.onStartup(new ArrayList<TaskControlBean>());
    }
}