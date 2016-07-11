package com.voyageone.common.configs;

import com.voyageone.common.util.JacksonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class VmsChannelConfigsTest {

    @Test
    public void testGetConfigBean() throws Exception {
        System.out.println(JacksonUtil.bean2Json(VmsChannelConfigs.getConfigBean("001", "configKey1", "configCode1")));
        System.out.println(JacksonUtil.bean2Json(VmsChannelConfigs.getConfigBean("001", "configKey1", "configCode2")));
    }

    @Test
    public void testGetConfigBeanNoCode() throws Exception {
        System.out.println(JacksonUtil.bean2Json(VmsChannelConfigs.getConfigBeanNoCode("001", "configKey1")));
    }

    @Test
    public void testGetConfigBeans() throws Exception {
        System.out.println(JacksonUtil.bean2Json(VmsChannelConfigs.getConfigBeans("001", "configKey1")));
    }
}
