package com.voyageone.task2.cms.service.feed;

import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gump on 2016/08/01.
 * @version 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class ShoeMetroAnalysisServiceTest {
    @Autowired
    ShoeMetroAnalysisService shoeMetroAnalysisService;

    @Test
    public void testOnStartup() throws Exception {
        TaskControlBean taskControlBean = new TaskControlBean();
        taskControlBean.setCfg_name("feed_full_copy_temp");
        taskControlBean.setCfg_val1("1");
        List<TaskControlBean> taskControlBeans = new ArrayList<TaskControlBean>();
        taskControlBeans.add(taskControlBean);

        shoeMetroAnalysisService.onStartup(taskControlBeans);
    }
}