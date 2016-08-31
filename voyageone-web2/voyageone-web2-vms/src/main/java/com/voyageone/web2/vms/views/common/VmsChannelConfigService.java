package com.voyageone.web2.vms.views.common;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.VmsChannelConfigs;
import com.voyageone.common.configs.beans.VmsChannelConfigBean;
import com.voyageone.common.configs.dao.VmsChannelConfigDao;
import com.voyageone.web2.core.bean.UserSessionBean;
import com.voyageone.web2.vms.VmsConstants;
import com.voyageone.web2.vms.bean.VmsChannelSettingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * channelConfig配置信息
 * Created by vantis on 16-7-26.
 */
@Service
public class VmsChannelConfigService {

    VmsChannelConfigDao vmsChannelConfigDao;

    @Autowired
    public VmsChannelConfigService(VmsChannelConfigDao vmsChannelConfigDao) {
        this.vmsChannelConfigDao = vmsChannelConfigDao;
    }

    /**
     * 读取channel相应配置
     *
     * @param user 当前用户
     * @return 当前用户所选择channel的配置
     */
    public VmsChannelSettingBean getChannelConfigs(UserSessionBean user) {

        VmsChannelConfigBean vendorOperateType = VmsChannelConfigs.getConfigBean(user.getSelChannelId(),
                VmsConstants.ChannelConfig.VENDOR_OPERATE_TYPE, VmsConstants.ChannelConfig.COMMON_CONFIG_CODE);

        VmsChannelConfigBean salePriceShow = VmsChannelConfigs.getConfigBean(user.getSelChannelId(),
                VmsConstants.ChannelConfig.SALE_PRICE_SHOW, VmsConstants.ChannelConfig.COMMON_CONFIG_CODE);

        VmsChannelConfigBean defaultDeliveryCompany = VmsChannelConfigs.getConfigBean(user.getSelChannelId(),
                VmsConstants.ChannelConfig.DEFAULT_DELIVERY_COMPANY, VmsConstants.ChannelConfig.COMMON_CONFIG_CODE);

        VmsChannelConfigBean defaultNamingConverter = VmsChannelConfigs.getConfigBean(user.getSelChannelId(),
                VmsConstants.ChannelConfig.DEFAULT_SHIPMENT_NAMING_CONVERTER, VmsConstants.ChannelConfig.COMMON_CONFIG_CODE);
        VmsChannelConfigBean emailAddress = VmsChannelConfigs.getConfigBean(user.getSelChannelId(),
                VmsConstants.ChannelConfig.EMAIL_ADDRESS, VmsConstants.ChannelConfig.COMMON_CONFIG_CODE);
        VmsChannelConfigBean additionalAttributes = VmsChannelConfigs.getConfigBean(user.getSelChannelId(),
                VmsConstants.ChannelConfig.ADDITIONAL_ATTRIBUTES, VmsConstants.ChannelConfig.COMMON_CONFIG_CODE);

        // Missing required configures for this channel, please contact with the system administrator for help.
        if (null == vendorOperateType) throw new BusinessException("8000019");

        VmsChannelSettingBean vmsChannelSettingBean = new VmsChannelSettingBean();
        vmsChannelSettingBean.setVendorOperateType(vendorOperateType.getConfigValue1());
        if (null != salePriceShow)
            vmsChannelSettingBean.setSalePriceShow(salePriceShow.getConfigValue1());
        if (null != defaultDeliveryCompany)
            vmsChannelSettingBean.setDefaultDeliveryCompany(defaultDeliveryCompany.getConfigValue1());
        if (null != defaultNamingConverter)
            vmsChannelSettingBean.setNamingConverter(defaultNamingConverter.getConfigValue1());
        if (null != emailAddress)
            vmsChannelSettingBean.setEmailAddress(emailAddress.getConfigValue1());
        if (null != additionalAttributes) {
            String value1 = additionalAttributes.getConfigValue1();
            // 多个属性按逗号分割
            String[] attributeKeys1 = value1.split(",");
            List<String> attributeKeysList = new ArrayList<>();
            if (attributeKeys1 != null && attributeKeys1.length > 0) {
                for (String attributeKey1 : attributeKeys1) {
                    // 按点分割一个属性
                    String[] contents = attributeKey1.split("\\.");
                    if (contents != null && contents.length == 2) {
                        attributeKeysList.add(contents[1]);
                    }
                }
            }
            vmsChannelSettingBean.setAdditionalAttributes(attributeKeysList);
            String value2 = additionalAttributes.getConfigValue2();
            // 多个属性按逗号分割
            String[] attributeKeys2 = value2.split(",");
            List<String> attributeKeysClassList = new ArrayList<>();
            if (attributeKeys2 != null && attributeKeys2.length > 0) {
                for (String attributeKey2 : attributeKeys2) {
                    // 按点分割一个属性
                    attributeKeysClassList.add(attributeKey2);
                }
            }
            vmsChannelSettingBean.setAdditionalAttributesClass(attributeKeysClassList);
        }

        return vmsChannelSettingBean;
    }

    public int insertOrUpdateConfig(VmsChannelConfigBean vmsChannelConfigBean) {
        if (null == VmsChannelConfigs.getConfigBean(vmsChannelConfigBean.getChannelId(),
                vmsChannelConfigBean.getConfigKey(), VmsConstants.ChannelConfig.COMMON_CONFIG_CODE)) {
            return vmsChannelConfigDao.insertConfig(vmsChannelConfigBean);
        }
        return vmsChannelConfigDao.updateConfig(vmsChannelConfigBean);
    }
}
