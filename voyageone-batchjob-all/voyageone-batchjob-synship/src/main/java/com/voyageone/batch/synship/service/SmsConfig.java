package com.voyageone.batch.synship.service;

import com.voyageone.batch.synship.modelbean.SmsConfigBean;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 提供对 sms_config 配置的便捷访问
 * <p>
 * Created by Jonas on 9/23/15.
 */
public class SmsConfig {

    private Map<String, Map<String, SmsConfigBean>> data;

    public SmsConfig(Map<String, List<SmsConfigBean>> mapSmsConfig) {
        data = new HashMap<>();
        for (Map.Entry<String, List<SmsConfigBean>> entry : mapSmsConfig.entrySet()) {
            Map<String, SmsConfigBean> configBeanMap =
                    entry.getValue().stream().collect(Collectors.toMap(SmsConfigBean::getSms_code1, b -> b));
            data.put(entry.getKey(), configBeanMap);
        }
    }

    /**
     * 获取 channel 下所有的配置
     *
     * @param channel 渠道
     * @return code1 -> SmsConfigBean
     */
    public Map<String, SmsConfigBean> get(ChannelConfigEnums.Channel channel) {
        return data.get(channel.getId());
    }

    /**
     * 获取 channel 下 code1 的配置
     *
     * @param channel 渠道
     * @param code1   code1
     * @return SmsConfigBean
     */
    public SmsConfigBean get(ChannelConfigEnums.Channel channel, String code1) {
        return get(channel).get(code1);
    }
}
