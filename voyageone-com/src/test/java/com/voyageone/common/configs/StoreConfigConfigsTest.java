package com.voyageone.common.configs;

import com.voyageone.common.configs.Enums.StoreConfigEnums;
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
public class StoreConfigConfigsTest {

    @Test
    public void testGetVal1() throws Exception {
        System.out.println(JsonUtil.getJsonString(StoreConfigs.getVal1(21, StoreConfigEnums.Name.valueOf("transfer_display"))));
    }

    @Test
    public void testGetVal2() throws Exception {
        System.out.println(JsonUtil.getJsonString(StoreConfigs.getVal2(21, StoreConfigEnums.Name.valueOf("transfer_display"),"1")));
    }

    @Test
    public void testGetConfigs() throws Exception {
        System.out.println(JsonUtil.getJsonString(StoreConfigs.getConfigs(49, StoreConfigEnums.Name.valueOf("site"))));
    }
}