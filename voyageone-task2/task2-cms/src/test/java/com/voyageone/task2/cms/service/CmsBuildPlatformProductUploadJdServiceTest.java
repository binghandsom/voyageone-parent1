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
 * Created by desmond on 2016/6/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsBuildPlatformProductUploadJdServiceTest {

    @Autowired
    CmsBuildPlatformProductUploadJdService uploadJdService;

    @Test
    public void testOnStartup() throws Exception {
        List<TaskControlBean> taskControlList = new ArrayList<>();
        TaskControlBean tcb = new TaskControlBean();
        tcb.setTask_id("CmsBuildPlatformProductUploadJdJob");
        tcb.setCfg_name("order_channel_id");
        tcb.setCfg_val1("929");
        tcb.setTask_comment("京东国际悦境店上新允许运行的渠道");
        taskControlList.add(tcb);
        uploadJdService.onStartup(taskControlList);
    }
}
