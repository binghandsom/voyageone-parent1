package com.voyageone.common.configs;

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
public class CodeConfigsTest {

    @Test
    public void testGetCodeName() throws Exception {
        System.out.println(Codes.getCodeName("BAIDU_TRANSLATE_CONFIG","ApiKey1"));
        System.out.println(Codes.getCodeName("BAIDU_TRANSLATE_CONFIG"));
    }

    @Test
    public void testGetCodeName1() throws Exception {
        System.out.println(Codes.getCodeName1("BAIDU_TRANSLATE_CONFIG","ApiKey1"));
    }

    @Test
    public void testGetCodeBean() throws Exception {
        System.out.println(JsonUtil.bean2Json(Codes.getCodeBean("BAIDU_TRANSLATE_CONFIG")));
    }

    @Test
    public void testGetCodeMap() throws Exception {
        System.out.println(JsonUtil.bean2Json(Codes.getCodeMap("BAIDU_TRANSLATE_CONFIG")));
    }

    @Test
    public void testGetCodeMapList() throws Exception {
        System.out.println(JsonUtil.bean2Json(Codes.getCodeMapList("BAIDU_TRANSLATE_CONFIG",true)));
        System.out.println(JsonUtil.bean2Json(Codes.getCodeMapList("BAIDU_TRANSLATE_CONFIG",false)));
    }

    @Test
    public void testGetCodeList() throws Exception {
        System.out.println(JsonUtil.bean2Json(Codes.getCodeList("BAIDU_TRANSLATE_CONFIG")));
    }

    @Test
    public void testGetCode() throws Exception {
        System.out.println(JsonUtil.bean2Json(Codes.getCode("BAIDU_TRANSLATE_CONFIG","Tzadu9fNzPlQ4YsD3gmHbWDm")));
    }
}