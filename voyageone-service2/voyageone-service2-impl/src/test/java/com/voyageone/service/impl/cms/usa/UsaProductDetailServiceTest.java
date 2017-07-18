package com.voyageone.service.impl.cms.usa;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by james on 2017/7/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-service2.xml")
public class UsaProductDetailServiceTest {

    @Autowired
    UsaProductDetailService usaProductDetailService;
    @Test
    public void getProductPlatform() throws Exception {
        usaProductDetailService.getProductPlatform("001",4356631L,1);
    }

}