package com.voyageone.task2.cms.service;

import com.voyageone.common.configs.beans.ShopBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by desmond on 2016/11/11
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class GetAllPlatformsInfoServiceTest {

    @Autowired
    private GetAllPlatformsInfoService getAllPlatformsInfoService;

    @Test
    public void testDoTmPlatformCategory() throws Exception {
        // 天猫类目和类目schema取得
        String channnelId = "002";
        int cartId = 23;

        // 测试用PortAmerican海外专营店
        ShopBean shop = new ShopBean();
        shop.setOrder_channel_id(channnelId);
        shop.setCart_id(String.valueOf(cartId));
        shop.setApp_url("http://gw.api.taobao.com/router/rest");
        shop.setAppKey("");
        shop.setAppSecret("");
        shop.setSessionKey("");
        // platformid设成天猫（1）
        shop.setPlatform_id("1");

        // 取得天猫类目和类目schema
        getAllPlatformsInfoService.doTmPlatformCategory(channnelId, cartId, shop);
        System.out.println("取得天猫类目和类目schema成功！");
    }

}
