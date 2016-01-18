package com.voyageone.batch.cms.service.feed;

import com.voyageone.batch.core.modelbean.TaskControlBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author james.li on 2016/1/15.
 * @version 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class JewelryAnalysisServiceTest {

    @Autowired
    private JewelryAnalysisService jewelryAnalysisService;
    @Test
    public void testOnStartup() throws Exception {
        jewelryAnalysisService.onStartup(new ArrayList<TaskControlBean>());
    }
}