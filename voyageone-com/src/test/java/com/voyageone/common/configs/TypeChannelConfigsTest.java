package com.voyageone.common.configs;

import com.voyageone.common.Constants;
import com.voyageone.common.util.JacksonUtil;
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
public class TypeChannelConfigsTest {

    @Test
    public void testGetTypeList() throws Exception {
        System.out.println(JacksonUtil.bean2Json(TypeChannels.getTypeList("brand","017")));
    }

    @Test
    public void testGetValue() throws Exception {
        System.out.println(JacksonUtil.bean2Json(TypeChannels.getValue("brand","017","1 Up Nutrition")));
    }

    @Test
    public void testGetTypeMapList() throws Exception {
        System.out.println(JacksonUtil.bean2Json(TypeChannels.getTypeMapList("brand","017")));
    }

    @Test
    public void testGetOptions() throws Exception {
        System.out.println(JacksonUtil.bean2Json(TypeChannels.getOptions("brand","017")));
    }

    @Test
    public void testGetTypeWithLang() throws Exception {
        System.out.println(JacksonUtil.bean2Json(TypeChannels.getTypeWithLang("brand","017","en")));
    }

    @Test
    public void testGetTypeChannelByCode() throws Exception {
        System.out.println(JacksonUtil.bean2Json(TypeChannels.getTypeChannelByCode("brand","017","1 Up Nutrition","en")));
    }

    @Test
    public void testGetTypeListSkuCarts() throws Exception {
        System.out.println(JacksonUtil.bean2Json(TypeChannels.getTypeListSkuCarts("017", Constants.comMtTypeChannel.SKU_CARTS_53_D,"en")));
    }
}