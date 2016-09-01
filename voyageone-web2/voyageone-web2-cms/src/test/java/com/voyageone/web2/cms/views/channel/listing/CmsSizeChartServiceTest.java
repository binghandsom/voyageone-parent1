package com.voyageone.web2.cms.views.channel.listing;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"classpath*:META-INF/context-web2.xml","classpath*:META-INF/context-web2-mvc.xml"})
public class CmsSizeChartServiceTest {

    @Autowired
    CmsSizeChartService service;
    @Test
    public void getListImageGroupBySizeChartId() {
        List<Map<String,Object>> list=service.getListImageGroupBySizeChartId("010",321);
    }
}
