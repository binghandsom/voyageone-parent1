package com.voyageone.components.jd.service;

import com.jd.open.api.sdk.response.promotion.PromoSkuVO;
import com.jd.open.api.sdk.response.promotion.PromotionVO;
import com.voyageone.common.configs.beans.ShopBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * 京东促销相关API调用服务测试
 *
 * Created by desmond on 2016/11/22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class JdPromotionServiceTest {

    @Autowired
    private JdPromotionService jdPromotionService;

    @Test
    public void testGetPromotionInfo() throws Exception {
        ShopBean shopBean = new ShopBean();
        shopBean.setApp_url("https://api.jd.com/routerjson");
        shopBean.setAppKey("BFA3102EFD4B981E9EEC2BE32DF1E44E");
        shopBean.setAppSecret("90742900899f49a5acfaf3ec1040a35c");
        shopBean.setSessionKey("4326ace5-57d7-4b9e-b24a-3ac2471eabe3"); // 京东国际匠心界全球购专营店(SessionKey)
        shopBean.setOrder_channel_id("928");
        shopBean.setCart_id("28");
        shopBean.setShop_name("京东国际匠心界全球购专营店");
        shopBean.setPlatform_id("2");

        Long promoId = 1045702247L;
        PromotionVO result = jdPromotionService.getPromotionInfo(shopBean, promoId);
        System.out.println("result status = " + (result == null ? "" : result.getStatus()));
    }

    @Test
    public void testGetPromotionSkuListAllById() throws Exception {
        ShopBean shopBean = new ShopBean();
        shopBean.setApp_url("https://api.jd.com/routerjson");
        shopBean.setAppKey("BFA3102EFD4B981E9EEC2BE32DF1E44E");
        shopBean.setAppSecret("90742900899f49a5acfaf3ec1040a35c");
        shopBean.setSessionKey("4326ace5-57d7-4b9e-b24a-3ac2471eabe3"); // 京东国际匠心界全球购专营店(SessionKey)
        shopBean.setOrder_channel_id("928");
        shopBean.setCart_id("28");
        shopBean.setShop_name("京东国际匠心界全球购专营店");
        shopBean.setPlatform_id("2");

//        Long promoId = 1045702247L;
        Long promoId = 1046178147L;
        List<PromoSkuVO> result = jdPromotionService.getPromotionSkuListAllById(shopBean, promoId);
        System.out.println("result count = " + (result == null ? "0" : result.size()));
    }

}
