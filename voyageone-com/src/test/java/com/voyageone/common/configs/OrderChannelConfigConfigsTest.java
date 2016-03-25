package com.voyageone.common.configs;

import com.voyageone.common.configs.Enums.ChannelConfigEnums;
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
public class OrderChannelConfigConfigsTest {

    @Test
    public void testGetVal1() throws Exception {
        System.out.println(JsonUtil.getJsonString(ChannelConfigs.getVal1("001", ChannelConfigEnums.Name.valueOf("add_order_detail_permit"))));
    }

    @Test
    public void testGetVal2() throws Exception {
        System.out.println(JsonUtil.getJsonString(ChannelConfigs.getVal2("001", ChannelConfigEnums.Name.valueOf("add_order_detail_permit_status"),"GZ")));
    }

    @Test
    public void testGetConfigs() throws Exception {
        System.out.println(JsonUtil.getJsonString(ChannelConfigs.getConfigs("001", ChannelConfigEnums.Name.valueOf("add_order_detail_permit_status"))));
    }
}