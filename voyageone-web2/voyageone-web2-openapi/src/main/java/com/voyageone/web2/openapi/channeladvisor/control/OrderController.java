package com.voyageone.web2.openapi.channeladvisor.control;

import com.voyageone.web2.openapi.OpenApiBaseController;
import com.voyageone.web2.openapi.channeladvisor.constants.UrlConstants;
import com.voyageone.web2.sdk.api.VoApiResponse;
import com.voyageone.web2.sdk.api.channeladvisor.request.OrderCancellationRequest;
import com.voyageone.web2.sdk.api.channeladvisor.request.ShipRequest;
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
        value = UrlConstants.ORDERS.ROOT
)
public class OrderController extends OpenApiBaseController {

    @RequestMapping(value = UrlConstants.ORDERS.GET_ORDERS, method = RequestMethod.GET)
    public VoApiResponse getOrders(@RequestParam String status, @RequestParam String limit) {

        //check param

        // call service


        return null;
    }

    @RequestMapping(value = UrlConstants.ORDERS.GET_ORDER, method = RequestMethod.GET)
    public VoApiResponse getOrderById(@PathVariable String id) {

        //check param

        // call service


        return null;
    }

    @RequestMapping(value = UrlConstants.ORDERS.ACKNOWLEDGE_ORDER, method = RequestMethod.POST)
    public VoApiResponse acknowledgeOrder(@PathVariable String id) {

        //check param

        // call service


        return null;
    }

    @RequestMapping(value = UrlConstants.ORDERS.SHIP_ORDER, method = RequestMethod.POST)
    public VoApiResponse shipOrder(@PathVariable String id, @RequestBody ShipRequest request) {

        //check param

        // call service


        return null;
    }

    @RequestMapping(value = UrlConstants.ORDERS.CANCEL_ORDER, method = RequestMethod.POST)
    public VoApiResponse cancelOrder(@PathVariable String id, @RequestBody OrderCancellationRequest request) {

        //check param

        // call service


        return null;
    }

    @RequestMapping(value = UrlConstants.ORDERS.REFUND_ORDER, method = RequestMethod.POST)
    public VoApiResponse refundOrder(@PathVariable String id, @RequestBody OrderCancellationRequest request) {
        //check param

        // call service


        return null;
    }

}

