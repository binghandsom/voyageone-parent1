package com.voyageone.task2.cms.service.feed;

import com.voyageone.task2.base.dao.TaskDao;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author james.li on 2016/3/21.
 * @version 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class Gilt2AnalysisServiceTest {

    @Autowired
    GiltAnalysisService gilt2AnalysisService;
    @Autowired
    private TaskDao taskDao;
    @Test
    public void testOnStartup() throws Exception {

        List<TaskControlBean> taskControlList = taskDao.getTaskControlList("Cms2GiltAnalysisJob");
        gilt2AnalysisService.onStartup(taskControlList);
    }
}