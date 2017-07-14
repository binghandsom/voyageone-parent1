package com.voyageone.task2.cms.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by james on 2017/7/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsBuildPaltformCategoryTreeAmazonMqServiceTest {

    @Autowired
    CmsBuildPaltformCategoryTreeAmazonMqService cmsBuildPaltformCategoryTreeAmazonMqService;
    @Test
    public void onStartup() throws Exception {
        cmsBuildPaltformCategoryTreeAmazonMqService.onStartup(null);
    }

}