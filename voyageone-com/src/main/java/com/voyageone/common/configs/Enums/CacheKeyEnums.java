package com.voyageone.common.configs.Enums;

import com.voyageone.common.redis.CacheTemplateFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;

/**
 * @author aooer 2016/3/10.
 * @version 2.0.0
 * @since 2.0.0
 */
public enum CacheKeyEnums {

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

    ConfigData_$ModifyTime$,

    ConfigData_USJoinConfig
    ;

    /**
     * 根据枚举Key删除，
     * @return boolean true代表删除成功，false代表删除失败
     */
    public boolean delete(){
        RedisTemplate<String, Map<String, Object>> template=CacheTemplateFactory.getCacheTemplate();
        if(template.hasKey(this.toString()))
            template.delete(this.toString());
        return !template.hasKey(this.toString());
    }
}
