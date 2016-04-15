package com.voyageone.components.intltarget.service;

import com.voyageone.components.intltarget.TargetBase;
import com.voyageone.components.intltarget.bean.cart.*;
import org.springframework.stereotype.Service;

/**
 * @author aooer 2016/4/13.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class TargetCartService extends TargetBase{

    private static final String Url="/carts";

    /**
     * 添加shipping地址
     * @param request 请求对象
     * @return 地址信息
     * @throws Exception
     */
    public TargetCartShippingAddress addCartShippingAddress(TargetCartShippingAddressRequest request) throws Exception {
        return getApiResponseWithKey(Url+"/v2/shipping_address",request,TargetCartShippingAddress.class,true);
    }

    /**
     * 添加产品信息到cart
     * @param request 请求体
     * @return 产品信息
     * @throws Exception
     */
    public TargetCartProduct addProductToCart(TargetCartProductRequest request) throws Exception {
        return getApiResponseWithKey(Url+"/v2/products",request,TargetCartProduct.class,true);
    }

    /**
     * checkout
     * @param request 请求体
     * @return check信息
     * @throws Exception
     */
    public TargetCartCheckout checkout(TargetCartCheckoutRequest request) throws Exception {
        return getApiResponseWithKey(Url+"/v2/checkout",request,TargetCartCheckout.class,true);
    }
}
