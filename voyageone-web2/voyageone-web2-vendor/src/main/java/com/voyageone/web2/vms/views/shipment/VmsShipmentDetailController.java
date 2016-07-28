package com.voyageone.web2.vms.views.shipment;

import com.voyageone.web2.base.BaseController;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.vms.bean.order.ScanInfoBean;
import com.voyageone.service.bean.vms.shipment.ShipmentBean;
import com.voyageone.web2.vms.views.common.VmsChannelConfigService;
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
    private VmsChannelConfigService vmsChannelConfigService;

    @Autowired
    public VmsShipmentDetailController(VmsShipmentService vmsShipmentService, VmsOrderInfoService
            vmsOrderInfoService, VmsChannelConfigService vmsChannelConfigService) {
        this.vmsShipmentService = vmsShipmentService;
        this.vmsOrderInfoService = vmsOrderInfoService;
        this.vmsChannelConfigService = vmsChannelConfigService;
    }

    @RequestMapping(SHIPMENT.ShipmentDetail.INIT)
    public AjaxResponse init(@RequestBody Integer shipmentId) {
        Map<String, Object> result = new HashMap<>();
        ShipmentBean shipment = vmsShipmentService.getShipment(this.getUser(), shipmentId);
        result.put("shipment", shipment);
        result.put("scannedSkuList", vmsOrderInfoService.getScannedSkuList(this.getUser(), shipment));
        result.put("orderStatusList", vmsOrderInfoService.getAllOrderStatusesList());
        result.put("shipmentStatusList", vmsShipmentService.getAllStatus());
        result.put("expressCompanies", vmsShipmentService.getAllExpressCompanies());
        result.put("channelConfigs", vmsChannelConfigService.getChannelConfigs(this.getUser()));
        return success(result);
    }

    @RequestMapping(SHIPMENT.ShipmentDetail.SCAN)
    public AjaxResponse scan(@RequestBody ScanInfoBean scanInfoBean) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", vmsOrderInfoService.scanBarcodeInSku(this.getUser(), scanInfoBean));
        result.put("scannedSkuList", vmsOrderInfoService.getScannedSkuList(this.getUser(), scanInfoBean.getShipment()));
        return success(result);
    }

    @RequestMapping(SHIPMENT.ShipmentDetail.SHIP)
    public AjaxResponse ship(@RequestBody ShipmentBean shipment) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", vmsShipmentService.endShipment(this.getUser(), shipment));
        return success(result);
    }

    @RequestMapping(SHIPMENT.ShipmentDetail.GET_INFO)
    public AjaxResponse getInfo(@RequestBody Integer shipmentId) {
        Map<String, Object> result = new HashMap<>();
        result.put("shipment", vmsShipmentService.getShipment(this.getUser(), shipmentId));
        return success(result);
    }
}
