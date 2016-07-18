package com.voyageone.web2.vms.openapi.control;

import com.voyageone.web2.cms.openapi.OpenAipCmsBaseController;
import com.voyageone.web2.sdk.api.request.VmsOrderAddGetRequest;
import com.voyageone.web2.sdk.api.response.VmsOrderAddGetResponse;
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
    public VmsOrderAddGetResponse addOrderInfo(@RequestBody VmsOrderAddGetRequest request) {
        return vmsOrderService.addOrderInfo(request);
    }
}