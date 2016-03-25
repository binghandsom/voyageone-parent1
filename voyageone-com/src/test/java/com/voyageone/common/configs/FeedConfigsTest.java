package com.voyageone.common.configs;

import com.voyageone.common.configs.Enums.FeedEnums;
import com.voyageone.common.util.JsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author aooer 2016/3/23.
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class FeedConfigsTest {

    @Test
    public void testGetVal1() throws Exception {
        System.out.println(JsonUtil.getJsonString(Feeds.getVal1("010", FeedEnums.Name.valueOf("attribute1"))));
    }

    @Test
    public void testGetConfigs() throws Exception {
        System.out.println(JsonUtil.getJsonString(Feeds.getConfigs("010", FeedEnums.Name.valueOf("attribute1"))));
    }
}