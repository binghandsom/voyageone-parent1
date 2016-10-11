package com.voyageone.task2.cms.service.feed.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * @author james.li on 2016/9/22.
 * @version 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class TargetSkuStatusCheckServiceTest {

    @Autowired
    TargetSkuStatusCheckService targetSkuStatusCheckService;
    @Test
    public void testGetSkuList() throws Exception {
        targetSkuStatusCheckService.onStartup(new ArrayList<>());
    }
}