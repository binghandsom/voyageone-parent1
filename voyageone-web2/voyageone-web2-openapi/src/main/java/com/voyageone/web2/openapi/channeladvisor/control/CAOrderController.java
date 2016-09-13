package com.voyageone.web2.openapi.channeladvisor.control;

import com.voyageone.web2.openapi.OpenApiBaseController;
import com.voyageone.web2.openapi.channeladvisor.constants.CAUrlConstants;
import com.voyageone.web2.openapi.channeladvisor.service.CAOrderService;
import com.voyageone.service.bean.vms.channeladvisor.request.OrderCancellationRequest;
import com.voyageone.service.bean.vms.channeladvisor.request.ShipRequest;
import com.voyageone.service.bean.vms.channeladvisor.response.ActionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Order controller
 *
 * @author aooer 2016/9/6.
 * @version 2.0.0
 * @since 2.0.0
 */
@RestController
@RequestMapping(
        value = CAUrlConstants.ROOT
)
public class CAOrderController extends OpenApiBaseController {

    @Autowired
    private CAOrderService caOrderService;

    /**
     * 根据状态或限制条数获取订单
     *
     * @param request request
     * @return response
     */
    @RequestMapping(value = CAUrlConstants.ORDERS.GET_ORDERS, method = RequestMethod.GET)
    public ActionResponse getOrders(HttpServletRequest request) {
        return caOrderService.getOrders(request.getParameter("status"), request.getParameter("limit"));
    }

    /**
     * 根据id查询订单
     *
     * @param id id
     * @return response
     */
    @RequestMapping(value = CAUrlConstants.ORDERS.GET_ORDER, method = RequestMethod.GET)
    public ActionResponse getOrderById(@PathVariable String id) {
        return caOrderService.getOrderById(id);
    }

    /**
     * 确认订单
     *
     * @param id id
     * @return response
     */
    @RequestMapping(value = CAUrlConstants.ORDERS.ACKNOWLEDGE_ORDER, method = RequestMethod.POST)
    public ActionResponse acknowledgeOrder(@PathVariable String id) {
        return caOrderService.acknowledgeOrder(id);
    }

    /**
     * 运送订单
     *
     * @param id      id
     * @param request request
     * @return response
     */
    @RequestMapping(value = CAUrlConstants.ORDERS.SHIP_ORDER, method = RequestMethod.POST)
    public ActionResponse shipOrder(@PathVariable String id, @RequestBody ShipRequest request) {
        return caOrderService.shipOrder(id, request);
    }

    /**
     * 取消未运送订单
     *
     * @param id      id
     * @param request request
     * @return response
     */
    @RequestMapping(value = CAUrlConstants.ORDERS.CANCEL_ORDER, method = RequestMethod.POST)
    public ActionResponse cancelOrder(@PathVariable String id, @RequestBody OrderCancellationRequest request) {
        return caOrderService.cancelOrder(id, request);
    }

    /**
     * 退返订单
     *
     * @param id      id
     * @param request request
     * @return response
     */
    @RequestMapping(value = CAUrlConstants.ORDERS.REFUND_ORDER, method = RequestMethod.POST)
    public ActionResponse refundOrder(@PathVariable String id, @RequestBody OrderCancellationRequest request) {
        return caOrderService.refundOrder(id, request);
    }

}

