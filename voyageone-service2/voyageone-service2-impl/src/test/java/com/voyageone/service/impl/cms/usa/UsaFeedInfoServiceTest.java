package com.voyageone.service.impl.cms.usa;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by rex.wu on 2017/7/6.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-service2.xml")
public class UsaFeedInfoServiceTest {

    @Autowired
    private UsaFeedInfoService usaFeedInfoService;

    @Test
    public void getTopModelsByModel() throws Exception {

        usaFeedInfoService.getTopModelsByModel("001", "patagoniadownsweatervestkids");

    }

}