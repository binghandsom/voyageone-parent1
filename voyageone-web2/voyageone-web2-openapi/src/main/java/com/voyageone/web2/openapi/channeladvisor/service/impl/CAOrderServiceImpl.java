package com.voyageone.web2.openapi.channeladvisor.service.impl;

import com.voyageone.common.configs.Properties;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.web2.openapi.channeladvisor.CAOpenApiBaseService;
import com.voyageone.web2.openapi.channeladvisor.service.CAOrderService;
import com.voyageone.web2.sdk.api.channeladvisor.request.OrderCancellationRequest;
import com.voyageone.web2.sdk.api.channeladvisor.request.ShipRequest;
import com.voyageone.web2.sdk.api.channeladvisor.response.ActionResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * @author aooer 2016/9/6.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
@Profile("product")
public class CAOrderServiceImpl extends CAOpenApiBaseService implements CAOrderService {

    public ActionResponse getOrders(String status, String limit) {
        /* 用来限制查询订单的状态，默认任何状态 */
        if (StringUtils.isEmpty(status)) ;
        // TODO: 2016/9/7 空值处理 logger或其他
        /* 用来限制查询订单的条数，默认全部条数 */
        if (StringUtils.isEmpty(limit)) ;

        // TODO: 2016/9/7 空值处理 logger或其他
        // TODO: 2016/9/7 获取jsonbody 响应 mock response
        return JacksonUtil.json2Bean(Properties.readValue("getOrders"), ActionResponse.class);
    }

    public ActionResponse getOrderById(String orderID) {
        if (StringUtils.isEmpty(orderID)) ;
        // TODO: 2016/9/7 非空参数校验
        // TODO: 2016/9/7 根据订单id查询订单，如果为找到订单数据，返回errorid 6000(orderNotFount)
        // TODO: 2016/9/7 获取jsonbody 响应 mock response
        return JacksonUtil.json2Bean(Properties.readValue("getOrderById"), ActionResponse.class);
    }

    public ActionResponse acknowledgeOrder(String orderID) {
        if (StringUtils.isEmpty(orderID)) ;
        // TODO: 2016/9/7 非空参数校验
        // TODO: 2016/9/7 根据订单id查询订单，如果为找到订单数据，返回errorid 6000(orderNotFount)
        // TODO: 2016/9/7 获取jsonbody 响应 mock response
        return JacksonUtil.json2Bean(Properties.readValue("acknowledgeOrder"), ActionResponse.class);
    }

    public ActionResponse shipOrder(String orderID, ShipRequest request) {
        if (StringUtils.isEmpty(orderID)) ;
        // TODO: 2016/9/7 非空参数校验
        // TODO: 2016/9/7 根据订单id查询订单，如果为找到订单数据，返回errorid 6000(orderNotFount)
        // TODO: 2016/9/7 获取jsonbody 响应 mock response
        return JacksonUtil.json2Bean(Properties.readValue("shipOrder"), ActionResponse.class);
    }

    public ActionResponse cancelOrder(String orderID, OrderCancellationRequest request) {
        if (StringUtils.isEmpty(orderID)) ;
        // TODO: 2016/9/7 非空参数校验
        // TODO: 2016/9/7 根据订单id查询订单，如果为找到订单数据，返回errorid 6000(orderNotFount)
        // TODO: 2016/9/7 获取jsonbody 响应 mock response
        return JacksonUtil.json2Bean(Properties.readValue("cancelOrder"), ActionResponse.class);
    }

    public ActionResponse refundOrder(String orderID, OrderCancellationRequest request) {
        if (StringUtils.isEmpty(orderID)) ;
        // TODO: 2016/9/7 非空参数校验
        // TODO: 2016/9/7 根据订单id查询订单，如果为找到订单数据，返回errorid 6000(orderNotFount)
        // TODO: 2016/9/7 获取jsonbody 响应 mock response
        return JacksonUtil.json2Bean(Properties.readValue("refundOrder"), ActionResponse.class);
    }


}
