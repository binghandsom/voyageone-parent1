package com.voyageone.components.dt.service;

import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.beans.ShopBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 分销商品调用服务测试
 *
 * Created by desmond on 2016/12/29.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class DtWareServiceTest {

    @Autowired
    private DtWareService dtWareService;

    @Test
    public void testAddProduct() throws Exception {
        // 分销平台新增/更新商品测试

        String channelId = "928";
        String cartId = CartEnums.Cart.DT.getId();   // 33
        String code = "022-EA3060501754";

        ShopBean shop = new ShopBean();
        shop.setApp_url("http://10.0.1.39:8082/dist");
        shop.setAppKey("");
        shop.setAppSecret("");
        shop.setSessionKey("");     // 分销测试店(SessionKey)
        shop.setOrder_channel_id(channelId);
        shop.setCart_id(cartId);
        shop.setShop_name("分销测试店");
        shop.setPlatform_id(PlatFormEnums.PlatForm.DT.getId());

        String result = dtWareService.addProduct(shop, code);
        System.out.println("result = " + (result == null ? "分销商品上新返回值为空" : result));
    }

}
