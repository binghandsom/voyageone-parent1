package com.voyageone.common.configs.Enums;

/**
 * @author aooer 2016/3/10.
 * @version 2.0.0
 * @since 2.0.0
 */
/**
 * 对应 ct_cart 表中存在的配置名称
 */
public class CacheKeyEnums {

    public final static String CONFIG_ALL_KEY_REGEX = "ConfigData_*";

    /* 公用分隔符，防止因分割符产生的bug而声明 */
    public final static String SKIP = "$-SKIP-$";

    public enum KeyEnum {

        ConfigData_FeedConfigs,

        ConfigData_Codes,

        ConfigData_Type,

        ConfigData_TypeChannel,

        ConfigData_ThirdPartyConfigs,

        ConfigData_ImsCategoryConfigs,

        ConfigData_Properties,

        ConfigData_CmsChannelConfigs,

        ConfigData_StoreConfigConfigs,

        ConfigData_StoreConfigs,

        ConfigData_CarrierConfigs,

        ConfigData_OrderChannelConfigConfigs,

        ConfigData_OrderChannelConfigs,

        ConfigData_DiscountRateConfigs,

        ConfigData_PortConfigs,

        ConfigData_ShopConfigConfigs,

        ConfigData_ShopConfigs,

        ConfigData_CartConfigs,

        ConfigData_UsJois,

        ConfigData_VmsChannelConfigs;
    }
}
