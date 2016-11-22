package com.voyageone.task2.cms.service;

import org.apache.commons.collections.map.HashedMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by james on 2016/11/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsShelvesImageUploadMQServiceTest {

    @Autowired
    CmsShelvesImageUploadMQService cmsShelvesImageUploadMQService;
    @Test
    public void onStartup() throws Exception {
        Map<String,Object> map = new HashedMap();
        map.put("shelvesId",1);
        cmsShelvesImageUploadMQService.onStartup(map);
    }
}