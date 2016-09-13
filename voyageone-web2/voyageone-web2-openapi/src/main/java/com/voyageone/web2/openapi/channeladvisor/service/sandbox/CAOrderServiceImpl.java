package com.voyageone.web2.openapi.channeladvisor.service.sandbox;

import com.voyageone.common.util.StringUtils;
import com.voyageone.web2.openapi.channeladvisor.CAOpenApiBaseService;
import com.voyageone.web2.openapi.channeladvisor.exception.CAApiException;
import com.voyageone.web2.openapi.channeladvisor.service.CAOrderService;
import com.voyageone.service.bean.vms.channeladvisor.order.OrderModel;
import com.voyageone.service.bean.vms.channeladvisor.enums.ErrorIDEnum;
import com.voyageone.service.bean.vms.channeladvisor.request.OrderCancellationRequest;
import com.voyageone.service.bean.vms.channeladvisor.request.ShipRequest;
import com.voyageone.service.bean.vms.channeladvisor.response.ActionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author aooer 2016/9/6.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
@Profile("sandbox")
public class CAOrderServiceImpl extends CAOpenApiBaseService implements CAOrderService {

    @Autowired
    private JsonResourcesService resourcesService;

    public ActionResponse getOrders(String status, String limit) {

        List<OrderModel> responseBody = resourcesService.getResourceDataList(this.getClass().getName(), "getOrders", OrderModel.class);

        return success(responseBody);
    }

    public ActionResponse getOrderById(String orderID) {
        if (StringUtils.isEmpty(orderID)) {
            //If there is no order with that ID, return Error ID 6000 (OrderNotFound)
            throw new CAApiException(ErrorIDEnum.OrderNotFound);
        }

        OrderModel responseBody = resourcesService.getResourceDataModel(this.getClass().getName(), "getOrderById", OrderModel.class);

        return success(responseBody);
    }

    public ActionResponse acknowledgeOrder(String orderID) {
        if (StringUtils.isEmpty(orderID)) {
            //If there is no order with that ID, return Error ID 6000 (OrderNotFound)
            throw new CAApiException(ErrorIDEnum.OrderNotFound);
        }

        return success();
    }

    public ActionResponse shipOrder(String orderID, ShipRequest request) {
        if (StringUtils.isEmpty(orderID)) {
            //If there is no order with that ID, return Error ID 6000 (OrderNotFound)
            throw new CAApiException(ErrorIDEnum.OrderNotFound);
        }

        return success();
    }

    public ActionResponse cancelOrder(String orderID, OrderCancellationRequest request) {
        if (StringUtils.isEmpty(orderID)) {
            //If there is no order with that ID, return Error ID 6000 (OrderNotFound)
            throw new CAApiException(ErrorIDEnum.OrderNotFound);
        }

        return success();
    }

    public ActionResponse refundOrder(String orderID, OrderCancellationRequest request) {
        if (StringUtils.isEmpty(orderID)) {
            //If there is no order with that ID, return Error ID 6000 (OrderNotFound)
            throw new CAApiException(ErrorIDEnum.OrderNotFound);
        }

        return success();
    }


}
