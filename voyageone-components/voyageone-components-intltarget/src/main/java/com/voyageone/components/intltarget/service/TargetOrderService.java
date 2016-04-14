package com.voyageone.components.intltarget.service;

import com.voyageone.components.intltarget.TargetBase;
import com.voyageone.components.intltarget.bean.TargetSimpleMessage;
import com.voyageone.components.intltarget.bean.order.TargetOrderCancelRequest;
import com.voyageone.components.intltarget.bean.order.TargetOrderDetail;
import org.springframework.stereotype.Service;

/**
 * @author aooer 2016/4/13.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class TargetOrderService extends TargetBase {

    private static final String Url="/orders";

    /**
     * 取消订单
     * @param request 请求
     * @return 简单消息
     * @throws Exception
     */
    public TargetSimpleMessage cancelOrder(TargetOrderCancelRequest request) throws Exception {
        return getApiResponseWithKey(Url+"/v1",request,TargetSimpleMessage.class,true);
    }

    /**
     * 获取订单明细
     * @param orderId 订单id
     * @return 订单详情
     * @throws Exception
     */
    public TargetOrderDetail getOrderDetails(String orderId) throws Exception {
        return getApiResponseWithKey(Url+"/v2/"+orderId,null,TargetOrderDetail.class,true);
    }

}
