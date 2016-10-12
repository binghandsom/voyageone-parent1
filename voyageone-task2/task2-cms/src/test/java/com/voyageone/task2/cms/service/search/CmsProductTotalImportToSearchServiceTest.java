package com.voyageone.task2.cms.service.search;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:context-cms-test.xml")
public class CmsProductTotalImportToSearchServiceTest {

    @Autowired
    private CmsProductTotalImportToSearchService cmsProductTotalImportToSearchService;

    @Test
    public void testImportDataToSearchFromMongo() {
        String channelId = "001";
        cmsProductTotalImportToSearchService.importDataToSearchFromMongo(channelId);
    }

    @Test
    public void testOnStartup() throws Exception {
        cmsProductTotalImportToSearchService.onStartup(null);
    }
}
