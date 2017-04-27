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
 * Created by Charis on 2017/4/20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsBuildPlatformQuantityCheckJdServiceTest {

    @Autowired
    private CmsBuildPlatformQuantityCheckJdService cmsBuildPlatformQuantityCheckJdService;


    @Test
    public void testOnStartUp() {
        List<TaskControlBean> taskControlBeanList = new ArrayList<>();
        TaskControlBean tcb = new TaskControlBean();
        tcb.setTask_id("CmsBuildPlatformQuantityCheckJdJob");
        tcb.setCfg_name("order_channel_id");
        tcb.setCfg_val1("001");
        tcb.setTask_comment("京东系允许运行的店铺");
        taskControlBeanList.add(tcb);
        cmsBuildPlatformQuantityCheckJdService.onStartup(taskControlBeanList);
    }


    @Test
    public void testCheckProduct() {
        String channelId = "010";
        int cartId = 26;
        cmsBuildPlatformQuantityCheckJdService.checkProduct(channelId, cartId);
    }
}
