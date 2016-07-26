package com.voyageone.web2.vms.views.shipment;

import com.voyageone.web2.base.BaseController;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.vms.bean.shipment.ShipmentBean;
import com.voyageone.web2.vms.bean.shipment.ShipmentDetailSearchInfo;
import com.voyageone.web2.vms.views.order.VmsOrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static com.voyageone.web2.vms.VmsUrlConstants.SHIPMENT;

/**
 * shipment detail画面
 * Created by vantis on 16-7-26.
 */
@RestController
@RequestMapping(value = SHIPMENT.ShipmentDetail.ROOT, method = RequestMethod.POST)
public class VmsShipmentDetailController extends BaseController {

    private VmsShipmentService vmsShipmentService;
    private VmsOrderInfoService vmsOrderInfoService;

    @Autowired
    public VmsShipmentDetailController(VmsShipmentService vmsShipmentService, VmsOrderInfoService vmsOrderInfoService) {
        this.vmsShipmentService = vmsShipmentService;
        this.vmsOrderInfoService = vmsOrderInfoService;
    }

    @RequestMapping(SHIPMENT.ShipmentDetail.INIT)
    public AjaxResponse init(@RequestBody ShipmentDetailSearchInfo shipmentDetailSearchInfo) {
        Map<String, Object> result = new HashMap<>();
        ShipmentBean shipment = vmsShipmentService.getShipment(this.getUser(), shipmentDetailSearchInfo.getShipmentId());
        result.put("shipment", shipment);
        result.put("scannedSkuList", vmsOrderInfoService.getScannedSkuList(this.getUser(), shipment, shipmentDetailSearchInfo));
        result.put("shipmentStatusList", vmsShipmentService.getAllStatus());
        result.put("expressCompanies", vmsShipmentService.getAllExpressCompanies());
        return success(result);
    }
}
