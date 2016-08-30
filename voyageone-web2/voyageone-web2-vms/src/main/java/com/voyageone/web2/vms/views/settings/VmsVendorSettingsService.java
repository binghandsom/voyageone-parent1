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
        VmsChannelConfigBean vmsChannelConfigBean = new VmsChannelConfigBean();
        vmsChannelConfigBean.setChannelId(vmsChannelConfigBean.getChannelId());
        vmsChannelConfigBean.setConfigKey(vmsChannelConfigBean.getConfigKey());
        vmsChannelConfigBean.setConfigCode(VmsConstants.ChannelConfig.COMMON_CONFIG_CODE);
        vmsChannelConfigBean.setConfigValue1(vmsChannelConfigBean.getConfigValue1());
        vmsChannelConfigBean.setConfigValue2(vmsChannelConfigBean.getConfigValue2());
        vmsChannelConfigBean.setConfigValue3(vmsChannelConfigBean.getConfigValue3());
        vmsChannelConfigBean.setCreater(user.getUserName());
        vmsChannelConfigBean.setModifier(user.getUserName());
        int count = vmsChannelConfigService.insertOrUpdateConfig(vmsChannelConfigBean);
        VmsChannelConfigs.reload();
        return count;
    }
}
