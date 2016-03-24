package com.voyageone.common.configscache;

import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.Enums.ShopConfigEnums;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.JsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

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
        System.out.println(JacksonUtil.bean2Json(ShopConfigs.getShop("001","20")));
    }

    @Test
    public void testGetChannelShopList() throws Exception {
        System.out.println(JacksonUtil.bean2Json(ShopConfigs.getChannelShopList("001")));
    }

    @Test
    public void testGetCartShopList() throws Exception {
        System.out.println(JacksonUtil.bean2Json(ShopConfigs.getCartShopList("20")));
    }

    @Test
    public void testGetShopListByPlatform() throws Exception {
        System.out.println(JacksonUtil.bean2Json(ShopConfigs.getShopListByPlatform(PlatFormEnums.PlatForm.TM)));
    }

    @Test
    public void testGetShopList() throws Exception {
        System.out.println(JacksonUtil.bean2Json(ShopConfigs.getShopList()));
    }

    @Test
    public void testGetShopNameDis() throws Exception {
        System.out.println(JacksonUtil.bean2Json(ShopConfigs.getShopNameDis("001","20")));
    }
}