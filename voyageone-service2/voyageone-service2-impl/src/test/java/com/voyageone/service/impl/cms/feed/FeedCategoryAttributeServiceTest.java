package com.voyageone.service.impl.cms.feed;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by james on 2017/3/7.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-service2.xml")
public class FeedCategoryAttributeServiceTest {
    @Autowired
    FeedCategoryAttributeService feedCategoryAttributeService;
    @Test
    public void getAttributeNameByChannelId() throws Exception {
        feedCategoryAttributeService.getAttributeNameByChannelId("001");
    }

}