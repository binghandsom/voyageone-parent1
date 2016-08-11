package com.voyageone.task2.cms.service.feed;

import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.Exception;import java.util.ArrayList;

/**
 * @author gump on 2016/08/09.
 * @version 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class ModotexAnalysisServiceTest {
    @Autowired
    ModotexAnalysisService modotexAnalysisService;

    @Test
    public void testOnStartup() throws Exception {
        modotexAnalysisService.onStartup(new ArrayList<TaskControlBean>());
    }
}