package com.voyageone.batch.cms.service.feed;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.FileNotFoundException;

/**
 * 解析测试..
 * Created by Jonas on 10/14/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class BcbgAnalysisServiceTest {
    @Autowired
    private BcbgAnalysisService bcbgAnalysisService;

    @Before
    public void setUp() throws Exception {
        BcbgWsdlConstants.init();
    }

    @Test
    public void testAppendDataFromFile() throws FileNotFoundException {
        bcbgAnalysisService.appendDataFromFile();
    }

    @Test
    public void testBcbgAnalysisService() {
        bcbgAnalysisService.startup();
    }
}