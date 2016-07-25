package com.voyageone.components.tmall.service;

import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * @author james.li on 2016/7/21.
 * @version 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class TbProductServiceTest {

    @Autowired
    TbProductService tbProductService;

    @Test
    public void testDelItem() throws Exception {
        ShopBean shopBean = Shops.getShop("002",23);
        shopBean.setShop_name("PortAmerican海外专营店");
        shopBean.setApp_url("http://gw.api.taobao.com/router/rest");
        shopBean.setAppKey("21008948");
        shopBean.setAppSecret("0a16bd08019790b269322e000e52a19f");
        shopBean.setSessionKey("6201b033f0bdf6896ca8b52f9b679698ea4f6e3c4047b352183719539");
        tbProductService.delItem(shopBean,"528312220835");
    }
}