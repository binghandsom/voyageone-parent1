package com.voyageone.components.intltarget.service;

import com.voyageone.components.intltarget.TargetBase;
import com.voyageone.components.intltarget.bean.cart.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;

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

    /**
     * 添加订单email到cart
     * @param request email信息
     * @return 是否添加成功
     * @throws Exception
     */
    public boolean addCartOrderTrackingEmail(TargetCartEmailRequest request) throws Exception{
        getApiResponseWithKey(Url+"/v2/email",request,null,true);
        return true;
    }

    /**
     * 初始化checkOut
     * @return check信息
     * @throws Exception
     */
    public TargetCartCheckout initiateCheckout() throws Exception {
        return getApiResponseWithKey(Url+"/v2/initiate_checkout",new HashMap<>(),TargetCartCheckout.class,true);
    }

    /**
     * 添加paypalDetails
     * @return TargetCartPayPal
     * @throws Exception
     */
    public TargetCartPayPal addCartPayPalDetails(TargetCartPayPalRequest request) throws Exception {
        return getApiResponseWithKey(Url+"/v2/paypal",request,TargetCartPayPal.class,true);
    }
}
