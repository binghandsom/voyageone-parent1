package com.voyageone.batch.cms.service.feed;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.regex.Pattern;

import static com.voyageone.batch.cms.service.feed.BcbgWsdlConstants.apparels_duty;
import static com.voyageone.batch.cms.service.feed.BcbgWsdlConstants.fixed_exchange_rate;

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
    public void testBigDecimal() {
        BcbgWsdlConstants.init();
        System.out.println("");
        System.out.println("=================");


        BigDecimal c = new BigDecimal("228.0000");

        System.out.println(c.toString());
        System.out.println(c.intValue());
        System.out.println(fixed_exchange_rate);
        System.out.println(apparels_duty);
        Assert.isTrue(c.intValue() == 228);

        System.out.println(c.multiply(fixed_exchange_rate).divide(apparels_duty, BigDecimal.ROUND_DOWN).intValue());

        BigDecimal a = new BigDecimal(1);
        BigDecimal b = new BigDecimal(2);
        Assert.isTrue(a.divide(b, BigDecimal.ROUND_DOWN).toString().equals("0"));
        System.out.println("=================");
    }

    @Test
    public void testClear() {
        Pattern pattern = Pattern.compile("[%\\[~@\\#\\$&\\*_'/‘’\\^\\(\\)\\]\\\\]");
        String result = pattern.matcher("%1[2]3~4@5#6\\7$8&9_10*11'12)13").replaceAll("");
        System.out.print(result);
    }
}