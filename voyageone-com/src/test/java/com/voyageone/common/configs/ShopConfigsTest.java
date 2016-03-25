package com.voyageone.common.configs;

import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.util.JacksonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author aooer 2016/3/24.
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class ShopConfigsTest {

    @Test
    public void testGetShop() throws Exception {
        System.out.println(JacksonUtil.bean2Json(Shops.getShop("001","20")));
    }

    @Test
    public void testGetChannelShopList() throws Exception {
        System.out.println(JacksonUtil.bean2Json(Shops.getChannelShopList("001")));
    }

    @Test
    public void testGetCartShopList() throws Exception {
        System.out.println(JacksonUtil.bean2Json(Shops.getCartShopList("20")));
    }

    @Test
    public void testGetShopListByPlatform() throws Exception {
        System.out.println(JacksonUtil.bean2Json(Shops.getShopListByPlatform(PlatFormEnums.PlatForm.TM)));
    }

    @Test
    public void testGetShopList() throws Exception {
        System.out.println(JacksonUtil.bean2Json(Shops.getShopList()));
    }

    @Test
    public void testGetShopNameDis() throws Exception {
        System.out.println(JacksonUtil.bean2Json(Shops.getShopNameDis("001","20")));
    }
}