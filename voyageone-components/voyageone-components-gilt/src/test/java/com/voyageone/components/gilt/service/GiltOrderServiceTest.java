package com.voyageone.components.gilt.service;

import com.voyageone.components.gilt.bean.*;
import com.voyageone.components.gilt.exceptions.GiltException;
import com.voyageone.common.util.JsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author aooer 2016/2/2.
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class GiltOrderServiceTest {

    @Autowired
    private GiltOrderService giltOrderService;

    @Test
    public void testPageGetOrders() throws Exception {

        GiltPageGetOrdersRequest request=new GiltPageGetOrdersRequest();
        //request.setOrder_ids("e3eb4b7d-d1bc-4d33-bfe5-4a095485b6b9");

        List<GiltOrder> orders= giltOrderService
                .pageGetOrders(request);
        System.out.println(JsonUtil.getJsonString(orders));
    }

    @Test
    public void testGetOrderById() throws Exception {

        GiltOrder giltOrder= giltOrderService.getOrderById("d0ca18a3-03f7-44ee-a6f8-9e3a34ac3213");
        System.out.println(JsonUtil.getJsonString(giltOrder));
    }

    @Test
    public void testPutOrder() throws Exception {
        GiltPutOrderRequest request=new GiltPutOrderRequest();
//        request.setId(UUID.randomUUID());
        request.setId(UUID.fromString("8384a597-bb29-4447-8288-a794ee2eea7c"));
        List<GiltOrderItem> orderItems=new ArrayList<>();

        GiltOrderItem orderItem=new GiltOrderItem();
        orderItem.setSku_id(5072313);
        orderItem.setQuantity(1);
        orderItems.add(orderItem);

        GiltOrderItem orderItem2=new GiltOrderItem();
        orderItem2.setSku_id(5072313);
        orderItem2.setQuantity(1);
        orderItems.add(orderItem2);

        request.setOrder_items(orderItems);

        GiltOrder orders= giltOrderService.putOrder(request);
        System.out.println(orders.getId().toString());
        System.out.println(JsonUtil.getJsonString(orders));
    }

    @Test
    public void testPatchOrder() {
        try {
            GiltPatchOrderRequest request=new GiltPatchOrderRequest();
            request.setId(UUID.fromString("9ad6d3ea-ea66-4fcc-bbeb-57805f0549b2"));
            request.setStatus(GiltOrderStatus.confirmed);
            GiltOrder orders= giltOrderService.patchOrder(request);
            System.out.println(JsonUtil.getJsonString(orders));
        } catch (GiltException e) {
            System.out.println("GiltException = " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Exception = " + e.getMessage());
        }

    }

    @Test
    public void testRandomUUID() throws Exception {
        System.out.println(UUID.randomUUID());
    }
}