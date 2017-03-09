package com.voyageone.service.impl.cms.feed;

import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by james on 2017/3/9.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-service2.xml")
public class FeedToCms2ServiceTest {

    @Autowired
    FeedToCms2Service feedToCms2Service;

    @Autowired
    FeedInfoService feedInfoService;
    @Test
    public void updateProduct() throws Exception {
        CmsBtFeedInfoModel cmsBtFeedInfoModel = feedInfoService.getProductByCode("015", "1129588737");
        feedToCms2Service.updateProduct("015", Arrays.asList(cmsBtFeedInfoModel), "");
    }

}