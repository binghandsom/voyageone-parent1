package com.voyageone.service.impl.cms;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * @author james.li on 2016/7/11.
 * @version 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class PlatformCategoryServiceTest {

    @Autowired
    PlatformCategoryService platformCategoryService;

    @Test
    public void testGetCategoryByCatPath() throws Exception {

        platformCategoryService.getCategoryByCatPath("111","运动/瑜伽/健身/球迷用品",23);
    }
}