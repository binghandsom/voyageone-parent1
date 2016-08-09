package com.voyageone.task2.cms.service.product;

import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jason.jiang on 2016/08/03
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsGetPlatformStatusServiceTest {

    @Autowired
    CmsGetPlatformStatusService targetService;

    @Test
    public void testOnStartup() {
        List<TaskControlBean> paramList = new ArrayList<>();
        TaskControlBean param = new TaskControlBean();
        param.setTask_id("CmsGetPlatformStatusJob");
        param.setCfg_name("run_flg");
        param.setCfg_val1("1");
        paramList.add(param);

        TaskControlBean param2 = new TaskControlBean();
        param2.setTask_id("CmsGetPlatformStatusJob");
        param2.setCfg_name("channel_id");
        param2.setCfg_val1("010");
        paramList.add(param2);

        TaskControlBean param3 = new TaskControlBean();
        param3.setTask_id("CmsGetPlatformStatusJob");
        param3.setCfg_name("cart_id");
        param3.setCfg_val1("23");
        paramList.add(param3);

        try {
            targetService.onStartup(paramList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}