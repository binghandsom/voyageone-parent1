package com.voyageone.web2.openapi.channeladvisor.service.sandbox;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.web2.openapi.channeladvisor.CAOpenApiBaseService;
import com.voyageone.web2.openapi.channeladvisor.service.CAOrderService;
import com.voyageone.web2.sdk.api.channeladvisor.domain.EmptyObject;
import com.voyageone.web2.sdk.api.channeladvisor.domain.OrderModel;
import com.voyageone.web2.sdk.api.channeladvisor.request.OrderCancellationRequest;
import com.voyageone.web2.sdk.api.channeladvisor.request.ShipRequest;
import com.voyageone.web2.sdk.api.channeladvisor.response.ActionResponse;
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
        /* 用来限制查询订单的状态，默认任何状态 */
        if (StringUtils.isEmpty(status)) ;
        // TODO: 2016/9/7 空值处理 logger或其他
        /* 用来限制查询订单的条数，默认全部条数 */
        if (StringUtils.isEmpty(limit)) ;

        // TODO: 2016/9/7 空值处理 logger或其他
        // TODO: 2016/9/7 获取jsonbody 响应 mock response

        List<OrderModel> responseBody = resourcesService.getResourceDataList(this.getClass().getName(), "getOrders", OrderModel.class);

        return success(responseBody);
    }

    public ActionResponse getOrderById(String orderID) {
        if (StringUtils.isEmpty(orderID)) ;
        // TODO: 2016/9/7 非空参数校验
        // TODO: 2016/9/7 根据订单id查询订单，如果为找到订单数据，返回errorid 6000(orderNotFount)
        // TODO: 2016/9/7 获取jsonbody 响应 mock response

        OrderModel responseBody = resourcesService.getResourceDataModel(this.getClass().getName(), "getOrderById", OrderModel.class);

        return success(responseBody);
    }

    public ActionResponse acknowledgeOrder(String orderID) {
        if (StringUtils.isEmpty(orderID)) ;
        // TODO: 2016/9/7 非空参数校验
        // TODO: 2016/9/7 根据订单id查询订单，如果为找到订单数据，返回errorid 6000(orderNotFount)
        // TODO: 2016/9/7 获取jsonbody 响应 mock response
        EmptyObject responseBody = resourcesService.getResourceDataModel(this.getClass().getName(), "acknowledgeOrder", EmptyObject.class);

        return success(responseBody);
    }

    public ActionResponse shipOrder(String orderID, ShipRequest request) {
        if (StringUtils.isEmpty(orderID)) ;
        // TODO: 2016/9/7 非空参数校验
        // TODO: 2016/9/7 根据订单id查询订单，如果为找到订单数据，返回errorid 6000(orderNotFount)
        // TODO: 2016/9/7 获取jsonbody 响应 mock response
        EmptyObject responseBody = resourcesService.getResourceDataModel(this.getClass().getName(), "shipOrder", EmptyObject.class);

        return success(responseBody);
    }

    public ActionResponse cancelOrder(String orderID, OrderCancellationRequest request) {
        if (StringUtils.isEmpty(orderID)) ;
        // TODO: 2016/9/7 非空参数校验
        // TODO: 2016/9/7 根据订单id查询订单，如果为找到订单数据，返回errorid 6000(orderNotFount)
        // TODO: 2016/9/7 获取jsonbody 响应 mock response
        EmptyObject responseBody = resourcesService.getResourceDataModel(this.getClass().getName(), "cancelOrder", EmptyObject.class);

        return success(responseBody);
    }

    public ActionResponse refundOrder(String orderID, OrderCancellationRequest request) {
        if (StringUtils.isEmpty(orderID)) ;
        // TODO: 2016/9/7 非空参数校验
        // TODO: 2016/9/7 根据订单id查询订单，如果为找到订单数据，返回errorid 6000(orderNotFount)
        // TODO: 2016/9/7 获取jsonbody 响应 mock response
        EmptyObject responseBody = resourcesService.getResourceDataModel(this.getClass().getName(), "refundOrder", EmptyObject.class);

        return success(responseBody);
    }


    public static void main(String[] args) {

        EmptyObject responseBody = JacksonUtil.json2Bean("{}",EmptyObject.class);
        ActionResponse response = new ActionResponse();
        response.setResponseBody(responseBody);
        System.out.println(response);

    }

}
