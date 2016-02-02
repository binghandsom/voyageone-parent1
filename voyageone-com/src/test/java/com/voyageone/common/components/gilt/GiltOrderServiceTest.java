package com.voyageone.common.components.gilt;

import com.voyageone.common.components.gilt.bean.GiltOrder;
import com.voyageone.common.components.gilt.bean.GiltPageGetOrdersRequest;
import com.voyageone.common.components.gilt.bean.GiltPageGetSkusRequest;
import com.voyageone.common.components.gilt.bean.GiltRealTimeInventory;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.JsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author aooer 2016/2/2.
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class GiltOrderServiceTest {

    @Autowired
    private GiltOrderService giltOrderService;

    @Test
    public void testPageGetOrders() throws Exception {
        ShopBean shopBean=new ShopBean();
        shopBean.setApp_url("https://api-sandbox.gilt.com/global/");
        shopBean.setAppKey("YTE5N2YzM2M1ZmFhZmRjZDY3YmZiNjgxMzJiYTgzNGY6");

        GiltPageGetOrdersRequest request=new GiltPageGetOrdersRequest();
        request.setOrder_ids("e3eb4b7d-d1bc-4d33-bfe5-4a095485b6b9");

        List<GiltOrder> orders= giltOrderService
                .pageGetOrders(shopBean,request);
        System.out.println(JsonUtil.getJsonString(orders));
    }

    @Test
    public void testGetOrderById() throws Exception {
        ShopBean shopBean=new ShopBean();
        shopBean.setApp_url("https://api-sandbox.gilt.com/global/");
        shopBean.setAppKey("YTE5N2YzM2M1ZmFhZmRjZDY3YmZiNjgxMzJiYTgzNGY6");
        GiltOrder giltOrder= giltOrderService.getOrderById(shopBean,"e3eb4b7d-d1bc-4d33-bfe5-4a095485b6b9");
        System.out.println(JsonUtil.getJsonString(giltOrder));
    }
}