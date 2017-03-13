package com.voyageone.task2.cms.service.product;

import com.voyageone.task2.base.dao.TaskDao;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author Edward
 * @version 2.0.0, 2017/3/8
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsCheckProductIsRightServiceTest {

    @Autowired
    CmsCheckProductIsRightService service;
    @Autowired
    TaskDao taskDao;

    @Test
    public void testOnStartup() throws Exception {
        List<TaskControlBean> taskControlList = taskDao.getTaskControlList("CmsCheckProductIsRightJob");
        service.onStartup(taskControlList);
    }
}