package com.voyageone.task2.cms.service.promotion.beat;

import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonasvlag on 16/3/8.
 *
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class BeatJobServiceTest {

    @Autowired
    private BeatJobService beatJobService;

    @Test
    public void testStartup() {
        beatJobService.startup();
    }

    @Test
    public void testOnStartup() throws Exception {

        List< TaskControlBean > taskControlList = new ArrayList<>();
        TaskControlBean taskControl = new TaskControlBean();
        taskControl.setCfg_name(TaskControlEnums.Name.thread_count.toString());
        taskControl.setCfg_val1("5");
        taskControlList.add(taskControl);
        taskControl = new TaskControlBean();
        taskControl.setCfg_name(TaskControlEnums.Name.atom_count.toString());
        taskControl.setCfg_val1("10");
        taskControlList.add(taskControl);
        beatJobService.onStartup(taskControlList);
    }
}