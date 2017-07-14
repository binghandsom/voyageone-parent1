package com.voyageone.task2.cms.usa.job;

import com.voyageone.task2.cms.BaseTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by rex.wu on 2017/7/14.
 */
public class UsaCmsDataAmountJobServiceTest extends BaseTest {

    @Autowired
    private UsaCmsDataAmountJobService usaCmsDataAmountJobService;

    @Test
    public void onStartup() throws Exception {
        usaCmsDataAmountJobService.startup();
    }

}