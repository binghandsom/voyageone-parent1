package com.voyageone.common.configs;

import com.voyageone.common.configs.beans.CmsChannelConfigBean;
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
public class CmsChannelConfigsTest {

    @Test
    public void testGetConfigBean() throws Exception {
        System.out.println(JsonUtil.bean2Json(CmsChannelConfigs.getConfigBean("010", "PROD_CUST_ATTR", "short_title")));
    }

    @Test
    public void testGetConfigBeans() throws Exception {
        System.out.println(JsonUtil.getJsonString(CmsChannelConfigs.getConfigBeans("010", "PROD_CUST_ATTR")));
    }
}