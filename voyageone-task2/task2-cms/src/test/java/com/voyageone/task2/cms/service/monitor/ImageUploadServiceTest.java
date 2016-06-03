package com.voyageone.task2.cms.service.monitor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.TimeUnit;

/**
 * @author aooer 2016/6/2.
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class ImageUploadServiceTest {

    @Autowired
    ImageUploadService imageUploadService;

    @Test
    public void testOnModify() throws Exception {
        TimeUnit.DAYS.sleep(1);
    }
}