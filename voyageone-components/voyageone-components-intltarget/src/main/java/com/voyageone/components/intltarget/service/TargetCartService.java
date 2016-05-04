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
public class TargetCartService extends TargetBase {

    private static final String Url = "/carts";

    /**
     * 添加产品信息到cart
     *
     * @param request 请求体
     * @return 产品信息
     * @throws Exception
     */
    public TargetCartAddProductResponse addProductToCart(TargetCartAddProductRequest request) throws Exception {
        return postApiResponseWithKey(Url + "/v2/products?responseGroup=cartDetails", request, TargetCartAddProductResponse.class, true);
    }

    /**
     * 获取已添加使用的地址信息
     *
     * @return 地址信息响应内容
     * @throws Exception
     */
    public TargetCartUsableShippingAddressResponse getUsableShippingAddress() throws Exception {
        return getApiResponseWithKey(Url + "/v2/usable_shipping_address", null, TargetCartUsableShippingAddressResponse.class, true);
    }

    /**
     * 应用运输详细信息
     *
     * @param request 应用运输信息
     * @return 操作结果
     * @throws Exception
     */
    public TargetCartApplyShippingDetailResponse applyShippingDetails(TargetCartApplyShippingDetailRequest request) throws Exception {
        return postApiResponseWithKey(Url + "/v2/shipping_details", request, TargetCartApplyShippingDetailResponse.class, true);
    }

    /**
     * 添加支付卡信息
     *
     * @param request 支付信息
     * @return 信息添加是否成功
     */
    public boolean addTenders(TargetCartAddTenderRequest request) {
        try {
            postApiResponseWithKey(Url + "/v2/shipping_details", request, TargetCartApplyShippingDetailResponse.class, true);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 订单review
     *
     * @return 订单review响应信息
     * @throws Exception
     */
    public TargetCartOrderReviewResponse orderReview() throws Exception {
        return getApiResponseWithKey(Url + "/v2/order_review", null, TargetCartOrderReviewResponse.class, true);
    }

    /**
     * checkout
     *
     * @param request 请求体
     * @return check信息
     * @throws Exception
     */
    public TargetCartCheckoutResponse checkout(TargetCartCheckoutRequest request) throws Exception {
        return postApiResponseWithKey(Url + "/v2/checkout", request, TargetCartCheckoutResponse.class, true);
    }
}
