package com.voyageone.task2.cms.service;

import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.cms.service.promotion.stock.StockIncrementService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author james.li on 2016/1/6.
 * @version 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsUploadImageToPlatformServiceTest {

    @Autowired
    private CmsUploadImageToPlatformService cmsUploadImageToPlatformService;

    @Test
    public void testStartup() {
        cmsUploadImageToPlatformService.startup();
    }
}