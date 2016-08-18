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
 * Created by zhujiaye on 15/12/21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsSetMainPropMongoServiceTest {

    @Autowired
    private CmsSetMainPropMongoService cmsSetMainPropMongoService;

    @Test
    public void testOnStartup() throws Exception {
        List<TaskControlBean> taskControlList = new ArrayList<>();
        TaskControlBean taskControlBean = new TaskControlBean();
        taskControlBean.setCfg_name("order_channel_id");
        taskControlBean.setCfg_val1("010");
        taskControlList.add(taskControlBean);

        TaskControlBean taskControlBean2 = new TaskControlBean();
        taskControlBean2.setCfg_name("order_channel_id");
        taskControlBean2.setCfg_val1("019");
        taskControlList.add(taskControlBean2);

        TaskControlBean taskControlBean3 = new TaskControlBean();
        taskControlBean3.setCfg_name("order_channel_id");
        taskControlBean3.setCfg_val1("020");
        taskControlList.add(taskControlBean3);

        cmsSetMainPropMongoService.onStartup(taskControlList);
    }
}