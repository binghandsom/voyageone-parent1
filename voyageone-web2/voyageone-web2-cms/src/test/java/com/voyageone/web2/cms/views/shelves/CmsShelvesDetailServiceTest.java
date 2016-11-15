package com.voyageone.web2.cms.views.shelves;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by james on 2016/11/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"classpath*:META-INF/context-web2.xml","classpath*:META-INF/context-web2-mvc.xml"})
public class CmsShelvesDetailServiceTest {

    @Autowired
    CmsShelvesDetailService cmsShelvesDetailService;
    @Test
    public void getShelvesInfo() throws Exception {
        cmsShelvesDetailService.getShelvesInfo("010", Arrays.asList(1));
    }

}