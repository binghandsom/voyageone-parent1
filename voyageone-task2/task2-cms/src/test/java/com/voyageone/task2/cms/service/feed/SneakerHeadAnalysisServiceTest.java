package com.voyageone.task2.cms.service.feed;

import com.voyageone.common.configs.Feeds;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author james.li on 2016/5/4.
 * @version 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class SneakerHeadAnalysisServiceTest {
    @Autowired
    SneakerHeadAnalysisService sneakerHeadAnalysisService;

    @Test
    public void testOnStartup() throws Exception {
        Feeds.reload();
        TaskControlBean taskControlBean = new TaskControlBean();
        taskControlBean.setTask_id("CmsSneakerHeadAnalysisJob");
        taskControlBean.setCfg_name("run_flg");
        taskControlBean.setCfg_val1("1");
        taskControlBean.setEnd_time("1464163223766");
        List<TaskControlBean> taskControlBeans = new ArrayList<TaskControlBean>();
        taskControlBeans.add(taskControlBean);
        sneakerHeadAnalysisService.onStartup(taskControlBeans);

    }
}