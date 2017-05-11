package com.voyageone.task2.cms.service;

import com.voyageone.task2.base.modelbean.TaskControlBean;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;

/**
 * @author james.li on 2016/3/10.
 * @version 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsPromotrionServiceTest  {

    @Autowired
    private CmsPromotrionService cmsPromotrionService;

    @Test
    public void testOnStartup() throws Exception {
        cmsPromotrionService.updatePromotion("928","31");

    }
}