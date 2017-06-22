package com.voyageone.task2.cms.service.search;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:context-cms-test.xml")
public class CmsProductTotalImportToDistSearchServiceTest {

    @Autowired
    private CmsProductTotalImportToDistSearchService cmsProductTotalImportToDistSearchService;

    @Test
    public void testImportDataToSearchFromMongo() {
        String channelId = "001";
        cmsProductTotalImportToDistSearchService.importDataToSearchFromMongo(channelId, new HashMap<>());
    }

    @Test
    public void testOnStartup() throws Exception {
        cmsProductTotalImportToDistSearchService.onStartup(null);
    }
}
