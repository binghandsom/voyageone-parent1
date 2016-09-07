package com.voyageone.web2.openapi.channeladvisor.control;

import com.voyageone.web2.openapi.channeladvisor.CAOpenApiBaseController;
import com.voyageone.web2.openapi.channeladvisor.constants.CAUrlConstants;
import com.voyageone.web2.sdk.api.channeladvisor.request.OrderCancellationRequest;
import com.voyageone.web2.sdk.api.channeladvisor.request.ShipRequest;
import com.voyageone.web2.sdk.api.channeladvisor.response.ActionResponse;
import org.springframework.web.bind.annotation.*;

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
public class CAOrderController extends CAOpenApiBaseController {

    @RequestMapping(value = CAUrlConstants.ORDERS.GET_ORDERS, method = RequestMethod.GET)
    public ActionResponse getOrders(@RequestParam String status, @RequestParam String limit) {

        //check param

        // call service


        return null;
    }

    @RequestMapping(value = CAUrlConstants.ORDERS.GET_ORDER, method = RequestMethod.GET)
    public ActionResponse getOrderById(@PathVariable String id) {

        //check param

        // call service


        return null;
    }

    @RequestMapping(value = CAUrlConstants.ORDERS.ACKNOWLEDGE_ORDER, method = RequestMethod.POST)
    public ActionResponse acknowledgeOrder(@PathVariable String id) {

        //check param

        // call service


        return null;
    }

    @RequestMapping(value = CAUrlConstants.ORDERS.SHIP_ORDER, method = RequestMethod.POST)
    public ActionResponse shipOrder(@PathVariable String id, @RequestBody ShipRequest request) {

        //check param

        // call service


        return null;
    }

    @RequestMapping(value = CAUrlConstants.ORDERS.CANCEL_ORDER, method = RequestMethod.POST)
    public ActionResponse cancelOrder(@PathVariable String id, @RequestBody OrderCancellationRequest request) {

        //check param

        // call service


        return null;
    }

    @RequestMapping(value = CAUrlConstants.ORDERS.REFUND_ORDER, method = RequestMethod.POST)
    public ActionResponse refundOrder(@PathVariable String id, @RequestBody OrderCancellationRequest request) {
        //check param

        // call service


        return null;
    }

}

