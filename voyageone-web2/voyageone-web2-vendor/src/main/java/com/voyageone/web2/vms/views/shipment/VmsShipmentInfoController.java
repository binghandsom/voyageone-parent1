package com.voyageone.web2.vms.views.shipment;

import com.voyageone.web2.base.BaseController;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.vms.bean.shipment.ShipmentSearchInfoBean;
import com.voyageone.web2.vms.views.common.VmsChannelConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.voyageone.web2.vms.VmsUrlConstants.SHIPMENT;

/**
 * shipment页面
 * Created by vantis on 16-7-25.
 */
@RestController
@RequestMapping(SHIPMENT.SHIPMENT_INFO.ROOT)
public class VmsShipmentInfoController extends BaseController {

    private VmsShipmentService vmsShipmentService;
    private VmsChannelConfigService vmsChannelConfigService;

    @Autowired
    public VmsShipmentInfoController(VmsShipmentService vmsShipmentService, VmsChannelConfigService vmsChannelConfigService) {
        this.vmsShipmentService = vmsShipmentService;
        this.vmsChannelConfigService = vmsChannelConfigService;
    }

    @RequestMapping(SHIPMENT.SHIPMENT_INFO.INIT)
    public AjaxResponse init() {
        Map<String, Object> result = new HashMap<>();
        result.put("channelConfigs", vmsChannelConfigService.getChannelConfigs(this.getUser()));
        result.put("shipmentStatusList", vmsShipmentService.getAllStatus());
        return success(result);
    }

    @RequestMapping(SHIPMENT.SHIPMENT_INFO.SEARCH)
    public AjaxResponse search(@RequestBody ShipmentSearchInfoBean shipmentSearchInfoBean) {
        Map<String, Object> result = new HashMap<>();
        Date date = new Date();
        result.put("shipmentInfo", vmsShipmentService.search(this.getUser(), shipmentSearchInfoBean));
        $debug("this action takes totally " + String.valueOf(new Date().getTime() - date.getTime()) + " milliseconds.");
        return success(result);
    }
}
