package com.voyageone.batch.cms.service;

import com.sun.jersey.api.spring.Autowire;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.common.components.jumei.JumeiImageFileService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.SystemProfileValueSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author aooer 2016/1/26.
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsUploadJmPicServiceTest {

    @Autowired
    CmsUploadJmPicService cmsUploadJmPicService;

    @Test
    public void testOnStartup() throws Exception {
        long start= System.currentTimeMillis();
        List<TaskControlBean> taskControlList = new ArrayList<>();
        cmsUploadJmPicService.onStartup(taskControlList);
        System.out.println("耗时："+(System.currentTimeMillis()-start));
    }
}