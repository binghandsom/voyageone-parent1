package com.voyageone.task2.cms.service;

import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author james.li on 2016/7/11.
 * @version 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsPlatformProductImport2ServiceTest {

    @Autowired
    CmsPlatformProductImport2Service cmsPlatformProductImport2Service;
    @Test
    public void testOnStartup() throws Exception {
        List<TaskControlBean> taskControlList = new ArrayList<>();
        TaskControlBean taskControlBean = new TaskControlBean();
        taskControlBean.setCfg_name("order_channel_id");
        taskControlBean.setCfg_val1("010");
        taskControlList.add(taskControlBean);
        Map<String,Object> parma= new HashMap<>();
        parma.put("channelId","018");
        cmsPlatformProductImport2Service.onStartup(parma);
    }
}