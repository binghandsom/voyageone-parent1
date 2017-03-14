package com.voyageone.task2.cms.service;

import com.voyageone.service.impl.cms.CmsBtDataAmountService;
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
 * Created by dell on 2016/7/5.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsDataAmountServiceTest {

    @Autowired
    CmsDataAmountService cmsDataAmountService;

    @Autowired
    CmsBtDataAmountService cmsBtDataAmountService;

    @Test
    public void testOnStartup() throws Exception {
        cmsBtDataAmountService.sumByChannelId("001");
    }
}