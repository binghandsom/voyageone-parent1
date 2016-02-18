package com.voyageone.batch.cms.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by lewis on 15-11-28.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class ImportExcelFileServiceTest {

    @Autowired
    ImportExcelFileService importExcelFileService;

    @Test
    public void testOnStartup() throws Exception {

        Assert.assertFalse(false);

        //Excel文件导入
        importExcelFileService.startup();

        Assert.assertTrue(true);


    }

}
