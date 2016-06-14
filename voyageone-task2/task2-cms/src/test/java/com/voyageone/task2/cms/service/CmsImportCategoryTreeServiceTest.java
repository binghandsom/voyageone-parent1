package com.voyageone.task2.cms.service;

import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeff.duan on 16/6/12.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsImportCategoryTreeServiceTest {

    @Autowired
    private CmsImportCategoryTreeService cmsImportCategoryTreeService;

    @Test
    public void testOnStartup() throws Exception {
        List<TaskControlBean> taskControlList = new ArrayList<>();
        cmsImportCategoryTreeService.onStartup(taskControlList);
    }
}