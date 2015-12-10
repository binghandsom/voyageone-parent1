package com.voyageone.batch.cms.service;

import com.voyageone.batch.core.modelbean.TaskControlBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by james.li on 2015/12/7.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsPlatformMappingServiceTest {

    @Autowired
    CmsPlatformMappingService cmsPlatformMappingService;
    @Test
    public void testOnStartup() throws Exception {

        List<TaskControlBean> taskControlList = new ArrayList<>();
        cmsPlatformMappingService.onStartup(taskControlList);
    }
}