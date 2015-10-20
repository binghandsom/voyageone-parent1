package com.voyageone.batch.cms.service.feed;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.regex.Pattern;

/**
 * 解析测试..
 * Created by Jonas on 10/14/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class BcbgAnalysisServiceTest {
    @Autowired
    private BcbgAnalysisService bcbgAnalysisService;

    @Test
    public void testBcbgAnalysisService() {
        bcbgAnalysisService.startup();
    }

    @Test
    public void testClear() {
        Pattern pattern = Pattern.compile("[%\\[~@\\#\\$&\\*_'/‘’\\^\\(\\)\\]\\\\]");

        String result = pattern.matcher("%1[2]3~4@5#6\\7$8&9_10*11'12)13").replaceAll("");

        System.out.print(result);
    }
}