package com.voyageone.components.intltarget.service;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.components.intltarget.bean.TargetSimpleMessage;
import com.voyageone.components.intltarget.bean.order.TargetOrderCancelRequest;
import com.voyageone.components.intltarget.bean.order.TargetOrderDetail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * @author aooer 2016/4/13.
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class TargetOrderServiceTest {

    @Autowired
    private TargetOrderService targetOrderService;

    @Test
    public void testCancelOrder() throws Exception {

        TargetOrderCancelRequest request=new TargetOrderCancelRequest();
        request.setOrderItemId(Arrays.asList("11420006", "11420007"));
        request.setOrderNumber("101529002");
        TargetSimpleMessage message=targetOrderService.cancelOrder(request);
        System.out.println(JacksonUtil.bean2Json(message));
    }

    @Test
    public void testGetOrderDetails() throws Exception {
        TargetOrderDetail detail=targetOrderService.getOrderDetails("911794142522");
        System.out.println(JacksonUtil.bean2Json(detail));
    }
}