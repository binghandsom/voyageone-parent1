package com.voyageone.web2.vms.views.settings;

import com.voyageone.web2.base.BaseController;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.vms.views.common.VmsChannelConfigService;
import com.voyageone.web2.vms.views.shipment.VmsShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static com.voyageone.web2.vms.VmsUrlConstants.SETTINGS;
/**
 * vendor settings
 * Created by vantis on 16-8-30.
 */
@RestController
@RequestMapping(value = SETTINGS.ROOT, method = RequestMethod.POST)
public class VendorSettingsController extends BaseController {

    private VmsChannelConfigService vmsChannelConfigService;
    private VmsShipmentService vmsShipmentService;


    @Autowired
    public VendorSettingsController(VmsChannelConfigService vmsChannelConfigService, VmsShipmentService vmsShipmentService) {
        this.vmsChannelConfigService = vmsChannelConfigService;
        this.vmsShipmentService = vmsShipmentService;
    }

    @RequestMapping(SETTINGS.INIT)
    public AjaxResponse init() {
        Map<String, Object> result = new HashMap<>();
        result.put("channelConfig", vmsChannelConfigService.getChannelConfigs(this.getUser()));
        result.put("deliveryCompanyList", vmsShipmentService.getAllExpressCompanies());
        return success(result);
    }

}
