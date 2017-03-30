package com.voyageone.task2.cms.mqjob.advanced.search;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by james on 2017/3/20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsAdvSearchProductApprovalMQJobTest {

    @Autowired
    CmsAdvSearchProductApprovalMQJob cmsAdvSearchProductApprovalMQJob;
    @Test
    public void getAllSubBeanName() throws Exception {
        cmsAdvSearchProductApprovalMQJob.getAllSubBeanName();
    }

}