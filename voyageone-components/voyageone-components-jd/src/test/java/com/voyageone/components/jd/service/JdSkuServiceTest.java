package com.voyageone.components.jd.service;

import com.google.common.base.Joiner;
import com.jd.open.api.sdk.domain.ware.Sku;
import com.voyageone.common.configs.beans.ShopBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

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
        shopBean.setCart_id("28");
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
        shopBean.setCart_id("28");
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

    @Test
    public void testGetSkusByWareId() throws Exception {
        ShopBean shopBean = new ShopBean();
        shopBean.setApp_url("https://api.jd.com/routerjson");
        shopBean.setAppKey("");
        shopBean.setAppSecret("");
        shopBean.setSessionKey(""); // 京东国际匠心界全球购专营店(SessionKey)
        shopBean.setOrder_channel_id("928");
        shopBean.setCart_id("28");
        shopBean.setShop_name("京东国际匠心界全球购专营店");

        String wareId = "1955593691";
        StringBuilder failCause = new StringBuilder("");
        List<Sku> skus = jdSkuService.getSkusByWareId(shopBean, wareId, failCause);
        System.out.println("result skuId = " + (skus == null ? "no skus" : Joiner.on(",").join(skus.stream().map(Sku::getSkuId).toArray())));
        System.out.println("failCause = " + failCause.toString());
    }

    @Test
    public void testGetSkusByWareIds() throws Exception {
        ShopBean shopBean = new ShopBean();
        shopBean.setApp_url("https://api.jd.com/routerjson");
        shopBean.setAppKey("");
        shopBean.setAppSecret("");
        shopBean.setSessionKey(""); // 京东国际匠心界全球购专营店(SessionKey)
        shopBean.setOrder_channel_id("928");
        shopBean.setCart_id("28");
        shopBean.setShop_name("京东国际匠心界全球购专营店");

        List<String> wareIds = new ArrayList() {{
            add("1955593691");
            add("1955182318");
            add("1955593098");
            add("1955208725");
            add("1955173805");
            add("1955602904");
            add("1955174905");
            add("1956471339");
            add("1955178904");
            add("1955126566");
            add("abcddddddtest");
//            add("9999888899");
            add("1955172111");
        }};

        StringBuilder failCause = new StringBuilder("");
        List<Sku> skus = jdSkuService.getSkusByWareIds(shopBean, wareIds, failCause);
        System.out.println("result skuId = " + (skus == null ? "no skus" : Joiner.on(",").join(skus.stream().map(Sku::getSkuId).toArray())));
        System.out.println("failCause = " + failCause.toString());
    }

    @Test
    public void testGetSkuByOuterId() throws Exception {
        ShopBean shopBean = new ShopBean();
        shopBean.setApp_url("https://api.jd.com/routerjson");
        shopBean.setAppKey("");
        shopBean.setAppSecret("");
        shopBean.setSessionKey(""); // 京东国际匠心界全球购专营店(SessionKey)
        shopBean.setOrder_channel_id("928");
        shopBean.setCart_id("28");
        shopBean.setShop_name("京东国际匠心界全球购专营店");

        String outerId = "022-0410S0RAW0862";
        StringBuilder failCause = new StringBuilder("");
        Sku sku = jdSkuService.getSkuByOuterId(shopBean, outerId, failCause);
        System.out.println("result skuId = " + (sku == null ? "no sku" : sku.getSkuId()));
        System.out.println("failCause = " + failCause.toString());
    }
}
