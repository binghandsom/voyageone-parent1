package com.voyageone.task2.cms.service.feed;

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
 * @author james.li on 2016/5/4.
 * @version 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class SummerGuruAnalysisServiceTest {

    @Autowired
    SummerGuruAnalysisService summerGuruAnalysisService;

    @Test
    public void testOnStartup() throws Exception {

        List<TaskControlBean> parmas = new ArrayList<TaskControlBean>();
        TaskControlBean parma = new TaskControlBean();
        parma.setCfg_name("feed_full_copy_temp");
        parma.setCfg_val1("1");
        parmas.add(parma);
        summerGuruAnalysisService.onStartup(parmas);
    }
}