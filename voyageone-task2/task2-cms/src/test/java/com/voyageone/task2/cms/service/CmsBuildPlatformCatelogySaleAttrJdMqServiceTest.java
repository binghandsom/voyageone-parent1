package com.voyageone.task2.cms.service;

import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.beans.ShopBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 取得京东类目销售属性(颜色和尺码)MQ服务测试
 *
 * Created by desmond on 2016/12/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsBuildPlatformCatelogySaleAttrJdMqServiceTest {

    @Autowired
    private CmsBuildPlatformCatelogySaleAttrJdMqService cmsBuildPlatformCatelogySaleAttrJdMqService;

    @Test
    public void testOnStartup() throws Exception {
        // 指定code的翻译(用OnStatup启动测试需要确保，表中取得店铺信息appkey等时有效的)

        String channelId = "928";
        String cartId = "28";
        List<String> catIdList = new ArrayList() {{
//            add("1440");
//            add("2580");
//            add("2629");    // 只有颜色没有尺码
//            add("6166");    // 京东平台上根本不存在的类目(6166:珠宝首饰>钻石>钻石DIY定制)
//            add("6150");    // 京东平台上类目(6150:珠宝首饰>黄金>黄金耳饰)存在，但颜色和尺码属性值都没有，追加之后再取得
        }};

        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("channelId", channelId);
        messageMap.put("cartId", cartId);
        messageMap.put("catIdList", catIdList);

        cmsBuildPlatformCatelogySaleAttrJdMqService.onStartup(messageMap);
        System.out.println("testOnStartup 测试正常结束!");
    }

    @Test
    public void testDoGetJdCategorySaleAttr() {
        String channelId = "928";
        String cartId = "28";
        List<String> catIdList = new ArrayList() {{
//            add("1440");
//            add("2629");    // 只有颜色没有尺码
//            add("6166");    // 京东平台上根本不存在的类目(6166:珠宝首饰>钻石>钻石DIY定制)
            add("6150");    // 京东平台上类目(6150:珠宝首饰>黄金>黄金耳饰)存在，但颜色和尺码属性值都没有，追加之后再取得
//            add("9762");
        }};

        ShopBean shop = new ShopBean();
        shop.setApp_url("https://api.jd.com/routerjson");
        shop.setOrder_channel_id(channelId);
        shop.setCart_id(cartId);
        shop.setAppKey("");
        shop.setAppSecret("");
        shop.setSessionKey("");
        shop.setShop_name("京东国际匠心界全球购专营店");
        shop.setPlatform_id(PlatFormEnums.PlatForm.JD.getId());

        try {
            cmsBuildPlatformCatelogySaleAttrJdMqService.doGetJdCategorySaleAttr(shop, channelId, cartId, catIdList);
        } catch (Exception e) {
            System.out.println("testDoGetJdCategorySaleAttr 测试异常结束!" + e.getMessage());
        }

        System.out.println("testDoGetJdCategorySaleAttr 测试正常结束!");
    }

}