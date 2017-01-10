package com.voyageone.task2.cms.service.feed;

import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;

/**
 * Created by gjl on 2016/12/9.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class ChampionAnalysisServiceTest {
    @Autowired
    ChampionAnalysisService championAnalysisService;

    @Test
    public void testOnStartup() throws Exception {
        championAnalysisService.onStartup(new ArrayList<TaskControlBean>());
    }
}
