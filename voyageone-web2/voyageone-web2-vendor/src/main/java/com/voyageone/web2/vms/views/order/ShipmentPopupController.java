package com.voyageone.web2.vms.views.order;

import com.voyageone.web2.base.BaseController;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.vms.views.shipment.ShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static com.voyageone.web2.vms.VmsUrlConstants.POPUP;

/**
 * order页面popup的shipment
 * Created by vantis on 16-7-14.
 */
@RestController
@RequestMapping(value = POPUP.SHIPMENT.ROOT, method = RequestMethod.POST)
public class ShipmentPopupController extends BaseController {

    private ShipmentService shipmentService;

    @Autowired
    public ShipmentPopupController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @RequestMapping(POPUP.SHIPMENT.INIT)
    public AjaxResponse init() {
        Map<String, Object> result = new HashMap<>();
        result.put("searchStatuses", shipmentService.getAllStatus());
        result.put("expressCompanies", shipmentService.getAllExpressCompines());

        return success(result);
    }
}
