package com.voyageone.components.intltarget.service;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.components.intltarget.bean.cart.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author aooer 2016/4/13.
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class TargetCartServiceTest {

    @Autowired
    private TargetCartService targetCartService;

    @Test
    public void testAddCartShippingAddress() throws Exception {
        TargetCartShippingAddressRequest request=new TargetCartShippingAddressRequest();
        request.setAddressId("2892265373");
        TargetCartShippingAddress address=targetCartService.addCartShippingAddress(request);
        System.out.println(JacksonUtil.bean2Json(address));
    }

    @Test
    public void testAddProductToCart() throws Exception {

        TargetCartProductRequest request=new TargetCartProductRequest();
        request.setProductId("200012454");
        request.setQuantity("1");
        TargetCartProduct product=targetCartService.addProductToCart(request);
        System.out.println(JacksonUtil.bean2Json(product));
    }

    @Test
    public void testCheckout() throws Exception {

        TargetCartCheckoutRequest request=new TargetCartCheckoutRequest();
        request.setTcPinNum("1234");
        TargetCartCheckout checkout=targetCartService.checkout(request);
        System.out.println(JacksonUtil.bean2Json(checkout));
    }
}