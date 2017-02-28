package com.voyageone.task2.cms.service.platform;

import com.voyageone.common.CmsConstants;
import com.voyageone.common.configs.Enums.CacheKeyEnums;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jason.jiang on 2016/07/24.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsPlatformActiveLogServiceTest {

    @Autowired
    private CmsPlatformActiveLogService targetService;

    private static final String KEY = CacheKeyEnums.KeyEnum.ConfigData_ShopConfigs.toString();

    @Before
    public void setUp() {
//        // 准备参数
//        Shops.reload();
//        Map<String, ShopBean> shopBeanMap = new HashMap<>();
//
//        // tmall
//        ShopBean bean1 = new ShopBean();
//        bean1.setCart_id("23");
//        bean1.setPlatform_id("1");
//        bean1.setOrder_channel_id("010");
//        bean1.setApp_url("http://gw.api.taobao.com/router/rest");
//
//        shopBeanMap.put(buildKey(bean1.getCart_id(), bean1.getOrder_channel_id()), bean1);
//        // jingdong
//        ShopBean bean2 = new ShopBean();
//        bean2.setCart_id("28");
//        bean2.setPlatform_id("2");
//        bean2.setOrder_channel_id("928");
//        bean2.setApp_url("https://api.jd.com/routerjson");
//
//        shopBeanMap.put(buildKey(bean2.getCart_id(), bean2.getOrder_channel_id()), bean2);
//
//        // jumei
//        ShopBean bean3 = new ShopBean();
//        bean3.setCart_id("27");
//        bean3.setPlatform_id("4");
//        bean3.setOrder_channel_id("010");
//        bean3.setApp_url("https://api.jd.com/routerjson");
//
//        shopBeanMap.put(buildKey(bean3.getCart_id(), bean3.getOrder_channel_id()), bean3);
//
//        CacheHelper.reFreshSSB(KEY, shopBeanMap);

    }

    // 测试tmall上架
    @Test
    public void testTMPlatformToOnsale() {
        // 发送请求到MQ,插入操作历史记录
        Map<String, Object> logParams = new HashMap<>(6);
        logParams.put("channelId", "012");
        logParams.put("cartId", 27);
        logParams.put("activeStatus", "ToOnSale");
        logParams.put("creator", "edward");
        logParams.put("comment", "高级检索 批量上下架");
        logParams.put("codeList", new ArrayList(Arrays.asList("BCH60F46-6R3")));
        try {
            targetService.setProductOnSaleOrInStock(logParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 测试tmall下架
    @Test
    public void testTMPlatformToInstock() {
        // 发送请求到MQ,插入操作历史记录
        Map<String, Object> logParams = new HashMap<>(6);
        logParams.put("channelId", "010");
        logParams.put("cartId", 23);
        logParams.put("activeStatus", "ToInStock");
        logParams.put("creator", "will2");
        logParams.put("comment", "高级检索 批量上下架");
        logParams.put("codeList", new ArrayList(Arrays.asList("CRWNRG01LBO-10","CRWNRG01LBO-9","CRWNRG01LBO-8","CRWNRG01LBO-7","CRWNRG01LBO-6","CRWNRG01LBO-5")));

        String json = "{\"channelId\":\"012\",\"userName\":\"edward\",\"cartList\":[27],\"activeStatus\":\"ToOnSale\",\"codeList\":[\"BBE1U688-Z5I\"],\"comment\":\"高级检索 批量上下架\",\"consumerRetryTimes\":0,\"mqId\":0,\"delaySecond\":0,\"sender\":\"edward\"}";

        try {
            targetService.setProductOnSaleOrInStock(logParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 测试jindong上架
    @Test
    public void testJDPlatformToOnsale() {
        // 发送请求到MQ,插入操作历史记录
        Map<String, Object> logParams = new HashMap<>(6);
        logParams.put("channelId", "928");
        logParams.put("cartId", 23);
        logParams.put("activeStatus", "ToOnSale");
        logParams.put("creator", "will2");
        logParams.put("comment", "高级检索 批量上下架");
        logParams.put("codeList", new ArrayList(Arrays.asList("VN-04OJJPV")));
        try {
            targetService.setProductOnSaleOrInStock(logParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 测试jindong下架
    @Test
    public void testJDPlatformToInstock() {
        // 发送请求到MQ,插入操作历史记录
        Map<String, Object> logParams = new HashMap<>(6);
        logParams.put("channelId", "928");
        logParams.put("cartId", 23);
        logParams.put("activeStatus", "ToInStock");
        logParams.put("creator", "will2");
        logParams.put("comment", "高级检索 批量上下架");
        logParams.put("codeList", new ArrayList(Arrays.asList("VN-04OJJPV")));
        try {
            targetService.setProductOnSaleOrInStock(logParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 测试jumei上架
    @Test
    public void testJMPlatformToOnsale() {
        // 发送请求到MQ,插入操作历史记录
        Map<String, Object> logParams = new HashMap<>(6);
        logParams.put("channelId", "010");
        logParams.put("cartId", 23);
        logParams.put("activeStatus", "ToOnSale");
        logParams.put("creator", "gump");
        logParams.put("comment", "高级检索 批量上下架");
        logParams.put("codeList", new ArrayList(Arrays.asList("00341",
                "00395",
                "00772YAK7",
                "01411YAA",
                "01614YAC")));
        logParams.put("statusVal", CmsConstants.PlatformActive.ToInStock);
        try {
            targetService.setProductOnSaleOrInStock(logParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 测试jumei下架
    @Test
    public void testJMPlatformToInstock() {
        // 发送请求到MQ,插入操作历史记录
        Map<String, Object> logParams = new HashMap<>(6);
        logParams.put("channelId", "010");
        logParams.put("cartId", 23);
        logParams.put("activeStatus", "ToInStock");
        logParams.put("creator", "will2");
        logParams.put("comment", "高级检索 批量上下架");
        logParams.put("codeList", new ArrayList(Arrays.asList("51A0HC13E1-00LCNB0")));
        try {
            targetService.setProductOnSaleOrInStock(logParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * build redis hash Key
     */
    private static String buildKey(String cart_id, String order_channel_id) {
        return cart_id + CacheKeyEnums.SKIP + order_channel_id;
    }
}