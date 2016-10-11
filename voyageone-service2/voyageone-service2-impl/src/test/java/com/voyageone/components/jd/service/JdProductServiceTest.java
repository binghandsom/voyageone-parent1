package com.voyageone.components.jd.service;

import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * @author james.li on 2016/8/1.
 * @version 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-service2.xml")
public class JdProductServiceTest {

    @Autowired
    JdProductService jdProductService;
    @Test
    public void testDelItem() throws Exception {

        ShopBean shopBean = Shops.getShop("928", 28);
        shopBean.setShop_name("匠心界全球购专营店");
        shopBean.setApp_url("https://api.jd.com/routerjson");
        shopBean.setAppKey("BFA3102EFD4B981E9EEC2BE32DF1E44E");
        shopBean.setAppSecret("90742900899f49a5acfaf3ec1040a35c");
        shopBean.setSessionKey("7d7cb9f2-8011-4004-9cad-1f046966a06b");
        jdProductService.delItem(shopBean,"1955123581");
    }
}