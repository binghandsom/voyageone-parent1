package com.voyageone.common.components.gilt;

import com.voyageone.common.components.gilt.bean.*;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.JsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

        GiltPageGetOrdersRequest request=new GiltPageGetOrdersRequest();
        request.setOrder_ids("e3eb4b7d-d1bc-4d33-bfe5-4a095485b6b9");

        List<GiltOrder> orders= giltOrderService
                .pageGetOrders(request);
        System.out.println(JsonUtil.getJsonString(orders));
    }

    @Test
    public void testGetOrderById() throws Exception {

        GiltOrder giltOrder= giltOrderService.getOrderById("e3eb4b7d-d1bc-4d33-bfe5-4a095485b6b9");
        System.out.println(JsonUtil.getJsonString(giltOrder));
    }

    @Test
    public void testPutOrder() throws Exception {
             GiltPutOrderRequest request=new GiltPutOrderRequest();
        request.setId(UUID.randomUUID());
        List<GiltOrderItem> orderItems=new ArrayList<>();

        GiltOrderItem orderItem=new GiltOrderItem();
        orderItem.setSku_id(1124545654);
        orderItem.setQuantity(0);
        request.setOrder_items(orderItems);
        orderItems.add(orderItem);

        GiltOrder orders= giltOrderService.putOrder(request);
        System.out.println(JsonUtil.getJsonString(orders));
    }

    @Test
    public void testPatchOrder() throws Exception {
                GiltPageGetOrdersRequest request=new GiltPageGetOrdersRequest();
        request.setOrder_ids("e3eb4b7d-d1bc-4d33-bfe5-4a095485b6b9");

        List<GiltOrder> orders= giltOrderService
                .pageGetOrders(request);
        System.out.println(JsonUtil.getJsonString(orders));
    }
}