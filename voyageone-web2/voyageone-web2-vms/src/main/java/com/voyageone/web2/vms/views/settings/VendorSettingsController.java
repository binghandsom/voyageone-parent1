package com.voyageone.web2.vms.views.settings;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.voyageone.web2.vms.VmsUrlConstants.SETTINGS;
/**
 * vendor settings
 * Created by vantis on 16-8-30.
 */
@RestController
@RequestMapping(value = SETTINGS.ROOT, method = RequestMethod.POST)
public class VendorSettingsController {
}
