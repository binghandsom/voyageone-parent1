package com.voyageone.components.overstock.service;

import com.overstock.mp.mpc.externalclient.api.Result;
import com.overstock.mp.mpc.externalclient.model.OrderLineType;
import com.overstock.mp.mpc.externalclient.model.OrderType;
import com.overstock.mp.mpc.externalclient.model.OrdersType;
import com.voyageone.components.overstock.OverstockBase;
import com.voyageone.components.overstock.bean.order.OverstockOrderLineCancelRequest;
import com.voyageone.components.overstock.bean.order.OverstockOrderMultipleQueryRequest;
import com.voyageone.components.overstock.bean.order.OverstockOrderWholeCancelRequest;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

/**
 * @author aooer 2016/6/7.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class OverstockOrderService extends OverstockBase {

    /**
     * 推送一个订单
     *
     * @param request request
     * @return 结果集
     * @throws Exception
     */
    @Retryable
    public Result<OrderType> placeOneOrder(OrderType request) throws Exception {
        return getClientFactory().forOrders()
                .post(request)
                .execute(getCredentials());
    }

    /**
     * 取消整个订单
     *
     * @param request request
     * @return 结果集
     * @throws Exception
     */
    @Retryable
    public Result<OrderType> cancelTheWholeOrder(OverstockOrderWholeCancelRequest request) throws Exception {
        OrderType order = new OrderType();
        order.setOrderStatus(request.getOrderStatusType());
        return getClientFactory().forOrders()
                .patch(request.getOrderId(), order)
                .execute(getCredentials());
    }

    /**
     * 取消订单一条记录
     *
     * @param request request
     * @return 结果集
     * @throws Exception
     */
    @Retryable
    public Result<OrderType> cancelTheLineOrder(OverstockOrderLineCancelRequest request) throws Exception {
        final Result<OrderType> orderResult = getClientFactory().forOrders()
                .getOne(request.getOrderId())
                .build()
                .execute(getCredentials());
        final OrderType order = orderResult.getEntity();
        OrderLineType lineToCancel = null;
        for (final OrderLineType line : order.getLines().getLine()) {
            if (request.getOrderRemoveLineNumber().equals(line.getOrderLineNumber())) {
                lineToCancel = line;
                break;
            }
        }
        // remove line to cancel from the order.
        order.getLines().getLine().remove(lineToCancel);
        return getClientFactory().forOrders()
                .patch(request.getOrderId(), order)
                .execute(getCredentials());
    }

    /**
     * 批量查询订单信息
     *
     * @param request request
     * @return 结果集
     * @throws Exception
     */
    @Retryable
    public Result<OrdersType> queryingForMultipleOrders(OverstockOrderMultipleQueryRequest request) throws Exception {
        return getClientFactory()
                .forOrders()
                .getMany()
                .withStatus(request.getStatus())
                .withOrderNumber(request.getOrderNumber())
                .withOffset(request.getOffset())
                .withLimit(request.getLimit())
                .build()
                .execute(getCredentials());
    }

    /**
     * 查询单条订单信息
     *
     * @param orderId orderId
     * @return 订单结果
     * @throws Exception
     */
    @Retryable
    public Result<OrderType> queryingForOneOrder(String orderId) throws Exception {
        return getClientFactory()
                .forOrders()
                .getOne(orderId)
                .build()
                .execute(getCredentials());
    }

}
