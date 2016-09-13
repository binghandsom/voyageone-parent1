package com.voyageone.web2.openapi.channeladvisor.service.impl;

import com.voyageone.common.util.StringUtils;
import com.voyageone.web2.openapi.channeladvisor.CAOpenApiBaseService;
import com.voyageone.web2.openapi.channeladvisor.exception.CAApiException;
import com.voyageone.web2.openapi.channeladvisor.service.CAOrderService;
import com.voyageone.service.bean.vms.channeladvisor.order.OrderModel;
import com.voyageone.service.bean.vms.channeladvisor.enums.ErrorIDEnum;
import com.voyageone.service.bean.vms.channeladvisor.request.OrderCancellationRequest;
import com.voyageone.service.bean.vms.channeladvisor.request.ShipRequest;
import com.voyageone.service.bean.vms.channeladvisor.response.ActionResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author aooer 2016/9/6.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
@Profile("product")
public class CAOrderServiceImpl extends CAOpenApiBaseService implements CAOrderService {

    public ActionResponse getOrders(String status, String limit) {
        String channelId = getClientChannelId();
        List<OrderModel> responseBody = new ArrayList<>();

        /* 用来限制查询订单的状态，默认任何状态 */
        /* 用来限制查询订单的条数，默认全部条数 */
        // TODO: 根据实际的业务处理

        return success(responseBody);
    }

    public ActionResponse getOrderById(String orderID) {
        if (StringUtils.isEmpty(orderID)) {
            //If there is no order with that ID, return Error ID 6000 (OrderNotFound)
            throw new CAApiException(ErrorIDEnum.OrderNotFound);
        }

        String channelId = getClientChannelId();
        OrderModel responseBody = new OrderModel();

        // TODO: 根据实际的业务处理

        return success(responseBody);
    }

    public ActionResponse acknowledgeOrder(String orderID) {
        if (StringUtils.isEmpty(orderID)) {
            //If there is no order with that ID, return Error ID 6000 (OrderNotFound)
            throw new CAApiException(ErrorIDEnum.OrderNotFound);
        }

        String channelId = getClientChannelId();
        // TODO: 根据实际的业务处理

        return success();
    }

    public ActionResponse shipOrder(String orderID, ShipRequest request) {
        if (StringUtils.isEmpty(orderID)) {
            //If there is no order with that ID, return Error ID 6000 (OrderNotFound)
            throw new CAApiException(ErrorIDEnum.OrderNotFound);
        }

        if (request == null) {
            throw new CAApiException(ErrorIDEnum.InvalidRequest, "ShipRequest not found.");
        }

        String channelId = getClientChannelId();
        // TODO: 根据实际的业务处理

        return success();
    }

    public ActionResponse cancelOrder(String orderID, OrderCancellationRequest request) {
        if (StringUtils.isEmpty(orderID)) {
            //If there is no order with that ID, return Error ID 6000 (OrderNotFound)
            throw new CAApiException(ErrorIDEnum.OrderNotFound);
        }

        if (request == null) {
            throw new CAApiException(ErrorIDEnum.InvalidRequest, "OrderCancellationRequest not found.");
        }

        String channelId = getClientChannelId();
        // TODO: 根据实际的业务处理

        return success();
    }

    public ActionResponse refundOrder(String orderID, OrderCancellationRequest request) {
        if (StringUtils.isEmpty(orderID)) {
            //If there is no order with that ID, return Error ID 6000 (OrderNotFound)
            throw new CAApiException(ErrorIDEnum.OrderNotFound);
        }

        if (request == null) {
            throw new CAApiException(ErrorIDEnum.InvalidRequest, "OrderCancellationRequest not found.");
        }

        String channelId = getClientChannelId();
        // TODO: 根据实际的业务处理

        return success();
    }


}
