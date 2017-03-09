package com.voyageone.task2.cms.service;

import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by james on 2017/3/8.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsSetMainPropMongo2ServiceTest {

    @Autowired
    CmsSetMainPropMongo2Service cmsSetMainPropMongo2Service;

    @Test
    public void onStartup() throws Exception {
        List<TaskControlBean> taskControlList = new ArrayList<>();
        TaskControlBean taskControlBean = new TaskControlBean();
        taskControlBean.setCfg_name("order_channel_id");
        taskControlBean.setCfg_val1("010");
        taskControlList.add(taskControlBean);
        taskControlBean = new TaskControlBean();
        taskControlBean.setCfg_name("order_channel_id");
        taskControlBean.setCfg_val1("009");
        taskControlList.add(taskControlBean);

        cmsSetMainPropMongo2Service.onStartup(taskControlList);
    }

}