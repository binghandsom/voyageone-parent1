package com.voyageone.web2.vms.views.common;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.VmsChannelConfigs;
import com.voyageone.common.configs.beans.VmsChannelConfigBean;
import com.voyageone.web2.core.bean.UserSessionBean;
import com.voyageone.web2.vms.VmsConstants;
import com.voyageone.web2.vms.bean.VmsChannelSettings;
import org.springframework.stereotype.Service;

/**
 * channelConfig配置信息
 * Created by vantis on 16-7-26.
 */
@Service
public class VmsChannelConfigService {

    /**
     * 读取channel相应配置
     *
     * @param user 当前用户
     * @return 当前用户所选择channel的配置
     */
    public VmsChannelSettings getChannelConfigs(UserSessionBean user) {

        VmsChannelConfigBean vendorOperateType = VmsChannelConfigs.getConfigBean(user.getSelChannelId(),
                VmsConstants.ChannelConfig.VENDOR_OPERATE_TYPE, VmsConstants.ChannelConfig.COMMON_CONFIG_CODE);

        VmsChannelConfigBean salePriceShow = VmsChannelConfigs.getConfigBean(user.getSelChannelId(),
                VmsConstants.ChannelConfig.SALE_PRICE_SHOW, VmsConstants.ChannelConfig.COMMON_CONFIG_CODE);

        // Missing required configures for this channel, please contact with the system administrator for help.
        if (null == vendorOperateType) throw new BusinessException("8000019");

        VmsChannelSettings vmsChannelSettings = new VmsChannelSettings();
        vmsChannelSettings.setVendorOperateType(vendorOperateType.getConfigValue1());
        if (null != salePriceShow)
            vmsChannelSettings.setSalePriceShow(salePriceShow.getConfigValue1());
        return vmsChannelSettings;
    }
}
