package com.voyageone.web2.openapi.channeladvisor.service;

import com.voyageone.web2.sdk.api.channeladvisor.request.OrderCancellationRequest;
import com.voyageone.web2.sdk.api.channeladvisor.request.ShipRequest;
import com.voyageone.web2.sdk.api.channeladvisor.response.ActionResponse;

/**
 * @author aooer 2016/9/6.
 * @version 2.0.0
 * @since 2.0.0
 */
public interface CAOrderService {

    ActionResponse getOrders(String status, String limit);

    ActionResponse getOrderById(String orderID);

    ActionResponse acknowledgeOrder(String orderID);

    ActionResponse shipOrder(String orderID, ShipRequest request);

    ActionResponse cancelOrder(String orderID, OrderCancellationRequest request);

    ActionResponse refundOrder(String orderID, OrderCancellationRequest request);

}
