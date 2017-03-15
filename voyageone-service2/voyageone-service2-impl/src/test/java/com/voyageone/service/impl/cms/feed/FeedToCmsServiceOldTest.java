package com.voyageone.service.impl.cms.feed;

import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

/**
 * Created by james on 2017/3/9.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-service2.xml")
public class FeedToCmsServiceOldTest {

    @Autowired
    FeedToCmsService_old feedToCmsServiceOld;

    @Autowired
    FeedInfoService feedInfoService;
    @Test
    public void updateProduct() throws Exception {
        CmsBtFeedInfoModel cmsBtFeedInfoModel = feedInfoService.getProductByCode("015", "1129588737");
        feedToCmsServiceOld.updateProduct("015", Arrays.asList(cmsBtFeedInfoModel), "");
    }

}