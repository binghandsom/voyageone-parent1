package com.voyageone.components.jd.service;

import com.voyageone.common.configs.beans.ShopBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 京东商品sku相关API(更新sku价格等)调用服务测试
 *
 * Created by desmond on 2016/11/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class JdSkuServiceTest {

    @Autowired
    private JdSkuService jdSkuService;

    @Test
    public void testUpdateSkuPriceByOuterId() throws Exception {
        ShopBean shopBean = new ShopBean();
        shopBean.setApp_url("https://api.jd.com/routerjson");
        shopBean.setAppKey("");
        shopBean.setAppSecret("");
        shopBean.setSessionKey(""); // 京东国际匠心界全球购专营店(SessionKey)
        shopBean.setOrder_channel_id("928");
        shopBean.setCart_id("29");
        shopBean.setShop_name("京东国际匠心界全球购专营店");

        String outerId = "skucode001";
        String price = "9301";
        String result = jdSkuService.updateSkuPriceByOuterId(shopBean, outerId, price);
        System.out.println("result = " + (result == null ? "" : result));
    }

    @Test
    public void testUpdateSkuPrice() throws Exception {
        ShopBean shopBean = new ShopBean();
        shopBean.setApp_url("https://api.jd.com/routerjson");
        shopBean.setAppKey("");
        shopBean.setAppSecret("");
        shopBean.setSessionKey(""); // 京东国际匠心界全球购专营店(SessionKey)
        shopBean.setOrder_channel_id("928");
        shopBean.setCart_id("29");
        shopBean.setShop_name("京东国际匠心界全球购专营店");

        String skuId = "";
        String outerId = "skucode001";    // outerId(就是skuCode)
//        String outerId = "skutest001";  // productCode，不是outerId,会报"外部id不存在"错误
        String price = "9101";
        String marketPrice = "10101";
        String jdPrice = "9301";
        String result = jdSkuService.updateSkuPrice(shopBean, skuId, outerId, price, null, marketPrice, jdPrice);
        System.out.println("result = " + (result == null ? "" : result));
    }
}
