package com.voyageone.components.intltarget.service;

import com.voyageone.components.intltarget.TargetBase;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author aooer 2016/4/13.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class TargetOrderService extends TargetBase {

    private static final String Url = "/orders";

    /**
     * 获取订单明细
     *
     * @param orderId 订单id
     * @return 订单详情
     * @throws Exception
     */
    public Map getOrderDetails(String orderId) throws Exception {
        return postApiResponseWithKey(Url + "/v2/" + orderId, null, HashMap.class, true);
    }

}
