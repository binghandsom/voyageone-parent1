package com.voyageone.web2.vms.views.settings;

import com.voyageone.web2.base.BaseController;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.vms.bean.VmsChannelSettingBean;
import com.voyageone.web2.vms.views.common.VmsChannelConfigService;
import com.voyageone.web2.vms.views.shipment.VmsShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
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
public class VmsVendorSettingsController extends BaseController {

    private VmsChannelConfigService vmsChannelConfigService;
    private VmsVendorSettingsService vmsVendorSettingsService;
    private VmsShipmentService vmsShipmentService;


    @Autowired
    public VmsVendorSettingsController(VmsChannelConfigService vmsChannelConfigService,
                                       VmsShipmentService vmsShipmentService,
                                       VmsVendorSettingsService vmsVendorSettingsService) {
        this.vmsChannelConfigService = vmsChannelConfigService;
        this.vmsShipmentService = vmsShipmentService;
        this.vmsVendorSettingsService = vmsVendorSettingsService;
    }

    @RequestMapping(SETTINGS.INIT)
    public AjaxResponse init() {
        Map<String, Object> result = new HashMap<>();
        result.put("channelConfig", vmsChannelConfigService.getChannelConfig(this.getUser()));
        result.put("deliveryCompanyList", vmsShipmentService.getAllExpressCompanies());
        return success(result);
    }

    @RequestMapping(SETTINGS.SAVE)
    public AjaxResponse save(@RequestBody VmsChannelSettingBean vmsChannelSettingBean) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", vmsVendorSettingsService.save(this.getUser(), vmsChannelSettingBean));
        return success(result);
    }

}
