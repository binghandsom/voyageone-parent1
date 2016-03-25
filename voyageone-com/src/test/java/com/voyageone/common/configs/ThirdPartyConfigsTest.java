package com.voyageone.common.configs;

import com.voyageone.common.util.JacksonUtil;
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
public class ThirdPartyConfigsTest {

    @Test
    public void testGetThirdPartyConfig() throws Exception {
        System.out.println(JacksonUtil.bean2Json(ThirdPartyConfigs.getThirdPartyConfig("108","oms_upload_file_path")));
    }

    @Test
    public void testGetThirdPartyConfigList() throws Exception {
        System.out.println(JacksonUtil.bean2Json(ThirdPartyConfigs.getThirdPartyConfigList("108","oms_upload_file_path")));
    }

    @Test
    public void testGetThirdPartyConfigMap() throws Exception {
        System.out.println(JacksonUtil.bean2Json(ThirdPartyConfigs.getThirdPartyConfigMap("108")));
    }

    @Test
    public void testGetFirstConfig() throws Exception {
        System.out.println(JacksonUtil.bean2Json(ThirdPartyConfigs.getFirstConfig("108")));
    }

    @Test
    public void testGetConfigList() throws Exception {
        System.out.println(JacksonUtil.bean2Json(ThirdPartyConfigs.getConfigList("108")));
    }

    @Test
    public void testGetVal1() throws Exception {
        System.out.println(JacksonUtil.bean2Json(ThirdPartyConfigs.getVal1("108","oms_upload_file_path")));
    }
}