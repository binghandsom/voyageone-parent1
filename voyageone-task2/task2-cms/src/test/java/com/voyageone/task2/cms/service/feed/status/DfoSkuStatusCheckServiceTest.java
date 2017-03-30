package com.voyageone.task2.cms.service.feed.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by james on 2017/3/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class DfoSkuStatusCheckServiceTest {

    @Autowired
    DfoSkuStatusCheckService dfoSkuStatusCheckService;

    @Test
    public void testGetSkuList() throws Exception {
        dfoSkuStatusCheckService.onStartup(new ArrayList<>());
    }
}