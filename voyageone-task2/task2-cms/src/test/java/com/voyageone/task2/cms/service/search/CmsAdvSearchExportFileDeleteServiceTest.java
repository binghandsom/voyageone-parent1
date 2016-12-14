package com.voyageone.task2.cms.service.search;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;

import static org.junit.Assert.*;

/**
 * Created by dell on 2016/12/12.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:context-cms-test.xml")
public class CmsAdvSearchExportFileDeleteServiceTest {
    @Autowired
    private CmsAdvSearchExportFileDeleteService cmsAdvSearchExportFileDeleteService;

    @Test
    public void onStartup() throws Exception {
        cmsAdvSearchExportFileDeleteService.onStartup(null);
    }

}