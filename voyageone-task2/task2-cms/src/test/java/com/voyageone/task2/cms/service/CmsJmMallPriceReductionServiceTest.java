package com.voyageone.task2.cms.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by james on 2016/10/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsJmMallPriceReductionServiceTest {

    @Autowired
    CmsJmMallPriceReductionService cmsJmMallPriceReductionServiceTest;
    @Test
    public void onStartup() throws Exception {
        cmsJmMallPriceReductionServiceTest.onStartup(new ArrayList<>());
    }

}