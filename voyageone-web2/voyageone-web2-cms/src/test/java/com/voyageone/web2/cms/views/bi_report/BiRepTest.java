package com.voyageone.web2.cms.views.bi_report;

import com.voyageone.web2.cms.views.biReport.consult.BiRepConsultService;
import com.voyageone.web2.cms.views.biReport.consult.BiRepSupport;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by dell on 2017/1/11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:META-INF/context-web2.xml")
public class BiRepTest {
    @Autowired
    BiRepSupport biRepSupport;
    @Autowired
    private BiRepConsultService biRepConsultService;
/*
    @Test
    public void biReportTest()
    {
        biRepConsultService.createXLSFile();
    }*/
    @Test
    public void getData()
    {
        biRepConsultService.getData();
    }
}
