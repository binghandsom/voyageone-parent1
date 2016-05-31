package com.voyageone.task2.cms.service;

import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.cms.service.CmsPlatformMappingService;
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
        TaskControlBean taskControlBean = new TaskControlBean();
        taskControlBean.setCfg_name("order_channel_id");
        taskControlBean.setCfg_val1("017");
        taskControlBean.setCfg_val2("29");
        taskControlList.add(taskControlBean);
        cmsPlatformMappingService.onStartup(taskControlList);
    }
}