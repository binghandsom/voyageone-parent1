package com.voyageone.batch.cms.service;

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
 * @author james.li on 2016/1/26.
 * @version 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsUploadJmProductServiceTest {

    @Autowired
    CmsUploadJmProductService cmsUploadJmProductService;
    @Test
    public void testOnStartup() throws Exception {
        List<TaskControlBean> taskControlList = new ArrayList<>();
        cmsUploadJmProductService.onStartup(taskControlList);
    }

    @Test
    public void testGetTime() throws Exception {

        Long a=CmsUploadJmProductService.getTime("2016-03-10 10:00:00");
        System.out.println(a);
    }
}