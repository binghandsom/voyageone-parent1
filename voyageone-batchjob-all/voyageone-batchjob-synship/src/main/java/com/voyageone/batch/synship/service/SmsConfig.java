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

    private Map<String, Map<String, List<SmsConfigBean>>> data;

    public SmsConfig(Map<String, List<SmsConfigBean>> mapSmsConfig) {
        data = new HashMap<>();
        for (Map.Entry<String, List<SmsConfigBean>> entry : mapSmsConfig.entrySet()) {
            Map<String, List<SmsConfigBean>> configBeanMap =
                    entry.getValue().stream().collect(Collectors.groupingBy(SmsConfigBean::getSms_code1));
            data.put(entry.getKey(), configBeanMap);
        }
    }

    /**
     * 获取 channel 下所有的配置
     *
     * @param channel 渠道
     * @return code1 -> SmsConfigBean
     */
    public Map<String, List<SmsConfigBean>> get(ChannelConfigEnums.Channel channel) {
        return data.get(channel.getId());
    }

    /**
     * 获取 channel 下 code1 的配置
     *
     * @param channel 渠道
     * @param code1   code1
     * @return SmsConfigBean 集合
     */
    public List<SmsConfigBean> get(ChannelConfigEnums.Channel channel, String code1) {
        return get(channel).get(code1);
    }

    /**
     * 获取 channel 下 code1 的第一个有效配置
     *
     * @param channel 渠道
     * @param code1   code1
     * @return SmsConfigBean
     */
    public SmsConfigBean getAvailable(ChannelConfigEnums.Channel channel, String code1) {
        return get(channel, code1).stream().filter(this::isAvailable).findFirst().orElse(null);
    }

    private boolean isAvailable(SmsConfigBean smsConfigBean) {
        return smsConfigBean.getDel_flg().equals("0");
    }
}
