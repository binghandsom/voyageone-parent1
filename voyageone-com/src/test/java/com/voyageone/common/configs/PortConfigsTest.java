package com.voyageone.common.configs;

import com.voyageone.common.configs.Enums.PortConfigEnums;
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
public class PortConfigsTest {

    @Test
    public void testGetVal1() throws Exception {
        System.out.println(JsonUtil.getJsonString(PortConfigs.getVal1("10", PortConfigEnums.Name.valueOf("scan_type"))));
    }

    @Test
    public void testGetVal2() throws Exception {
        System.out.println(JsonUtil.getJsonString(PortConfigs.getVal2("15", PortConfigEnums.Name.valueOf("status_reset"),"14")));
    }

    @Test
    public void testGetConfigs() throws Exception {
        System.out.println(JsonUtil.getJsonString(PortConfigs.getConfigs("10", PortConfigEnums.Name.valueOf("scan_type"))));
    }
}