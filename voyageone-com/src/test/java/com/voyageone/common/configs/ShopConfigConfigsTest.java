package com.voyageone.common.configs;

import com.voyageone.common.configs.Enums.ShopConfigEnums;
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
public class ShopConfigConfigsTest {

    @Test
    public void testGetVal1() throws Exception {
        System.out.println(JsonUtil.getJsonString(ShopConfigs.getVal1("001","20", ShopConfigEnums.Name.valueOf("expected_ship_date"))));
    }

    @Test
    public void testGetVal2() throws Exception {
        System.out.println(JsonUtil.getJsonString(ShopConfigs.getVal2("001","20", ShopConfigEnums.Name.valueOf("expected_ship_date"),"6")));
    }

    @Test
    public void testGetConfigs() throws Exception {
        System.out.println(JsonUtil.getJsonString(ShopConfigs.getConfigs("001","20", ShopConfigEnums.Name.valueOf("sim_shipping"))));
    }
}