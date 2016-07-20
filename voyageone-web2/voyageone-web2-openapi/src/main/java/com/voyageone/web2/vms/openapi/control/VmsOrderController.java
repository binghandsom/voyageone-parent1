package com.voyageone.web2.vms.openapi.control;

import com.voyageone.web2.cms.openapi.OpenAipCmsBaseController;
import com.voyageone.web2.sdk.api.request.VmsOrderAddRequest;
import com.voyageone.web2.sdk.api.request.VmsOrderCancelRequest;
import com.voyageone.web2.sdk.api.request.VmsOrderInfoGetRequest;
import com.voyageone.web2.sdk.api.request.VmsOrderStatusUpdateRequest;
import com.voyageone.web2.sdk.api.response.VmsOrderAddResponse;
import com.voyageone.web2.sdk.api.response.VmsOrderCancelResponse;
import com.voyageone.web2.sdk.api.response.VmsOrderInfoGetResponse;
import com.voyageone.web2.sdk.api.response.VmsOrderStatusUpdateResponse;
import com.voyageone.web2.vms.openapi.service.VmsOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * VmsOrderController
 *
 * Created on 16/07/18.
 * @author jeff.duan
 * @version 1.0
 */

@RestController
@RequestMapping(
        value  = "/rest/vms/order/",
        method = RequestMethod.POST
)
public class VmsOrderController extends OpenAipCmsBaseController {

    @Autowired
    private VmsOrderService vmsOrderService;

    @RequestMapping("addOrderInfo")
    public VmsOrderAddResponse addOrderInfo(@RequestBody VmsOrderAddRequest request) {
        return vmsOrderService.addOrderInfo(request);
    }

    @RequestMapping("cancelOrder")
    public VmsOrderCancelResponse cancelOrder(@RequestBody VmsOrderCancelRequest request) {
        return vmsOrderService.cancelOrder(request);
    }

    @RequestMapping("getOrderInfo")
    public VmsOrderInfoGetResponse getOrderInfo(@RequestBody VmsOrderInfoGetRequest request) {
        return vmsOrderService.getOrderInfo(request);
    }

    @RequestMapping("updateOrderStatus")
    public VmsOrderStatusUpdateResponse getOrderInfo(@RequestBody VmsOrderStatusUpdateRequest request) {
        return vmsOrderService.updateOrderStatus(request);
    }
}