package com.voyageone.web2.vms.views.settings;

import com.voyageone.common.configs.VmsChannelConfigs;
import com.voyageone.common.configs.beans.VmsChannelConfigBean;
import com.voyageone.common.configs.dao.VmsChannelConfigDao;
import com.voyageone.web2.core.bean.UserSessionBean;
import com.voyageone.web2.vms.VmsConstants;
import com.voyageone.web2.vms.bean.VmsChannelSettingBean;
import com.voyageone.web2.vms.views.common.VmsChannelConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.voyageone.web2.vms.VmsConstants.ChannelConfig.*;

/**
 * vendor settings
 * Created by vantis on 16-8-30.
 */
@Service
public class VmsVendorSettingsService {

    private VmsChannelConfigService vmsChannelConfigService;

    @Autowired
    public VmsVendorSettingsService(VmsChannelConfigService vmsChannelConfigService) {
        this.vmsChannelConfigService = vmsChannelConfigService;
    }

    public int save(UserSessionBean user, VmsChannelSettingBean vmsChannelSettingBean) {

        // Default Delivery Company
        VmsChannelConfigBean defaultDeliveryCompanyConfig = new VmsChannelConfigBean();
        defaultDeliveryCompanyConfig.setChannelId(user.getSelChannelId());
        defaultDeliveryCompanyConfig.setConfigKey(DEFAULT_DELIVERY_COMPANY);
        defaultDeliveryCompanyConfig.setConfigCode(VmsConstants.ChannelConfig.COMMON_CONFIG_CODE);
        defaultDeliveryCompanyConfig.setConfigValue1(vmsChannelSettingBean.getDefaultDeliveryCompany());
        defaultDeliveryCompanyConfig.setCreater(user.getUserName());
        defaultDeliveryCompanyConfig.setModifier(user.getUserName());
        int defaultDeliveryCompanyConfigCount = vmsChannelConfigService.insertOrUpdateConfig
                (defaultDeliveryCompanyConfig);

        // E-mail Address
        VmsChannelConfigBean emailAddressConfig = new VmsChannelConfigBean();
        emailAddressConfig.setChannelId(user.getSelChannelId());
        emailAddressConfig.setConfigKey(EMAIL_ADDRESS);
        emailAddressConfig.setConfigCode(VmsConstants.ChannelConfig.COMMON_CONFIG_CODE);
        emailAddressConfig.setConfigValue1(vmsChannelSettingBean.getEmailAddress());
        emailAddressConfig.setCreater(user.getUserName());
        emailAddressConfig.setModifier(user.getUserName());
        int emailAddressConfigCount = vmsChannelConfigService.insertOrUpdateConfig(emailAddressConfig);

        // Shipment Naming Converter
        VmsChannelConfigBean shipmentNamingConverterConfig = new VmsChannelConfigBean();
        shipmentNamingConverterConfig.setChannelId(user.getSelChannelId());
        shipmentNamingConverterConfig.setConfigKey(DEFAULT_SHIPMENT_NAMING_CONVERTER);
        shipmentNamingConverterConfig.setConfigCode(VmsConstants.ChannelConfig.COMMON_CONFIG_CODE);
        shipmentNamingConverterConfig.setConfigValue1(vmsChannelSettingBean.getNamingConverter());
        shipmentNamingConverterConfig.setCreater(user.getUserName());
        shipmentNamingConverterConfig.setModifier(user.getUserName());
        int shipmentNamingConverterConfigCount = vmsChannelConfigService.insertOrUpdateConfig(shipmentNamingConverterConfig);

        VmsChannelConfigs.reload();
        return defaultDeliveryCompanyConfigCount + emailAddressConfigCount + shipmentNamingConverterConfigCount;
    }
}
