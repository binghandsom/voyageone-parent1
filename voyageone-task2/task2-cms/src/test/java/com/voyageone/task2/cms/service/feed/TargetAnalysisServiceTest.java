package com.voyageone.task2.cms.service.feed;

import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.apache.commons.lang.StringEscapeUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.URLDecoder;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * @author james.li on 2016/5/6.
 * @version 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class TargetAnalysisServiceTest {
    @Autowired
    private TargetAnalysisService targetAnalysisService;
    @Test
    public void testOnStartup() throws Exception {


        targetAnalysisService.onStartup(new ArrayList<TaskControlBean>());
    }
}